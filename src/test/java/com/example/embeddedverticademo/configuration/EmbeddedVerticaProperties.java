package com.example.embeddedverticademo.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("embedded.vertica")
public class EmbeddedVerticaProperties
{
    String host = "localhost";
    Integer port = 5433;
    String database = "test";
    String user = "dbadmin";
    String password = "";

    public String getDefaultDockerImage()
    {
        return "vertica/vertica-ce:10.1.1-0";
    }
}
