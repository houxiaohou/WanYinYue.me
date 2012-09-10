package me.wanyinyue.action;

import java.util.List;

import javax.annotation.Resource;

import me.wanyinyue.model.Tab;
import me.wanyinyue.model.TabPic;
import me.wanyinyue.service.TabManager;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(value = "viewTabAction")
@Scope(value = "prototype")
public class ViewTabAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1704941146755288960L;

	private TabManager tabManager;

	private int id;
	private Tab tab;
	private List<TabPic> tabPics;

	public String viewTab() {
		if (id != 0) {
			tab = tabManager.getTabById(id);
			if (tab != null) {
				tabPics = tab.getTabPic();
				tab.setCheckedTimes(tab.getCheckedTimes() + 1);
				tabManager.addTab(tab);
				if (tabPics.size() > 0)
					return SUCCESS;
				else
					return ERROR;
			}
			return ERROR;
		}
		return ERROR;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}

	@Resource(name = "tabManager")
	public void setTabManager(TabManager tabManager) {
		this.tabManager = tabManager;
	}

	public List<TabPic> getTabPics() {
		return tabPics;
	}

	public void setTabPics(List<TabPic> tabPics) {
		this.tabPics = tabPics;
	}
	
	

}
