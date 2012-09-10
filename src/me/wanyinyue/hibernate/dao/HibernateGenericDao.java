package me.wanyinyue.hibernate.dao;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.wanyinyue.utils.BeanUtils;
import me.wanyinyue.utils.Page;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.BooleanClause.Occur;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.wltea.analyzer.lucene.IKQueryParser;

/**
 * Hibernate Dao的泛型基类.
 * <p/>
 * 继承于Spring的<code>HibernateDaoSupport</code>,提供分页函数和若干便捷查询方法，并对返回值作了泛型类型转换.
 * 
 * @author springside
 * 
 * @see HibernateDaoSupport
 * @see HibernateEntityDao
 */
@SuppressWarnings("unchecked")
public class HibernateGenericDao extends HibernateDaoSupport {

	/**
	 * 根据ID获取对象. 实际调用Hibernate的session.load()方法返回实体或其proxy对象. 如果对象不存在，抛出异常.
	 */
	public <T> T get(Class<T> entityClass, Serializable id) {
		return (T) getHibernateTemplate().load(entityClass, id);
	}

	/**
	 * 获取全部对象.
	 */
	public <T> List<T> getAll(Class<T> entityClass) {
		return getHibernateTemplate().loadAll(entityClass);
	}

	/**
	 * 获取全部对象,带排序字段与升降序参数.
	 */
	public <T> List<T> getAll(Class<T> entityClass, String orderBy,
			boolean isAsc) {
		Assert.hasText(orderBy);
		if (isAsc)
			return getHibernateTemplate().findByCriteria(
					DetachedCriteria.forClass(entityClass).addOrder(
							Order.asc(orderBy)));
		else
			return getHibernateTemplate().findByCriteria(
					DetachedCriteria.forClass(entityClass).addOrder(
							Order.desc(orderBy)));
	}

	/**
	 * 保存对象.
	 */
	public void save(Object o) {
		getHibernateTemplate().saveOrUpdate(o);
	}

