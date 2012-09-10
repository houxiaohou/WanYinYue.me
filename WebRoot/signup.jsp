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
		<script type="text/javascript" src="js/account.js">
</script>
		<title>注册账号 - 玩音乐么</title>
	</head>
	<body>
		<div id="mainarea">
			<div id="maincontent">
				<s:include value="/header.jsp"></s:include>
				<div class="upload">
					<div class="upload_txt">
						注册账号
					</div>
					<form name="signupForm" onsubmit="AccountManager.postSignupForm();"
						action="javascript:;">
						<table border="0" class="userinfo_table">
							<tr>
								<td>
									<label>
										用户名
									</label>
									<input name="userName" type="text" id="userName"
										placeholder="可以是中文哦" onblur="AccountManager.checkUserName();" />
									<div class="ajaxMessage" id="userNameMessage"></div>
								</td>
							</tr>
							<tr>
								<td>
									<label>
										邮&nbsp;&nbsp;&nbsp;箱
									</label>
									<input name="email" type="text" id="email"
										placeholder="输入格式正确的邮箱" onblur="AccountManager.checkEmail();" />
									<div class="ajaxMessage" id="emailMessage"></div>
								</td>
							</tr>
							<tr>
								<td>
									<label>
										密&nbsp;&nbsp;&nbsp;码
									</label>
									<input name="password" type="password" id="password"
										placeholder="密码长度至少六位"
										onblur="AccountManager.checkPassword();" />
									<div class="ajaxMessage" id="pwdMessage"></div>
								</td>
							</tr>
						</table>
						<div class="sLogin">
							<a href="login">已有账号 ?</a>
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