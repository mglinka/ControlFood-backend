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
import pl.lodz.pl.it.config.AbstractIntegrationTest;
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
    String accessTokenForAdmin = getAccessTokenForAdmin();
    given().auth()
        .oauth2(accessTokenForAdmin)
        .when()
        .get("/api/v1/account/" + testId)
        .then()
        .statusCode(HttpStatus.OK.value())
        .header(HttpHeaders.ETAG, notNullValue())
        .body("id", equalTo(testId.toString()));
    logout(accessTokenForAdmin);
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

//  @Test
//  public void testAmazonLogin() {
//    String idToken = "ztza|IwEBIO_XUAJavwG6AeI2JGiuf1uwFQ51sg3ojsxeZfIx--KChsg6XUkuPEE2x9wNnllIsHNEzHYP6rD6PRpbHGL3vrN2qYU3VeIq7Ic0B_ilutMeE_y9sHAHJUoLk916uToEBjeTpuWSowdDq-pcR3RKcRqlyhoT-kFGmqjpZ_idW80L9frVFaggnJoWMwgeaTdb7e77eRmleMWZ4fENJXhNsO9RL5p2oxsIHPDdhAOZCCjDpqb3sS1ga169to790w7V1XOg3JacyLohDeiT54JOZD26OVozpODGpwMIBTiZrCAYdfQd3tOrcvJfqP4Tbo6KO26ONhHAukC1S1MvdWc9GrKdUG_QuHBwYegtPNSXq9Acp3RcdW5MmAcZ6j6-4WK5YpnnvEjb4R_qvhKh00qCMo5S2L7KZtrQBq_F6gjBts46leqih49DzWtlpoa9eORNuMc";
//
//    given()
//        .contentType(ContentType.JSON)
//        .body(idToken)
//        .when()
//        .post("api/v1/auth/amazon/redirect")
//        .then()
//        .statusCode(400);
//  }
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

  @Test
  public void createAccountTestSuccessfulForbidden() {
    CreateAccountDTO createAccountDTO = new CreateAccountDTO();
    createAccountDTO.setFirstName(firstName);
    createAccountDTO.setLastName(lastName);
    createAccountDTO.setEmail(email);
    createAccountDTO.setPassword(password);
    createAccountDTO.setRole(userRole);

    RestAssured.given()
        .auth().oauth2(getAccessTokenForUser())
        .contentType(ContentType.JSON)
        .body(createAccountDTO)
        .when()
        .post("/api/v1/accounts/create")
        .then()
        .statusCode(403);
  }

  @Test
  public void createAccountTestBadRequestWithoutFirstName() {
    CreateAccountDTO createAccountDTO = new CreateAccountDTO();
    createAccountDTO.setLastName(lastName);
    createAccountDTO.setEmail(email);
    createAccountDTO.setPassword(password);
    createAccountDTO.setRole(userRole);

    RestAssured.given()
        .auth().oauth2(getAccessTokenForUser())
        .contentType(ContentType.JSON)
        .body(createAccountDTO)
        .when()
        .post("/api/v1/accounts/create")
        .then()
        .statusCode(400);
  }

  @Test
  public void createAccountTestSuccessfulWithoutLastName() {
    CreateAccountDTO createAccountDTO = new CreateAccountDTO();
    createAccountDTO.setFirstName(firstName);
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
        .statusCode(400);
  }

  @Test
  public void createAccountTestBadRequestWithoutEmail() {
    CreateAccountDTO createAccountDTO = new CreateAccountDTO();
    createAccountDTO.setFirstName(firstName);
    createAccountDTO.setLastName(lastName);
    createAccountDTO.setPassword(password);
    createAccountDTO.setRole(userRole);

    RestAssured.given()
        .auth().oauth2(getAccessTokenForAdmin())
        .contentType(ContentType.JSON)
        .body(createAccountDTO)
        .when()
        .post("/api/v1/accounts/create")
        .then()
        .statusCode(400);
  }

  @Test
  public void createAccountTestBadRequestWithoutPassword() {
    CreateAccountDTO createAccountDTO = new CreateAccountDTO();
    createAccountDTO.setFirstName(firstName);
    createAccountDTO.setLastName(lastName);
    createAccountDTO.setEmail(email);
    createAccountDTO.setRole(userRole);

    RestAssured.given()
        .auth().oauth2(getAccessTokenForAdmin())
        .contentType(ContentType.JSON)
        .body(createAccountDTO)
        .when()
        .post("/api/v1/accounts/create")
        .then()
        .statusCode(400);
  }


  @Test
  public void testDisableAccount() {
    given()
        .auth().oauth2(getAccessTokenForAdmin())
        .when()
        .put("/api/v1/account/disableAccount?id={id}", testId)
        .then()
        .statusCode(HttpStatus.OK.value());
  }


  @Test
  public void testDisableAccountForbidden() {
    given()
        .auth().oauth2(getAccessTokenForUser())
        .when()
        .put("/api/v1/account/disableAccount?id={id}", testId)
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value());
  }

  @Test
  public void testEnableAccount() {
    given()
        .auth().oauth2(getAccessTokenForAdmin())
        .when()
        .put("/api/v1/account/disableAccount?id={id}", testId);
    given()
        .auth().oauth2(getAccessTokenForAdmin())
        .when()
        .put("/api/v1/account/enableAccount?id={id}", testId)
        .then()
        .statusCode(HttpStatus.OK.value());

  }




  @Test
  public void testEnableAccountForbiddenUser() {
    given()
        .auth().oauth2(getAccessTokenForUser())
        .when()
        .put("/api/v1/account/disableAccount?id={id}", testId);
    given()
        .auth().oauth2(getAccessTokenForUser())
        .when()
        .put("/api/v1/account/enableAccount?id={id}", testId)
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value());

  }

