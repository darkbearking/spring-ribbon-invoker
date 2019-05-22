 package com.troila.lw;

import java.util.Scanner;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class InvokerApp {

	public static void main(String[] args) {
		new SpringApplicationBuilder(InvokerApp.class).web(true).run(args);
		
//		Scanner scan = new Scanner(System.in);
//		String port = scan.nextLine();
//		//下面這裡可以通過自己指定properties，作用相當於在yml配置文件中添加相同的內容
//		new SpringApplicationBuilder(InvokerApp.class).properties("server.port=" + port).run(args);
	}

}
