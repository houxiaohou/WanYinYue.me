
json_parse=(function(){var at,ch,escapee={'"':'"','\'':'\'','\\':'\\','/':'/',b:'\b',f:'\f',n:'\n',r:'\r',t:'\t'},text,error=function(m){throw{name:'SyntaxError',message:m,at:at,text:text};},next=function(c){if(c&&c!==ch){error("Expected '"+c+"' instead of '"+ch+"'");}
	ch=text.charAt(at);at+=1;return ch;},number=function(){var number,string='';if(ch==='-'){string='-';next('-');}
	while(ch>='0'&&ch<='9'){string+=ch;next();}
	if(ch==='.'){string+='.';while(next()&&ch>='0'&&ch<='9'){string+=ch;}}
	if(ch==='e'||ch==='E'){string+=ch;next();if(ch==='-'||ch==='+'){string+=ch;next();}
	while(ch>='0'&&ch<='9'){string+=ch;next();}}
	number=+string;if(isNaN(number)){error("Bad number");}else{return number;}},string=function(){var hex,i,string='',uffff;if(ch==='"'){while(next()){if(ch==='"'){next();return string;}else if(ch==='\\'){next();if(ch==='u'){uffff=0;for(i=0;i<4;i+=1){hex=parseInt(next(),16);if(!isFinite(hex)){break;}
	uffff=uffff*16+hex;}
	string+=String.fromCharCode(uffff);}else if(typeof escapee[ch]==='string'){string+=escapee[ch];}else{break;}}else{string+=ch;}}}
	error("Bad string");},white=function(){while(ch&&ch<=' '){next();}},word=function(){switch(ch){case't':next('t');next('r');next('u');next('e');return true;case'f':next('f');next('a');next('l');next('s');next('e');return false;case'n':next('n');next('u');next('l');next('l');return null;}
	error("Unexpected '"+ch+"'");},value,array=function(){var array=[];if(ch==='['){next('[');white();if(ch===']'){next(']');return array;}
	while(ch){array.push(value());white();if(ch===']'){next(']');return array;}
	next(',');white();}}
	error("Bad array");},object=function(){var key,object={};if(ch==='{'){next('{');white();if(ch==='}'){next('}');return object;}
	while(ch){key=string();white();next(':');if(Object.hasOwnProperty.call(object,key)){error('Duplicate key "'+key+'"');}
	object[key]=value();white();if(ch==='}'){next('}');return object;}
	next(',');white();}}
	error("Bad object");};value=function(){white();switch(ch){case'{':return object();case'[':return array();case'"':return string();case'-':return number();default:return ch>='0'&&ch<='9'?number():word();}};return function(source,reviver){var result;text=source;at=0;ch=' ';result=value();white();if(ch){error("Syntax error");}
	return typeof reviver==='function'?(function walk(holder,key){var k,v,value=holder[key];if(value&&typeof value==='object'){for(k in value){if(Object.hasOwnProperty.call(value,k)){v=walk(value,k);if(v!==undefined){value[k]=v;}else{delete value[k];}}}}
		return reviver.call(holder,key,value);}({'':result},'')):result;};}());


var AccountManager = {

	checkUserName : function() {
		var userName = document.getElementById("userName").value;
		var params = "authorize/userName=" + encodeURIComponent(userName) + "/op=u/";
		var elem =  document.getElementById("userNameMessage");
		this.makeRequest (params, elem);
	},

	checkEmail : function() {
		var email = document.getElementById("email").value;
		var params = "authorize/email=" + encodeURIComponent(email) + "/op=e/";
		var elem =  document.getElementById("emailMessage");
		this.makeRequest (params, elem);
	},

	checkPassword : function() {
		var password = document.getElementById("password").value;
		var params = "authorize/password=" + encodeURIComponent(password) + "/op=p/";
		var elem =  document.getElementById("pwdMessage");
		this.makeRequest (params, elem);
	},

	postSignupForm : function() {
		var userName = document.getElementById("userName").value;
		var email = document.getElementById("email").value;
		var password = document.getElementById("password").value;
		var params = "authorize/userName=" + encodeURIComponent(userName) + "/email=" +  encodeURIComponent(email) + "/password=" + encodeURIComponent(password) + "/op=c/";
		var elem =  document.getElementById("pwdMessage");
		this.makeRequest(params, "c");
	},

	postLoginForm : function() {
		var userName = document.getElementById("userName").value;
		var password = document.getElementById("password").value;
		var params = "authorize/userName=" + encodeURIComponent(userName) + "/password=" + encodeURIComponent(password) + "/op=l/";
		this.makeRequest(params, "l");
	},

	makeRequest : function(params, elem) {
		xmlhttp=null;
		if (window.XMLHttpRequest) {
			xmlhttp=new XMLHttpRequest();
		} else if (window.ActiveXObject) {
			xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		}
		if (xmlhttp!=null) {
			xmlhttp.onreadystatechange = function() {
				if (xmlhttp.readyState==4) {
					if (xmlhttp.status == 200) {
						var result = xmlhttp.responseText.toString();
						var resultObj = json_parse(result);
						if (elem == "c") {
							if (resultObj.success == "true") {
								document.location = "";
							} else {
								checkUserName;
								checkEmail;
								checkPassword;
							}
						} else if(elem == "l"){
							if (resultObj.success == "true") {
								document.location = "";
							} else {
								if (resultObj.error == "2"){
									document.getElementById("pwdMessage").innerHTML = "<div>" + resultObj.errorMessage + "</div>";
								} else if (resultObj.error == "3") {
									document.getElementById("userNameMessage").innerHTML = "<div>" + resultObj.errorMessage + "</div>";
								}
							}
						}else {
							if (resultObj.success == "true") {
								elem.innerHTML = "<img src='img/xv.png' >";
							} else {
								elem.innerHTML = "<div>" + resultObj.errorMessage + "</div>";
							}
						}
					} else {
						alert("连接失败，请重试！");
					}
				}
			}
			xmlhttp.open("POST",params,true);
			xmlhttp.send(null);
		} else {
			alert("无语了。。你的浏览器太低端了。赶快升级吧~");
		}
	}
}