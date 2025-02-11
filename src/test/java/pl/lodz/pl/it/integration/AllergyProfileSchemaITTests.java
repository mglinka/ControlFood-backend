package pl.lodz.pl.it.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsNot.not;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.lodz.pl.it.integration.config.AbstractIntegrationTest;

public class AllergyProfileSchemaITTests extends AbstractIntegrationTest {

  @Test
  public void testGetAllProfilesSchemas() {
    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .contentType("application/json")
        .body("{\"name\":\"Test\",\"allergens\":[{\"allergen_id\":\"a92e583c-3521-4e98-b4b7-32cb87baf8eb\",\"intensity\":\"\"},{\"allergen_id\":\"715cd2e2-31a8-4e94-a0c3-5fb4290a0872\",\"intensity\":\"\"},{\"allergen_id\":\"5920d68d-73e2-4a62-8436-dcbb3e3b1787\",\"intensity\":\"\"},{\"allergen_id\":\"caf827a5-5649-4e1d-a35a-16314dcb374a\",\"intensity\":\"\"}]}")
        .when()
        .post("/api/v1/allergy-profile-schemas/create")
        .then()
        .statusCode(HttpStatus.CREATED.value());
    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .when()
        .get("/api/v1/allergy-profile-schemas")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("$", notNullValue())
        .body("size()", not(0));
  }

  @Test
  public void testGetAllergyProfileSchemaById() {
    String id = given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .contentType("application/json")
        .body("{\"name\":\"nowy\",\"allergens\":[{\"allergen_id\":\"a92e583c-3521-4e98-b4b7-32cb87baf8eb\",\"intensity\":\"\"},{\"allergen_id\":\"e4a9b3c5-f741-4c1c-8654-4a26f61e9b73\",\"intensity\":\"\"}]}")
        .when()
        .post("/api/v1/allergy-profile-schemas/create")
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .extract()
        .asString();

    id = id.replaceAll("\"", "");

    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .when()
        .get("/api/v1/allergy-profile-schemas/{id}", id)
        .then()
        .statusCode(HttpStatus.FOUND.value())
        .body("$", notNullValue());
  }


