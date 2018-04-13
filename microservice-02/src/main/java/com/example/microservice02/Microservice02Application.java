package com.example.microservice02;

import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@SpringBootApplication
public class Microservice02Application {

	public static void main(String[] args) {
		SpringApplication.run(Microservice02Application.class, args);
	}

	 static final String[] CONTEXTUAL_HEADERS = {
		"CORRELATION_ID",
		"x-newrelic-id",
		"x-newrelic-transaction"
	};

	@Bean
	public FilterRegistrationBean someFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
				for (String header : CONTEXTUAL_HEADERS) {
					String corrId = httpServletRequest.getHeader(header);
					if (corrId == null) {
						corrId = UUID.randomUUID().toString();
					}
					MDC.put(header, corrId);
					filterChain.doFilter(httpServletRequest, httpServletResponse);
				}
			}
		});
		registration.addUrlPatterns("/*");
		registration.setName("someFilter");
		registration.setOrder(1);
		return registration;
	}
}
