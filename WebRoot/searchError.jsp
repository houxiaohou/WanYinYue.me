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
		<link rel="shortcut icon" href="img/favicon.ico">
		<title>玩音乐么</title>
	</head>
	<body>
		<div id="mainarea">
			<div id="maincontent">
				<s:include value="/header.jsp"></s:include>
				<div class="tabError">
					o(>﹏<)o 没找到符合 "${keyword}" 条件的谱子
				</div>
				<div class="intro">
					填写下面的内容，更快获得你想要的曲谱！
				</div>
				<div class="formdiv">
					<form class="recordForm" method="post" action="postRecord">
						<div class="inputCationbox">
							曲谱名字
							<div class="inputbox">
								<input class="input_name" maxlength="100" name="tabName"
									type="text" />
							</div>
						</div>
						<div class="inputCationbox">
							作者名字
							<div class="inputbox">
								<input class="input_singer" maxlength="100" name="tabSinger"
									type="text" />
							</div>
						</div>
						<div class="inputCationbox">
							你的邮箱
							<div class="inputbox">
								<input class="input_email" maxlength="100" name="email"
									type="text" />
							</div>
						</div>
						<button id='submit' class='sBtn' name="submit" value="">
							提 交
						</button>
					</form>
				</div>
			</div>
		</div>
		<s:include value="/footer.jsp"></s:include>
	</body>
</html>


