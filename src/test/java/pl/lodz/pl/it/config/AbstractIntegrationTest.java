package pl.lodz.pl.it.config;

import io.restassured.RestAssured;
import javax.sql.DataSource;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)  // Restart app after each class
@ContextConfiguration(initializers = {AbstractIntegrationTest.Initializer.class})
@ActiveProfiles({"test"})
public abstract class AbstractIntegrationTest extends AbstractAuthorizationUtil {

  @LocalServerPort
  private int port;

  @ClassRule
  public static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
      "postgres:16")
      .withUsername("postgres")
      .withPassword("postgres")
      .withDatabaseName("postgres");


  @Autowired
  private DataSource dataSource;

  @AfterAll
  void afterAll() {
    postgresContainer.stop();
  }

  @BeforeAll
  void beforeAll() {
    RestAssured.baseURI = "http://localhost:" + port;
  }

  @Test
  void testDatabaseConnection() {
    assert (dataSource != null);
  }

  static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      postgresContainer.start();
      TestPropertyValues.of(
          "app.datasource.jdbc-url=" + postgresContainer.getJdbcUrl(),
          "app.datasource.username=" + postgresContainer.getUsername(),
          "app.datasource.password=" + postgresContainer.getPassword(),

          "app.mok.jdbc-url=" + postgresContainer.getJdbcUrl(),
          "app.mok.username=" + postgresContainer.getUsername(),
          "app.mok.password=" + postgresContainer.getPassword(),

          "app.mopa.jdbc-url=" + postgresContainer.getJdbcUrl(),
          "app.mopa.username=" + postgresContainer.getUsername(),
          "app.mopa.password=" + postgresContainer.getPassword(),

          "app.auth.jdbc-url=" + postgresContainer.getJdbcUrl(),
          "app.auth.username=" + postgresContainer.getUsername(),
          "app.auth.password=" + postgresContainer.getPassword()
      ).applyTo(configurableApplicationContext.getEnvironment());
    }
  }
}
