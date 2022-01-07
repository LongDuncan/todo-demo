package com.demo.todo

import java.util.stream.Stream

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.h2.H2DatabaseTestResource
import io.quarkus.test.TestTransaction
import io.quarkus.test.common.QuarkusTestResource
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponse
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`

@QuarkusTest
@TestTransaction
@TestInstance(PER_CLASS)
@QuarkusTestResource(H2DatabaseTestResource::class)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ToDoControllerTest {

    @Test @Order(1)
    fun testGetAll() {
        given()
            .accept(ContentType.JSON)
        .`when`()
            .get("/api")
        .then()
            .statusCode(200)
            .body(`is`("[]"))
    }

    @Test @Order(2)
    fun testGetNotFound() {
        given()
            .accept(ContentType.JSON)
        .`when`()
            .get("/api/1")
        .then()
            .statusCode(404)
    }

    @Test @Order(3)
    fun testCreateNew() {
        given()
            .contentType(ContentType.JSON)
        .`when`()
            .body(ONE)
            .post("/api")
        .then()
            .statusCode(201)
    }

    @Test @Order(4)
    fun testGet() {
        given()
            .accept(ContentType.JSON)
        .`when`()
            .get("/api/1")
        .then()
            .statusCode(200)
            .body("completed", `is`(false))
            .body("order", `is`(0))
            .body("title", `is`("Test ToDo One"))
            .body("url", `is`("string"))
    }

    @Test @Order(5)
    fun testUpdate() {
        given()
            .contentType(ContentType.JSON)
        .`when`()
            .body(TWO)
            .patch("/api/1")
        .then()
            .statusCode(200)
            .body("completed", `is`(true))
            .body("order", `is`(0))
            .body("title", `is`("Test ToDo Two"))
            .body("url", `is`("string"))
    }

    @ParameterizedTest
    @Order(6)
    @MethodSource("todoItemsToDelete")
    fun testDelete(id:Int, expectedStatus:Int) {
        given()
                .pathParam("id", id)
        .`when`()
                .delete("/api/{id}")
        .then()
                .statusCode(expectedStatus)
    }

    private fun  todoItemsToDelete():Stream<Arguments> {
        return Stream.of(
                Arguments.of(1, 204),
                Arguments.of(2, 404)
        )
    }

    val ONE:String = "{\"completed\":false,\"order\":0,\"title\":\"Test ToDo One\",\"url\": \"string\"}"
    val TWO:String = "{\"completed\":true,\"order\":0,\"title\":\"Test ToDo Two\",\"url\": \"string\"}"
}