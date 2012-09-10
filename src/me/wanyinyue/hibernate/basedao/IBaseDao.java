package me.wanyinyue.hibernate.basedao;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import me.wanyinyue.hibernate.dao.HibernateGenericDao;
import me.wanyinyue.utils.Page;

import org.apache.lucene.queryParser.ParseException;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;

/**
 * @author
 * 
 *         提供hibernate dao的所有操作,
 *         实现类由spring注入HibernateEntityDao和HibernateEntityExtendDao来实现
 *         最大限度的解耦hibernate持久层的操作
 */
public interface IBaseDao<T> {

	/**
	 * 根据ID获取对象.
	 * 
	 * @see HibernateGenericDao#getId(Class,Object)
	 */
	public T get(Serializable id);

	/**
	 * 获取全部对象
	 * 
	 * @see HibernateGenericDao#getAll(Class)
	 */
	public List<T> getAll();

	/**
	 * 获取全部对象,带排序参数.
	 * 
	 * @see HibernateGenericDao#getAll(Class,String,boolean)
	 */
	public List<T> getAll(String orderBy, boolean isAsc);

	/**
	 * 根据ID移除对象.
	 * 
	 * @see HibernateGenericDao#removeById(Class,Serializable)
	 */
	public void removeById(Serializable id);

	/**
	 * 取得Entity的Criteria.
	 * 
	 * @see HibernateGenericDao#createCriteria(Class,Criterion[])
	 */
	public Criteria createCriteria(Criterion... criterions);

	/**
	 * 取得Entity的Criteria,带排序参数.
	 * 
	 * @see HibernateGenericDao#createCriteria(Class,String,boolean,Criterion[])
	 */
	public Criteria createCriteria(String orderBy, boolean isAsc,
			Criterion... criterions);

	/**
	 * 根据属性名和属性值查询对象.
	 * 
	 * @return 符合条件的对象列表
	 * @see HibernateGenericDao#findBy(Class,String,Object)
	 */
	public List<T> findBy(String propertyName, Object value);

	/**
	 * 根据属性名和属性值查询对象,带排序参数.
	 * 
	 * @return 符合条件的对象列表
	 * @see HibernateGenericDao#findBy(Class,String,Object,String,boolean)
	 */
	public List<T> findBy(String propertyName, Object value, String orderBy,
			boolean isAsc);

	/**
	 * 根据属性名和属性值查询单个对象.
	 * 
	 * @return 符合条件的唯一对象 or null
	 * @see HibernateGenericDao#findUniqueBy(Class,String,Object)
	 */
	public T findUniqueBy(String propertyName, Object value);

	/**
	 * 判断对象某些属性的值在数据库中唯一.
	 * 
	 * @param uniquePropertyNames
	 *            在POJO里不能重复的属性列表,以逗号分割 如"name,loginid,password"
	 * @see HibernateGenericDao#isUnique(Class,Object,String)
	 */
	public boolean isUnique(Object entity, String uniquePropertyNames);

	/**
	 * 消除与 Hibernate Session 的关联
	 * 
	 * @param entity
	 */
	public void evit(Object entity);

	/*******************************************************************************************/


	/**
	 * 与数据库相关的校验,比如判断名字在数据库里有没有重复, 在保存时被调用,在子类重载.
	 * 
	 * @see #save(Object)
	 */
	public void onValid(T entity);

	/*******************************************************************************************/

	/**
	 * 根据ID获取对象. 实际调用Hibernate的session.load()方法返回实体或其proxy对象. 如果对象不存在，抛出异常.
	 */
	public T get(Class<T> entityClass, Serializable id);

	/**
	 * 获取全部对象.
	 */
	public List<T> getAll(Class<T> entityClass);

	/**
	 * 获取全部对象,带排序字段与升降序参数.
	 */
	public List<T> getAll(Class<T> entityClass, String orderBy, boolean isAsc);

	/**
	 * 保存对象.
	 */
	public void save(Object o);
	
	/**
	 * 保存对象.
	 * @throws Exception 
	 */
	public void saveWithMerge(Object o) throws Exception;

	/**
	 * 删除对象.
	 */
	public void remove(Object o);
	
	/**
	 * 删除对象.
	 */
	public void removeWithMerge(Object o);

	public void flush();

	public void clear();

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
	public Query createQuery(String hql, Object... values);

	/**
	 * 创建Criteria对象.
	 * 
	 * @param criterions
	 *            可变的Restrictions条件列表,见{@link #createQuery(String,Object...)}
	 */
	public Criteria createCriteria(Class<T> entityClass,
			Criterion... criterions);

	/**
	 * 创建Criteria对象，带排序字段与升降序字段.
	 * 
	 * @see #createCriteria(Class,Criterion[])
	 */
	public Criteria createCriteria(Class<T> entityClass, String orderBy,
			boolean isAsc, Criterion... criterions);

	/**
	 * 根据hql查询,直接使用HibernateTemplate的find函数.
	 * 
	 * @param values
	 *            可变参数,见{@link #createQuery(String,Object...)}
	 */
	@SuppressWarnings("unchecked")
	public List find(String hql, Object... values);

	/**
	 * 根据属性名和属性值查询对象.
	 * 
	 * @return 符合条件的对象列表
	 */
	public List<T> findBy(Class<T> entityClass, String propertyName,
			Object value);

	/**
	 * 根据属性名和属性值查询对象,带排序参数.
	 */
	public List<T> findBy(Class<T> entityClass, String propertyName,
			Object value, String orderBy, boolean isAsc);
	
