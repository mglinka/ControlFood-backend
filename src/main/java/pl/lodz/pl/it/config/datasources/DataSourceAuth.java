package pl.lodz.pl.it.config.datasources;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"pl.lodz.pl.it.auth.repository"},
        entityManagerFactoryRef = "authEntityManagerFactory", transactionManagerRef = "authTransactionManager")
public class DataSourceAuth {


    @Bean
    @ConfigurationProperties("app.auth")
    public DataSourceProperties authDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "authDataSource")
    @ConfigurationProperties("app.auth.hikari")
    public HikariDataSource authDataSource() {
        return authDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "authEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean authEntityManagerFactory(@Qualifier("authDataSource") HikariDataSource dataSource, EntityManagerFactoryBuilder builder) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
        properties.put("hibernate.implicit_naming_strategy", "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy");

        return builder.dataSource(dataSource)
                .properties(properties)
                .packages("pl.lodz.pl.it.entity").build();
    }


    @Bean(name = "authTransactionManager")
    public PlatformTransactionManager authTransactionManager(@Qualifier("authEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactory.getObject()));
    }
}
