## 码匠社区

## 部署
### 环境
Ubuntu 18.04.1 LTS (GNU/Linux 4.15.0-29-generic x86_64)
#### 配置
(1)更新系统
```bash
$ sudo apt-get install yum
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
### 依赖
- Git
- JDK
- Maven
- MySQL
### 步骤
- apt-get install git
- cd ..
- mkdir Ap
- cd App
- 

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