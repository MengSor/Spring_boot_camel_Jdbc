package com.example.spring_boot_camel_jdbc.route;

import com.example.spring_boot_camel_jdbc.user.User;
import com.example.spring_boot_camel_jdbc.user.UserRepository;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class MyRoute extends RouteBuilder {
    private final Environment env;

    public MyRoute(Environment env) {
        this.env = env;
    }

    @Override
    public void configure() throws Exception {

        // REST Configuration
      restConfiguration()
              .contextPath(env.getProperty("camel.servlet.mapping.context-path", "/rest/*"))
              .apiContextPath("/api-doc")
              .apiProperty("api.title", "Spring Boot Camel Postgres Rest API.")
              .apiProperty("api.version", "1.0")
              .apiProperty("cors", "true")
              .apiContextRouteId("doc-api")
              .port(env.getProperty("server.port","8090"))
              .bindingMode(RestBindingMode.json);

        // REST Endpoints
      rest("/users")
              .consumes(MediaType.APPLICATION_JSON_VALUE)
              .produces(MediaType.APPLICATION_JSON_VALUE)
              .get("/")
              .route()
              .to("{{route.findUserAll}}")
              .endRest()
              .get("/{id}")
              .route()
              .to("{{route.findUserById}}")
              .endRest()
              .post("/")
              .route()
              .marshal().json()
              .unmarshal(getJacksonDataFormat(User.class))
              .to("{{route.saveUser}}")
              .endRest()
              .delete("/{id}")
              .route()
              .to("{{route.deleteUser}}")
              .endRest()
              .put("/{id}")
              .route()
              .marshal().json()
              .unmarshal(getJacksonDataFormat(User.class))
              .to("{{route.updateUser}}")
              .end();

        // Route Definitions
      from("{{route.findUserAll}}")
              .bean(UserRepository.class, "findUserAll");

      from("{{route.findUserById}}")
              .log("Received header : ${header.id}")
              .bean(UserRepository.class, "findUserById(${header.id})");

      from("{{route.saveUser}}")
              .log("Received Body: ${body}")
              .bean(UserRepository.class, "saveUser(${body})");

      from("{{route.deleteUser}}")
              .log("Received header : ${header.id}")
              .bean(UserRepository.class, "deleteUser(${header.id})");

      from("{{route.updateUser}}")
              .log("Received header : ${body}, ${header.id} ")
              .bean(UserRepository.class, "updateUser(${body},${header.id})");
    }
    private JacksonDataFormat getJacksonDataFormat(Class<?> unmarshalType){
        JacksonDataFormat format = new JacksonDataFormat();
        format.setUnmarshalType(unmarshalType);
        return format;
    }
}
