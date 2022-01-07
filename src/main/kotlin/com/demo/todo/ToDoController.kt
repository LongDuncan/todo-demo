package com.demo.todo

import io.quarkus.panache.common.Sort

import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.DELETE
import javax.ws.rs.PATCH
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.PathParam
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.Status;

import javax.validation.Valid;
import javax.transaction.Transactional

import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.tags.Tag


@Path("/api")
@Tag(name = "Todo Resource", description = "All Todo Operations")
class ToDoController(val todoRepository: ToDoRepository) {

    @GET
    @Operation(description = "Get all the todos")
    fun getAll() : List<ToDoEntity> {
        return todoRepository.listAll(Sort.by("order"))
    }

    @GET
    @Path("/{id}")
    @Operation(description = "Get a specific todo by id")
    fun getOne(@PathParam("id") id: Long): ToDoEntity {
        var entity: ToDoEntity ?= todoRepository.findById(id)
        if (entity == null) {
            throw WebApplicationException("Todo with id of " + id + " does not exist.", Status.NOT_FOUND)
        }
        return entity
    }


    @POST
    @Operation(description = "Create a new todo")
    @Transactional
    fun createTodo(@Valid todo: ToDoEntity): Response {
        todoRepository.persist(todo)
        return Response.status(Status.CREATED).entity(todo).build()

    }

    @DELETE
    @Transactional
    @Operation(description = "Remove all completed todos")
    fun deleteCompleted(): Response {
        todoRepository.deleteCompleted()
        return Response.noContent().build()
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    @Operation(description = "Delete a specific todo")
    fun deleteOne(@PathParam("id") id: Long):Response {
        var entity: ToDoEntity ?= todoRepository.findById(id)
        if (entity == null) {
            throw WebApplicationException("Todo with id of " + id + " does not exist.", Status.NOT_FOUND)
        }
        entity.delete()
        return Response.noContent().build()
    }

    @PATCH
    @Path("/{id}")
    @Transactional
    @Operation(description = "Update an exiting todo")
    fun update(@Valid todo: ToDoEntity, @PathParam("id") id:Long): Response {
        var entity: ToDoEntity ?= todoRepository.findById(id)
        entity?.id = id
        entity?.completed = todo.completed
        entity?.order = todo.order
        entity?.title = todo.title
        entity?.url = todo.url
        return Response.ok(entity).build()
    }
}

