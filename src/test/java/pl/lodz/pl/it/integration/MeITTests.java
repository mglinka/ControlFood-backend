package pl.lodz.pl.it.integration;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.lodz.pl.it.integration.config.AbstractIntegrationTest;

public class MeITTests extends AbstractIntegrationTest {

  @Test
  public void testChangePassword_Success() {
    String newPassword = "NewPassword123!";
    String confirmationPassword = newPassword;

    given()
        .auth().oauth2(getAccessTokenForAdmin())
        .contentType(ContentType.JSON)
        .body("{"
            + "\"newPassword\":\"" + newPassword + "\","
            + "\"confirmationPassword\":\"" + confirmationPassword + "\""
            + "}")
        .when()
        .post("api/v1/me/change-password")
        .then()
        .statusCode(HttpStatus.OK.value());
  }

  @Test
  public void testChangePassword_BadRequest() {
    String newPassword = "NewPassword123!";
    String confirmationPassword = "newPassword";

    given()
        .auth().oauth2(getAccessTokenForAdmin())
        .contentType(ContentType.JSON)
        .body("{"
            + "\"newPassword\":\"" + newPassword + "\","
            + "\"confirmationPassword\":\"" + confirmationPassword + "\""
            + "}")
        .when()
        .post("api/v1/me/change-password")
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value());
  }

}
