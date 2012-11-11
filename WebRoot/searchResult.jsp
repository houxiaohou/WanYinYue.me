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
		<title>玩音乐么-专注于高质量曲谱</title>
	</head>
	<body>
		<div id="mainarea">
			<div id="maincontent">
				<s:include value="/header.jsp"></s:include>
				<div id="body">
					<div id="count" class="count">
						<span id="number" class="number">${totalNum}</span> 个有关
						"${keyword}" 的吉他谱
					</div>
					<div id="PG_1" class="list">
						<div class="tabcase">
							<s:iterator value="tabs">
								<div class="tab_info">
									<div class="tab">
										<a href="tabs/${id}"> <img src="tab/${id}_thumb.jpg">
											<s:if test=" tabPic.get(0).width >= 800">
												<img src="img/hd.png" />
											</s:if> </a>

									</div>
									<s:if test="name.length() + singer.length() <= 20">
										<div class="info" style="font-size: 26px;">
											<div class="tab_name">
												<a href="tabs/${id}">${name}-${singer}</a>
											</div>
										</div>
									</s:if>
									<s:elseif
										test="name.length() + singer.length() > 20 && name.length() + singer.length() < 25">
										<div class="info" style="font-size: 20px;">
											<div class="tab_name">
												<a href="tabs/${id}">${name}-${singer}</a>
											</div>
										</div>
									</s:elseif>
									<s:elseif
										test="name.length() + singer.length() >= 25 && name.length() + singer.length() < 30">
										<div class="info" style="font-size: 18px;">
											<div class="tab_name">
												<a href="tabs/${id}">${name}-${singer}</a>
											</div>
										</div>
									</s:elseif>
									<div class="download_count">
										<span class="number">${checkedTimes} </span>人浏览过
									</div>
								</div>
							</s:iterator>
						</div>
						<div id="case_bg2"></div>
						<div id="case_bg3"></div>
					</div>
					<div class='pagination'>
						<div class='paginationbar'>
							<s:if test="pageNumber >= 2">
								<a class="pageButton" href="search/${keyword}/p/${pageNumber-1}"
									style="padding-right: 11px;">< 上一页</a>
							</s:if>
							<s:if test="pageNumber-4 > 0 && pageNumber <= totalPage">
								<a class='pageNumber' href='search/${keyword}/p/${pageNumber-4}'>${pageNumber-4}</a>
							</s:if>
							<s:if test="pageNumber-3 > 0 && pageNumber <= totalPage">
								<a class='pageNumber' href='search/${keyword}/p/${pageNumber-3}'>${pageNumber-3}</a>
							</s:if>
							<s:if test="pageNumber-2 > 0 && pageNumber <= totalPage">
								<a class='pageNumber' href='search/${keyword}/p/${pageNumber-2}'>${pageNumber-2}</a>
							</s:if>
							<s:if test="pageNumber-1 > 0 && pageNumber <= totalPage">
								<a class='pageNumber' href='search/${keyword}/p/${pageNumber-1}'>${pageNumber-1}</a>
							</s:if>
							<s:if test="pageNumber <= totalPage">
								<div class='pageNumberOn'>
									${pageNumber}
								</div>
							</s:if>
							<s:if test="pageNumber+1 <= totalPage">
								<a class='pageNumber' href='search/${keyword}/p/${pageNumber+1}'>${pageNumber+1}</a>
							</s:if>
							<s:if test="pageNumber+2 <= totalPage">
								<a class='pageNumber' href='search/${keyword}/p/${pageNumber+2}'>${pageNumber+2}</a>
							</s:if>
							<s:if test="pageNumber+3 <= totalPage">
								<a class='pageNumber' href='search/${keyword}/p/${pageNumber+3}'>${pageNumber+3}</a>
							</s:if>
							<s:if test="pageNumber+4 <= totalPage">
								<a class='pageNumber' href='search/${keyword}/p/${pageNumber+4}'>${pageNumber+4}</a>
							</s:if>
							<s:if test="pageNumber < totalPage">
								<a class='pageButton' href='search/${keyword}/p/${pageNumber+1}'
									style='border-right: 1px solid white; padding-left: 11px;'>下一页
									></a>
							</s:if>
						</div>
					</div>
				</div>
			</div>
		</div>
		<s:include value="/footer.jsp"></s:include>
	</body>
</html>

