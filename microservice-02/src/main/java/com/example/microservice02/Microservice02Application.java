package com.example.microservice02;

import com.newrelic.api.agent.NewRelic;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.config.GlobalChannelInterceptor;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@SpringBootApplication
public class Microservice02Application {

	public static void main(String[] args) {
		SpringApplication.run(Microservice02Application.class, args);
	}

	static final String[] CONTEXTUAL_HEADERS = {
			"CORRELATION_ID",
//			"x-newrelic-id",
//			"x-newrelic-transaction"
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
				}

				System.err.println("=================================================");
				NewRelic.addCustomParameter("CORRELATION_ID", MDC.get("CORRELATION_ID"));
				filterChain.doFilter(httpServletRequest, httpServletResponse);
			}
		});
		registration.addUrlPatterns("/*");
		registration.setName("someFilter");
		registration.setOrder(1);
		return registration;
	}

	@Bean
	@GlobalChannelInterceptor(patterns="*")
	public ChannelInterceptor messageChannelInterceptor() {
		ChannelInterceptor channelInterceptor = new ChannelInterceptorAdapter() {

			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				MessageBuilder<?> mb = MessageBuilder.fromMessage(message);
				Arrays.asList(CONTEXTUAL_HEADERS).forEach(h -> mb.setHeader(h, MDC.get(h)));
				System.err.println("PRE SEND =============>>>>>>>>>>>>>>>>>> " + message);
				return mb.build();
			}

		};
		return channelInterceptor;
	}
}
