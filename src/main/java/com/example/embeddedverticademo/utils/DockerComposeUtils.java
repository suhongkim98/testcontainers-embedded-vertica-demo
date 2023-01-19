package com.example.embeddedverticademo.utils;

import com.example.embeddedverticademo.configuration.EmbeddedVerticaProperties;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.time.Duration;

@Slf4j
public class DockerComposeUtils
{
    private DockerComposeUtils()
    {
    }

    public static DockerComposeContainer createContainers(EmbeddedVerticaProperties properties)
    {
        return new DockerComposeContainer(new File("src/main/resources/docker-compose-local.yml"))
            .withExposedService("vertica", properties.getPort(), Wait.forListeningPort())
            .waitingFor("vertica",
                Wait.forLogMessage(".*Database " + properties.getDatabase() + " created successfully..*", 1)
                    .withStartupTimeout(Duration.ofMinutes(3))); // 3분 내로 해당 로그가 뜨면 컨테이너가 생성된 후 준비됐다고 판단, 더 좋은 방법이 있을까?
    }
}
