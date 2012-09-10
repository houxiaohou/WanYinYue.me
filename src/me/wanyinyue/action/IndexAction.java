package me.wanyinyue.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import me.wanyinyue.model.Tab;
import me.wanyinyue.service.TabManager;
import me.wanyinyue.utils.Page;
import me.wanyinyue.utils.SystemConstant;

@Component(value = "indexAction")
@Scope(value = "prototype")
public class IndexAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5028658381289159071L;

	private TabManager tabManager;

	private Page tabPage;

	private long totalNum;
	private int start = 0;
	private int pageSize = SystemConstant.PAGE_SIZE;

	private String pageNum;
	private int pageNumber;
	private List<Tab> tabs;
	private int totalPage;

	@SuppressWarnings("unchecked")
	public String indexDate() throws Exception {
		String totalNumHql = "select count(*) from me.wanyinyue.model.Tab t where t.featured=1";
		totalNum = (Long) tabManager.createQuery(totalNumHql).list().get(0);
		totalPage = (int) Math.ceil((float) totalNum / 9);
		String tabHql = "from me.wanyinyue.model.Tab t where t.featured =1 order by t.id DESC";
		if (pageNum == null) {
			tabPage = tabManager.dataQuery(tabHql, start, pageSize);
			pageNumber = 1;
		} else {
			pageNumber = Integer.parseInt(pageNum);
			tabPage = tabManager.dataQuery(tabHql, (pageNumber - 1) * 9,
					pageSize);

		}
		tabs = (List<Tab>) tabPage.getResult();
		return SUCCESS;
	}

	@Resource(name = "tabManager")
	public void setTabManager(TabManager tabManager) {
		this.tabManager = tabManager;
	}

	public long getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(long totalNum) {
		this.totalNum = totalNum;
	}

	public Page getTabPage() {
		return tabPage;
	}

	public void setTabPage(Page tabPage) {
		this.tabPage = tabPage;
	}

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public List<Tab> getTabs() {
		return tabs;
	}

	public void setTabs(List<Tab> tabs) {
		this.tabs = tabs;
	}

	public long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

}
