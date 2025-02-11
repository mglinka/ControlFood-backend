package pl.lodz.pl.it.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import pl.lodz.pl.it.auth.service.TokenVerifier;
import pl.lodz.pl.it.integration.config.AbstractIntegrationTest;
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

  @MockBean
  private TokenVerifier tokenVerifier;

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

  @Test
  public void testGoogleLogin() throws GeneralSecurityException, IOException {
    // Mock the behavior of the tokenVerifier
    GoogleIdToken mockToken = mock(GoogleIdToken.class);
    when(tokenVerifier.verifyGoogle(anyString())).thenReturn(mockToken);
    Payload payload = new Payload();
    payload.setEmail("user@gmail.com");
    payload.set("given_name", "Test");
    payload.set("family_name", "Test");

    when(mockToken.getPayload()).thenReturn(payload);

    String token = "tokenik";

    given()
        .contentType(ContentType.JSON)
        .body(token)
        .when()
        .post("api/v1/auth/google/redirect")
        .then()
        .statusCode(200);
  }
  @Test
  public void testGoogleLoginNewAccount() throws GeneralSecurityException, IOException {
    // Mock the behavior of the tokenVerifier
    GoogleIdToken mockToken = mock(GoogleIdToken.class);
    when(tokenVerifier.verifyGoogle(anyString())).thenReturn(mockToken);
    Payload payload = new Payload();
    payload.setEmail("new_account@gmail.com");
    payload.set("given_name", "Test");
    payload.set("family_name", "Test");

    when(mockToken.getPayload()).thenReturn(payload);

    String token = "tokenik";

    given()
        .contentType(ContentType.JSON)
        .body(token)
        .when()
        .post("api/v1/auth/google/redirect")
        .then()
        .statusCode(200);
  }
  @Test
  public void testAmazonLogin() throws GeneralSecurityException, IOException {
    // Mock the behavior of the tokenVerifier
    Map<String, Object> data = new HashMap<>();
    data.put("name", "Test");
    data.put("email", "user@gmail.com");

    when(tokenVerifier.verifyAmazon(anyString())).thenReturn(data);

    String token = "tokenik";

    given()
        .contentType(ContentType.JSON)
        .body(token)
        .when()
        .post("api/v1/auth/amazon/redirect")
        .then()
        .statusCode(200);
  }
  @Test
  public void testAmazonLoginNewAccount() throws GeneralSecurityException, IOException {
    // Mock the behavior of the tokenVerifier
    Map<String, Object> data = new HashMap<>();
    data.put("name", "Test");
    data.put("email", "new_account_2@gmail.com");

    when(tokenVerifier.verifyAmazon(anyString())).thenReturn(data);

    String token = "tokenik";

    given()
        .contentType(ContentType.JSON)
        .body(token)
        .when()
        .post("api/v1/auth/amazon/redirect")
        .then()
        .statusCode(200);
  }

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

