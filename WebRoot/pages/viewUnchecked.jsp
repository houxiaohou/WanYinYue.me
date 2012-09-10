<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD xhtml 1.0 strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns:og="http://ogp.me/ns#">
	<head>
		<base href="<%=basePath%>">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="css/main.css" rel="stylesheet" type="text/css" />
		<link rel="shortcut icon" href="img/favicon.ico" >
		<title>${unCheckedTab.singer} ${unCheckedTab.name} 吉他谱 -玩音乐么</title>
	</head>
	<body>
		<s:include value="/header.jsp"></s:include>
		<div class="aboutTab">
			${unCheckedTab.singer}《 <span>${unCheckedTab.name} </span> 》吉他谱
		</div>
		<div class="checkButton">
			<a href="delete/${unCheckedTab.id}">删除</a>
			<a href="pass/${unCheckedTab.id}">通过</a>
		</div>
		<s:iterator value="unTabPics">
			<s:if test="width >= 1000">
				<div class="tab_pic" >
					<img src="tabs/${src}" style="width:1000px;" alt="${unCheckedTab.singer} ${unCheckedTab.name} 吉他谱"/>
				</div>
			</s:if>
			<s:else>
				<div class="tab_pic">
					<img src="tabs/${src}" style="width:${width}px;" alt="${unCheckedTab.singer} ${unCheckedTab.name} 吉他谱" />
				</div>
			</s:else>
		</s:iterator>
		<s:include value="/footer.jsp"></s:include>
	</body>
</html>