	/**
	 * 保存对象，主要用在更新多个相连的对象中
	 * 
	 * @throws Exception
	 */
	public void saveWithMerge(Object o) throws Exception {
		try {
			getHibernateTemplate()
					.saveOrUpdate(getHibernateTemplate().merge(o));
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 删除对象.
	 */
	public void remove(Object o) {
		getHibernateTemplate().delete(o);
	}

	/**
	 * 删除对象.
	 */
	public void removeWithMerge(Object o) {
		getHibernateTemplate().delete(getHibernateTemplate().merge(o));
	}

	/**
	 * 根据ID删除对象.
	 */
	public <T> void removeById(Class<T> entityClass, Serializable id) {
		remove(get(entityClass, id));
	}

	public void flush() {
		getHibernateTemplate().flush();
	}

	public void clear() {
		getHibernateTemplate().clear();
	}

	/**
	 * 创建Query对象.
	 * 对于需要first,max,fetchsize,cache,cacheRegion等诸多设置的函数,可以在返回Query后自行设置.
	 * 留意可以连续设置,如下：
	 * 
	 * <pre>
	 * dao.getQuery(hql).setMaxResult(100).setCacheable(true).list();
	 * </pre>
	 * 
	 * 调用方式如下：
	 * 
	 * <pre>
	 *        dao.createQuery(hql) 
	 *        dao.createQuery(hql,arg0); 
	 *        dao.createQuery(hql,arg0,arg1); 
	 *        dao.createQuery(hql,new Object[arg0,arg1,arg2])
	 * </pre>
	 * 
	 * @param values
	 *            可变参数.
	 */
	public Query createQuery(String hql, Object... values) {
		Assert.hasText(hql);
		Query query = getSession().createQuery(hql);
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		return query;
	}

	/**
	 * 创建Criteria对象.
	 * 
	 * @param criterions
	 *            可变的Restrictions条件列表,见{@link #createQuery(String,Object...)}
	 */
	public <T> Criteria createCriteria(Class<T> entityClass,
			Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(entityClass);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	/**
	 * 创建Criteria对象，带排序字段与升降序字段.
	 * 
	 * @see #createCriteria(Class,Criterion[])
	 */
	public <T> Criteria createCriteria(Class<T> entityClass, String orderBy,
			boolean isAsc, Criterion... criterions) {
		Assert.hasText(orderBy);

		Criteria criteria = createCriteria(entityClass, criterions);

		if (isAsc)
			criteria.addOrder(Order.asc(orderBy));
		else
			criteria.addOrder(Order.desc(orderBy));

		return criteria;
	}

	/**
	 * 根据hql查询,直接使用HibernateTemplate的find函数.
	 * 
	 * @param values
	 *            可变参数,见{@link #createQuery(String,Object...)}
	 */
	public List find(String hql, Object... values) {
		Assert.hasText(hql);
		return getHibernateTemplate().find(hql, values);
	}

	/**
	 * 根据属性名和属性值查询对象.
	 * 
	 * @return 符合条件的对象列表
	 */
	public <T> List<T> findBy(Class<T> entityClass, String propertyName,
			Object value) {
		Assert.hasText(propertyName);
		return createCriteria(entityClass, Restrictions.eq(propertyName, value))
				.list();
	}

	/**
	 * 根据属性名和属性值查询对象,带排序参数.
	 */
	public <T> List<T> findBy(Class<T> entityClass, String propertyName,
			Object value, String orderBy, boolean isAsc) {
		Assert.hasText(propertyName);
		Assert.hasText(orderBy);
		return createCriteria(entityClass, orderBy, isAsc,
				Restrictions.eq(propertyName, value)).list();
	}

	/**
	 * 根据属性名和属性值查询唯一对象.
	 * 
	 * @return 符合条件的唯一对象 or null if not found.
	 */
	public <T> T findUniqueBy(Class<T> entityClass, String propertyName,
			Object value) {
		Assert.hasText(propertyName);
		return (T) createCriteria(entityClass,
				Restrictions.eq(propertyName, value)).uniqueResult();
	}

	/**
	 * 分页查询函数，使用hql.
	 * 
	 * @param pageNo
	 *            页号,从1开始.
	 */
	public Page pagedQuery(String hql, int pageNo, int pageSize,
			Object... values) {
		Assert.hasText(hql);
		Assert.isTrue(pageNo >= 1, "pageNo should start from 1");
		// Count查询
		String countQueryString = "select count(*) "
				+ removeSelect(removeOrders(hql));
		List countlist = getHibernateTemplate().find(countQueryString, values);
		long totalCount = (Long) countlist.get(0);

		if (totalCount < 1)
			return new Page();
		// 实际查询返回分页对象
		int startIndex = Page.getStartOfPage(pageNo, pageSize);
		Query query = createQuery(hql, values);
		List list = query.setFirstResult(startIndex).setMaxResults(pageSize)
				.list();

		return new Page(startIndex, totalCount, pageSize, list);
	}

	/**
	 * 查询总记录数
	 * 
	 * @param hql
	 * @param values
	 * @return
	 */
	public long getTotalCount(String hql, Object... values) {
		Assert.hasText(hql);
		// Count查询
		String countQueryString = "select count(*) "
				+ removeSelect(removeOrders(hql));
		List countlist = getHibernateTemplate().find(countQueryString, values);
		long totalCount = (Long) countlist.get(0);
		return totalCount;
	}

	/**
	 * @author Scott.wanglei
	 * @since 2008-7-21
	 * @param hql
	 *            查询sql
	 * @param start
	 *            分页从哪一条数据开始
	 * @param pageSize
	 *            每一个页面的大小
	 * @param values
	 *            查询条件
	 * @return page对象
	 * @throws Exception
	 */
	public Page dataQuery(String hql, int start, int pageSize, Object... values)
			throws Exception {
		try {

			// Count查询
			String countQueryString = " select count (*) "
					+ removeSelect(removeOrders(hql));
			List countlist = getHibernateTemplate().find(countQueryString,
					values);
			long totalCount = (Long) countlist.get(0);

			if (totalCount < 1)
				return new Page();
			// 实际查询返回分页对象
			int startIndex = start;
			Query query = createQuery(hql, values);
			List list = query.setFirstResult(startIndex)
					.setMaxResults(pageSize).list();
			return new Page(startIndex, totalCount, pageSize, list);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public Page dataQuery(String hql, int realStart, int virtualstart, int pageSize,
			Object... values) throws Exception {
		try {

			// Count查询
			String countQueryString = " select count (*) "
					+ removeSelect(removeOrders(hql));
			List countlist = getHibernateTemplate().find(countQueryString,
					values);
			long totalCount = (Long) countlist.get(0);

			if (totalCount < 1)
				return new Page();
			// 实际查询返回分页对象
			int startIndex = realStart;
			Query query = createQuery(hql, values);
			List list = query.setFirstResult(startIndex)
					.setMaxResults(pageSize).list();
			return new Page(virtualstart, totalCount, pageSize, list);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * @author lilin
	 * @param hql
	 *            查询sql
	 * @param start
	 *            从哪一条数据开始
	 * @param pageSize
	 *            每一个页面的大小
	 * @param values
	 *            查询条件
	 * @return list对象
	 */
	public <T> List<T> listQuery(String hql, int start, int pageSize,
			Object... values) {
		// Count查询
		String countQueryString = " select count (*) "
				+ removeSelect(removeOrders(hql));
		List countlist = getHibernateTemplate().find(countQueryString, values);
		long totalCount = (Long) countlist.get(0);

		if (totalCount < 1)
			return new ArrayList();
		// 实际查询返回分页对象
		int startIndex = start;
		Query query = createQuery(hql, values);
		List list = query.setFirstResult(startIndex).setMaxResults(pageSize)
				.list();
		return list;
	}

	/**
	 * 分页查询函数，使用已设好查询条件与排序的<code>Criteria</code>.
	 * 
	 * @param start
	 *            起始索引号,从0开始.
	 * @return 含总记录数和当前页数据的Page对象.
	 */
	public Page pagedQueryFromStart(Criteria criteria, int start, int pageSize) {
		Assert.notNull(criteria);
		Assert.isTrue(start >= 0, "start should start from 1");
		CriteriaImpl impl = (CriteriaImpl) criteria;

		// 先把Projection和OrderBy条件取出来,清空两者来执行Count操作
		Projection projection = impl.getProjection();
		List<CriteriaImpl.OrderEntry> orderEntries;
		try {
			orderEntries = (List) BeanUtils.forceGetProperty(impl,
					"orderEntries");
			BeanUtils.forceSetProperty(impl, "orderEntries", new ArrayList());
		} catch (Exception e) {
			throw new InternalError(" Runtime Exception impossibility throw ");
		}

		/*
		 * 
		 * // 执行查询 int totalCount = (Integer) criteria.setProjection(
		 * Projections.rowCount()).uniqueResult();
		 */

		// 执行查询
		long totalCount = (Long) criteria.setProjection(Projections.rowCount())
				.uniqueResult();

		// 将之前的Projection和OrderBy条件重新设回去
		criteria.setProjection(projection);
		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}

		try {
			BeanUtils.forceSetProperty(impl, "orderEntries", orderEntries);
		} catch (Exception e) {
			throw new InternalError(" Runtime Exception impossibility throw ");
		}

		// 返回分页对象
		if (totalCount < 1)
			return new Page();

		List list = criteria.setFirstResult(start).setMaxResults(pageSize)
				.list();
		return new Page(start, totalCount, pageSize, list);
	}

	/**
	 * 分页查询函数，使用已设好查询条件与排序的<code>Criteria</code>.
	 * 
	 * @param pageNo
	 *            页号,从0开始.
	 * @return 含总记录数和当前页数据的Page对象.
	 */
	public Page pagedQuery(Criteria criteria, int pageNo, int pageSize) {
		Assert.notNull(criteria);
		Assert.isTrue(pageNo >= 1, "pageNo should start from 1");
		CriteriaImpl impl = (CriteriaImpl) criteria;

		// 先把Projection和OrderBy条件取出来,清空两者来执行Count操作
		Projection projection = impl.getProjection();
		List<CriteriaImpl.OrderEntry> orderEntries;
		try {
			orderEntries = (List) BeanUtils.forceGetProperty(impl,
					"orderEntries");
			BeanUtils.forceSetProperty(impl, "orderEntries", new ArrayList());
		} catch (Exception e) {
			throw new InternalError(" Runtime Exception impossibility throw ");
		}

		/*
		 * 
		 * // 执行查询 int totalCount = (Integer) criteria.setProjection(
		 * Projections.rowCount()).uniqueResult();
		 */

		// 执行查询
		long totalCount = (Long) criteria.setProjection(Projections.rowCount())
				.uniqueResult();

		// 将之前的Projection和OrderBy条件重新设回去
		criteria.setProjection(projection);
		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}

		try {
			BeanUtils.forceSetProperty(impl, "orderEntries", orderEntries);
		} catch (Exception e) {
			throw new InternalError(" Runtime Exception impossibility throw ");
		}

		// 返回分页对象
		if (totalCount < 1)
			return new Page();

		int startIndex = Page.getStartOfPage(pageNo, pageSize);
		if (startIndex >= 4985) {
			startIndex = 4985;
		}
		List list = criteria.setFirstResult(startIndex).setMaxResults(pageSize)
				.list();
		return new Page(startIndex, totalCount, pageSize, list);
	}

	/**
	 * 分页查询函数，根据entityClass和查询条件参数创建默认的<code>Criteria</code>.
	 * 
	 * @param pageNo
	 *            页号,从1开始.
	 * @return 含总记录数和当前页数据的Page对象.
	 */
	public Page pagedQuery(Class entityClass, int pageNo, int pageSize,
			Criterion... criterions) {
		Criteria criteria = createCriteria(entityClass, criterions);
		return pagedQuery(criteria, pageNo, pageSize);
	}

	/**
	 * 分页查询函数，根据entityClass和查询条件参数创建默认的<code>Criteria</code>.
	 * 
	 * @param start
	 *            起始索引号,从0开始.
	 * @return 含总记录数和当前页数据的Page对象.
	 */
	public Page pagedQueryFromStart(Class entityClass, int start, int pageSize,
			Criterion... criterions) {
		Criteria criteria = createCriteria(entityClass, criterions);
		return pagedQueryFromStart(criteria, start, pageSize);
	}

	/**
	 * 分页查询函数，根据entityClass和查询条件参数,排序参数创建默认的<code>Criteria</code>.
	 * 
	 * @param pageNo
	 *            页号,从1开始.
	 * @return 含总记录数和当前页数据的Page对象.
	 */
	public Page pagedQuery(Class entityClass, int pageNo, int pageSize,
			String orderBy, boolean isAsc, Criterion... criterions) {
		Criteria criteria = createCriteria(entityClass, orderBy, isAsc,
				criterions);
		return pagedQuery(criteria, pageNo, pageSize);
	}

	/**
	 * 根据关键词分页查询,筛选特定类型的对象
	 * 
	 * @param type
	 * @param pageNo
	 *            第几页
	 * @param pageSize
	 * @param keyword
	 * @param field
	 * @param fieldToSort
	 *            排序索引属性
	 * @param isDes是否降序
	 * @return page
	 * @throws IOException
	 */
	public Page pageQueryByKeywordByField(Class type, int pageNo, int pageSize,
			String keyword, String field, String fieldToSort, Boolean isDes)
			throws IOException {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		session.beginTransaction();
		FullTextSession ftSession = Search.getFullTextSession(session);
		org.apache.lucene.search.Query luceneQuery = null;
		luceneQuery = IKQueryParser.parse(field, keyword);
		Criteria criteria = ftSession.createCriteria(type);
		int totalCount = ftSession.createFullTextQuery(luceneQuery, type)
				.setCriteriaQuery(criteria).list().size();
		int startIndex = Page.getStartOfPage(pageNo, pageSize);

		// 根据fieldToSort排序,到时候可以换成默认排序规则
		org.apache.lucene.search.Sort sort = new Sort(new SortField(
				fieldToSort, SortField.DOUBLE, isDes));
		org.hibernate.Query qurey = ftSession.createFullTextQuery(luceneQuery,
				type).setCriteriaQuery(criteria).setSort(sort).setFirstResult(
				startIndex).setMaxResults(pageSize);
		List list = qurey.list();
		Page page = new Page(startIndex, totalCount, pageSize, list);
		return page;
	}

	/**
	 * 根据关键词分页查询，返回多种类型的结果集
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param keyword
	 * @param field
	 * @param fieldToSort
	 * @param isDes
	 * @return page
	 * @throws IOException
	 */
	public Page pageQueryByKeywordByField(final int pageNo, final int pageSize,
			final String keyword, final String field, final String fieldToSort,
			final Boolean isDes) throws IOException {
		return (Page) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				FullTextSession ftSession = Search.getFullTextSession(session);
				org.apache.lucene.search.Query luceneQuery = null;
				try {
					luceneQuery = IKQueryParser.parse(field, keyword);
				} catch (IOException e) {
					e.printStackTrace();
				}
				int totalCount = ftSession.createFullTextQuery(luceneQuery)
						.getResultSize();
				int startIndex = Page.getStartOfPage(pageNo, pageSize);
				org.apache.lucene.search.Sort sort = new Sort(new SortField(
						fieldToSort, SortField.DOUBLE, isDes));
				org.hibernate.Query qurey = ftSession.createFullTextQuery(
						luceneQuery).setSort(sort).setFirstResult(startIndex)
						.setMaxResults(pageSize);
				List list = qurey.list();
				Page page = new Page(startIndex, totalCount, pageSize, list);
				return page;
			}
		});
	}

	/**
	 * 回调实现，根据多个关键字在对应的多个属性上查询，筛选，分页
	 * 
	 * @param type
	 * @param pageNo
	 * @param pageSize
	 * @param keywords
	 * @param fields
	 * @param fieldToSort
	 * @param isDes
	 * @return page
	 * @throws IOException
	 * @throws ParseException
	 */
	public Page pageQueryByKeywordsByFields(final Class type, final int pageNo,
			final int pageSize, final String[] keywords, final String[] fields,
			final String fieldToSort, final Boolean isDes) throws IOException {
		return (Page) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				FullTextSession ftSession = Search.getFullTextSession(session);

				final QueryBuilder b = ftSession.getSearchFactory()
						.buildQueryBuilder().forEntity(type).get();
				org.apache.lucene.search.Query luceneQuery1 = null;
				luceneQuery1 = b.keyword().onField("checkStatus").matching(
						"checked").createQuery();

				org.apache.lucene.search.Query luceneQuery = null;
				Occur[] flags = new Occur[keywords.length];
				for (int i = 0; i < keywords.length; i++) {
					flags[i] = Occur.MUST;
				}
				try {
					luceneQuery = IKQueryParser.parseMultiField(fields,
							keywords, flags);
				} catch (IOException e) {
					e.printStackTrace();
				}

				org.hibernate.search.FullTextQuery fullTextQuery = null;
				Criteria criteria = ftSession.createCriteria(type);
				criteria.add(Restrictions.eq("checkStatus", "checked"));

				luceneQuery1 = luceneQuery1
						.combine(new org.apache.lucene.search.Query[] { luceneQuery });

				fullTextQuery = ftSession.createFullTextQuery(luceneQuery1,
						type).setCriteriaQuery(criteria);

				int totalCount = fullTextQuery.getResultSize();
				int startIndex = Page.getStartOfPage(pageNo, pageSize);

				// 根据fieldToSort排序,可以换成默认排序规则
				org.apache.lucene.search.Sort sort = new Sort(new SortField(
						fieldToSort, SortField.DOUBLE, isDes));
				org.hibernate.Query qurey = fullTextQuery.setSort(sort)
						.setFirstResult(startIndex).setMaxResults(pageSize);
				List list = qurey.list();
				Page page = new Page(startIndex, totalCount, pageSize, list);
				return page;
			}
		});
	}

	/**
	 * 回调实现,根据一个关键字在多个属性上查询同一种类型的对象集合，筛选，分页
	 * 
	 * @param type
	 * @param pageNo
	 * @param pageSize
	 * @param keyword
	 * @param fields
	 * @param fieldToSort
	 * @param isDes
	 * @return page
	 * @throws IOException
	 * @throws ParseException
	 */
	public Page pageQueryByKeywordByFields(final Class type, final int pageNo,
			final int pageSize, final String keyword, final String[] fields,
			final String fieldToSort, final Boolean isDes) throws IOException {
		return (Page) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				FullTextSession ftSession = Search.getFullTextSession(session);
				org.apache.lucene.search.Query luceneQuery = null;
				Occur[] flags = new Occur[fields.length];
				for (int i = 0; i < fields.length; i++) {
					flags[i] = Occur.SHOULD;
				}
				try {
					luceneQuery = IKQueryParser.parseMultiField(fields,
							keyword, flags);
				} catch (IOException e) {
					e.printStackTrace();
				}

				int totalCount = ftSession.createFullTextQuery(luceneQuery,
						type).getResultSize();
				int startIndex = Page.getStartOfPage(pageNo, pageSize);

				// 根据fieldToSort排序,到时候可以换成默认排序规则
				org.apache.lucene.search.Sort sort = new Sort(new SortField(
						fieldToSort, SortField.DOUBLE, isDes));
				org.hibernate.Query qurey = ftSession.createFullTextQuery(
						luceneQuery, type).setSort(sort).setFirstResult(
						startIndex).setMaxResults(pageSize);
				List list = qurey.list();
				Page page = new Page(startIndex, totalCount, pageSize, list);
				return page;
			}
		});
	}

	/**
	 * 回调实现,根据一个关键字在多个属性上查询同一种类型的对象集合，筛选，分页
	 * 
	 * @param type
	 * @param start
	 * @param pageSize
	 * @param keyword
	 * @param fields
	 * @param fieldToSort
	 * @param isDes
	 * @return page
	 * @throws IOException
	 * @throws ParseException
	 */
	public Page pageQueryByKeywordByFieldsFromStart(final Class type,
			final int start, final int pageSize, final String keyword,
			final String[] fields, final String fieldToSort, final Boolean isDes)
			throws IOException {
		return (Page) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				FullTextSession ftSession = Search.getFullTextSession(session);
				org.apache.lucene.search.Query luceneQuery = null;
				Occur[] flags = new Occur[fields.length];
				for (int i = 0; i < fields.length; i++) {
					flags[i] = Occur.SHOULD;
				}
				try {
					luceneQuery = IKQueryParser.parseMultiField(fields,
							keyword, flags);
				} catch (IOException e) {
					e.printStackTrace();
				}

				int totalCount = ftSession.createFullTextQuery(luceneQuery,
						type).getResultSize();

				// 根据fieldToSort排序,到时候可以换成默认排序规则
				org.apache.lucene.search.Sort sort = new Sort(new SortField(
						fieldToSort, SortField.DOUBLE, isDes));
				org.hibernate.Query qurey = ftSession.createFullTextQuery(
						luceneQuery, type).setSort(sort).setFirstResult(start)
						.setMaxResults(pageSize);
				List list = qurey.list();
				Page page = new Page(start, totalCount, pageSize, list);
				return page;
			}
		});
	}

	/**
	 * 回调实现,根据一个关键字在多个属性上查询同一种类型的对象集合，筛选，分页 这是搜索product的方法
	 * 
	 * @param type
	 * @param start
	 * @param pageSize
	 * @param keyword
	 * @param fields
	 * @param fieldToSort
	 * @param isDes
	 * @return page
	 * @throws IOException
	 * @throws ParseException
	 */
	public Page pageQueryByKeywordByFieldsFromStart(final Class type,
			final int start, final int pageSize, final String keyword,
			final String[] fields, final String fieldToSort,
			final boolean isDes, final boolean isProduct) throws IOException {
		return (Page) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				FullTextSession ftSession = Search.getFullTextSession(session);
				org.apache.lucene.search.Query luceneQuery = null;
				Occur[] flags = new Occur[fields.length];
				for (int i = 0; i < fields.length; i++) {
					flags[i] = Occur.SHOULD;
				}
				BooleanQuery bq = new BooleanQuery();
				try {
					luceneQuery = IKQueryParser.parseMultiField(fields,
							keyword, flags);
					bq.add(luceneQuery, Occur.MUST);
					if (isProduct) {
						bq.add(IKQueryParser.parseMultiField(
								new String[] { "type" }, "2",
								new Occur[] { Occur.MUST }), Occur.MUST);
					} else {
						bq.add(IKQueryParser.parseMultiField(
								new String[] { "type" }, "1",
								new Occur[] { Occur.MUST }), Occur.MUST);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				int totalCount = ftSession.createFullTextQuery(bq, type)
						.getResultSize();

				// 根据fieldToSort排序,到时候可以换成默认排序规则
				org.apache.lucene.search.Sort sort = new Sort(new SortField(
						fieldToSort, SortField.DOUBLE, isDes));
				org.hibernate.Query qurey = ftSession.createFullTextQuery(bq,
						type).setSort(sort).setFirstResult(start)
						.setMaxResults(pageSize);
				List list = qurey.list();
				Page page = new Page(start, totalCount, pageSize, list);
				return page;
			}
		});
	}

	/**
	 * 回调实现,根据一个关键字在多个属性上查询同一种类型的全部对象
	 * 
	 * @param type
	 * @param keyword
	 * @param fields
	 * @param fieldToSort
	 * @param isDes
	 * @return list
	 * @throws IOException
	 */
	public <T> List<T> listByKeywordByFields(final Class<T> type,
			final String keyword, final String[] fields,
			final String fieldToSort, final Boolean isDes) throws IOException {
		return (List<T>) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						FullTextSession ftSession = Search
								.getFullTextSession(session);
						org.apache.lucene.search.Query luceneQuery = null;
						Occur[] flags = new Occur[fields.length];
						for (int i = 0; i < fields.length; i++) {
							flags[i] = Occur.SHOULD;
						}
						try {
							luceneQuery = IKQueryParser.parseMultiField(fields,
									keyword, flags);
						} catch (IOException e) {
							e.printStackTrace();
						}
						org.apache.lucene.search.Sort sort = new Sort(
								new SortField(fieldToSort, SortField.DOUBLE,
										isDes));
						org.hibernate.Query qurey = ftSession
								.createFullTextQuery(luceneQuery, type)
								.setSort(sort);
						return qurey.list();
					}
				});
	}

	/**
	 * 回调实现,根据一个关键字在多个属性上查询全部类型的对象
	 * 
	 * @param <T>
	 * @param keyword
	 * @param fields
	 * @param fieldToSort
	 * @param isDes
	 * @return
	 * @throws IOException
	 */
	public <T> List<T> listByKeywordByFields(final String keyword,
			final String[] fields, final String fieldToSort, final Boolean isDes)
			throws IOException {
		return (List<T>) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						List list = null;
						FullTextSession ftSession = Search
								.getFullTextSession(session);
						org.apache.lucene.search.Query luceneQuery = null;
						Occur[] flags = new Occur[fields.length];
						for (int i = 0; i < fields.length; i++) {
							flags[i] = Occur.SHOULD;
						}
						try {
							luceneQuery = IKQueryParser.parseMultiField(fields,
									keyword, flags);
						} catch (IOException e) {
							e.printStackTrace();
						}
						org.apache.lucene.search.Sort sort = new Sort(
								new SortField(fieldToSort, SortField.DOUBLE,
										isDes));
						org.hibernate.Query qurey = ftSession
								.createFullTextQuery(luceneQuery).setSort(sort);

						list = qurey.list();
						/*
						 * // 高亮设置 SimpleHTMLFormatter formatter = new
						 * SimpleHTMLFormatter( "<b><font color=\"red\">",
						 * "</font></b>"); QueryScorer qs = new
						 * QueryScorer(luceneQuery); Highlighter highlighter =
						 * new Highlighter(formatter, qs);
						 * 
						 * 
						 * 
						 * for (int i = 0; i < list.size(); i++) { String
						 * findResult = ""; Object o = list.get(i); Analyzer
						 * analyzer = new IKAnalyzer();
						 * 
						 * if (o instanceof Case) { Case c = (Case) o; try {
						 * findResult = highlighter.getBestFragment( analyzer,
						 * "name", c.getName()); } catch
						 * (InvalidTokenOffsetsException e) {
						 * e.printStackTrace(); } catch (IOException e) {
						 * e.printStackTrace(); } // 重新封装 c.setName(findResult);
						 * } else if (o instanceof Company) { Company company =
						 * (Company) o; try { findResult =
						 * highlighter.getBestFragment( analyzer, "name",
						 * keyword); } catch (InvalidTokenOffsetsException e) {
						 * e.printStackTrace(); } catch (IOException e) {
						 * e.printStackTrace(); } // 重新封装
						 * company.setName(findResult); } else if (o instanceof
						 * Designer) { Designer designer = (Designer) o; try {
						 * findResult = highlighter.getBestFragment( analyzer,
						 * "name", keyword); } catch
						 * (InvalidTokenOffsetsException e) {
						 * e.printStackTrace(); } catch (IOException e) {
						 * e.printStackTrace(); } // 重新封装
						 * designer.setName(findResult); } }
						 */
						return list;
					}
				});
	}

	/**
	 * 回调实现,根据多个关键字在多个属性上查询全部对象
	 * 
	 * @param type
	 * @param keywords
	 * @param fields
	 * @param fieldToSort
	 * @param isDes
	 * @return list
	 * @throws IOException
	 */
	public <T> List<T> listByKeywordsByFields(final Class<T> type,
			final String[] keywords, final String[] fields,
			final String fieldToSort, final Boolean isDes) throws IOException {
		return (List<T>) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						FullTextSession ftSession = Search
								.getFullTextSession(session);
						org.apache.lucene.search.Query luceneQuery = null;
						Occur[] flags = new Occur[fields.length];
						for (int i = 0; i < fields.length; i++) {
							flags[i] = Occur.MUST;
						}
						try {
							luceneQuery = IKQueryParser.parseMultiField(fields,
									keywords, flags);
						} catch (IOException e) {
							e.printStackTrace();
						}
						org.apache.lucene.search.Sort sort = new Sort(
								new SortField(fieldToSort, SortField.DOUBLE,
										isDes));
						org.hibernate.Query qurey = ftSession
								.createFullTextQuery(luceneQuery, type)
								.setSort(sort);
						return qurey.list();
					}
				});
	}

	/**
	 * 根据索引查询出所有记录？
	 * 
	 * @param type
	 * @param pageNo
	 * @param pageSize
	 * @param fieldToSort
	 * @param isDes
	 * @return
	 * @throws IOException
	 */
	public Page pageQueryAll(final Class type, final int pageNo,
			final int pageSize, final String fieldToSort, final Boolean isDes)
			throws IOException {
		return (Page) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				FullTextSession ftSession = Search.getFullTextSession(session);
				org.apache.lucene.search.Query luceneQuery = null;

				luceneQuery = IKQueryParser.parse("");
				int totalCount = ftSession.createFullTextQuery(luceneQuery,
						type).getResultSize();
				int startIndex = Page.getStartOfPage(pageNo, pageSize);

				// 根据fieldToSort排序,到时候可以换成默认排序规则
				org.apache.lucene.search.Sort sort = new Sort(new SortField(
						fieldToSort, SortField.DOUBLE, isDes));
				org.hibernate.Query qurey = ftSession.createFullTextQuery(
						luceneQuery, type).setSort(sort).setFirstResult(
						startIndex).setMaxResults(pageSize);
				List list = qurey.list();
				Page page = new Page(startIndex, totalCount, pageSize, list);
				return page;
			}
		});
	}

	/**
	 * 判断对象某些属性的值在数据库中是否唯一.
	 * 
	 * @param uniquePropertyNames
	 *            在POJO里不能重复的属性列表,以逗号分割 如"name,loginid,password"
	 */
	public <T> boolean isUnique(Class<T> entityClass, Object entity,
			String uniquePropertyNames) {
		Assert.hasText(uniquePropertyNames);
		Criteria criteria = createCriteria(entityClass).setProjection(
				Projections.rowCount());
		String[] nameList = uniquePropertyNames.split(",");
		try {
			// 循环加入唯一列
			for (String name : nameList) {
				criteria.add(Restrictions.eq(name, PropertyUtils.getProperty(
						entity, name)));
			}

			// 以下代码为了如果是update的情况,排除entity自身.

			String idName = getIdName(entityClass);

			// 取得entity的主键值
			Serializable id = getId(entityClass, entity);

			// 如果id!=null,说明对象已存在,该操作为update,加入排除自身的判断
			if (id != null)
				criteria.add(Restrictions.not(Restrictions.eq(idName, id)));
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
		}
		return (Integer) criteria.uniqueResult() == 0;
	}

	/**
	 * 取得对象的主键值,辅助函数.
	 */
	public Serializable getId(Class entityClass, Object entity)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		Assert.notNull(entity);
		Assert.notNull(entityClass);
		return (Serializable) PropertyUtils.getProperty(entity,
				getIdName(entityClass));
	}

	/**
	 * 取得对象的主键名,辅助函数.
	 */
	public String getIdName(Class clazz) {
		Assert.notNull(clazz);
		ClassMetadata meta = getSessionFactory().getClassMetadata(clazz);
		Assert.notNull(meta, "Class " + clazz
				+ " not define in hibernate session factory.");
		String idName = meta.getIdentifierPropertyName();
		Assert.hasText(idName, clazz.getSimpleName()
				+ " has no identifier property define.");
		return idName;
	}

	/**
	 * 去除hql的select 子句，未考虑union的情况,用于pagedQuery.
	 * 
	 * @see #pagedQuery(String,int,int,Object[])
	 */
	private static String removeSelect(String hql) {
		Assert.hasText(hql);
		int beginPos = hql.toLowerCase().indexOf("from");
		Assert.isTrue(beginPos != -1, " hql : " + hql
				+ " must has a keyword 'from'");
		return hql.substring(beginPos);
	}

	/**
	 * 去除hql的orderby 子句，用于pagedQuery.
	 * 
	 * @see #pagedQuery(String,int,int,Object[])
	 */
	private static String removeOrders(String hql) {
		Assert.hasText(hql);
		Pattern p = Pattern.compile("order//s*by[//w|//W|//s|//S]*",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}
}