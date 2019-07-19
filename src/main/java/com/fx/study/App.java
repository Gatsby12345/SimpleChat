package com.fx.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.fx.study.config.ReadProperties;
import com.fx.study.server.OfflineMsgServer;
import com.fx.study.server.TcpServer;

/**
 * @author fx
 * The program start from here !
 *
 */
@SpringBootApplication
@EnableTransactionManagement
public class App 
{
    public static void main( String[] args )
    { 
    	ReadProperties.Read();
        SpringApplication.run(App.class, args);
        Thread tcpServer = new Thread(new TcpServer());
        tcpServer.start();
        Thread afServer = new Thread(new OfflineMsgServer());
        afServer.start();
    }
}
