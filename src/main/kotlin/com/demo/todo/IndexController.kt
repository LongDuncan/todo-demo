package com.demo.todo

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.eclipse.microprofile.openapi.annotations.Operation;

@Path("/index")
class IndexController() {

    @GET
    @Operation(hidden = true)
    fun redirect(): Response {
        val redirect: URI
        redirect = UriBuilder.fromUri("todo.html").build()
        return Response.temporaryRedirect(redirect).build()
    }
}