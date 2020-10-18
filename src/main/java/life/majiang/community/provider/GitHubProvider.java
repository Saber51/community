package life.majiang.community.provider;

import com.alibaba.fastjson.JSON;
import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GitHubUser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author saber-kings
 * @Auther:luanzhaofei@outlook.com
 * @Date:2019/9/19
 * @Description:life.majiang.community.provider
 * @version:1.0
 */
@Component
@Slf4j
public class GitHubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient.Builder()
                //设置连接超时时间
                .connectTimeout(60, TimeUnit.SECONDS)
                //设置读取超时时间
                .readTimeout(120, TimeUnit.SECONDS)
                .build();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String string = responseBody.string();
                return string.split("&")[0].split("=")[1];
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("getAccessToken error,{}", accessTokenDTO, e);
        }
        return null;
    }

    public GitHubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient.Builder()
                //设置连接超时时间
                .connectTimeout(60, TimeUnit.SECONDS)
                //设置读取超时时间
                .readTimeout(120, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization", "token " + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            if (body != null) {
                String string = body.string();
                return JSON.parseObject(string, GitHubUser.class);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("getUser error,{}", accessToken, e);
        }
        return null;
    }
}
