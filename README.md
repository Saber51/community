## 码匠社区

## 快速运行
1. 安装必备工具      
JDK，Maven    
2. 克隆代码到本地     
3. 运行命令创建数据库脚本       
```sh
mvn flyway:migrate
```
4. 运行打包命令
```sh
mvn package
```
5. 运行项目  
```sh
java -jar target/community-0.0.1-SNAPSHOT.jar
```
6. 访问项目
```
http://localhost:8887
```

## 部署
### 环境
Ubuntu 18.04.1 LTS (GNU/Linux 4.15.0-29-generic x86_64)
#### 配置
(1)更新系统
```bash
$ sudo apt update
```
(2)安装必备的编译环境:  
Ubuntu缺省情况下,并没有提供C/C++的编译环境,因此还需要手动安装。      
如果单独安装gcc以及g++比较麻烦,但是,
为了能够编译Ubuntu的内核,
Ubuntu提供了一个build-essential软件包。  
(注意：现在Ubuntu默认是自带build-essential 的)   

查看该软件包的依赖关系,可以看到以下内容:
```bash
$ apt-cache depends build-essential
build-essential
依赖:libc6-dev    
依赖: gcc     
依赖: g++    
依赖: make   
依赖: dpkg-dev
```
(3)安装yum工具
安装命令：
```bash
$ sudo apt-get install yum
```
 ### Ubuntu jar包运行
- 方法一：java -jar jar包名   (这样ssh窗口被锁定，直接关闭窗口，ctrl+c打断程序运行)     
- 方法二：java -jar jar包名  &   (&表示在后台运行，ssh窗口不被锁定，但窗口关闭程序终止运行)        
- 方法三： nohup java -jar jar包名 &  
（关闭窗口，用户退出登录程序仍然运行，但执行nohup命令时，缺省情况下输出作业被重定向到nohup.out文件中，除非指定文件位置）
- 方法四: nohup java -jar jar包名 & >a.txt          

### 依赖
- Git
- JDK
- Maven
- MySQL
### 步骤
- apt-get install git
- cd ..
- mkdir App
- cd App
- git clone https://github.com/saber-kings/community.git
- apt install maven
- mvn -v
- mvn clean compile package
- more src/main/resources/application.properties
- cp -i src/main/resources/application.properties src/main/resources/application-production.properties
- vim src/main/resources/application-production.properties
- mvn package
- java -jar -Dspring.profiles.active=production target/community-0.0.1-SNAPSHOT.jar
- ps -aux | grep java
- git pull

### 对象存储参数
#### UCloud云对象存储参数
- ucloud.ufile.public-key=TOKEN_f465454a-f3ab-4c9b-91a5-c7babf59cc38
- ucloud.ufile.private-key=18977163-f21e-4deb-a2e3-9bfa0b888ba5
- ucloud.ufile.bucket-name=mawen
- ucloud.ufile.region=cn-bj
- ucloud.ufile.suffix=ufileos.com
- ucloud.ufile.expires=315360000

#### 腾讯云对象存储参数
- tencent.cos.SecretId=asjfgasfjshgkfhjsgjkadsgahadkjsg4454
- tencent.cos.SecretKey=daslghghdfklhgfdkjhgjk
- tencent.cos.bucket=mawen-12345678
- tencent.cos.region=ap-beijing
- tencent.cos.allowPrefix=*
- tencent.cos.durationSeconds=3600
- tencent.cos.expires=315360000000

## 资料
[Spring 文档](https://spring.io/guides)   
[Spring Web文档](https://spring.io/guides/gs/serving-web-content/)  
[es社区](https://elasticsearch.cn/explore)  
[GitHub deploy key](https://developer.github.com/v3/guides/managing-deploy-keys/#deploy-keys)  
[BootStrap 文档](https://v3.bootcss.com/getting-started/)  
[GitHub OAuth](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)  
[菜鸟教程](https://www.runoob.com/)    
[Thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#setting-attribute-values)    
[Spring Dev Tool](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#using-boot-devtools)  
[Spring MVC](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html)  
[Markdown 插件](https://pandao.github.io/editor.md/)   
[COS SDK](https://cloud.tencent.com/document/product/436/10199)  
[UFfile SDK](https://github.com/ucloud/ufile-sdk-java)
[Count(*) VS Count(1)](https://mp.weixin.qq.com/s/Rwpke4BHu7Fz7KOpE2d3Lw)  

## 工具
[Git](https://git-scm.com/downloads)  
[Visual Paradigm](https://www.visual-paradigm.com/cn/)      
[Flyway](https://flywaydb.org/getstarted/firststeps/maven)     
[lombok](https://projectlombok.org/)  
[ctotree](https://www.octotree.io/)  
[Table of content sidebar](https://chrome.google.com/webstore/detail/table-of-contents-sidebar/ohohkfheangmbedkgechjkmbepeikkej)            
[One Tab](https://chrome.google.com/webstore/detail/chphlpgkkbolifaimnlloiipkdnihall)         
[Live Reload](https://chrome.google.com/webstore/detail/livereload/jnihajbhpnppcggbcgedagnkighmdlei/related)         
[Postman](https://chrome.google.com/webstore/detail/coohjcphdfgbiolnekdpbcijmhambjff)          
[LiveReload](https://chrome.google.com/webstore/detail/livereload/jnihajbhpnppcggbcgedagnkighmdlei)

# 脚本
```sql
CREATE TABLE USER
(
    ID int AUTO_INCREMENT PRIMARY KEY NOT NULL,
    ACCOUNT_ID VARCHAR(100),
    NAME VARCHAR(50),
    TOKEN VARCHAR(36),
    GMT_CREATE BIGINT,
    GMT_MODIFIED BIGINT
);
```
```bash
mvn flyway:migrate
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```
修复因为更改已存在的migration脚本，而出现的版本不一致冲突
```bash
mvn flyway:repair
```

### 注意
因为我使用了BootStrap日期选择器，而且想要回显所以
在日期格式的字段上加 @DateTimeFormat(pattern="yyyy-MM-dd")
然后使用th:value=${{user.birth}}的写法实现了回显
```java
public class User{
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date birth;
}
```

修复github授权用户查询方式过期以及授权登陆连接超时问题，
具体解决方式如下：
```java
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
                //新查询方式将 accessToken 添加到请求头中
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

```

## 更新日志
- 2019-10-18 修复 session 过期时间很短问题   
- 2019-10-18 修复 JSONObject 类多引用路径的警告问题   
- 2019-10-19 修复因为*和+号产生的搜索异常问题  
- 2019-10-19 修复提问页样式和效果问题
- 2019-10-19 添加首页按照最新、最热、零回复排序  
- 2019-10-19 修复搜索输入 ? 号出现异常问题
- 2019-10-19 修复图片大小限制和提问内容为空问题
- 2019-10-19 添加动态导航栏
- 2019-10-23 修复bug
- 2019-10-24 添加个人主页
- 2019-10-25 添加点赞功能
- 2019-10-25 修复图片上传访问时间太短的问题
- 2020-10-18 添加前缀并修复github授权用户查询以及登陆请求超时问题