package com.fx.study.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fx.study.bean.Message;
import com.google.gson.Gson;

/**
 * to send the message for offine friends 
 * @author fx
 *
 */

public class OfflineMsgServer implements Runnable {

	public static ConcurrentHashMap<Integer,List<Message>> id_messageList = new ConcurrentHashMap<Integer,List<Message>>();
	
	@Override
	public void run() {
		
			while(true)
			{
				if(id_messageList.size() == 0)
				{
					continue;
				}
				else
				{
					Map<List<Message>, Socket> map = new HashMap<>();
					id_messageList.forEach((id,list) -> {if(MsgHandler.id_session.containsKey(id)) map.put(list, MsgHandler.id_session.get(id));});
					if(map.size() > 0)
					{
						SendOfflineMsg(map);
					}
				}
			}
	}
	
	private void SendOfflineMsg(Map<List<Message>, Socket> map)
	{
		try {
			Gson gson = new Gson();
			for(List<Message> msgList : map.keySet())
			{
				Socket socket = map.get(msgList);
				if(socket.isConnected())
				{
					BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));//向客户端写消息
					writer.write(gson.toJson(msgList));  
					writer.flush();  
				}
				else
				{
					MsgHandler.id_session.remove(msgList.get(0).getDesId());
				}
				id_messageList.remove(msgList.get(0).getDesId());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
