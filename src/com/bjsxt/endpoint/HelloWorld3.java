
package com.bjsxt.endpoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.bjsxt.vo.Login;
import com.bjsxt.vo.Msg;
import com.google.gson.Gson;




@ServerEndpoint("/hello3")
public class HelloWorld3 {
	
	
	// ÿ�δ�һ�� webSocketͨ���� ��������һ�� helloWorldʵ����   ��һ�� endPointʵ���� ����Ӧ��һ��ͨ������Ϣ���ݡ�  
	// ����  ���� helloWorld  �̰߳�ȫŶ������ 
	
	private static  Set<HelloWorld3>  ss= new HashSet<HelloWorld3>();
	private static  List<String>  nickNames=new ArrayList<String>();
	private Session  session ;
	private Gson gson=new Gson();
	private String nickName;
	public HelloWorld3() {
		System.out.println("������һ�� helloWorldʵ����@������");
	
	}
	@OnOpen
	public void  open(Session  session ){
		System.out.println("������һ�� webSocketͨ��������  sid:"+session.getId());
		
		
		
		String queryString= session.getQueryString();
		
		
		
		this.nickName = queryString.substring(queryString.indexOf("=")+1);
		ss.add(this);
		nickNames.add(nickName);
		this.session = session;
		
		Login  login=new Login();
		login.setWelcome(nickName+"���������ң���");
		login.setNickNames(nickNames);
		
		//login����תΪ  json��ʽ
		String msg = gson.toJson(login);
		broadcast(ss,msg);
	}
	
	@OnClose
	public void close(){
		
			ss.remove(this);
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
			
			broadcast(ss, gson.toJson(temp));
			
	}
	
	
	private void  broadcast(Set<HelloWorld3>  ss, String msg){
		for (Iterator iterator = ss.iterator(); iterator.hasNext();) {
			HelloWorld3 helloWorld = (HelloWorld3) iterator.next();
			
			try {
				helloWorld.session.getBasicRemote().sendText(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
	
	
	
	
}
