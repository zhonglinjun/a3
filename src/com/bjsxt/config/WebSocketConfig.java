package com.bjsxt.config;

import java.util.HashSet;
import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

import com.bjsxt.endpoint.HelloWorld;

public class WebSocketConfig  implements  ServerApplicationConfig {

	/*
	 * 1.  getAnnotatedEndpointClasses
	 * 2.  getEndpointConfigs
	 * 
	 * ���������������������ע�� webSocket�ġ�   ֻ����ע��ķ�ʽ��ͬ��
	 * 1������ ע��ķ�ʽ
	 * 2������ �ӿڵķ�ʽ
	 * 
	 * ��Ȼ ע��ķ�ʽ���ӵ� ���򵥡�  �ӿڵķ�ʽ���ӵĴ�ͳ���Ͻ���
	 * 
	 * (non-Javadoc)
	 * @see javax.websocket.server.ServerApplicationConfig#getAnnotatedEndpointClasses(java.util.Set)
	 */
	
	public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scan) {
		System.out.println("����ɨ�����е�webSocket���񣡣���      " + scan.size());
		return scan; 
	}

	
	
	
	public Set<ServerEndpointConfig> getEndpointConfigs(
			Set<Class<? extends Endpoint>> scanned) {
		
		return null;
	}

}
