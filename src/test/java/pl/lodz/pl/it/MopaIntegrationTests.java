package pl.lodz.pl.it;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.lodz.pl.it.entity.Account;
import pl.lodz.pl.it.entity.Role;
import pl.lodz.pl.it.entity.allergy.AllergenType;
import pl.lodz.pl.it.mopa.dto.CreateAllergenDTO;
import pl.lodz.pl.it.mopa.dto.product.UpdateAllergenDTO;
import pl.lodz.pl.it.utils._enum.AccountRoleEnum;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsEqual.equalTo;

public class MopaIntegrationTests extends AbstractIntegrationTest {

  private Account testAccount;
  private Role role;
  private UUID testId;

  private String token;


  @BeforeEach
  public void setup() {
    testId = UUID.fromString("038e200a-188c-4eb5-acf5-00f296efe0f9");
    role = new Role(AccountRoleEnum.ROLE_USER);
    testAccount = new Account(testId, "user@gmail.com", "User", "UserOne",
        "$2a$10$jXCmzT7d0y0i2EUlRY9mNun.sV7MqaHvMW/Abp/xckUSUOaxjrAFW", 0L, role, true, null);

  }


  @Test
  public void testGetAllAllergens() {
    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .when()
        .get("/api/v1/allergens")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("size()", greaterThan(0));
  }

  @Test
  public void testAddAllergen() {
    CreateAllergenDTO createAllergenDTO = new CreateAllergenDTO();
    AllergenType allergenType = AllergenType.ALLERGEN;
    createAllergenDTO.setName("test");
    createAllergenDTO.setType(allergenType);

    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .contentType(ContentType.JSON)
        .body(createAllergenDTO)
        .when()
        .post("/api/v1/allergens/add")
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .body("name", equalTo("test"));
  }

  @Test
  public void testEditAllergen() {
    UUID allergenId = UUID.fromString("23bca9d7-8c3f-4a74-b8f2-1b673d09fa53");
    UpdateAllergenDTO updateAllergenDTO = new UpdateAllergenDTO();
    updateAllergenDTO.setName("newName");
    AllergenType allergenType = AllergenType.ALLERGEN;
    updateAllergenDTO.setType(allergenType);

    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .contentType(ContentType.JSON)
        .body(updateAllergenDTO)
        .when()
        .put("/api/v1/allergens/edit/{id}", allergenId)
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("name", equalTo("newName"));
  }

  @Test
  public void testRemoveAllergen() {
    UUID allergenId = UUID.fromString("23bca9d7-8c3f-4a74-b8f2-1b673d09fa53");

    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .when()
        .delete("/api/v1/allergens/remove/{id}", allergenId)
        .then()
        .statusCode(HttpStatus.OK.value())
        .body(equalTo("Allergen removed successfully."));  // Sprawdzamy komunikat
  }

}
