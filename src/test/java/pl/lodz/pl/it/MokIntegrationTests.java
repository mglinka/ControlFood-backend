package pl.lodz.pl.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import pl.lodz.pl.it.dto.account.CreateAccountDTO;
import pl.lodz.pl.it.entity.Account;
import pl.lodz.pl.it.entity.Role;
import pl.lodz.pl.it.utils._enum.AccountRoleEnum;


public class MokIntegrationTests extends AbstractIntegrationTest {

  private Role role;
  private UUID testId;
  private String token;

  private final String firstName = "John";
  private final String lastName = "Doe";
  private final String email = "john.doe@example.com";
  private final String password = "P@ssword123";
  private final AccountRoleEnum userRole = AccountRoleEnum.ROLE_USER;

  @BeforeEach
  public void setup() {
    testId = UUID.fromString("9d0c611f-98bb-4dd9-a492-1e55182340d3");
    role = new Role(AccountRoleEnum.ROLE_SPECIALIST);
    Account testAccount = new Account(testId, "specialist@example.com", "Specialist", "Specialist",
        "$2a$10$jXCmzT7d0y0i2EUlRY9mNun.sV7MqaHvMW/Abp/xckUSUOaxjrAFW", 0L, role, true, null);

  }

  @Test
  public void getAccountByIdTest() {
    given().auth()
        .oauth2(getAccessTokenForAdmin())
        .when()
        .get("/api/v1/account/" + testId)
        .then()
        .statusCode(HttpStatus.OK.value())
        .header(HttpHeaders.ETAG, notNullValue())
        .body("id", equalTo(testId.toString()));

  }

  @Test
  public void getAllRolesTest() {
    given()
        .auth().oauth2(getAccessTokenForAdmin())
        .when()
        .get("/api/v1/roles")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("size()", greaterThan(0));

  }

  @Test
  public void getAllAccountsTest() {
    given()
        .auth().oauth2(getAccessTokenForAdmin())
        .when()
        .get("/api/v1/accounts")
        .then()
        .statusCode(200)
        .body("size()", greaterThan(0));

  }

  @Test
  public void createAccountTestSuccessful() {
    CreateAccountDTO createAccountDTO = new CreateAccountDTO();
    createAccountDTO.setFirstName(firstName);
    createAccountDTO.setLastName(lastName);
    createAccountDTO.setEmail(email);
    createAccountDTO.setPassword(password);
    createAccountDTO.setRole(userRole);

    RestAssured.given()
        .auth().oauth2(getAccessTokenForAdmin())
        .contentType(ContentType.JSON)
        .body(createAccountDTO)
        .when()
        .post("/api/v1/accounts/create")
        .then()
        .statusCode(201);
  }

}
