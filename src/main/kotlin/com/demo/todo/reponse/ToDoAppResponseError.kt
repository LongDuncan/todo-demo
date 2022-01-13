package com.demo.todo

import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.Status

sealed class ToDoAppResponseError {
    data class DatabaseError(val e: Throwable): ToDoAppResponseError()
    data class NotFoundError(val e: String): ToDoAppResponseError()

    companion object {
        fun toResponse(e: ToDoAppResponseError): Response = when(e){
            is DatabaseError -> Response.serverError().entity("DB Error ${e.e.stackTraceToString()}").build()
            is NotFoundError -> Response.status(Status.NO_CONTENT).entity(e).build()
        }
    }

}