package com.example.embeddedverticademo.configuration;

import com.example.embeddedverticademo.utils.VerticaContainerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.testcontainers.containers.GenericContainer;


/**
 * GenericContainer로 버티카 테스트 컨테이너 생성 예제
 * Docker Compose 방식과 이 방식 중 하나만 사용하세요
 */
@Slf4j
@TestConfiguration
@EnableConfigurationProperties(EmbeddedVerticaProperties.class)
public class EmbeddedVerticaConfiguration
{

    /**
     * GenericContainer를 이용하여 컨테이너 생성
     *
     * @param environment
     * @param properties
     * @return
     */
    @Bean(name = "embeddedVertica", destroyMethod = "stop")
    public GenericContainer<?> embeddedVertica(
        ConfigurableEnvironment environment,
        EmbeddedVerticaProperties properties) throws Throwable
    {
        GenericContainer<?> verticaContainer = VerticaContainerUtils.createVerticaContainer(properties);
        verticaContainer.start();

        // init vertica
        VerticaContainerUtils.initializeVerticaContainer(verticaContainer, properties, environment);
        return verticaContainer;
    }
}