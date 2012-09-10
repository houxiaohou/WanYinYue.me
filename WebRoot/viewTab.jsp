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
				<ul class="share">
					<li>
						<script type="text/javascript"
							src="http://widget.renren.com/js/rrshare.js">
</script>
						<a name="xn_share" onclick="shareClick()" type="icon"
							href="javascript:;"></a>
						<script type="text/javascript">
function shareClick() {
	var rrShareParam = {
		resourceUrl : 'http://wanyinyue.me/tabs/${tab.id}', //分享的资源Url
		srcUrl : 'http://wanyinyue.me/tabs/${tab.id}', //分享的资源来源Url,默认为header中的Referer,如果分享失败可以调整此值为resourceUrl试试
		pic : 'http://wanyinyue.me/tabs/${tab.id}_1.jpg', //分享的主题图片Url
		title : '我分享了${tab.singer}的《${tab.name}》吉他谱', //分享的标题
		description : '玩音乐么(wanyinyue.me)体验最好的吉他谱网站 ' //分享的详细描述
	};
	rrShareOnclick(rrShareParam);
}
</script>
					</li>
					<li>
						<script type="text/javascript" charset="utf-8">
(function() {
	var _w = 72, _h = 16;
	var param = {
		url : location.href,
		type : '3',
		count : '0',
		/**是否显示分享数，1显示(可选)*/
		appkey : '',
		/**您申请的应用appkey,显示分享来源(可选)*/
		title : '我分享了${tab.singer}的《${tab.name}》高清吉他谱，学吉他的有福了！ ',
		/**分享的文字内容(可选，默认为所在页面的title)*/
		pic : '',
		/**分享图片的路径(可选)*/
		ralateUid : '2876259790',
		/**关联用户的UID，分享微博会@该用户(可选)*/
		language : 'zh_cn',
		/**设置语言，zh_cn|zh_tw(可选)*/
		rnd : new Date().valueOf()
	}
	var temp = [];
	for ( var p in param) {
		temp.push(p + '=' + encodeURIComponent(param[p] || ''))
	}
	document
			.write('<iframe allowTransparency="true" frameborder="0" scrolling="no" src="http://hits.sinajs.cn/A1/weiboshare.html?'
					+ temp.join('&')
					+ '" width="'
					+ _w
					+ '" height="'
					+ _h
					+ '"></iframe>')
})()
</script>
					</li>
				</ul>
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

