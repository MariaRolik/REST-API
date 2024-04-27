package tests;

import models.lombok.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import static org.junit.jupiter.api.Assertions.assertAll;

import static specs.ReqresSpec.*;
import static org.assertj.core.api.Assertions.assertThat;


public class UsersTests extends TestBase {

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


    @Test
    @DisplayName("Проверка данных пользователя с id: 1")
    void singleUserId1Test() {
        UserBodyLombokModel authData = new UserBodyLombokModel();

        UserDataResponseModel response = given(userRequestSpec)
                .body(authData)

                .when()
                .get("/users/1")

                .then()
                .spec(userResponseSpec)
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/success-single-user-schema.json"))
                .extract().as((Type) UserDataResponseModel.class);

        step("Check response", () ->
                assertAll("Check response values",
                        () -> {
                            assertThat(response.getUser().getId()).isEqualTo(1);
                            assertThat(response.getUser().getEmail()).isEqualTo("george.bluth@reqres.in");
                            assertThat(response.getUser().getFirstName()).isEqualTo("George");
                            assertThat(response.getUser().getLastName()).isEqualTo("Bluth");
                            assertThat(response.getUser().getAvatar()).isEqualTo("https://reqres.in/img/faces/1-image.jpg");
                       }
                )
        );

    }


    @Test
    @DisplayName("Успешое удаление пользователя")
    void successfulDeleteUserTest() {
            step("Send user delete request", () -> given(userRequestSpec)
                    .when()
                    .delete("/users/2")
                    .then()
                    .spec(userResponseSpec))
                    .statusCode(204);
        }
    }
