package me.wanyinyue.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name = "w_comment")
public class Comment extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1349820717251386350L;
	private int id;
	private int isReply;
	private String content;
	private Tab tab;
	private User commentUser;
	private User toUser;

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIsReply() {
		return isReply;
	}

	public void setIsReply(int isReply) {
		this.isReply = isReply;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public User getCommentUser() {
		return commentUser;
	}

	public void setCommentUser(User commentUser) {
		this.commentUser = commentUser;
	}

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public User getToUser() {
		return toUser;
	}

	public void setToUser(User toUser) {
		this.toUser = toUser;
	}

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}

}
