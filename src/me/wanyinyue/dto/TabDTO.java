package me.wanyinyue.dto;

public class TabDTO {

	private int id;
	private String name;
	private String singer;
	private String date;
	private String uploadUserDisplay;
	private String userPage;
	private int checkedTimes;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUploadUserDisplay() {
		return uploadUserDisplay;
	}

	public void setUploadUserDisplay(String uploadUserDisplay) {
		this.uploadUserDisplay = uploadUserDisplay;
	}

	public String getUserPage() {
		return userPage;
	}

	public void setUserPage(String userPage) {
		this.userPage = userPage;
	}

	public int getCheckedTimes() {
		return checkedTimes;
	}

	public void setCheckedTimes(int checkedTimes) {
		this.checkedTimes = checkedTimes;
	}

}
