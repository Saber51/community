package life.majiang.community.provider;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.tencent.cloud.CosStsClient;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.TreeMap;
import java.util.UUID;

/**
 * @Auther:luanzhaofei@outlook.com
 * @Date:2019/10/14
 * @Description:life.majiang.community.provider
 * @version:1.0
 */
@Slf4j
@Service
public class COSProvider {

    //腾讯云的SecretId
    @Value("${tencent.cos.SecretId}")
    private String secretId;

    //腾讯云的SecretKey
    @Value("${tencent.cos.SecretKey}")
    private String secretKey;

    //腾讯云的bucket (存储桶)
    @Value("${tencent.cos.bucket}")
    private String bucket;

    //腾讯云的region(bucket所在地区)
    @Value("${tencent.cos.region}")
    private String region;

    //腾讯云的allowPrefix(允许上传的路径)
    @Value("${tencent.cos.allowPrefix}")
    private String allowPrefix;

    //腾讯云的临时密钥时长(单位秒)
    @Value("${tencent.cos.durationSeconds}")
    private Integer durationSeconds;

    //腾讯云的访问链接时长:
    @Value("${tencent.cos.expires}")
    private Integer expires;

    /**
     * 上传文件
     *
     * @param multipartFile 得到的MultipartFile文件流，用于转换成File文件流
     * @param fileName 文件名,用于件服务器下的根路径,即key,如: doc/picture.jpg
     * @return 成功返回文件路径,失败返回错误信息
     */
    public String upload(MultipartFile multipartFile, String fileName) {

        //获取文件后缀名
        String generatedFileName;
        String[] filePaths = fileName.split("\\.");
        if (filePaths.length > 1) {
            generatedFileName = UUID.randomUUID().toString() + "." + filePaths[filePaths.length - 1];
        } else {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
        //获取临时密钥
        JSONObject temp = getTempKey();
        // 用户基本信息:解析临时密钥中的相关信息
        String tmpSecretId = temp.getJSONObject("credentials").getString("tmpSecretId");
        String tmpSecretKey = temp.getJSONObject("credentials").getString("tmpSecretKey");
        String sessionToken = temp.getJSONObject("credentials").getString("sessionToken");
        COSClient cosclient = getCosClient(tmpSecretId, tmpSecretKey);

        try {
            //将上传到服务器上的文件创建成临时文件
            File localFile = File.createTempFile("Temp", null);
            multipartFile.transferTo(localFile);

            // 上传 object, 建议 20M 以下的文件使用该接口
            String key = "image/" + generatedFileName;
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, localFile);
            // 设置 x-cos-security-token header 字段
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setSecurityToken(sessionToken);
            putObjectRequest.setMetadata(objectMetadata);
            PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);

            // 成功：putObjectResult 会返回文件的 etag
            String etag = putObjectResult.getETag();
            if (StringUtils.isNotBlank(etag)) {
                boolean ret = delFile(localFile);
                if (!ret){
                    log.error("upload error,{}", CustomizeErrorCode.FILE_DEL_FAIL.getMessage());
                }
                return generatePresignedUrl(key);
            } else {
                log.error("upload error,{}", putObjectResult);
                throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
            }
        } catch (Exception e) {
            log.error("upload error,{}", e.getMessage());
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        } finally {
            cosclient.shutdown();
        }
    }

    /**
     * 删除临时文件
     * @param file
     * @return 返回结果
     */
    private boolean delFile(File file) {
        if (!file.exists()) {
            return false;
        }

        if (file.isFile()) {
            return file.delete();
        } else {
            File[] files = file.listFiles();
            for (File f : files) {
                delFile(f);
            }
            return file.delete();
        }
    }

    /**
     * 生成 cos 客户端
     * @param allSecretId 各种 SecretId (临时或者永久)
     * @param allSecretKey 各种 SecretKey (临时或者永久)
     * @return 返回 cos 客户端
     */
    private COSClient getCosClient(String allSecretId, String allSecretKey) {
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(allSecretId, allSecretKey);
        // 2 设置 bucket 区域
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        // 3 生成 cos 客户端
        return new COSClient(cred, clientConfig);
    }

    /**
     * 得到临时访问链接
     * @param key 对象在 COS 上的对象键
     * @return 返回访问链接
     */
    private String generatePresignedUrl(String key){
        COSClient cosClient = getCosClient(secretId, secretKey);
        // 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucket, key, HttpMethodName.GET);
        // 设置签名过期时间(可选), 若未进行设置, 则默认使用 ClientConfig 中的签名过期时间(1小时)
        // 这里设置签名在十年后过期
        Date expirationDate = new Date(new Date().getTime() + expires);
        req.setExpiration(expirationDate);
        cosClient.shutdown();
        return cosClient.generatePresignedUrl(req).toString();
    }

    /**
     * 生成临时密钥
     *
     * @return 返回json数据的结果
     */
    private JSONObject getTempKey() {
        TreeMap<String, Object> config = new TreeMap<>();
        try {
            //使用永久密钥生成临时密钥
            // 替换为您的 SecretId
            config.put("SecretId", secretId);
            // 替换为您的 SecretKey
            config.put("SecretKey", secretKey);
            // 临时密钥有效时长，单位是秒，默认1800秒，最长可设定有效期为7200秒
            config.put("durationSeconds", durationSeconds);
            // 换成您的 bucket
            config.put("bucket", bucket);
            // 换成 bucket 所在地区
            config.put("region", region);
            // 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的目录，例子：* 或者 doc/* 或者 picture.jpg
            config.put("allowPrefix", allowPrefix);
            //密钥的权限列表。简单上传、表单上传和分片上传需要以下的权限
            //https://cloud.tencent.com/document/product/436/31923
            String[] allowActions = new String[]{
                    // 简单上传
                    "name/cos:PutObject",
                    // 表单上传、小程序上传
                    "name/cos:PostObject",
                    // 分片上传
                    "name/cos:InitiateMultipartUpload",
                    "name/cos:ListMultipartUploads",
                    "name/cos:ListParts",
                    "name/cos:UploadPart",
                    "name/cos:CompleteMultipartUpload"
            };
            config.put("allowActions", allowActions);
            JSONObject credential = CosStsClient.getCredential(config);
            //成功返回临时密钥信息，如下打印密钥信息
//            System.out.println(credential);
            return credential;
        } catch (Exception e) {
            //失败抛出异常
            log.error("upload error{}", e.getMessage());
            throw new CustomizeException(CustomizeErrorCode.TEMPKEY_GET_FAIL);
        }
    }
}


