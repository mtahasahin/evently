package com.github.mtahasahin.evently;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(ResetDatabaseStateExtension.class)
@AutoConfigureMockMvc
public abstract class BaseIT {

    static final PostgreSQLContainer<?> postgreSQLContainer;
    static final ElasticsearchContainer elasticsearchContainer;

    static {
        postgreSQLContainer =
                new PostgreSQLContainer<>("postgres:14.1")
                        .withDatabaseName("evently")
                        .withUsername("postgres")
                        .withPassword("mysecretpassword")
                        .withReuse(true);

        elasticsearchContainer =
                new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.10.0")
                        .withReuse(true);


        postgreSQLContainer.start();
        elasticsearchContainer.start();
    }

    @DynamicPropertySource
    static void datasourceConfig(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("elasticsearch.host", elasticsearchContainer::getHttpHostAddress);
        registry.add("spring.jpa.properties.hibernate.search.backend.uris", elasticsearchContainer::getHttpHostAddress);
    }


}
