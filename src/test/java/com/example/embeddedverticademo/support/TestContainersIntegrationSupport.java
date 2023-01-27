package com.example.embeddedverticademo.support;

import com.example.embeddedverticademo.configuration.EmbeddedVerticaConfiguration;
import com.example.embeddedverticademo.configuration.TestContainersDockerComposeConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

// EmbeddedVerticaConfiguration.class과 TestContainersDockerComposeConfiguration.class 중 하나만 임포트 해 빈으로 등록하여 테스트한다.
@Import({TestContainersDockerComposeConfiguration.class})
@SpringBootTest
public class TestContainersIntegrationSupport
{
}
