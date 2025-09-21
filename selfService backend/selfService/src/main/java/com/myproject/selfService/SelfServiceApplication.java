package com.myproject.selfService;

import com.myproject.selfService.Service.SerialService;
import com.myproject.selfService.Service.WebSocketConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SelfServiceApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SelfServiceApplication.class, args);

		WebSocketConfig wsConfig = context.getBean(WebSocketConfig.class);
		SerialService serialService = new SerialService("COM6", wsConfig.getHandler());

		new Thread(serialService::startListening).start();
	}

}
