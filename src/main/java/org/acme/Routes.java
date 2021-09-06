package org.acme;

import io.quarkus.vertx.ConsumeEvent;
import io.vertx.camel.CamelBridge;
import io.vertx.camel.CamelBridgeOptions;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import static io.vertx.camel.InboundMapping.fromCamel;

@ApplicationScoped
public class Routes extends RouteBuilder {

    private final Logger log = LoggerFactory.getLogger(Routes.class);

    @Inject
    CamelContext camelContext;

    @Inject
    Vertx vertx;

    @Override
    public void configure() throws Exception {
        CamelBridge bridge = CamelBridge.create(vertx, new CamelBridgeOptions(camelContext)
                .addInboundMapping(fromCamel("direct:foo").toVertx("test").usePublish()));
        camelContext.start();
        bridge.start();
    }

    @ConsumeEvent("test")
    public void consumeMessage (Message message) {
        log.info(">>> one " + message.body());
    }

    @ConsumeEvent("test")
    public void consumeMessage2 (Message message) {
        log.info(">>> two " + message.body());
    }
}
