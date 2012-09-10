package me.wanyinyue.service;

import javax.annotation.Resource;

import me.wanyinyue.hibernate.basedao.BaseDao;
import me.wanyinyue.model.Comment;

import org.springframework.stereotype.Component;

@Component(value = "commentManager")
public class CommentManager {

	private BaseDao<Comment> commentDao;

	public void addComment(Comment comment) {
		commentDao.save(comment);
	}

	public void delComment(int id) {
		commentDao.removeById(id);
	}

	public Comment getCommentById(int id) {
		return commentDao.findUniqueBy("id", id);
	}

	@Resource(name = "commentDao")
	public void setCommentDao(BaseDao<Comment> commentDao) {
		this.commentDao = commentDao;
	}

}
