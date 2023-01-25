package com.example.embeddedverticademo.utils;

import com.example.embeddedverticademo.configuration.EmbeddedVerticaProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.ContainerState;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;
import java.time.Duration;
import java.util.LinkedHashMap;

@Slf4j
public class VerticaContainerUtils
{
    private VerticaContainerUtils()
    {
    }

    public static void initializeVerticaContainer(
        ContainerState verticaContainer,
        EmbeddedVerticaProperties properties,
        ConfigurableEnvironment environment) throws Throwable
    {
        // init schema
        initVerticaSchema(verticaContainer, properties);

        updateVerticaEnvironment(
            verticaContainer.getHost(),
            verticaContainer.getMappedPort(properties.getPort()),
            properties,
            environment);
    }

    public static void initVerticaSchema(
        ContainerState container,
        EmbeddedVerticaProperties properties) throws IOException, InterruptedException
    {
        container.copyFileToContainer(MountableFile.forClasspathResource("vertica/init.sql"), "/init.sql");

        Container.ExecResult execResult = container.execInContainer(
            "/opt/vertica/bin/vsql",
            "-d",
            properties.getDatabase(),
            "-U",
            properties.getUser(),
            "-w",
            "password",
            "-h",
            "localhost",
            "--variable",
            "dpt=101",
            "-f",
            "/init.sql");

        log.info(execResult.getStdout());
    }

    public static GenericContainer<?> createVerticaContainer(EmbeddedVerticaProperties properties)
    {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("VERTICA_DB_NAME", properties.getDatabase());
        map.put("VERTICA_DB_PASSWORD", properties.getPassword());
        map.put("VERTICA_MEMDEBUG", "2");

        return new GenericContainer<>(DockerImageName.parse(properties.getDefaultDockerImage()))
            .withExposedPorts(properties.getPort())
            .withEnv(map)
            .waitingFor(Wait.forLogMessage(".*Database " + properties.getDatabase() + " created successfully..*", 1)
                .withStartupTimeout(Duration.ofMinutes(3))); // 3분 내로 해당 로그가 뜨면 컨테이너가 생성된 후 준비됐다고 판단, 더 좋은 방법이 있을까?
    }

    public static void updateVerticaEnvironment(
        String host,
        int mappedPort,
        EmbeddedVerticaProperties properties,
        ConfigurableEnvironment environment)
    {
        log.info("host: {}, port: {}", host, mappedPort);

        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("embedded.vertica.port", mappedPort);
        map.put("embedded.vertica.host", host);
        map.put("embedded.vertica.database", properties.getDatabase());
        map.put("embedded.vertica.user", properties.getUser());
        map.put("embedded.vertica.password", properties.getPassword());


        MapPropertySource propertySource = new MapPropertySource("embeddedVerticaInfo", map);
        environment.getPropertySources().addFirst(propertySource);

        log.info("Started Vertica server. Connection details: {}, ", map);
    }

}
