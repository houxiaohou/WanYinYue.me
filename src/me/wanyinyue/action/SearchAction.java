package me.wanyinyue.action;

import java.util.List;

import javax.annotation.Resource;

import me.wanyinyue.model.Record;
import me.wanyinyue.model.Tab;
import me.wanyinyue.service.RecordManager;
import me.wanyinyue.service.TabManager;
import me.wanyinyue.utils.Page;
import me.wanyinyue.utils.SystemConstant;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(value = "searchAction")
@Scope(value = "prototype")
public class SearchAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4778434054357711463L;

	private String keyword;
	private String pageNum;
	private int pageNumber;

	private String tabName;
	private String tabSinger;
	private String email;

	private TabManager tabManager;
	private RecordManager recordManager;

	private int totalNum;
	private int totalPage;
	private Page tabPage;
	private List<Tab> tabs;

	@SuppressWarnings("unchecked")
	public String search() throws Exception {
		if (keyword != null && keyword != "") {
			String[] fields = { "name", "singer" };
			List<Tab> tab = tabManager.listQueryByKeywordsByFields(keyword,
					fields);
			totalNum = tab.size();
			totalPage = (int) Math.ceil(totalNum / SystemConstant.PAGE_SIZE);

			Record record = new Record();
			record.setKeyword(keyword);
			record.setSearchType(0);
			if (totalNum == 0) {
				record.setIsSuccess(0);
				recordManager.addRecord(record);
				return ERROR;
			} else {
				if (pageNum == null) {
					tabPage = tabManager.pageQueryByKeywordByFields(1,
							SystemConstant.PAGE_SIZE, keyword, fields, "id",
							true);
					pageNumber = 1;
				} else {
					pageNumber = Integer.parseInt(pageNum);
					tabPage = tabManager.pageQueryByKeywordByFields(pageNumber,
							SystemConstant.PAGE_SIZE, keyword, fields, "id",
							true);
				}
				tabs = (List<Tab>) tabPage.getResult();
				record.setIsSuccess(1);
				recordManager.addRecord(record);
				return SUCCESS;
			}
		}
		return ERROR;
	}

	public String searchForm() {
		Record record = new Record();
		record.setEmail(email);
		record.setTabName(tabName);
		record.setTabSinger(tabSinger);
		record.setSearchType(1);
		recordManager.addRecord(record);
		return SUCCESS;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	@Resource(name = "tabManager")
	public void setTabManager(TabManager tabManager) {
		this.tabManager = tabManager;
	}

	@Resource(name = "recordManager")
	public void setRecordManager(RecordManager recordManager) {
		this.recordManager = recordManager;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public Page getTabPage() {
		return tabPage;
	}

	public void setTabPage(Page tabPage) {
		this.tabPage = tabPage;
	}

	public List<Tab> getTabs() {
		return tabs;
	}

	public void setTabs(List<Tab> tabs) {
		this.tabs = tabs;
	}

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public String getTabSinger() {
		return tabSinger;
	}

	public void setTabSinger(String tabSinger) {
		this.tabSinger = tabSinger;
	}

	public TabManager getTabManager() {
		return tabManager;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

}
