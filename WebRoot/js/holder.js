/**
 * @author Young
 */
var TestHolder = new function(){
	var loginUserName = "用户名或邮箱";
	var loginPassword = "注册密码";
	
	var sinupUserName = "可以是中文哦";
	var signUpEmail = "格式正确的邮箱";
	var signUpPwd = "密码加密后保存";
	
	this.focus = function(type){
		switch (type) {
			case 1:
				if (document.getElementById("userName").value == loginUserName) {
					document.getElementById("userName").value == "";
				}
				break;
			case 2:
				if (document.getElementById("password").value == loginPassword) {
					document.getElementById("password").value == "";
				}
				break;
			case 3:
				if (document.getElementById("userName").value == sinupUserName) {
					document.getElementById("userName").value == "";
				}
				break;
			case 4:
				if (document.getElementById("email").value == signUpEmail) {
					document.getElementById("email").value == "";
				}
				break;
			case 5:
				if (document.getElementById("password").value == signUpPwd) {
					document.getElementById("password").value == "";
				}
				break;
		};
			}
	
	this.blur = function(type){
		switch (type) {
			case 1:
				if (document.getElementById("userName").value == "") {
					document.getElementById("userName").value == loginUserName;
				}
				break;
			case 2:
				if (document.getElementById("password").value == "") {
					document.getElementById("password").value == loginPassword;
				}
				break;
			case 3:
				if (document.getElementById("userName").value == "") {
					document.getElementById("userName").value == sinupUserName;
				}
				break;
			case 4:
				if (document.getElementById("email").value == "") {
					document.getElementById("email").value == signUpEmail;
				}
				break;
			case 5:
				if (document.getElementById("password").value == "") {
					document.getElementById("password").value == signUpPwd;
				}
				break;
		};
			}
	
};
