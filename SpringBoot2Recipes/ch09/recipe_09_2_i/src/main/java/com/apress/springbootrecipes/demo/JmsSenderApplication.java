package com.apress.springbootrecipes.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author Marten Deinum
 */
@SpringBootApplication
@EnableScheduling
public class JmsSenderApplication {

    public static void main(String[] args) {
			SpringApplication.run(JmsSenderApplication.class, args);
    }
}

@Component
class MessageSender {

	private final JmsTemplate jms;

	MessageSender(JmsTemplate jms) {
		this.jms = jms;
	}

	@Scheduled(fixedRate = 1000)
	public void sendTime() {
		jms.convertAndSend("time-queue", "Current Date & Time is: " + LocalDateTime.now());
	}
}
