package tests;

import models.lombok.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertAll;

import static specs.ReqresSpec.*;
import static org.assertj.core.api.Assertions.assertThat;


public class ReqresTests extends TestBase {

    @Test
    @DisplayName("Успешная авторизация пользователя (проверка длины токена)")
    void successfulLoginWithSpecsTest() {
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

    @Test
    @DisplayName("Успешное создание пользователя")
    void createUserTest() {
        UserBodyLombokModel authData = new UserBodyLombokModel();
        authData.setName("morpheus");
        authData.setJob("leader");

        CreateUserResponseLombokModel response = given(userRequestSpec)
                .body(authData)

                .when()
                .post("/users")

                .then()
                .spec(userResponseSpec)
                .statusCode(201)
                .extract().as((Type) CreateUserResponseLombokModel.class);

        step("Check response", () ->
                assertAll("Check response values",
                        () -> {
                            assertThat(response.getName()).isEqualTo("morpheus");
                            assertThat(response.getJob()).isEqualTo("leader");
                        }
                )
        );
    }


    @Test
    @DisplayName("Создание пользователя с пустыми значениями")
    void createEmptyUserTest() {
        UserBodyLombokModel authData = new UserBodyLombokModel();

        CreateUserResponseLombokModel response = given(userRequestSpec)
                .body(authData)

                .when()
                .post("/users")

                .then()
                .spec(userResponseSpec)
                .statusCode(201)
                .extract().as((Type) CreateUserResponseLombokModel.class);

        step("Check response", () ->
                assertAll("Check response values",
                        () -> {
                            assertThat(response.getName()).isNull();
                            assertThat(response.getJob()).isNull();
                        }
                )
        );

    }

    @Test
    @DisplayName("Изменение профессии пользователя c put")
    void updateUserWithPutTest() {
        UserBodyLombokModel authData = new UserBodyLombokModel();
        authData.setName("morpheus");
        authData.setJob("captain");

        UpdateUserResponseLombokModel response = given(userRequestSpec)
                .body(authData)

                .when()
                .put("/users/2")

                .then()
                .spec(userResponseSpec)
                .statusCode(200)
                .extract().as((Type) UpdateUserResponseLombokModel.class);

        step("Check response", () ->
                assertAll("Check response values",
                        () -> {
                            assertThat(response.getName()).isEqualTo("morpheus");
                            assertThat(response.getJob()).isEqualTo("captain");
                        }
                )
        );
    }

    @Test
    @DisplayName("Изменение профессии пользователя c patch")
    void updateUserWithPatchTest() {
        UserBodyLombokModel authData = new UserBodyLombokModel();
        authData.setName("morpheus");
        authData.setJob("mentor");

        UpdateUserResponseLombokModel response = given(userRequestSpec)
                .body(authData)

                .when()
                .patch("/users/2")

                .then()
                .spec(userResponseSpec)
                .statusCode(200)
                .extract().as((Type) UpdateUserResponseLombokModel.class);

        step("Check response", () ->
                assertAll("Check response values",
                        () -> {
                            assertThat(response.getName()).isEqualTo("morpheus");
                            assertThat(response.getJob()).isEqualTo("mentor");
                        }
                )
        );
    }
}
