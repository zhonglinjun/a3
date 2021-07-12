
package com.bjsxt.endpoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.annotation.WebServlet;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.bjsxt.vo.Login;
import com.bjsxt.vo.Msg;
import com.google.gson.Gson;




@ServerEndpoint("/hello")
public class HelloWorld {
	
	
	// ÿ�δ�һ�� webSocketͨ���� ��������һ�� helloWorldʵ����   ��һ�� endPointʵ���� ����Ӧ��һ��ͨ������Ϣ���ݡ�  
	// ����  ���� helloWorld  �̰߳�ȫŶ������ 
	
	private Gson gson=new Gson();
	
	private String nickName;
	private static  List<String>  nickNames=new ArrayList<String>();
	
	private static  Set<HelloWorld>  ss= new HashSet<HelloWorld>();
	private Session session;
	

	@OnOpen
	public void  open(Session  session){
		String queryString= session.getQueryString();
		//��ȡ��½��
		this.nickName = queryString.substring(queryString.indexOf("=")+1);
		//�ѵ�½�����뵽list����
		nickNames.add(nickName);
		
		//�ѵ�ǰSESSION������ǰ���������session
		this.session = session;
		
		ss.add(this);
		
		

		Login  login=new Login();
		login.setWelcome(nickName+"���������ң���");
		login.setNickNames(nickNames);
		
		
		String msg = gson.toJson(login);
		
		broadcast(ss,msg);
	}
	
	
	
	
	private void  broadcast(Set<HelloWorld>  ss, String msg){
		for (Iterator iterator = ss.iterator(); iterator.hasNext();) {
			HelloWorld helloWorld = (HelloWorld) iterator.next();
			
			try {
				helloWorld.session.getBasicRemote().sendText(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	@OnClose
	public void close(){
		
		ss.remove(this);//������Ϊ����,����SET����û�д�SESSION
		nickNames.remove(nickName);
		
		Login  login=new Login();
		login.setWelcome(nickName+"�˳������ң���");
		login.setNickNames(nickNames);
		
		String msg = gson.toJson(login);
		broadcast(ss,msg);
			
	}
	
	
	
	@OnMessage
	public   void  receiveMsg(Session session,String msg ){
		System.out.println("�յ���Ϣ�� �� ����sid:"+session.getId());
		System.out.println("��Ϣ��"+msg);
		
		Msg  temp= gson.fromJson(msg, Msg.class);
		temp.setDate(new Date().toLocaleString());
		temp.setFrom(this.nickName);
		
		broadcast1(ss, temp);
			
	}
	
	
	
	private void  broadcast1(Set<HelloWorld>  ss, Msg msg){
			if(msg.getTo().equals(""))
			{
				for (Iterator iterator = ss.iterator(); iterator.hasNext();) {
					HelloWorld helloWorld = (HelloWorld) iterator.next();
					
					try {
						helloWorld.session.getBasicRemote().sendText(gson.toJson(msg));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}else
			{
				String tos[] = msg.getTo().split(",");
				
				for (Iterator iterator = ss.iterator(); iterator.hasNext();) {
					HelloWorld helloWorld = (HelloWorld) iterator.next();
					
					for(int i = 0; i<tos.length;i++)
					{
						if (helloWorld.nickName.contentEquals(tos[i])) {
							try {
								helloWorld.session.getBasicRemote().sendText(gson.toJson(msg));
							} catch (IOException e) {
								e.printStackTrace();
							}
							break;
						}
					}
				}
			}
			
		
	}
	
	
	
	
	
	
}
