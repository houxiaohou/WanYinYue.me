package me.wanyinyue.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name = "w_user")
public class User extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7868402886268064631L;

	private int id;
	private String userName;
	private String email;
	private String password;
	private List<Tab> tabs = new ArrayList<Tab>();
	private List<Comment> comment = new ArrayList<Comment>();
	private List<Comment> commentReply = new ArrayList<Comment>();

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	@OneToMany(mappedBy = "uploadUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<Tab> getTabs() {
		return tabs;
	}

	public void setTabs(List<Tab> tabs) {
		this.tabs = tabs;
	}

	@OneToMany(mappedBy = "commentUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<Comment> getComment() {
		return comment;
	}

	public void setComment(List<Comment> comment) {
		this.comment = comment;
	}

	@OneToMany(mappedBy = "toUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<Comment> getCommentReply() {
		return commentReply;
	}

	public void setCommentReply(List<Comment> commentReply) {
		this.commentReply = commentReply;
	}

}
