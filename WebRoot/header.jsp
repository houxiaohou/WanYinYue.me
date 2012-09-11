
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="head">
	<div class="logo">
		<a href=""><img src="img/logo.png" /> </a>
	</div>
	<div class="beta">
	</div>
	<s:if test="#session.USER != ''">
		<div class="account">
			<a href="login" class="login">登 录</a>
			<a href="signup" class="register">注 册</a>
			<a href="upload" class="uphead"><img src="img/upload.png"
					width="40px" height="40px" /> </a>
		</div>
	</s:if>
	<s:else>
		<div class="user">
			<div class="username" />
				${session.USER.userName}
			</div>
			<div class="avatar">
				<img src="img/avatar.png" width="40px" height="40px" alt="头像" />
			</div>
			<a href="upload" class="uphead" title="上传吉他谱"><img
					src="img/upload.png" width="40px" height="40px" /> </a>
			<a href="logout" class="signout" title="退出登录"><img
					src="img/signout.png" /> </a>
		</div>
	</s:else>
	<div class="search_box">
		<form method="get" onsubmit="return Holder.submit();" action="#">
			<input name="keyword" id="searchtext" class="searchtext" type="text"
				onfocus="Holder.focus();" onblur="Holder.blur()" value="输入歌手,歌曲试试看" />
			<input class="searchbut" type="submit" value="搜 索" />
		</form>
	</div>

	<div class="fllow">
		<div class="span">
			关注
		</div>
		<a href="http://weibo.com/wanyinyueme" class="weibo"> <img
				src="img/sina.png" alt="" /> </a>
	</div>
	<script>
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

