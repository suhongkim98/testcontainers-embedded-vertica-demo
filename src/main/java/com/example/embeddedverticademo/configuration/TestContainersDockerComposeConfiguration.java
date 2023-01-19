package com.example.embeddedverticademo.configuration;

import com.example.embeddedverticademo.utils.DockerComposeUtils;
import com.example.embeddedverticademo.utils.VerticaContainerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.testcontainers.containers.ContainerState;
import org.testcontainers.containers.DockerComposeContainer;


/**
 * Docker Compose로 테스트컨테이너를 구성하는 예제
 * Generic Container를 직접 생성하는 방식과 도커컴포즈 방식 중 하나만 사용하세요
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(EmbeddedVerticaProperties.class)
public class TestContainersDockerComposeConfiguration
{
    /**
     * testcontainers를 docker-compose로 구성하여 생성
     *
     * @param environment
     * @param properties
     * @return
     */
    @Bean(name = "testcontainers", destroyMethod = "stop")
    public DockerComposeContainer testcontainers(
        ConfigurableEnvironment environment,
        EmbeddedVerticaProperties properties) throws Throwable
    {
        DockerComposeContainer composeContainer = DockerComposeUtils.createContainers(properties);
        composeContainer.start();

        ContainerState containerState = (ContainerState) composeContainer.getContainerByServiceName("vertica")
            .orElseThrow(() -> new RuntimeException());
        VerticaContainerUtils.initializeVerticaContainer(containerState, properties, environment);

        return composeContainer;
    }

}
