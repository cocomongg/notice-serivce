package com.rsupport.notice.support;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;

public abstract class TestContainerSupport {

    private static final String MYSQL_IMAGE = "mysql:8";
    private static final String REDIS_IMAGE = "redis:latest";

    private static final JdbcDatabaseContainer MYSQL;
    private static final GenericContainer<?> REDIS;

    static {
        MYSQL = new MySQLContainer(MYSQL_IMAGE);
        MYSQL.start();

        REDIS = new GenericContainer<>(REDIS_IMAGE)
            .withExposedPorts(6379);
        REDIS.start();
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.driver-class-name", MYSQL::getDriverClassName);
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);

        registry.add("spring.redis.host", REDIS::getHost);
        registry.add("spring.redis.port", () -> REDIS.getMappedPort(6379));
    }
}
