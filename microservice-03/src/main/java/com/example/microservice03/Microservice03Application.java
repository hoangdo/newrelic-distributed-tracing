package com.example.microservice03;

import com.newrelic.api.agent.ExtendedRequest;
import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.config.GlobalChannelInterceptor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

import java.util.Collections;
import java.util.Enumeration;

@EnableFeignClients
@EnableBinding(Sink.class)
@SpringBootApplication
public class Microservice03Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Microservice03Application.class);

    static final String[] CONTEXTUAL_HEADERS = {
            "CORRELATION_ID"
    };
    
    @Autowired
    private AnotherRest anotherRest;

    public static void main(String[] args) {
        SpringApplication.run(Microservice03Application.class, args);
    }

    @Trace(dispatcher = true, metricName = "Microservice03Application.loggerSink")
    @StreamListener(Sink.INPUT)
    public void loggerSink(Message<?> content) {
        NewRelic.addCustomParameter("CORRELATION_ID", MDC.get("CORRELATION_ID"));
        LOGGER.error("Received: " + content);
        try {
            Thread.sleep(3000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        anotherRest.sayhello();
    }

    @Bean
    @GlobalChannelInterceptor(patterns = "*")
    public ChannelInterceptor messageChannelInterceptor() {
        ChannelInterceptor channelInterceptor = new ChannelInterceptorAdapter() {

            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                System.err.println("PRE SEND =============>>>>>>>>>>>>>>>>>>" + message);
                MDC.put("CORRELATION_ID", message.getHeaders().get("CORRELATION_ID").toString());
                return message;
            }
        };
        return channelInterceptor;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                for (String header : CONTEXTUAL_HEADERS) {
                    requestTemplate.header(header, MDC.get(header));
                }
            }
        };
    }
}
