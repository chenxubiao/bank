package cn.longhaiyan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by chenxb on 17-3-30.
 */
@SpringBootApplication
@EntityScan(basePackages = {"cn.longhaiyan.user.domain", "cn.longhaiyan.attachment.domain", "cn.longhaiyan.account.domain"})
@ServletComponentScan
@ComponentScan(basePackages = "cn.longhaiyan")
@EnableTransactionManagement
//@EnableJpaRepositories(basePackages = {"cn.longhaiyan.picture.repository","cn.longhaiyan.tag.repository","cn.longhaiyan.project.repository","cn.longhaiyan.user.repository"})
public class Application extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
