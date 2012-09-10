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
		<meta name="keywords" content="高清吉他谱,吉他谱" />
		<meta name="description"
			content="玩音乐么有着最高质量的吉他谱，精选出高清吉他谱供吉他爱好者练习，玩音乐么努力成为体验最好的吉他谱网站" />
		<link href="css/main.css" rel="stylesheet" type="text/css" />
		<link rel="shortcut icon" href="http://wanyinyue.me/img/favicon.ico">
		<title>${tab.singer} ${tab.name} 吉他谱 -玩音乐么</title>
	</head>
	<body>
		<div id="mainarea">
			<div id="maincontent">
				<s:include value="/header.jsp"></s:include>
				<div class="aboutTab">
					${tab.singer}《
					<span>${tab.name} </span> 》吉他谱
				</div>
				<s:iterator value="tabPics">
					<s:if test="width >= 1000">
						<div class="tab_pic">
							<img src="http://wanyinyue.me/tabs/${src}"
								style="width: 1000px; height: "
								alt="${tab.singer} ${tab.name} 吉他谱" />
						</div>
					</s:if>
					<s:else>
						<div class="tab_pic">
							<img src="http://wanyinyue.me/tabs/${src}"
								style="width:${width}px;height:${height};"
								alt="${tab.singer} ${tab.name} 吉他谱" />
						</div>
					</s:else>
				</s:iterator>
			</div>
		</div>
		<s:include value="/footer.jsp"></s:include>
	</body>
	<!-- Baidu Button BEGIN -->
	<script type="text/javascript" id="bdshare_js"
		data="type=slide&amp;mini=1&amp;img=4&amp;uid=1144555">
</script>
	<script type="text/javascript" id="bdshell_js">
</script>
	<script type="text/javascript">
document.getElementById("bdshell_js").src = "http://share.baidu.com/static/js/shell_v2.js?cdnversion="
		+ new Date().getHours();
</script>
	<!-- Baidu Button END -->
</html>

