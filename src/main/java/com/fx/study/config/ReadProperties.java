package com.fx.study.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadProperties {

	public static int webPort;
	
	public static int tcpPort;
	
	public static String url;
	
	public static String username;
	
	public static String password;
	
	public static void Read()
	{
		FileInputStream  input = null;
	   	try {
	   		input = new FileInputStream("config.properties");
	   		Properties properties = new Properties();
			properties.load(input);
			String webPortStr = properties.getProperty("webport");
			String tcpPortStr = properties.getProperty("tcpport");
			webPort = Integer.parseInt(webPortStr);
			tcpPort = Integer.parseInt(tcpPortStr);
			url = properties.getProperty("url");
	    	username = properties.getProperty("username");
	    	password = properties.getProperty("password");
			input.close();
		} catch (IOException e) {
			try {
				input.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
}
