package com.fx.study.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.fx.study.bean.FriendsReq;
import com.fx.study.bean.Message;
import com.fx.study.dao.FriendsReqRepositary;
import com.fx.study.util.SpringUtil;
import com.google.gson.Gson;


public class MsgHandler implements Runnable {

	public static ConcurrentHashMap<Integer, Socket> id_session = new ConcurrentHashMap<Integer, Socket>();
	
	private Socket socket;
	
	public MsgHandler(Socket socket)
	{
		this.socket = socket;
	}
	
	InputStream inputStream;  //接收来自客户端的字节数据
    BufferedWriter writer;    //发送给客户端字符串数据
	
	@Override
	public void run() {
		
		try {
			inputStream = socket.getInputStream();//读取客户端消息  
            int byteLength = 0;  
            byte[] bytes = new byte[1024];
            while((byteLength = inputStream.read(bytes)) != -1){
            	String lineString = new String(bytes,0,byteLength);
            	Gson gson = new Gson();
            	Message message = gson.fromJson(lineString, Message.class);
            	List<Message> msgList = new ArrayList<Message>();
            	if(message.getType() == 0) //登录时发送的消息
            	{
            		if(id_session.containsKey(message.getId()))
            		{
            			msgList.add(message);
            			writer=new BufferedWriter(new OutputStreamWriter(id_session.get(message.getId()).getOutputStream()));//向客户端写消息
                		writer.write(gson.toJson(msgList));
                		writer.flush(); 
            		}
            		id_session.put(message.getId(), socket);
            	}
            	else if(message.getType() == 2 || message.getType() == 3) //2-加好友的请求消息  3-拒绝/同意
            	{
            		int destId = message.getDesId();
            		Socket passiveSocket = id_session.get(destId);
            		if(passiveSocket == null || !passiveSocket.isConnected())
            		{
            			if(OfflineMsgServer.id_messageList.containsKey(message.getDesId()))
            			{
            				msgList = OfflineMsgServer.id_messageList.get(message.getDesId());
            				boolean flag = false;
            				for(Message msg : msgList)
                			{
                				if(msg.getId() == message.getId() && msg.getDesId() == message.getDesId() && message.getType() == msg.getType()) 
                					flag = true;
                			}
                			if(!flag)
                				msgList.add(message);
            			}
            			else
            			{
            				msgList.add(message);
            				OfflineMsgServer.id_messageList.put(message.getDesId(), msgList);
            			}
            			
            		}
            		else
            		{
            			msgList.add(message);
            			writer=new BufferedWriter(new OutputStreamWriter(passiveSocket.getOutputStream()));//向客户端写消息
                		writer.write(gson.toJson(msgList));
            			writer.flush(); 
            		}
            		
            		FriendsReqRepositary frp = SpringUtil.getBean(FriendsReqRepositary.class);
            		if(message.getType() == 2)
            		{
            			if(frp.GetFRById(message.getId(), message.getDesId()).size() == 0)
                		{
                			FriendsReq fr = new FriendsReq(message.getId(), message.getDesId(), new Date(), 0, message.getContent());
                			frp.save(fr);
                		}
            		}
            		else if(message.getType() == 3)
            		{
            			if("no".equals(message.getContent()))
            			{
            				frp.update(2, message.getDesId(), message.getId());
            			}
            			
            		}
            		
            	}
            	else if(message.getType() == 4) //下线消息
            	{
            		id_session.remove(message.getId());
            	}
            	else
            	{
            		Socket passiveSocket = id_session.get(message.getDesId());
                	
                	if(passiveSocket != null)
                	{
                		msgList.add(message);
                		writer=new BufferedWriter(new OutputStreamWriter(passiveSocket.getOutputStream()));//向客户端写消息
                		writer.write(gson.toJson(msgList));
                		writer.flush(); 
                	}
                	else
                	{ 
                		if(OfflineMsgServer.id_messageList.containsKey(message.getDesId()))
                		{
                			msgList = OfflineMsgServer.id_messageList.get(message.getDesId());
                			msgList.add(message);
                		}
                		else
                		{
            				msgList.add(message);
            				OfflineMsgServer.id_messageList.put(message.getDesId(), msgList);
                		}
                	}
            	}
            }  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
