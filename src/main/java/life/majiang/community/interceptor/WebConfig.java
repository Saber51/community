package life.majiang.community.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Auther:luanzhaofei@outlook.com
 * @Date:2019/9/22
 * @Description:life.majiang.community.interceptor
 * @version:1.0
 */
@Configuration
//@EnableWebMvc
@ComponentScan
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public SessionInterceptor sessionInterceptor() {
        return new SessionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor()).addPathPatterns("/**");
    }

}
