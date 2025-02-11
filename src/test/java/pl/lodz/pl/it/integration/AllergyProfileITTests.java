package pl.lodz.pl.it.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import pl.lodz.pl.it.integration.config.AbstractIntegrationTest;

public class AllergyProfileITTests extends AbstractIntegrationTest {

  @BeforeAll
  public void befit(){
    testCreateAccountSuccess();
  }
  @Test
  public void testGetAllProfiles() {
    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .when()
        .get("/api/v1/allergy-profiles")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("$", notNullValue())
        .body("size()", greaterThan(0));
  }

  @Test
  public void testGetAllProfilesForbidden() {
    given()
        .auth().oauth2(getAccessTokenForAdmin())
        .when()
        .get("/api/v1/allergy-profiles")
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value());
  }

  @Test
  public void testGetAllergyProfileById() {
    UUID id = UUID.fromString("f472ad38-70a9-418d-8aba-2851c5ecc174");

    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .when()
        .get("/api/v1/allergy-profiles/{id}", id)
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("$", notNullValue())
        .header(HttpHeaders.ETAG, notNullValue());
  }

  @Test
  public void testGetAllergyProfileByIdNotFound() {
    UUID id = UUID.fromString("f572ad38-70a9-418d-8aba-2851c5ecc174");

    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .when()
        .get("/api/v1/allergy-profiles/{id}", id)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }

  @Test
  public void testGetAllergyProfileByIdForbidden() {
    UUID id = UUID.fromString("f572ad38-70a9-418d-8aba-2851c5ecc174");

    given()
        .auth().oauth2(getAccessTokenForAdmin())
        .when()
        .get("/api/v1/allergy-profiles/{id}", id)
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value());
  }

  @Test
  public void testGetAllergyProfileByAccountId() {
    UUID accountId = UUID.fromString("038e200a-188c-4eb5-acf5-00f296efe0f9");

    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .when()
        .get("/api/v1/allergy-profiles/byAccount/{id}", accountId)
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("$", notNullValue());
  }
  @Test
  public void testGetAllergyProfileByAccountIdNotFound() {
    UUID accountId = UUID.fromString("138e200a-188c-4eb5-acf5-00f296efe0f9");

    given()
        .auth().oauth2(getAccessTokenForUser())
        .when()
        .get("/api/v1/allergy-profiles/byAccount/{id}", accountId)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body("$", notNullValue());
  }

  @Test
  public void testGetAllergyProfileByAccountIdForbidden() {
    UUID accountId = UUID.fromString("138e200a-188c-4eb5-acf5-00f296efe0f9");

    given()
        .auth().oauth2(getAccessTokenForAdmin())
        .when()
        .get("/api/v1/allergy-profiles/byAccount/{id}", accountId)
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value());
  }

  @Test
  public void testAssignAllergyProfile() {
    String id1 = given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .contentType("application/json")
        .body("{\"name\":\"nowy1\",\"allergens\":[{\"allergen_id\":\"a92e583c-3521-4e98-b4b7-32cb87baf8eb\",\"intensity\":\"\"},{\"allergen_id\":\"e4a9b3c5-f741-4c1c-8654-4a26f61e9b73\",\"intensity\":\"\"}]}")
        .when()
        .post("/api/v1/allergy-profile-schemas/create")
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .extract()
        .asString();

    String id2 = given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .contentType("application/json")
        .body("{\"name\":\"nowy2\",\"allergens\":[{\"allergen_id\":\"a92e583c-3521-4e98-b4b7-32cb87baf8eb\",\"intensity\":\"\"}]}")
        .when()
        .post("/api/v1/allergy-profile-schemas/create")
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .extract()
        .asString();

    id1 = id1.replaceAll("\"", "");
    id2 = id2.replaceAll("\"", "");

    given()
        .auth().oauth2(getAccessTokenFoCreatedUser())
        .contentType("application/json")
        .body("{\"schema_ids\":[\"" + id1 + "\",\"" + id2 + "\"],\"intensity\":\"medium\"}")
        .when()
        .post("/api/v1/allergy-profiles/assignProfile")
        .then()
        .statusCode(HttpStatus.OK.value());
  }

  @Test
  public void testUpdateAllergyProfileSuccess() {
    String accountId = "038e200a-188c-4eb5-acf5-00f296efe0f9";

    given()
        .auth().oauth2(getAccessTokenForUser())
        .contentType("application/json")
        .body("{\"allergens\":[{\"allergen_id\":\"aa4b8769-0d7d-4cfb-870c-91e8b70e8542\",\"intensity\":\"high\"},{\"allergen_id\":\"e4a9b3c5-f741-4c1c-8654-4a26f61e9b73\",\"intensity\":\"high\"},{\"allergen_id\":\"b861387d-f5cc-42a0-8dc5-c8fbd40eb547\",\"intensity\":\"medium\"},{\"allergen_id\":\"c92b2d99-40bb-4e29-a7f2-f6d5a9e6cc13\",\"intensity\":\"low\"}]}")
        .when()
        .put("/api/v1/allergy-profiles/update/{accountId}", accountId)
        .then()
        .statusCode(HttpStatus.OK.value());
  }
  @Test
  public void testUpdateAllergyProfileNotFound() {
    String accountId = "038e200a-188c-4eb5-acf5-00f296efe0f1";

    given()
        .auth().oauth2(getAccessTokenForUser())
        .contentType("application/json")
        .body("{\"allergens\":[{\"allergen_id\":\"aa4b8769-0d7d-4cfb-870c-91e8b70e8542\",\"intensity\":\"high\"},{\"allergen_id\":\"e4a9b3c5-f741-4c1c-8654-4a26f61e9b73\",\"intensity\":\"high\"},{\"allergen_id\":\"b861387d-f5cc-42a0-8dc5-c8fbd40eb547\",\"intensity\":\"medium\"},{\"allergen_id\":\"c92b2d99-40bb-4e29-a7f2-f6d5a9e6cc13\",\"intensity\":\"low\"}]}")
        .when()
        .put("/api/v1/allergy-profiles/update/{accountId}", accountId)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }

  @Test
  public void testUpdateAllergyProfileForbidden() {
    String accountId = "038e200a-188c-4eb5-acf5-00f296efe0f9";

    given()
        .auth().oauth2(getAccessTokenForAdmin())
        .contentType("application/json")
        .body("{\"allergens\":[{\"allergen_id\":\"aa4b8769-0d7d-4cfb-870c-91e8b70e8542\",\"intensity\":\"high\"},{\"allergen_id\":\"e4a9b3c5-f741-4c1c-8654-4a26f61e9b73\",\"intensity\":\"high\"},{\"allergen_id\":\"b861387d-f5cc-42a0-8dc5-c8fbd40eb547\",\"intensity\":\"medium\"},{\"allergen_id\":\"c92b2d99-40bb-4e29-a7f2-f6d5a9e6cc13\",\"intensity\":\"low\"}]}")
        .when()
        .put("/api/v1/allergy-profiles/update/{accountId}", accountId)
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value());
  }

  @Test
  public void testUpdateAllergyProfileBadRequest() {
    String accountId = "038e200a-188c-4eb5-acf5-00f296efe0f9";

    given()
        .auth().oauth2(getAccessTokenForUser())
        .contentType("application/json")
        .body("{}")
        .when()
        .put("/api/v1/allergy-profiles/update/{accountId}", accountId)
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value());
  }





}