  @Test
  public void testCreateAllergyProfileSchema() {
    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .contentType("application/json")
        .body("{\"name\":\"Test1\",\"allergens\":[{\"allergen_id\":\"a92e583c-3521-4e98-b4b7-32cb87baf8eb\",\"intensity\":\"\"},{\"allergen_id\":\"715cd2e2-31a8-4e94-a0c3-5fb4290a0872\",\"intensity\":\"\"},{\"allergen_id\":\"5920d68d-73e2-4a62-8436-dcbb3e3b1787\",\"intensity\":\"\"},{\"allergen_id\":\"caf827a5-5649-4e1d-a35a-16314dcb374a\",\"intensity\":\"\"}]}")
        .when()
        .post("/api/v1/allergy-profile-schemas/create")
        .then()
        .statusCode(HttpStatus.CREATED.value());
  }
  @Test
  public void testCreateAllergyProfileSchemaBad() {
    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .contentType("application/json")
        .body("{\"name\":\"Test1\",\"allergens\":[{\"allergen_id\":\"a92e583c-3521-4e98-b4b7-32cb87baf8eb\",\"intensity\":\"\"},{\"allergen_id\":\"715cd2e2-31a8-4e94-a0c3-5fb4290a0872\",\"intensity\":\"\"},{\"allergen_id\":\"5920d68d-73e2-4a62-8436-dcbb3e3b1787\",\"intensity\":\"\"},{\"allergen_id\":\"caf827a5-5649-4e1d-a35a-16314dcb374a\",\"intensity\":\"\"}]}")
        .when()
        .post("/api/v1/allergy-profile-schemas/create")
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value());
  }
  @Test
  public void testCreateAllergyProfileSchemaForbidden() {
    given()
        .auth().oauth2(getAccessTokenForAdmin())  // Non-specialist role
        .contentType("application/json")
        .body("{\"name\":\"Test12\",\"allergens\":[{\"allergen_id\":\"a92e583c-3521-4e98-b4b7-32cb87baf8eb\",\"intensity\":\"\"},{\"allergen_id\":\"715cd2e2-31a8-4e94-a0c3-5fb4290a0872\",\"intensity\":\"\"},{\"allergen_id\":\"5920d68d-73e2-4a62-8436-dcbb3e3b1787\",\"intensity\":\"\"},{\"allergen_id\":\"caf827a5-5649-4e1d-a35a-16314dcb374a\",\"intensity\":\"\"}]}")
        .when()
        .post("/api/v1/allergy-profile-schemas/create")
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value());
  }

  @Test
  public void testCreateAllergyProfileSchemaForbiddenUser() {
    given()
        .auth().oauth2(getAccessTokenForUser())  // Non-specialist role
        .contentType("application/json")
        .body("{\"name\":\"Test12\",\"allergens\":[{\"allergen_id\":\"a92e583c-3521-4e98-b4b7-32cb87baf8eb\",\"intensity\":\"\"},{\"allergen_id\":\"715cd2e2-31a8-4e94-a0c3-5fb4290a0872\",\"intensity\":\"\"},{\"allergen_id\":\"5920d68d-73e2-4a62-8436-dcbb3e3b1787\",\"intensity\":\"\"},{\"allergen_id\":\"caf827a5-5649-4e1d-a35a-16314dcb374a\",\"intensity\":\"\"}]}")
        .when()
        .post("/api/v1/allergy-profile-schemas/create")
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value());
  }


  @Test
  public void testDeleteAllergyProfileSchema() {
    String id = given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .contentType("application/json")
        .body("{\"name\":\"nowy2\",\"allergens\":[{\"allergen_id\":\"a92e583c-3521-4e98-b4b7-32cb87baf8eb\",\"intensity\":\"\"},{\"allergen_id\":\"e4a9b3c5-f741-4c1c-8654-4a26f61e9b73\",\"intensity\":\"\"}]}")
        .when()
        .post("/api/v1/allergy-profile-schemas/create")
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .extract()
        .asString();

    id = id.replaceAll("\"", "");
    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .when()
        .delete("/api/v1/allergy-profile-schemas/delete/{id}", id)
        .then()
        .statusCode(HttpStatus.OK.value());
  }

  @Test
  public void testDeleteAllergyProfileSchemaNotFound() {
    String nonExistentId = "00000000-0000-0000-0000-000000000000";
    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .when()
        .delete("/api/v1/allergy-profile-schemas/delete/{id}", nonExistentId)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }

  @Test
  public void testEditAllergyProfileSchemaSuccess() {
    String id = given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .contentType("application/json")
        .body("{\"name\":\"nowy3\",\"allergens\":[{\"allergen_id\":\"a92e583c-3521-4e98-b4b7-32cb87baf8eb\",\"intensity\":\"\"},{\"allergen_id\":\"e4a9b3c5-f741-4c1c-8654-4a26f61e9b73\",\"intensity\":\"\"}]}")
        .when()
        .post("/api/v1/allergy-profile-schemas/create")
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .extract()
        .asString();

    id = id.replaceAll("\"", "");

    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .contentType("application/json")
        .body("{\"schema_id\":\"" + id + "\",\"name\":\"UpdatedSchema\",\"allergens\":[{\"allergen_id\":\"e4a9b3c5-f741-4c1c-8654-4a26f61e9b73\"}]}") // Usunięcie jednego alergenu i brak pola intensity
        .when()
        .put("/api/v1/allergy-profile-schemas/edit")
        .then()
        .statusCode(HttpStatus.OK.value());
  }

  @Test
  public void testEditAllergyProfileSchemaForbidden() {
    String id = given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .contentType("application/json")
        .body("{\"name\":\"nowy48\",\"allergens\":[{\"allergen_id\":\"a92e583c-3521-4e98-b4b7-32cb87baf8eb\",\"intensity\":\"\"},{\"allergen_id\":\"e4a9b3c5-f741-4c1c-8654-4a26f61e9b73\",\"intensity\":\"\"}]}")
        .when()
        .post("/api/v1/allergy-profile-schemas/create")
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .extract()
        .asString();

    id = id.replaceAll("\"", "");

    given()
        .auth().oauth2(getAccessTokenForUser())
        .contentType("application/json")
        .body("{\"schema_id\":\"" + id + "\",\"name\":\"UpdatedSchema\",\"allergens\":[{\"allergen_id\":\"e4a9b3c5-f741-4c1c-8654-4a26f61e9b73\"}]}") // Usunięcie jednego alergenu i brak pola intensity
        .when()
        .put("/api/v1/allergy-profile-schemas/edit")
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value());
  }


  @Test
  public void testEditAllergyProfileSchemaBadRequest() {
    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .contentType("application/json")
        .body("{\"name\":\"nowy4\",\"allergens\":[{\"allergen_id\":\"a92e583c-3521-4e98-b4b7-32cb87baf8eb\",\"intensity\":\"\"},{\"allergen_id\":\"e4a9b3c5-f741-4c1c-8654-4a26f61e9b73\",\"intensity\":\"\"}]}")
        .when()
        .post("/api/v1/allergy-profile-schemas/create")
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .extract()
        .asString();

    String id = given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .contentType("application/json")
        .body("{\"name\":\"nowy45\",\"allergens\":[{\"allergen_id\":\"a92e583c-3521-4e98-b4b7-32cb87baf8eb\",\"intensity\":\"\"},{\"allergen_id\":\"e4a9b3c5-f741-4c1c-8654-4a26f61e9b73\",\"intensity\":\"\"}]}")
        .when()
        .post("/api/v1/allergy-profile-schemas/create")
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .extract()
        .asString();

    id = id.replaceAll("\"", "");

    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .contentType("application/json")
        .body("{\"schema_id\":\"" + id + "\",\"name\":\"nowy4\",\"allergens\":[{\"allergen_id\":\"e4a9b3c5-f741-4c1c-8654-4a26f61e9b73\"}]}")
        .when()
        .put("/api/v1/allergy-profile-schemas/edit")
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value());
  }

}
