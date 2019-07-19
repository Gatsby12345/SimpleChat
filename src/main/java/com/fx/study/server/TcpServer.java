package com.fx.study.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.fx.study.config.ReadProperties;

public class TcpServer implements Runnable{
	@Override
	public void run() {
		try
		{
			@SuppressWarnings("resource")
			ServerSocket serverSocket =new ServerSocket(ReadProperties.tcpPort);
			System.out.println("服务端已经启动监听");
			Socket socket = null;
			while(true)
			{
				socket = serverSocket.accept();
				Thread thread = new Thread(new MsgHandler(socket));
				thread.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
