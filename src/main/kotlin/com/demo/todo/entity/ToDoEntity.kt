package com.demo.todo

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity

import javax.persistence.Column
import javax.persistence.Entity
import javax.validation.constraints.NotBlank

import org.eclipse.microprofile.openapi.annotations.media.Schema

@Entity()
data class ToDoEntity(

    // @NotBlank
    @Column(unique = true)
    var title: String = "",

    var completed: Boolean = false,

    @Column(name = "ordering")
    var order: Int = 0,

    @Schema(example="https://github.com/LongDuncan/todo-demo")
    var url: String = "",

):PanacheEntity()


//curl.exe -X POST -H "Content-Type: application/json" -d '{"id": 0,"title": "string","completed": true,"order": 0,"url": "string"}' http://127.0.0.1:8080/api