package me.wanyinyue.service;

import java.util.List;

import javax.annotation.Resource;

import me.wanyinyue.hibernate.basedao.BaseDao;
import me.wanyinyue.model.User;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(value = "userManager")
@Scope(value = "singleton")
public class UserManager {

	private BaseDao<User> userDao;
	
	public void addUser(User u){
		userDao.save(u);
	}
	
	public User getUserById(int id) {
		return userDao.findUniqueBy("id", id);
	}
	
	public User getUser(String propertyName, Object value) throws Exception {
		return userDao.findUniqueBy(propertyName, value);
	}
	
	public User login(String userName, String password) throws Exception {
		List<User> list = userDao.find("from me.wanyinyue.model.User u where u.username="+userName+" and u.password = "+password);
		if(list!=null&&list.size()==1){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Resource(name = "userDao")
	public void setUserDao(BaseDao<User> userDao) {
		this.userDao = userDao;
	}

}
