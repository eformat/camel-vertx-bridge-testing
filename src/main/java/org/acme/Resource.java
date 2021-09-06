package org.acme;

import io.quarkus.vertx.ConsumeEvent;
import io.vertx.camel.CamelBridge;
import io.vertx.camel.CamelBridgeOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Message;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.graalvm.polyglot.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static io.vertx.camel.InboundMapping.fromCamel;

@Path("hello")
public class Resource {

    private final Logger log = LoggerFactory.getLogger(Resource.class);

    @Inject
    CamelContext camelContext;

    @GET
    public Response get(@PathParam("name") @QueryParam("name") @DefaultValue("world") String name) {
        log.info(">>> get");
        Endpoint endpoint = camelContext.getEndpoint("direct:foo");
        ProducerTemplate producer = camelContext.createProducerTemplate();
        JsonObject object = new JsonObject();
        object.put("message","hello " + name);
        producer.asyncSendBody(endpoint, object);
        return Response.ok().build();
    }
}
