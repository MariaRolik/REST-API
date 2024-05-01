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

        CreateUserResponseLombokModel responseBody = given(userRequestSpec)
                .body(authData)

                .when()
                .post("/users")

                .then()
                .spec(userResponseSpec)
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/create-schema.json"))
                .extract().as((Type) CreateUserResponseLombokModel.class);

        step("Check response", () ->
                assertAll("Check response values",
                        () -> {
                            assertThat(responseBody.getName()).isEqualTo("morpheus");
                            assertThat(responseBody.getJob()).isEqualTo("leader");
                        }
                )
        );
    }


    @Test
    @DisplayName("Создание пользователя с пустыми значениями")
    void createEmptyUserTest() {
        UserBodyLombokModel authData = new UserBodyLombokModel();

        CreateUserResponseLombokModel responseBody = given(userRequestSpec)
                .body(authData)

                .when()
                .post("/users")

                .then()
                .spec(userResponseSpec)
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/create-schema.json"))
                .extract().as((Type) CreateUserResponseLombokModel.class);

        step("Check response", () ->
                assertAll("Check response values",
                        () -> {
                            assertThat(responseBody.getName()).isNull();
                            assertThat(responseBody.getJob()).isNull();
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

        UpdateUserResponseLombokModel responseBody = given(userRequestSpec)
                .body(authData)

                .when()
                .put("/users/2")

                .then()
                .spec(userResponseSpec)
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/edit-user-schema.json"))
                .extract().as((Type) UpdateUserResponseLombokModel.class);

        step("Check response", () ->
                assertAll("Check response values",
                        () -> {
                            assertThat(responseBody.getName()).isEqualTo("morpheus");
                            assertThat(responseBody.getJob()).isEqualTo("captain");
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

        UpdateUserResponseLombokModel responseBody = given(userRequestSpec)
                .body(authData)

                .when()
                .patch("/users/2")

                .then()
                .spec(userResponseSpec)
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/edit-user-schema.json"))
                .extract().as((Type) UpdateUserResponseLombokModel.class);

        step("Check response", () ->
                assertAll("Check response values",
                        () -> {
                            assertThat(responseBody.getName()).isEqualTo("morpheus");
                            assertThat(responseBody.getJob()).isEqualTo("mentor");
                        }
                )
        );
    }


    @Test
    @DisplayName("Проверка данных пользователя с id: 1")
    void singleUserId1Test() {
        UserBodyLombokModel authData = new UserBodyLombokModel();

        UserDataResponseModel responseBody = given(userRequestSpec)
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
                            assertThat(responseBody.getUser().getId()).isEqualTo(1);
                            assertThat(responseBody.getUser().getEmail()).isEqualTo("george.bluth@reqres.in");
                            assertThat(responseBody.getUser().getFirstName()).isEqualTo("George");
                            assertThat(responseBody.getUser().getLastName()).isEqualTo("Bluth");
                            assertThat(responseBody.getUser().getAvatar()).isEqualTo("https://reqres.in/img/faces/1-image.jpg");
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
