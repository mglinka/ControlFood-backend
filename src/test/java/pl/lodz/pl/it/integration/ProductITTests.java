package pl.lodz.pl.it.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public class ProductITTests extends AllergyProfileITTests{

  @Test
  public void testGetAllProducts() {
    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .when()
        .get("/api/v1/products")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("$", notNullValue())
        .body("size()", not(0));
  }
  @Test
  public void testGetAllProductsForbidden() {
    given()
        .auth().oauth2(getAccessTokenForAdmin())
        .when()
        .get("/api/v1/products")
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value());
  }

  @Test
  public void testGetProductById() {
    UUID productId = UUID.fromString("5376bd81-3acd-4966-b3fd-94f65f153d8b");

    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .when()
        .get("/api/v1/products/{id}", productId)
        .then()
        .statusCode(HttpStatus.FOUND.value())
        .body("id", equalTo(productId.toString()));
  }
  @Test
  public void testGetProductByIdForbidden() {
    UUID productId = UUID.fromString("5376bd81-3acd-4966-b3fd-94f65f153d8b");

    given()
        .auth().oauth2(getAccessTokenForAdmin())
        .when()
        .get("/api/v1/products/{id}", productId)
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value());

  }
  @Test
  public void testGetProductByIdNotFound() {
    UUID productId = UUID.fromString("6376bd81-3acd-4966-b3fd-94f65f153d8b");

    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .when()
        .get("/api/v1/products/{id}", productId)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }

  @Test
  public void testGetProductsByCategory() {
    String categoryName = "Owoce";

    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .when()
        .get("/api/v1/products/category?categoryName=" + categoryName)
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("size()", not(0));
  }
  @Test
  public void testGetProductsByCategoryForbidden() {
    String categoryName = "Owoce";

    given()
        .auth().oauth2(getAccessTokenForAdmin())
        .when()
        .get("/api/v1/products/category?categoryName=" + categoryName)
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value());
  }

  @Test
  public void testGetProductsByCategoryNotFound() {
    String categoryName = "Owocce";

    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .when()
        .get("/api/v1/products/category?categoryName=" + categoryName)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }
  @Test
  public void testGetProductByEan() {
    String ean = "5900512901220";

    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .when()
        .get("/api/v1/products/by-ean/{ean}", ean)
        .then()
        .statusCode(HttpStatus.OK.value())
        .header(HttpHeaders.ETAG, notNullValue())
        .body("ean", equalTo(ean));
  }

  @Test
  public void testGetProductByEanNotFound() {
    String ean = "0000000000000";

    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .when()
        .get("/api/v1/products/by-ean/{ean}", ean)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }
  @Test
  public void testGetProductByEanForbidden() {
    String ean = "5900512901220";

    given()
        .auth().oauth2(getAccessTokenForAdmin())
        .when()
        .get("/api/v1/products/by-ean/{ean}", ean)
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value());
  }

  @Test
  public void testGetAllProductsWithLabels() {
    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .when()
        .get("/api/v1/products/withLabels")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("$", notNullValue())
        .body("size()", not(0));
  }

  @Test
  public void testGetAllProductsWithLabelsForbidden() {
    given()
        .auth().oauth2(getAccessTokenForAdmin())
        .when()
        .get("/api/v1/products/withLabels")
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value());
  }

  @Test
  public void testGetAllProductsWithoutPagination() {
    given()
        .auth().oauth2(getAccessTokenForSpecialist())
        .when()
        .get("/api/v1/products/withoutPagination")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("$", notNullValue())
        .body("size()", not(0));
  }
  @Test
  public void testGetAllProductsWithoutPaginationForbidden() {
    given()
        .auth().oauth2(getAccessTokenForAdmin())
        .when()
        .get("/api/v1/products/withoutPagination")
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value());
  }


}
