package pl.lodz.pl.it.integration.config;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.springframework.http.HttpStatus;

public class AbstractAuthorizationUtil {
  protected String getAccessTokenForAdmin() {
    return authorize("admin@gmail.com", "P@ssword123");
  }

  protected String getAccessTokenForSpecialist() {
    return authorize("specialist@example.com", "P@ssword123");
  }
  protected String getAccessTokenForUser() {
    return authorize("user@gmail.com", "P@ssword123");
  }

  protected String getAccessTokenFoCreatedUser() {return authorize("jan.kowalski@example.com", "SecurePass1!"); }

  private String authorize(String email, String password) {
    String authEndpoint = "/api/v1/auth/authenticate";

    return given()
        .contentType(ContentType.JSON)
        .body(new LoginRequest(email, password))
        .when()
        .post(authEndpoint)
        .then()
        .statusCode(200)
        .extract()
        .asString();
  }

  protected void logout(String token) {
    String authEndpoint = "/api/v1/auth/logout";
    given().auth()
        .oauth2(token)
        .contentType(ContentType.JSON)
        .when()
        .post(authEndpoint)
        .then()
        .statusCode(200);
  }



  protected void testCreateAccountSuccess() {
    String firstName = "Jan";
    String lastName = "Kowalski";
    String email = "jan.kowalski@example.com";
    String password = "SecurePass1!";
    String roleUser = "ROLE_USER";
    extracted(firstName, lastName, email, password, roleUser);
  }

  private void extracted(String firstName, String lastName, String email, String password,
      String roleUser) {
    given()
        .auth().oauth2(getAccessTokenForAdmin())
        .contentType("application/json")
        .body("{\"firstName\":\"" + firstName
            + "\",\"lastName\":\"" + lastName
            + "\",\"email\":\"" + email
            + "\",\"password\":\"" + password + "\",\"role\":\"" + roleUser + "\"}")
        .when()
        .post("/api/v1/accounts/create")
        .then()
        .statusCode(HttpStatus.CREATED.value());
  }

  record LoginRequest(String email, String password) {

  }
}
