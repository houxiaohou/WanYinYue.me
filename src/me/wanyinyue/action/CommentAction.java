package me.wanyinyue.action;

import javax.annotation.Resource;

import me.wanyinyue.model.Comment;
import me.wanyinyue.model.User;
import me.wanyinyue.service.CommentManager;
import me.wanyinyue.service.TabManager;
import me.wanyinyue.service.UserManager;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(value = "commentAction")
@Scope(value = "prototype")
public class CommentAction extends BaseAction {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7575640177596303518L;
	private UserManager userManager;
	private CommentManager commentManager;
	private TabManager tabManager;

	private String content;
	private int tabId;
	private int replyId;

	private boolean success;
	private int isReply;

	public String comment() {
		if (content != "" && content != null) {
			User u = (User) getSession().get("USER");
			User user = userManager.getUserById(u.getId());
			if (user == null) {
				return LOGIN;
			}
			Comment comment = new Comment();
			comment.setContent(content);
			comment.setCommentUser(user);
			comment.setTab(tabManager.getTabById(tabId));
			if (replyId != 0) {
				comment.setIsReply(1);
				comment.setToUser(commentManager.getCommentById(replyId)
						.getCommentUser());
				isReply  = 1;
			} else {
				comment.setIsReply(0);
				comment.setToUser(null);
				isReply = 0;
			}

			commentManager.addComment(comment);
			
			success = true;

		}
		success = false;
		return SUCCESS;
	}

	@Resource(name = "userManager")
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	@Resource(name = "commentManager")
	public void setCommentManager(CommentManager commentManager) {
		this.commentManager = commentManager;
	}

	@Resource(name = "tabManager")
	public void setTabManager(TabManager tabManager) {
		this.tabManager = tabManager;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getTabId() {
		return tabId;
	}

	public void setTabId(int tabId) {
		this.tabId = tabId;
	}

	public int getReplyId() {
		return replyId;
	}

	public void setReplyId(int replyId) {
		this.replyId = replyId;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getIsReply() {
		return isReply;
	}

	public void setIsReply(int isReply) {
		this.isReply = isReply;
	}

}
