
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="head">
	<div class="logo">
		<a href=""><img src="http://wanyinyue.me/img/logo.png" /> </a>
	</div>
	<div class="beta"></div>
	<s:if test="#session.USER == null ">
		<ul class="account">
			<li class="login">
				<a href="http://wanyinyue.me/login">登录</a>
			</li>
			<li class="register">
				<a href="http://wanyinyue.me/signup">注册</a>
			</li>
		</ul>
	</s:if>
	<s:else>
		<div class="user">
			${session.USER.userName}
		</div>

	</s:else>
	<div class="uphead">
		<a href="upload"
			onMouseover="if (window.showUploadGuidelines) showUploadGuidelines(true);"
			onMouseout="if (window.showUploadGuidelines) showUploadGuidelines(false);"><img
				src="http://wanyinyue.me/img/upload.png" /> </a>
	</div>
	<div id="uploadGuidelines" style="display: none;">
		<div class="triangleBorder"></div>
		<div id="uploadGuidelinesBody">
			点击这里上传你的高清曲谱吧
		</div>
	</div>
	<div class="follow">
		<div class="txt">
			关注玩音乐么
		</div>
		<div class="sina_logo">
			<a href="http://weibo.com/wanyinyueme" target="_blank"><img
					src="http://wanyinyue.me/img/sina_h.png" /> </a>
		</div>
	</div>
	<div class="search_box">
		<form method="get" onsubmit="return Holder.submit();" action="#">
			<input name="keyword" id="searchtext" class="searchtext" type="text"
				onfocus="Holder.focus();"
				onblur="Holder.blur()"
				value="输入歌手,歌曲试试看" />
			<input class="searchbut" type="submit" value="" />
		</form>
	</div>
	<script>
function showUploadGuidelines(show) {
	var element = document.getElementById("uploadGuidelines");
	element.style.display = show ? "block" : "none";
}

Holder = new function searchHolder() {
	var searchText = "输入歌手,歌曲试试看";

	var inputClass = document.getElementById("searchtext");

	this.focus = function() {
		if (inputClass.value == searchText) {
			inputClass.value = "";
		}
		;
	}

	this.blur = function() {
		if (inputClass.value == "") {
			inputClass.value = searchText;
		}
		;
	}

	this.submit = function() {
		if (inputClass.value != searchText && inputClass.value != "") {
			window.location = "search/" + inputClass.value;
		}
		return false;
	}

}
</script>
</div>

