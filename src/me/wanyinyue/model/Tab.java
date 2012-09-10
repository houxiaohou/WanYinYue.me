package me.wanyinyue.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.wltea.analyzer.lucene.IKAnalyzer;

@Entity(name = "w_tab")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Indexed(index = "tab")
public class Tab extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6718693683156805573L;

	private int id;
	private String name;
	private String singer;
	private int checkedTimes;
	private User uploadUser;
	private int featured;
	private int totalPicNum;
	private List<TabPic> tabPic = new ArrayList<TabPic>();
	private List<Comment> comment = new ArrayList<Comment>();

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(impl = IKAnalyzer.class), name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(impl = IKAnalyzer.class), name = "singer")
	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public int getCheckedTimes() {
		return checkedTimes;
	}

	public void setCheckedTimes(int checkedTimes) {
		this.checkedTimes = checkedTimes;
	}

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public User getUploadUser() {
		return uploadUser;
	}

	public void setUploadUser(User uploadUser) {
		this.uploadUser = uploadUser;
	}

	public int getFeatured() {
		return featured;
	}

	public void setFeatured(int featured) {
		this.featured = featured;
	}

	public int getTotalPicNum() {
		return totalPicNum;
	}

	public void setTotalPicNum(int totalPicNum) {
		this.totalPicNum = totalPicNum;
	}
	
	@OneToMany(mappedBy = "tab", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<TabPic> getTabPic() {
		return tabPic;
	}

	public void setTabPic(List<TabPic> tabPic) {
		this.tabPic = tabPic;
	}
	@OneToMany(mappedBy = "tab", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<Comment> getComment() {
		return comment;
	}

	public void setComment(List<Comment> comment) {
		this.comment = comment;
	}

}
