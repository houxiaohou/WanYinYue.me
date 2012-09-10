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
		<title>管理后台 - 玩音乐么</title>
	</head>
	<body>
		<s:include value="/header.jsp"></s:include>
		<div class="upload">
			<div class="upload_txt">
				今天上传了 ${newTabs} 个曲谱
			</div>
			<div class="upload_txt">
				今天新增了 ${newUsers} 个用户
			</div>
			<div class="upload_txt">
				现在共有  ${unTabs} 个未审核的曲谱
			</div>
		</div>
		<div class="unTab" style="margin-left:auto;margin-right:auto;width:800px;">
			<table border="0" cellpadding="3">
				<s:iterator value="uncheckedTabs">
					<tr>
						<td width="400">
							<a href="unchecked/${id}" >${name}</a>
						</td>
						<td align="center" width="200">
							${singer}
						</td>
						<td align="center" width="200">
							${uploadUser.userName}
						</td>
					</tr>
				</s:iterator>
			</table>
		</div>
	</body>
</html>

