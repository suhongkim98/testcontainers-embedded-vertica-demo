package com.example.embeddedverticademo;

import com.example.embeddedverticademo.support.TestContainersIntegrationSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 버티카 테스트컨테이너를 빈으로 등록하여 @SpringBootTest를 통해 테스트코드에서 확인하는 예제
 */
public class EmbeddedVerticaSpringBootTest extends TestContainersIntegrationSupport
{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ConfigurableEnvironment environment;

    @Test
    @DisplayName("버티카 연결에 성공한다.")
    public void shouldConnectToVertica()
    {
        assertThat(jdbcTemplate.queryForObject("SELECT version()", String.class)).contains("Vertica Analytic Database");
    }

    @Test
    @DisplayName("임베디드 버티카 환경변수를 잘 불러온다.")
    public void propertiesAreAvailable()
    {
        assertThat(environment.getProperty("embedded.vertica.port")).isNotEmpty();
        assertThat(environment.getProperty("embedded.vertica.host")).isNotEmpty();
        assertThat(environment.getProperty("embedded.vertica.database")).isNotEmpty();
        assertThat(environment.getProperty("embedded.vertica.user")).isNotEmpty();
        assertThat(environment.getProperty("embedded.vertica.password")).isNotNull();
    }

    @Test
    @DisplayName("seed 값을 잘 불러온다.")
    public void shouldDiscoveryDBSeed()
    {
        assertThat(jdbcTemplate.queryForObject("SELECT count(*) FROM hello_world", Integer.class)).isEqualTo(4);
    }
}
