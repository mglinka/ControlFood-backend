package pl.lodz.pl.it.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;

import io.restassured.http.ContentType;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import pl.lodz.pl.it.auth.repository.AccountConfirmationRepository;
import pl.lodz.pl.it.integration.config.AbstractIntegrationTest;
import pl.lodz.pl.it.entity.Account;
import pl.lodz.pl.it.entity.AccountConfirmation;
import pl.lodz.pl.it.entity.Role;
import pl.lodz.pl.it.entity.allergy.AllergenType;
import pl.lodz.pl.it.mok.repository.AccountRepository;
import pl.lodz.pl.it.mopa.dto.CreateAllergenDTO;
import pl.lodz.pl.it.mopa.dto.product.UpdateAllergenDTO;
import pl.lodz.pl.it.utils._enum.AccountRoleEnum;

public class MopaIntegrationTests extends AbstractIntegrationTest {

  private Account testAccount;
  private Role role;
  private UUID testId;

  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private AccountConfirmationRepository accountConfirmationRepository;

  @BeforeAll
  public void befit() {
    testCreateAccountSuccess();
  }

  @BeforeEach
  public void setup() {
    testId = UUID.fromString("038e200a-188c-4eb5-acf5-00f296efe0f9");
    role = new Role(AccountRoleEnum.ROLE_USER);
    testAccount = new Account(testId, "user@gmail.com", "User", "UserOne",
        "$2a$10$jXCmzT7d0y0i2EUlRY9mNun.sV7MqaHvMW/Abp/xckUSUOaxjrAFW", 0L, role, true, null);

  }

  //allergens
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

  //units
  @Test
  public void testGetAllUnits() {
    given()
        .auth().oauth2(
            getAccessTokenForSpecialist())  // Obtain OAuth2 access token (implement this method)
        .when()
        .get("/api/v1/units")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("$", notNullValue())
        .body("size()", not(0));
  }

  @Test
  public void testGetAllPackageTypes() {
    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .when()
        .get("/api/v1/package-types")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("$", notNullValue())
        .body("size()", not(0));
  }

  @Test
  public void testGetAllNutritionalValueNames() {
    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .when()
        .get("/api/v1/nutritional-value/names")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("$", notNullValue())
        .body("size()", not(0));
  }

  @Test
  public void testGetAllNutritionalValueGroupNames() {
    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .when()
        .get("/api/v1/nutritional-value/group-names")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("$", notNullValue())
        .body("size()", not(0));
  }


  @Test
  public void testGetAllCategories() {
    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .when()
        .get("/api/v1/categories")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("$", notNullValue())
        .body("size()", not(0));
  }

  @Test
  public void testCreateProduct() {
    String jsonPayload = "{\"ean\":\"5901138046050\",\"producerDTO\":{\"name\":\"GlinkaCORP sp.zoo\",\"address\":\"Lodz Limanka\",\"countryCode\":2,\"contact\":\"glinka@onet.pl\",\"nip\":\"1231231231\",\"rmsd\":1},\"productName\":\"Gliceryna\",\"category\":{\"id\":\"c759d843-5e26-4155-b470-b2e7c3469078\",\"name\":\"Mięso\"},\"productDescription\":\"\",\"productQuantity\":1,\"unitDTO\":{\"name\":\"kcal\"},\"packageTypeDTO\":{\"name\":\"tacka biodegradowalna\"},\"country\":\"Polska\",\"labelDTO\":{\"storage\":\"\",\"durability\":\"\",\"instructionsAfterOpening\":\"\",\"preparation\":\"\",\"allergens\":\"\",\"image\":\"\"},\"portionDTO\":{\"portionQuantity\":2,\"unitDTO\":{\"name\":\"kcal\"}},\"compositionDTO\":{\"ingredientDTOS\":[{\"name\":\"skladnik 1\"},{\"name\":\"skladnik 2\"}],\"additionDTOS\":[{\"name\":\"\"}],\"flavourDTO\":{\"name\":\"\"}},\"nutritionalValueDTOS\":[{\"nutritionalValueName\":{\"group\":{\"groupName\":\"Witaminy inne\"},\"name\":\"Witamina K\"},\"quantity\":16,\"unit\":{\"name\":\"j.m.\"},\"nrv\":13},{\"nutritionalValueName\":{\"group\":{\"groupName\":\"Żywe bakterie\"},\"name\":\"Białko\"},\"quantity\":2,\"unit\":{\"name\":\"j.m.\"},\"nrv\":9}]}";

    given()
        .auth().oauth2(getAccessTokenForSpecialist()) // Assuming method for access token is defined
        .contentType("application/json")
        .body(jsonPayload)
        .when()
        .post("/api/v1/products/add")
        .then()
        .statusCode(HttpStatus.CREATED.value());
  }

  @Test
  public void testCreateProfile() {
    Optional<Account> byEmail = accountRepository.findByEmail("jan.kowalski@example.com");
    String jsonPayload =
        "{\"accountId\":\"" + byEmail.map(Account::getId).orElseThrow(NoSuchElementException::new)
            + "\",\"allergens\":[{\"allergen_id\":\"ff5f8578-3075-4f72-b446-4ac3a95cfb60\",\"intensity\":\"high\"},{\"allergen_id\":\"a92e583c-3521-4e98-b4b7-32cb87baf8eb\",\"intensity\":\"high\"},{\"allergen_id\":\"b861387d-f5cc-42a0-8dc5-c8fbd40eb547\",\"intensity\":\"high\"},{\"allergen_id\":\"715cd2e2-31a8-4e94-a0c3-5fb4290a0872\",\"intensity\":\"high\"},{\"allergen_id\":\"c92b2d99-40bb-4e29-a7f2-f6d5a9e6cc13\",\"intensity\":\"high\"},{\"allergen_id\":\"d455e3f8-1c83-4c7a-a4e8-e7e1b823a64b\",\"intensity\":\"high\"},{\"allergen_id\":\"c7f54cc4-0f8f-4c0b-b409-f16384c1e1f4\",\"intensity\":\"high\"},{\"allergen_id\":\"ca8c0c4c-b8a3-4fa4-b550-53d1b95fbc72\",\"intensity\":\"high\"},{\"allergen_id\":\"afc05894-2179-49b8-9186-43dd3177a960\",\"intensity\":\"high\"}]}";

    given()
        .auth().oauth2(getAccessTokenForSpecialist()) // Assuming method for access token is defined
        .contentType("application/json")
        .body(jsonPayload)
        .when()
        .post("/api/v1/allergy-profiles/create")
        .then()
        .statusCode(HttpStatus.CREATED.value());
  }

  @Test
  public void verifyAccount() {
    given()
        .contentType(ContentType.JSON)
        .body("{"
            + "\"firstName\":\"Test\","
            + "\"lastName\":\"Testtest\","
            + "\"email\":\"martadaria065@gmail.com\","
            + "\"password\":\"P@ssword123\""
            + "}")
        .when()
        .post("api/v1/auth/register")
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .body(Matchers.equalTo(""));

    Optional<Account> byEmail = accountRepository.findByEmail("martadaria065@gmail.com");
    Optional<AccountConfirmation> accountConfirmation = accountConfirmationRepository.findByAccount_Id(
        byEmail.map(Account::getId).orElseThrow(NoSuchElementException::new));

    String s = accountConfirmation.map(AccountConfirmation::getToken).orElse("");
    given()
        .contentType("application/json")
        .when()
        .get("/api/v1/auth/verify-account/" + s)
        .then()
        .statusCode(HttpStatus.OK.value());
  }

}
