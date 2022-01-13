package com.demo.todo

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Sort
import javax.enterprise.context.ApplicationScoped
import javax.transaction.Transactional
import arrow.core.Either
import com.demo.todo.ToDoAppResponseError

@ApplicationScoped
class ToDoRepository: PanacheRepository<ToDoEntity> {
    fun findByTitle(title: String): List<ToDoEntity> =
        list("title", title)

    fun findNotCompleted(): List<ToDoEntity> =
        list("completed", false)

    fun findCompleted(): List<ToDoEntity> =
        list("completed", true)

    @Transactional
    fun deleteCompleted(): Either< ToDoAppResponseError,  Long> =
        Either.catch {delete("completed", true)}
        .mapLeft { ToDoAppResponseError.DatabaseError(it) }

    @Transactional
    fun deleteTodoById(id: Long): Either< ToDoAppResponseError,  Boolean> =
        Either.catch {deleteById(id)}
        .mapLeft { ToDoAppResponseError.DatabaseError(it) }

    fun findTodoById(id: Long): Either< ToDoAppResponseError,  ToDoEntity> =
        Either.catch {
            val todo: ToDoEntity ?= findById(id)
            if (todo == null) {
                return Either.Left(ToDoAppResponseError.NotFoundError("Todo with id of " + id + " does not exist."))
            }
            return Either.Right(todo)
        }
        .mapLeft { ToDoAppResponseError.DatabaseError(it) }

    fun findAllSortByOrder(): Either< ToDoAppResponseError, List<ToDoEntity>> =
        Either.catch { listAll(Sort.by("order")) }
        .mapLeft { ToDoAppResponseError.DatabaseError(it) }

    @Transactional
    fun createTodo(todo: ToDoEntity): Either< ToDoAppResponseError,  Unit> =
        Either.catch {persist(todo)}
        .mapLeft { ToDoAppResponseError.DatabaseError(it) }

    @Transactional
    fun updateTodoById(id: Long, newTodo: ToDoEntity): Either< ToDoAppResponseError,  ToDoEntity> =
        Either.catch {
            val todo: ToDoEntity ?= findById(id)
            if (todo == null) {
                return Either.Left(ToDoAppResponseError.NotFoundError("Todo with id of " + id + " does not exist."))
            }
            todo.id = id
            todo.completed = newTodo.completed
            todo.order = newTodo.order
            todo.title = newTodo.title
            todo.url = newTodo.url
            return Either.Right(todo)
        }
        .mapLeft { ToDoAppResponseError.DatabaseError(it) }
}