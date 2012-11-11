package me.wanyinyue.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.wanyinyue.model.User;
import me.wanyinyue.service.UserManager;
import me.wanyinyue.utils.CookieUtils;
import me.wanyinyue.utils.MD5;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(value = "authorizeAction")
@Scope(value = "prototype")
public class AuthorizeAction extends BaseAction implements ServletRequestAware,
		ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6137737171799157994L;

	private UserManager userManager;

	private String op; // operation from op=u,e,p,c,l
	private String userName;
	private String email;
	private String password;

	private HttpServletResponse response;

	// the call back date
	private String success;

	/**
	 * 0-no error, 1-already exists, 2-user name too short 3-user name too long,
	 * 4-email not correct format, 5-pwd too short
	 */
	private int error;
	private String errorMessage;
	
	public String login() {
		return SUCCESS;
	}

	public String signup() {
		return SUCCESS;
	}

	public String User() throws Exception {
		String regex = "([a-zA-Z0-9]*[-_\\.]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][a-z]{2,3}([\\.][a-z]{2,3})?";
		if (op.equals("u")) {
			User u = userManager.getUser("userName", userName);
			if (u == null) {
				if (userName.getBytes().length < 3) {
					error = 2;
					success = "false";
					errorMessage = "用户名至少3个英文或2个汉语字符";
				} else if (userName.getBytes().length > 20) {
					error = 3;
					success = "false";
					errorMessage = "用户名最长20个英文或10个汉语字符";
				} else {
					error = 0;
					success = "true";
					errorMessage = "";
				}
			} else {
				error = 1;
				success = "false";
				errorMessage = "用户名已存在";
			}
			return SUCCESS;
		} else if (op.equals("e")) {
			if (email.matches(regex)) {
				User u = userManager.getUser("email", email);
				if (u == null) {
					error = 0;
					success = "true";
					errorMessage = "";
				} else {
					error = 1;
					success = "false";
					errorMessage = "邮箱已经被注册";
				}
			} else {
				error = 4;
				success = "false";
				errorMessage = "邮箱格式不正确";
			}
			return SUCCESS;
		} else if (op.equals("p")) {
			if (password == null || password.length() < 6) {
				error = 5;
				success = "false";
				errorMessage = "密码长度至少为6位";
			} else if (password.length() > 20) {
				error = 6;
				success = "false";
				errorMessage = "密码长度最多为20位";
			} else {
				error = 0;
				success = "true";
				errorMessage = "";
			}
			return SUCCESS;
		} else if (op.equals("c")) {
			if (userManager.getUser("userName", userName) != null) {
				error = 1;
				success = "false";
				errorMessage = "用户名已存在";
				return SUCCESS;
			}
			if (!email.matches(regex)) {
				error = 4;
				success = "false";
				errorMessage = "邮箱格式不正确";
				return SUCCESS;
			}
			if (userManager.getUser("email", email) != null) {
				error = 1;
				success = "false";
				errorMessage = "邮箱已经被注册";
				return SUCCESS;
			}
			if (password == null || password.length() < 6) {
				error = 5;
				success = "false";
				errorMessage = "密码长度至少为6位";
				return SUCCESS;
			}
			if (password.length() > 20) {
				error = 6;
				success = "false";
				errorMessage = "密码长度最多为20位";
				return SUCCESS;
			}
			User u1 = userManager.getUser("userName", userName);
			User u2 = userManager.getUser("email", email);
			if (u1 == null && u2 == null & password.length() > 5
					&& password.length() < 20 && email.matches(regex)) {
				User user = new User();
				user.setEmail(email);
				user.setUserName(userName);
				user.setPassword(MD5.getMD5(password.getBytes()));
				userManager.addUser(user);

				getSession().put("USER", user);
				CookieUtils.saveCookie(user, response);
				
				success = "true";
				error = 0;
				errorMessage = "";
				return SUCCESS;
			}
		} else if (op.equals("l")) {
			User u;
			if (userName.matches(regex))
				u = userManager.getUser("email", userName);
			else
				u = userManager.getUser("userName", userName);
			if (u != null && u.getId() > 0) {
				if (u.getPassword().equals(MD5.getMD5(password.getBytes()))) {
					getSession().put("USER", u);
					CookieUtils.saveCookie(u, response);
					success = "true";
					error = 0;
					errorMessage = "";
				} else {
					success = "false";
					error = 2;
					errorMessage = "密码错误！";
				}
			} else {
				success = "false";
				error = 3;
				errorMessage = "用户不存在！";
			}
		}
		return SUCCESS;
	}
	
	public String logout() {
		try {
			getSession().remove("USER");
			CookieUtils.clearCookie(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	@Resource(name = "userManager")
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setServletRequest(HttpServletRequest arg0) {

	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

}
