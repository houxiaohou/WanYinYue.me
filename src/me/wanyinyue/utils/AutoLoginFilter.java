package me.wanyinyue.utils;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.wanyinyue.service.UserManager;

import org.springframework.stereotype.Component;


@Component(value = "autoLoginFilter")
public class AutoLoginFilter implements Filter {

	private UserManager userManager;
	// 保存cookie时的cookieName
	private final static String cookieDomainName = "me.wanyinyue";

	//忽略静态文件的请求，只过滤action，还不清楚在配置文件中如何设置
	private final static String excludeUrls = "css,js,jsp,html,jpg,gif";

	public void destroy() {
	}

	/**
	 * 每次访问或者登录时会调用该方法，效率很低，最好是第一次访问时调用此方法，看看其他网站是怎么做的
	 */
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		try {
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			HttpServletResponse response = (HttpServletResponse) servletResponse;
			if (request.getSession(true).getAttribute("USER") == null) {

				String uri = request.getRequestURI();
				String[] excludes = excludeUrls.split(",");
				if (!StringUtils.uriEndsWithExcludes(uri, excludes)) {
					Cookie[] cookies = request.getCookies();
					if (cookies != null) {
						for (Cookie coo : cookies) {
							if (cookieDomainName.equals(coo.getName())) {
								CookieUtils.readCookieAndLogin(request,
										response, filterChain);
								break;
							}
						}
					}
				}
			}
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public UserManager getUserManager() {
		return userManager;
	}

	@Resource(name = "userManager")
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

}
