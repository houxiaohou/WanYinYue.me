package me.wanyinyue.service;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import me.wanyinyue.hibernate.basedao.BaseDao;
import me.wanyinyue.model.Tab;
import me.wanyinyue.utils.Page;

import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(value = "tabManager")
@Scope(value = "singleton")
public class TabManager {

	private BaseDao<Tab> tabDao;

	public void addTab(Tab tab) {
		tabDao.save(tab);
	}

	public void deleteTabById(int id) {
		tabDao.removeById(id);
	}

	public Tab getTabById(int id) {
		return tabDao.findUniqueBy("id", id);
	}

	public Query createQuery(String hql, Object... values) {
		return tabDao.createQuery(hql, values);
	}

	// 根据条件以及起始索引，pgeSize查找
	public Page pagedQueryFromStart(int startIndex, int pageSize,
			Criterion... criterions) {
		return tabDao.pagedQueryFromStart(Tab.class, startIndex, pageSize,
				criterions);
	}

	// 根据页号，pageSize查找
	public Page pagedQuery(int pageNo, int pageSize, Criterion[] criterions) {
		return tabDao.pagedQuery(Tab.class, pageNo, pageSize, criterions);
	}

	// 根据start，hql语句查询,返回page
	public Page dataQuery(String hql, int start, int pageSize, Object... values)
			throws Exception {
		try {
			return tabDao.dataQuery(hql, start, pageSize, values);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// 根据start，hql语句查询,返回page
	public Page dataQuery(String hql, int realStart, int virtualStart,
			int pageSize, Object... values) throws Exception {
		try {
			return tabDao.dataQuery(hql, realStart, virtualStart, pageSize,
					values);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// 根据start，hql语句查询,返回list
	public List<Tab> listQuery(String hql, int start, int pageSize,
			Object... values) {
		return tabDao.listQuery(hql, start, pageSize, values);
	}

	public List<Tab> find(String hql, Object... values) {
		return tabDao.find(hql, values);
	}

	public List<Tab> listQueryByKeywordsByFields(String keyword, String[] fields) throws IOException {
		return tabDao.listByKeywordByFields(Tab.class, keyword, fields, "id",
				true);
	}

	public Page pageQueryByKeywordByFields(int pageNo, int pageSize,
			String keyword, String[] fields, String fieldToSort, Boolean isDes)
			throws Exception {
		try {
			return tabDao.pageQueryByKeywordByFields(Tab.class, pageNo,
					pageSize, keyword, fields, fieldToSort, isDes);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public Page pageQueryByKeywordByFieldsFromStart(int start, int pageSize,
			String keyword, String[] fields, String fieldToSort, Boolean isDes,
			Boolean isProduct) throws Exception {
		try {
			return tabDao.pageQueryByKeywordByFieldsFromStart(Tab.class, start,
					pageSize, keyword, fields, fieldToSort, isDes, isProduct);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Resource(name = "tabDao")
	public void setTabDao(BaseDao<Tab> tabDao) {
		this.tabDao = tabDao;
	}

}
