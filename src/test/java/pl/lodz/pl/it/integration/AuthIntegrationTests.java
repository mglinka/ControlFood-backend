package pl.lodz.pl.it.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsNot.not;

import io.restassured.http.ContentType;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import pl.lodz.pl.it.integration.config.AbstractIntegrationTest;
import pl.lodz.pl.it.entity.Account;
import pl.lodz.pl.it.mok.repository.AccountRepository;

public class AuthIntegrationTests extends AbstractIntegrationTest {
  @Autowired
  private AccountRepository accountRepository;

  @BeforeAll
  public void befit() {
    testCreateAccountSuccess();
  }

  @Test
  public void updateAccount() {
    Optional<Account> byEmail = accountRepository.findByEmail("jan.kowalski@example.com");

    String etag = given()
        .auth().oauth2(super.getAccessTokenFoCreatedUser())
        .contentType(ContentType.JSON)
        .header("If-Match", "1")
        .when()
        .get("api/v1/account/" + byEmail.map(Account::getId).orElse(null))
        .then()
        .statusCode(HttpStatus.OK.value())
        .extract().header("Etag");

    given()
        .auth().oauth2(super.getAccessTokenFoCreatedUser())
        .contentType(ContentType.JSON)
        .header("If-Match", etag)
        .body("{\"firstName\":\"Karol\",\"lastName\":\"Balon\",\"version\":1}")
        .when()
        .put("api/v1/me/updateInfo")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body(Matchers.equalTo("Dane zostały zaktualizowane"));
  }
  @Test
  public void updateAccountEtagConflict() {
    given()
        .auth().oauth2(super.getAccessTokenFoCreatedUser())
        .contentType(ContentType.JSON)
        .header("If-Match", "invalid")
        .body("{\"firstName\":\"Karol\",\"lastName\":\"Balon\",\"version\":1}")
        .when()
        .put("api/v1/me/updateInfo")
        .then()
        .statusCode(HttpStatus.CONFLICT.value());
  }

}
