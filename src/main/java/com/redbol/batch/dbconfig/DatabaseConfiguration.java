package com.redbol.batch.dbconfig;
import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
@Configuration
@PropertySource("classpath:/application.yml") //①
public class DatabaseConfiguration {
	@Bean
    @ConfigurationProperties(prefix="spring.datasource.hikari")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    //③
    @Bean
    public DataSource dataSource() throws Exception {
        DataSource dataSource = new HikariDataSource(hikariConfig());
        System.out.println(dataSource.toString());
        return dataSource;
    }
}