//  @Test
//  public void testGoogleLogin() {
//    String idToken = "eyJjbGciOiJSUzI1NiIsImtpZCI6ImZhMDcyZjc1Nzg0NjQyNjE1MDg3YzcxODJjMTAxMzQxZTE4ZjdhM2EiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI5MDU5NjQ5NzI4MzktZHJmcHBkdW9yMDhxbzNobGxxZjJ1YXRqODFuYmZ2bWIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI5MDU5NjQ5NzI4MzktZHJmcHBkdW9yMDhxbzNobGxxZjJ1YXRqODFuYmZ2bWIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTU0MDc5OTg2MjQ4NjA2MDI2NTUiLCJlbWFpbCI6ImJhc2lhMTM1NDJAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF0X2hhc2giOiJ1R2pNcHBqcnhEU0dYQXZqYU5CSnBnIiwibmFtZSI6IkJhc2lhIEJhc2lhIiwicGljdHVyZSI6Imh0dHBzOi8vbGgzLmdvb2dsZXVzZXJjb250ZW50LmNvbS9hL0FDZzhvY0pmWjJPUllQSXBpQXNRRkVLYnA1dDlyVHVuWU9IRElsMlNnSUhSV0REQk81aWV3QT1zOTYtYyIsImdpdmVuX25hbWUiOiJCYXNpYSIsImZhbWlseV9uYW1lIjoiQmFzaWEiLCJpYXQiOjE3MzgzMjM4NDksImV4cCI6MTczODMyNzQ0OX0.BmTWyknDdGPUWnfaRYQJhGkpNA4c3MbquiMSu_XUQbf-PRk7s5oYJCZPExkmpgimQfKdsuQn72jPrpqC1JPkVcqgD08eEkcXolVavwZ_mfodIN2mkqr0YXUmoAMhJSJhRQbj3Jvu04YklYhibVj52aKPGBKIqOdC2RjoxHpIyuX-KiSs0VUWjhCYjNCF-SSSzUjMPGY7e27TIgI2Idnm5mBty_drrIHI_Tm6wBw822Ki6ohJGQAxyz0dYVvsDGwAkYCYDfokW69E55hURDL25C93AQ-eVwb9REQRDbaCPPH1Us8qRbaD3Df5iObE5m5nyrDEw9L9ZhNnAk7oGbraFw";  // Use your actual id_token here
//
//    given()
//        .contentType(ContentType.JSON)
//        .body(idToken)
//        .when()
//        .post("api/v1/auth/google/redirect")
//        .then()
//        .statusCode(400);
//  }

  @Test
  public void testRegisterValidRequest() {
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
        .body(equalTo(""));
  }

  @Test
  public void testRegisterBadRequest() {
    given()
            .contentType(ContentType.JSON)
            .body("{"
                    + "\"firstName\":\"Test\","
                    + "\"lastName\":\"Testtest\","
                    + "\"email\":\"martadaria0\","
                    + "\"password\":\"P@ssword123\""
                    + "}")
            .when()
            .post("api/v1/auth/register")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  public void testAuthenticateBadRequest() {
    given()
            .contentType(ContentType.JSON)
            .body("{"
                    + "\"email\":\"martadaria\","
                    + "\"password\":\"P@ssword123\""
                    + "}")
            .when()
            .post("api/v1/auth/authenticate")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  public void testRegisterInvalidEmail() {
    given()
        .contentType(ContentType.JSON)
        .body("{"
            + "\"firstName\":\"Test\","
            + "\"lastName\":\"Testtest\","
            + "\"email\":\"martadaria06\","
            + "\"password\":\"P@ssword123\""
            + "}")
        .when()
        .post("api/v1/auth/register")
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  public void testRegisterEmailAlreadyTaken() {
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
        .statusCode(HttpStatus.CONFLICT.value())
        .body("message", equalTo("Ten email jest już zajęty"));
  }

  @Test
  public void testRegisterInvalidPassword() {
    given()
        .contentType(ContentType.JSON)
        .body("{"
            + "\"firstName\":\"Test\","
            + "\"lastName\":\"Testtest\","
            + "\"email\":\"martadaria065@gmail.com\","
            + "\"password\":\"p@ssword123\""
            + "}")
        .when()
        .post("api/v1/auth/register")
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value());
  }

}

