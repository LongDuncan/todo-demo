package com.demo.todo

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ToDoRepository: PanacheRepository<ToDoEntity> {
    fun findByTitle(title: String): List<ToDoEntity> =
        list("title", title)

    fun findNotCompleted(): List<ToDoEntity> =
        list("completed", false)

    fun findCompleted(): List<ToDoEntity> =
        list("completed", true)

    fun deleteCompleted(): Long {
        return delete("completed", true)
    }
}