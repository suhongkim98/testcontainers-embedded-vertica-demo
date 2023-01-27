package com.example.embeddedverticademo;

import com.example.embeddedverticademo.configuration.EmbeddedVerticaProperties;
import com.example.embeddedverticademo.support.TestContainersUnitSupport;
import com.vertica.jdbc.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.ContainerState;


import static org.assertj.core.api.Assertions.assertThat;

/**
 * @SpringBootTest 없이 테스트컨테이너 활용해 단위테스트 예제
 */
public class EmbeddedVerticaUnitTest extends TestContainersUnitSupport
{
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void beforeEach() throws Throwable
    {
        // 테스트 컨테이너가 실행되면 환경변수 host 가 바뀌므로 springBootTest가 아니라면 이를 jdbcTemplate에 수동으로 반영해야한다. 더 좋은 방법이 있으려나
        ContainerState containerState = (ContainerState) composeContainer.getContainerByServiceName("vertica")
            .orElseThrow(() -> new RuntimeException());
        EmbeddedVerticaProperties properties = new EmbeddedVerticaProperties();

        DataSource dataSource = new DataSource();
        dataSource.setHost(containerState.getHost());
        dataSource.setPort(containerState.getMappedPort(properties.getPort()));
        dataSource.setDatabase(properties.getDatabase());
        dataSource.setUser(properties.getUser());
        dataSource.setUserID(properties.getUser());

        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    @DisplayName("버티카 연결에 성공한다.")
    public void shouldConnectToVertica()
    {
        assertThat(jdbcTemplate.queryForObject("SELECT version()", String.class)).contains("Vertica Analytic Database");
    }

    @Test
    @DisplayName("seed 값을 잘 불러온다.")
    public void shouldDiscoveryDBSeed()
    {
        assertThat(jdbcTemplate.queryForObject("SELECT count(*) FROM hello_world", Integer.class)).isEqualTo(4);
    }
}
