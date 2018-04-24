
package com.gws.utils.query.core;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;


/**
 * 基类的数据访问接口(继承了CrudRepository,PagingAndSortingRepository,JpaSpecificationExecutor的特性)
 *
 * @version 
 * @author wangdong  2016年4月16日 上午10:33:56
 * 
 */
@NoRepositoryBean
public abstract interface  GwsBaseRepository<T, ID extends Serializable> 
									extends JpaRepository<T,ID>,
        JpaSpecificationExecutor<T> {
	/**
	 * 
	 * 使用QBL进行查询列表
	 * 
	 * @author wangdong 2016年4月16日
	 * @param query
	 * @return
	 */
	public abstract List<T> findAll(BaseQuery query);
	
	/**
	 * 
	 * 封装分页查询
	 * 
	 * @author wangdong 2016年4月18日
	 * @param query
	 * @param pageable
	 * @return
	 */
	public abstract Page<T> findAll(BaseQuery query, Pageable pageable) ;

	/**
	 *
	 * 封装排序查询
	 *
	 * @author wangdong 2016年4月18日
	 * @param query
	 * @param sort
	 * @return
	 */
	public abstract List<T> findAll(BaseQuery query, Sort sort) ;


	public abstract long count(BaseQuery query);
	/**
	 *
	 * 使用QBL定位记录
	 *
	 * @author wangdong 2016年4月16日
	 * @param query
	 * @return
	 */
	public abstract T findOne(BaseQuery query);

	/**
	 *
	 * 更新方法
	 *
	 * @author wangdong 2016年7月16日
	 * @param t
	 * @param updateFileds
	 * @param where
	 * @return
	 */
    public abstract int  update(T t, BaseQuery where, String... updateFileds);

    /**
     *
     * 根据唯一主键更新方法
     *
     * @author wangdong 2016年7月16日
     * @param t
     * @param id
     * @param updateFileds
     * @return
     */
    public abstract int updateById(T t, ID id, String... updateFileds);


    EntityManager getEntityManager();
}
