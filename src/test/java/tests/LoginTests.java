package tests;

import models.lombok.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

import static specs.ReqresSpec.*;
import static org.assertj.core.api.Assertions.assertThat;

public class LoginTests extends TestBase {
    @Test
    @DisplayName("Успешная авторизация пользователя (проверка длины токена)")
    void successfulLoginTest() {
        LoginBodyLombokModel authData = new LoginBodyLombokModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("cityslicka");

        LoginResponseLombokModel response = step("Make request", () ->
                given(userRequestSpec)
                        .body(authData)

                        .when()
                        .post("/login")

                        .then()
                        .spec(userResponseSpec)
                        .statusCode(200)
                        .extract().as(LoginResponseLombokModel.class));

        step("Check response", () ->
                assertThat(response.getToken()).hasSize(17));
    }

}