	public List<T> listQuery(String hql, int start, int pageSize, Object... values);

	/**
	 * 根据属性名和属性值查询唯一对象.
	 * 
	 * @return 符合条件的唯一对象 or null if not found.
	 */
	public T findUniqueBy(Class<T> entityClass, String propertyName,
			Object value);

	/**
	 * 分页查询函数，使用hql.
	 * 
	 * @param pageNo
	 *            页号,从1开始.
	 */
	public Page pagedQuery(String hql, int pageNo, int pageSize,
			Object... values);

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
	public Page dataQuery(String hql, int start, int pageSize, Object... values) throws Exception;

	
	public Page dataQuery(String hql, int reaStart, int virtualStart, int pageSize, Object... values) throws Exception;
	
	/**
	 * 分页查询函数，使用已设好查询条件与排序的<code>Criteria</code>.
	 * 
	 * @param pageNo
	 *            页号,从1开始.
	 * @return 含总记录数和当前页数据的Page对象.
	 */
	public Page pagedQuery(Criteria criteria, int pageNo, int pageSize);

	/**
	 * 分页查询函数，根据entityClass和查询条件参数创建默认的<code>Criteria</code>.
	 * 
	 * @param pageNo
	 *            页号,从1开始.
	 * @return 含总记录数和当前页数据的Page对象.
	 */
	@SuppressWarnings("unchecked")
	public Page pagedQuery(Class entityClass, int pageNo, int pageSize,
			Criterion... criterions);

	/**
	 * 分页查询函数，根据entityClass和查询条件参数,排序参数创建默认的<code>Criteria</code>.
	 * 
	 * @param pageNo
	 *            页号,从1开始.
	 * @return 含总记录数和当前页数据的Page对象.
	 */
	@SuppressWarnings("unchecked")
	public Page pagedQuery(Class entityClass, int pageNo, int pageSize,
			String orderBy, boolean isAsc, Criterion... criterions);

	/**
	 * 判断对象某些属性的值在数据库中是否唯一.
	 * 
	 * @param uniquePropertyNames
	 *            在POJO里不能重复的属性列表,以逗号分割 如"name,loginid,password"
	 */
	public boolean isUnique(Class<T> entityClass, Object entity,
			String uniquePropertyNames);

	/**
	 * 取得对象的主键值,辅助函数.
	 */
	@SuppressWarnings("unchecked")
	public Serializable getId(Class entityClass, Object entity)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException;

	/**
	 * 取得对象的主键名,辅助函数.
	 */
	@SuppressWarnings("unchecked")
	public String getIdName(Class clazz);
	
	
	/**
	 * 根据关键词分页查询,筛选对应类型的对象集合
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
	public Page pageQueryByKeywordByField(Class<T> type, int pageNo,
			int pageSize, String keyword, String field, String fieldToSort,
			Boolean isDes) throws IOException;

	/**
	 * 根据关键词分页查询，返回多种类型的结果集
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param keyword
	 * @param field
	 * @param fieldToSort
	 *            排序索引属性
	 * @param isDes是否降序
	 * @return Page
	 * @throws IOException
	 */
	public Page pageQueryByKeywordByField(int pageNo, int pageSize,
			String keyword, String field, String fieldToSort, Boolean isDes)
			throws IOException;

	/**
	 * 根据多个关键字，多个属性查询，筛选
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
	public Page pageQueryByKeywordsByFields(Class<T> type, int pageNo,
			int pageSize, String[] keywords, String[] fields,
			String fieldToSort, Boolean isDes) throws IOException;

	/**
	 * 回调实现,根据一个关键字在多个属性上查询，筛选，返回全部结果
	 * 
	 * @param type
	 * @param keyword
	 * @param fields
	 * @param fieldToSort
	 * @param isDes
	 * @return
	 * @throws IOException
	 */
	public List<T> listByKeywordByFields(Class<T> type, String keyword,
			String[] fields, String fieldToSort, Boolean isDes)
			throws IOException;

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
	public List<T> listByKeywordByFields(final String keyword,
			final String[] fields, final String fieldToSort, final Boolean isDes)
			throws IOException;

	/**
	 * 回调实现,根据多个关键字在多个属性上查询，筛选，返回全部结果
	 * 
	 * @param type
	 * @param keywords
	 * @param fields
	 * @param fieldToSort
	 * @param isDes
	 * @return list
	 * @throws IOException
	 */
	public List<T> listByKeywordsByFields(Class<T> type, String[] keywords,
			String[] fields, String fieldToSort, Boolean isDes)
			throws IOException;

	/**
	 * 回调实现,根据一个关键字在多个属性上查询，筛选，分页
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
	public Page pageQueryByKeywordByFields(final Class<T> type,
			final int pageNo, final int pageSize, final String keyword,
			final String[] fields, final String fieldToSort, final Boolean isDes)
			throws IOException;
	
	public Page pageQueryByKeywordByFieldsFromStart(final Class<T> type,
			final int pageNo, final int pageSize, final String keyword,
			final String[] fields, final String fieldToSort, final Boolean isDes)
			throws IOException;
	
	public Page pageQueryByKeywordByFieldsFromStart(final Class<T> type,
			final int pageNo, final int pageSize, final String keyword,
			final String[] fields, final String fieldToSort, final Boolean isDes,final Boolean isProduct)
			throws IOException;
}