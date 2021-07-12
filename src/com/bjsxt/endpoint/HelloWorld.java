
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
	
	
	// 每次打开一个 webSocket通道， 都会生成一个 helloWorld实例，   而一个 endPoint实例， 用来应对一个通道的信息传递。  
	// 所以  我们 helloWorld  线程安全哦。。。 
	
	private Gson gson=new Gson();
	
	private String nickName;
	private static  List<String>  nickNames=new ArrayList<String>();
	
	private static  Set<HelloWorld>  ss= new HashSet<HelloWorld>();
	private Session session;
	

	@OnOpen
	public void  open(Session  session){
		String queryString= session.getQueryString();
		//获取登陆名
		this.nickName = queryString.substring(queryString.indexOf("=")+1);
		//把登陆名加入到list当中
		nickNames.add(nickName);
		
		//把当前SESSION赋给当前对象里面的session
		this.session = session;
		
		ss.add(this);
		
		

		Login  login=new Login();
		login.setWelcome(nickName+"进入聊天室！！");
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
		
		ss.remove(this);//就是因为这里,所以SET里面没有存SESSION
		nickNames.remove(nickName);
		
		Login  login=new Login();
		login.setWelcome(nickName+"退出聊天室！！");
		login.setNickNames(nickNames);
		
		String msg = gson.toJson(login);
		broadcast(ss,msg);
			
	}
	
	
	
	@OnMessage
	public   void  receiveMsg(Session session,String msg ){
		System.out.println("收到信息啦 ， 来自sid:"+session.getId());
		System.out.println("信息："+msg);
		
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
