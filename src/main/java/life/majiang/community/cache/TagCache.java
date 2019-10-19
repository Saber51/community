package life.majiang.community.cache;

import life.majiang.community.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther:luanzhaofei@outlook.com
 * @Date:2019/9/28
 * @Description:life.majiang.community.cache
 * @version:1.0
 */
public class TagCache {

    public static List<TagDTO> get() {
        List<TagDTO> tagDTOList = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("JavaScript", "PHP", "CSS", "HTML", "HTML5", "Java", "Node.js",
                "Python", "C++", "C", "GoLang", "Objective-C", "TypeScript", "Shell", "C#", "Swift", "Sass",
                "Bash", "R", "Less", "ASP.NET", "Lua", "Scala", "CoffeeScript", "ActionScript", "Rust",
                "ErLang", "Perl"));
        tagDTOList.add(program);
        TagDTO framework = new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("Laravel", "Spring", "Express", "Django", "Flask", "Yii",
                "Ruby-on-Rails", "Tornado", "Koa", "Struts"));
        tagDTOList.add(framework);


        TagDTO server = new TagDTO();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("Linux", "Nginx", "Docker", "Apache", "Ubuntu", "CentOS", "缓存 Tomcat",
                "负载均衡", "Unix", "Hadoop", "Windows-Server"));
        tagDTOList.add(server);

        TagDTO db = new TagDTO();
        db.setCategoryName("数据库和缓存");
        db.setTags(Arrays.asList("MySQL", "Redis", "MongoDB", "SQL", "Oracle", "NoSQL", "Memcached", "SQL Server",
                "PostgreSQL", "SQLite"));
        tagDTOList.add(db);

        TagDTO tool = new TagDTO();
        tool.setCategoryName("开发工具");
        tool.setTags(Arrays.asList("Git", "GitHub", "Visual-Studio-Code", "Vim", "Sublime-Text", "Xcode",
                "Intellij-IDEA", "Eclipse", "Maven", "IDE", "SVN", "Visual-Studio", "Atom", "Emacs",
                "TextMate", "hg-Mercurial-SCM"));
        tagDTOList.add(tool);


        return tagDTOList;
    }

    public static String filterInValid(String tags){
        String[] split = StringUtils.split(tags, ",");
        List<TagDTO> tagDTOList = get();

        List<String> tagList = tagDTOList.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> StringUtils.isBlank(t) || !tagList.contains(t)).collect(Collectors.joining(","));
        return invalid;
    }

    public static void main(String[] args) {
        int i = (5 - 1) >>> 1;
        System.out.println(i);
    }

}
