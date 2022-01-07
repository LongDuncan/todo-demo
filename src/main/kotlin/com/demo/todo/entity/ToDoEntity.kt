package com.demo.todo

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity

import javax.persistence.Id;
import javax.persistence.GeneratedValue
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GenerationType

import javax.validation.constraints.NotBlank
import org.eclipse.microprofile.openapi.annotations.media.Schema

@Entity()
data class ToDoEntity(

    @NotBlank
    @Column(unique = true)
    var title: String = "",

    var completed: Boolean = false,

    @Column(name = "ordering")
    var order: Int = 0,

    @Schema(example="https://github.com/LongDuncan/todo-demo")
    var url: String = "",

):PanacheEntity()