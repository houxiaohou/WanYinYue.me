package me.wanyinyue.action;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import me.wanyinyue.hibernate.basedao.BaseDao;
import me.wanyinyue.model.Tab;

import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.search.Search;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(value = "buildIndexAction")
@Scope(value = "prototype")
public class BuildIndexAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1281595106255382885L;

	private BaseDao<Tab> tabDao;

	private int index;

	private static int BATCH_SIZE = 100;

	@SuppressWarnings("unchecked")
	public String buildIndex() {
		Session session = tabDao.getHedao().getSessionFactory()
				.getCurrentSession();
		session.beginTransaction();
		org.hibernate.search.FullTextSession ftSession = Search
				.getFullTextSession(session);
		ftSession.setFlushMode(FlushMode.MANUAL);
		ftSession.setCacheMode(CacheMode.IGNORE);
		org.hibernate.Query query = ftSession.createQuery(
				"from me.wanyinyue.model.Tab").setFetchSize(BATCH_SIZE);
		List results = query.list();
		index = 0;
		for (Iterator it = results.iterator(); it.hasNext();) {
			index++;
			ftSession.index(it.next());
			if (index % BATCH_SIZE == 0) {
				ftSession.flushToIndexes();
			}
		}
		ftSession.flushToIndexes();
		ftSession.getTransaction().commit();
		return SUCCESS;
	}

	@Resource(name = "tabDao")
	public void setTabDao(BaseDao<Tab> tabDao) {
		this.tabDao = tabDao;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
