package com.demo.todo

import io.quarkus.panache.common.Sort

import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.DELETE
import javax.ws.rs.PATCH
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.PathParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.Status
import javax.validation.Valid
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import com.demo.todo.ToDoAppResponseError


@Path("/api")
@Tag(name = "Todo Resource", description = "All Todo Operations")
class ToDoController(val todoRepository: ToDoRepository) {


    @GET
    @Operation(description = "Get all the todos")
    @Produces(MediaType.APPLICATION_JSON)
    fun getAll() : Response =
        todoRepository.findAllSortByOrder().fold(
            ifRight = {todos -> Response.status(Status.OK).entity(todos).build()},
            ifLeft = {err -> ToDoAppResponseError.toResponse(err)}
        )

    @GET
    @Path("/{id}")
    @Operation(description = "Get a specific todo by id")
    fun getOne(@PathParam("id") id: Long): Response =
        todoRepository.findTodoById(id).fold(
        ifRight = {todo -> Response.status(Status.OK).entity(todo).build()},
        ifLeft = {err -> ToDoAppResponseError.toResponse(err)}
        )

    @POST
    @Operation(description = "Create a new todo")
    fun createTodo(@Valid todo: ToDoEntity): Response =
        todoRepository.createTodo(todo).fold(
            ifRight = {Response.status(Status.CREATED).entity(todo).build()},
            ifLeft = {err -> ToDoAppResponseError.toResponse(err)}
        )


    @DELETE
    @Operation(description = "Remove all completed todos")
    fun deleteCompleted(): Response =
        todoRepository.deleteCompleted().fold(
            ifRight = {Response.noContent().build()},
            ifLeft = {err -> ToDoAppResponseError.toResponse(err)}
        )

    @DELETE
    @Path("/{id}")
    @Operation(description = "Delete a specific todo")
    fun deleteOne(@PathParam("id") id: Long):Response =
        todoRepository.deleteTodoById(id).fold(
            ifRight = {Response.noContent().build()},
            ifLeft = {err -> ToDoAppResponseError.toResponse(err)}
        )

    @PATCH
    @Path("/{id}")
    @Operation(description = "Update an exiting todo")
    fun update(@Valid todo: ToDoEntity, @PathParam("id") id:Long): Response =
        todoRepository.updateTodoById(id, todo).fold(
            ifRight = {newTodo -> Response.status(Status.OK).entity(newTodo).build()},
            ifLeft = {err -> ToDoAppResponseError.toResponse(err)}
        )

}

