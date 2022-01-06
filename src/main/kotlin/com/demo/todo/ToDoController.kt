package com.demo.todo

import io.quarkus.panache.common.Sort

import javax.ws.rs.GET
import javax.ws.rs.POST
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

    // @GET
    // @Produces(MediaType.TEXT_PLAIN)
    // fun hello() = "Hello RESTEasy"


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
            throw WebApplicationException("Todo with id of " + id + " does not exist.", Status.NOT_FOUND);
        }
        return entity;
    }


    @POST
    @Operation(description = "Create a new todo")
    @Transactional
    fun createTodo(@Valid todo: ToDoEntity): Response {
        todoRepository.persist(todo)
        return Response.status(Status.CREATED).entity(todo).build();

    }
}

