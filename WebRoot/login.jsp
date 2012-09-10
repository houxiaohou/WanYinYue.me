
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = "http://wanyinyue.me";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD xhtml 1.0 strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns:og="http://ogp.me/ns#">
	<head>
		<base href="<%=basePath%>">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta name="keywords" content="高清吉他谱,吉他谱" />
		<meta name="description" content="玩音乐么有着最高质量的吉他谱，精选出高清吉他谱供吉他爱好者练习，玩音乐么努力成为体验最好的吉他谱网站" />
		<link href="css/main.css" rel="stylesheet" type="text/css" />
		<link rel="shortcut icon" href="img/favicon.ico">
		<script type="text/javascript" src="js/account.js">
</script>
		<title>登录 - 玩音乐么</title>
	</head>
	<body>
		<div id="mainarea">
			<div id="maincontent">
				<s:include value="/header.jsp"></s:include>
				<div class="upload">
					<div class="upload_txt">
						登录玩音乐么
					</div>
					<form name="signupForm" onsubmit="AccountManager.postLoginForm();"
						action="javascript:;">
						<table border="0" class="userinfo_table">
							<tr>
								<td>
									<label>
										账&nbsp;&nbsp;&nbsp;号
									</label>
									<input name="userName" type="text" placeholder="用户名或邮箱"
										id="userName" />
									<div class="ajaxMessage" id="userNameMessage"></div>
								</td>
							</tr>
							<tr>
								<td>
									<label>
										密&nbsp;&nbsp;&nbsp;码
									</label>
									<input name="password" type="password" placeholder="注册密码"
										id="password" />
									<div class="ajaxMessage" id="pwdMessage"></div>
								</td>
							</tr>
						</table>
						<input name="prePage" value="${prePage}" style="display: none;" />
						<div class="sLogin">
							<a href="signup">还没账号 ?</a>
						</div>
						<button id='submit' class='sBtn' name="submit" value="">
							登 录
						</button>
					</form>
				</div>
			</div>
		</div>
		<s:include value="/footer.jsp"></s:include>
	</body>

</html>