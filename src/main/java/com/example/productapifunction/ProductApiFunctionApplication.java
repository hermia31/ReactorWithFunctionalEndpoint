package com.example.productapifunction;

import com.example.productapifunction.handler.ProductHandler;
import com.example.productapifunction.model.Product;
import com.example.productapifunction.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
//import static org.springframework.web.reactive.function.server.RouterFunction.nest;
//import static org.springframework.web.reactive.function.server.RouterFunction.route;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@SpringBootApplication
public class ProductApiFunctionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductApiFunctionApplication.class, args);
	}


@Bean
CommandLineRunner init(ProductRepository repository){
	return args -> {
		Flux<Product> productFlux= Flux.just(
				new Product(null,"Home",325.00),
				new Product(null,"Education",100.00),
				new Product(null,"Car",50.60)).flatMap(repository::save);

		productFlux
		.thenMany(repository.findAll())
		.subscribe(System.out::println);
	};


	}

	@Bean
	RouterFunction<ServerResponse> routes(ProductHandler handler){
//		return route(GET("/products").and(accept(APPLICATION_JSON)), handler::getAllProducts)
//				.andRoute(POST("/products").and(contentType(APPLICATION_JSON)), handler::saveProduct)
//				.andRoute(DELETE("/products").and(contentType(APPLICATION_JSON)), handler::deleteAllProducts)
//				.andRoute(GET("/products/events").and(contentType(TEXT_EVENT_STREAM)), handler::getProductEvents)
//				.andRoute(GET("/product/{id}").and(accept(APPLICATION_JSON)), handler::getProduct)
//				.andRoute(PUT("/product/{id}").and(accept(APPLICATION_JSON)), handler::updateProduct)
//				.andRoute(DELETE("/product/{id}").and(accept(APPLICATION_JSON)), handler::deleteProduct);


		return nest(path("/products"),
				nest(accept(APPLICATION_JSON).or(contentType(APPLICATION_JSON)).or(accept(TEXT_EVENT_STREAM)),
						route(GET("/"),handler::getAllProducts)
				          .andRoute(method(HttpMethod.POST),handler::saveProduct)
								.andRoute(DELETE("/"),handler::deleteAllProducts)
								.andRoute(GET("/events"),handler::getAllProducts)
				.andNest(path("/{id}"),
						route(method(HttpMethod.GET),handler::getProduct)
				           .andRoute(method(HttpMethod.PUT),handler::updateProduct)
				            .andRoute(method(HttpMethod.DELETE),handler::deleteProduct)))
				);


	}
}


