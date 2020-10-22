package ru.netology.web.request;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.netology.web.data.DataHelper;

import static io.restassured.RestAssured.given;

@AllArgsConstructor
@Data
public class RestRequest {
    private static String token;
    private static DataHelper.Card[] cards;

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static void loginUser(DataHelper.AuthInfo authInfo) {
        given()
                .spec(requestSpec)
                .body(authInfo)
                .when()
                .post("/api/auth")
                .then()
                .statusCode(200);
    }

    public static String passVerification(DataHelper.VerificationInfo verificationInfo) {
        token =
        given()
                .spec(requestSpec)
                .body(verificationInfo)
                .when()
                .post("/api/auth/verification")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
        return token;
    }

    public static DataHelper.Card[] getCards(String token) {
        cards =
        given()
                .spec(requestSpec)
                .header("Authorization", "Bearer "+ token)
                .when()
                .get("/api/cards")
                .then()
                .statusCode(200)
                .extract()
                .as(DataHelper.Card[].class);
        for (int i=0; i < cards.length; i++) {
            cards[i].setNumber(DataHelper.getFullCardNumberFromCardId(cards[i].getId()));
        }
        return cards;
    }

    public static void setTransaction(DataHelper.TransactionInfo transactionInfo) {
        given()
                .spec(requestSpec)
                .header("Authorization", "Bearer "+ token)
                .body(transactionInfo)
                .when()
                .post("/api/transfer")
                .then()
                .statusCode(200);
        getCards(token);
    }

}