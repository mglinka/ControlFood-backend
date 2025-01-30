package pl.lodz.pl.it;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

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

  protected void logoutUser() {
    logout();
  }

  private String authorize(String email, String password) {
    String authEndpoint = "/api/v1/auth/authenticate";

    return RestAssured.given()
        .contentType(ContentType.JSON)
        .body(new LoginRequest(email, password))
        .when()
        .post(authEndpoint)
        .then()
        .statusCode(200)
        .extract()
        .asString();
  }

  private void logout() {
    String authEndpoint = "/api/v1/auth/logout";
    RestAssured.given()
        .contentType(ContentType.JSON)
        .when()
        .post(authEndpoint)
        .then()
        .statusCode(204);
  }

  record LoginRequest(String email, String password) {

  }
}
