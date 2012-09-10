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
		<script language='javascript' type='text/javascript'
			src='js/swfupload.js'>
</script>
		<script language='javascript' type='text/javascript'
			src='js/swfupload.queue.js'>
</script>
		<script language='javascript' type='text/javascript'
			src='js/uploadHandlers.js'>
</script>
		<script language='javascript' type='text/javascript'
			src='js/fileprogress.js'>
</script>
		<script language='javascript' type='text/javascript'
			src='js/uploadSpaces.js'>
</script>
		<title>玩音乐么-专注于高质量曲谱</title>
	</head>
	<body>
		<div id="mainarea">
			<div id="maincontent">
				<s:include value="/header.jsp"></s:include>
				<div class="upload">
					<form class="stdForm" method="post" enctype="multipart/form-data"
						id="upload" name="upload" action="upload">
						<div class="upload_txt">
							上传曲谱
						</div>
						<table border="0" class="userinfo_table">
							<tr>
								<td>
									<label>
										曲谱的名字
									</label>
									<input name="name" type="text" />
								</td>
							</tr>
							<tr>
								<td>
									<label>
										歌手或作者
									</label>
									<input name="singer" type="text" />
								</td>
							</tr>
						</table>
						<script type='text/javascript'>
var swfu;

window.onload = function() {
	var settings = {
		flash_url : "js/swfupload_f10.swf",
		upload_url : "upload", // Relative to the SWF file
		file_size_limit : "30 MB",
		file_types : "*.jpg;*.png;*.gif;*.bmp",
		file_types_description : "All Files",
		file_upload_limit : 100,
		file_queue_limit : 0,
		custom_settings : {
			progressTarget : "fsUploadProgress",
			cancelButtonId : "btnCancel"
		},
		debug : false,

		// Button settings
		button_image_url : "img/selectButton.png",
		button_width : "120",
		button_height : "40",
		button_placeholder_id : "selectBtn",
		button_cursor : SWFUpload.CURSOR.HAND,

		file_queued_handler : fileQueued,
		file_queue_error_handler : fileQueueError,
		file_dialog_complete_handler : fileDialogComplete,
		upload_start_handler : uploadStart,
		upload_progress_handler : uploadProgress,
		upload_error_handler : uploadError,
		upload_success_handler : uploadSuccess,
		upload_complete_handler : uploadComplete,
		queue_complete_handler : queueComplete

	};

	swfu = new SWFUpload(settings);
};

function submitSWFForm() {
	var params = {};
	frm = document.upload;

	if (frm.name.value == "") {
		alert('曲谱名字必须填哦~');
	} else if (frm.singer.value == "") {
		alert('歌手必须填哦~');
	} else {
		submitBtn = document.getElementById('submit');
		submitBtn.disabled = true;
		frmInputs = frm.getElementsByTagName("input");
		for ( var i = 0; i < frmInputs.length; i++) {
			inputElement = frmInputs.item(i);
			params[inputElement.name] = inputElement.value;
		}
		totalNum = swfu.getStats().files_queued;

		for ( var i = 0; i < totalNum; i++) {
			swfu.addFileParam("SWFUpload_0_" + i, "order", i + 1);
		}
		params["total"] = totalNum;
		swfu.setPostParams(params);
		swfu.startUpload();
	}
}

function afterUploadComplete() {
	window.location = "uploadSuccess";
}
</script>
						<fieldset class="flash" id="fsUploadProgress"
							style='font-size: 12px; color: black;'>
							<legend>
								上传曲谱
							</legend>
						</fieldset>
						<input name="sess" value="${sess}" style="display: none;">
						<button id='btnCancel' class='largeButton'
							style='display: none; font-size: 12px;'
							onclick="swfu.cancelQueue();return false;" value="cancel upload"
							disabled="disabled">
							取消
						</button>
						<div class="selectbutton">
							<div id="selectBtn"></div>
							<button id='submit' class='uploadBtn'
								onclick="submitSWFForm();return false;" name="submit" value="">
								上 传
							</button>
						</div>
					</form>
				</div>
			</div>
		</div>
		<s:include value="/footer.jsp"></s:include>
	</body>
</html>