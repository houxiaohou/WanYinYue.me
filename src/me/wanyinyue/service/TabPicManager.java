package me.wanyinyue.service;

import javax.annotation.Resource;

import me.wanyinyue.hibernate.basedao.BaseDao;
import me.wanyinyue.model.TabPic;

import org.springframework.stereotype.Component;

@Component(value = "tabPicManager")
public class TabPicManager {

	private BaseDao<TabPic> tabPicDao;
	
	public void addTabPic(TabPic tabPic) {
		tabPicDao.save(tabPic);
	}
	
	@Resource(name = "tabPicDao")
	public void setTabPicDao(BaseDao<TabPic> tabPicDao) {
		this.tabPicDao = tabPicDao;
	}

}
