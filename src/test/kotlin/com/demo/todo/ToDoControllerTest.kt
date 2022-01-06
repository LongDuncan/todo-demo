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

import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.json.JSONObject
import com.google.common.base.Objects

@QuarkusTest
@TestTransaction
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
            .body(`is`("[]"));
    }

    @Test @Order(2)
    fun testGetNotFound() {
        given()
            .accept(ContentType.JSON)
        .`when`()
            .get("/api/1")
        .then()
            .statusCode(404);
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
            .body("completed", `is`(true))
            .body("order", `is`(0))
            .body("title", `is`("Introduction to Quarkus"))
            .body("url", `is`("string"))
    }

    val ONE:String = "{\"completed\":true,\"order\":0,\"title\":\"Introduction to Quarkus\",\"url\": \"string\"}"
}