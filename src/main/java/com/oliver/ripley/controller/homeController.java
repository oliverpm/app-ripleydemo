package com.oliver.ripley.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.InetAddress;

@RestController
public class homeController {

	@GetMapping("/")
	public String inicio() {
		String name="", address="";
		try {
			InetAddress inetadd = InetAddress.getLocalHost();
			name = inetadd.getHostName();
			address = inetadd.getHostAddress();
		}
		catch(Exception e){
		}
		return "\n Hola Banco Ripley!!! \n" +
				"<br> \n" +
				"<br> Branch:  MASTER \n" +
				"<br> ==> POD: Host Name: " + name + "\n" +
				"<br> ==> POD: Ip Address: " + address + "\n";
	}
}
