package com.example.embeddedverticademo;

import com.example.embeddedverticademo.configuration.EmbeddedVerticaProperties;
import com.example.embeddedverticademo.utils.DockerComposeUtils;
import com.example.embeddedverticademo.utils.VerticaContainerUtils;
import org.testcontainers.containers.ContainerState;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class TestContainersIntegrationSupport
{
    @Container
    protected static final DockerComposeContainer composeContainer;

    static
    {
        EmbeddedVerticaProperties properties = new EmbeddedVerticaProperties();
        composeContainer = DockerComposeUtils.createContainers(properties);
        composeContainer.start();

        try
        {
            // init schema
            ContainerState containerState = (ContainerState) composeContainer.getContainerByServiceName("vertica")
                .orElseThrow(() -> new RuntimeException());
            VerticaContainerUtils.initVerticaSchema(containerState, properties);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }
}
