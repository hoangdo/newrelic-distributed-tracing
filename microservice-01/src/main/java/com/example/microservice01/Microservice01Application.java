package com.example.microservice01;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@SpringBootApplication
@EnableFeignClients
public class Microservice01Application {

	public static void main(String[] args) {
		SpringApplication.run(Microservice01Application.class, args);
	}

	@Bean
	public FilterRegistrationBean someFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
				MDC.put("CORRELATION_ID", UUID.randomUUID().toString());
				filterChain.doFilter(httpServletRequest, httpServletResponse);
			}
		});
		registration.addUrlPatterns("/*");
		registration.setName("someFilter");
		registration.setOrder(1);
		return registration;
	}

	@Bean
	public RequestInterceptor requestInterceptor() {
		return new RequestInterceptor() {
			@Override
			public void apply(RequestTemplate requestTemplate) {
				requestTemplate.header("CORRELATION_ID", MDC.get("CORRELATION_ID"));
			}
		};
	}
}
