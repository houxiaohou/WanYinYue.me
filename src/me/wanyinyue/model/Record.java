package me.wanyinyue.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "w_record")
public class Record extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4864398205717821424L;
	
	private String id;
	private String email;
	private int isSuccess;
	private String keyword;
	private int searchType;
	private int searchedTimes;
	private String tabName;
	private String tabSinger;
	
	@Id
	@GeneratedValue
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(int isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getSearchType() {
		return searchType;
	}

	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}

	public int getSearchedTimes() {
		return searchedTimes;
	}

	public void setSearchedTimes(int searchedTimes) {
		this.searchedTimes = searchedTimes;
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

}
