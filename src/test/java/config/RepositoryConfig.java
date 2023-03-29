package config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@ComponentScan(basePackages = {"com.klymovych.school.consoleViev.validators"})
@EnableJpaRepositories({"com.klymovych.school.consoleViev.dao.implementsAll", "com.klymovych.school.consoleViev.dao.DaoJpa"})
@EntityScan("com.klymovych.school.consoleViev.model")
public class RepositoryConfig {

}


