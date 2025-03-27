package com.strikezone.strikezone_backend.global.config.replica;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    // 마스터 DB 설정
    @Bean
    @ConfigurationProperties("spring.datasource.master")
    public DataSourceProperties masterDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "masterDataSource")
    public DataSource masterDataSource() {
        return masterDataSourceProperties().initializeDataSourceBuilder().build();
    }

    // 리플리카 DB 설정
    @Bean
    @ConfigurationProperties("spring.datasource.replica")
    public DataSourceProperties replicaDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "replicaDataSource")
    public DataSource replicaDataSource() {
        return replicaDataSourceProperties().initializeDataSourceBuilder().build();
    }

    // RoutingDataSource를 통해 읽기/쓰기를 분기
    @Primary
    @Bean
    public DataSource dataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                 @Qualifier("replicaDataSource") DataSource replicaDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(RoutingDataSource.DataSourceType.MASTER, masterDataSource);
        targetDataSources.put(RoutingDataSource.DataSourceType.REPLICA, replicaDataSource);

        RoutingDataSource routingDataSource = new RoutingDataSource();
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);
        return routingDataSource;
    }
}
