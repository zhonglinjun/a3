<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>My JSP 'index.jsp' starting page</title>
  </head>
  <body>
  		<form  action="loginServlet"  method="post">
  			用户名： <input name="username"  /><br/>
  			密码：  <input name="pwd"  /><br/>
  			<input type="submit"  />
  		</form>
  </body>
</html>
