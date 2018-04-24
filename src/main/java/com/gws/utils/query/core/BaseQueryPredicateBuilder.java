
package com.gws.utils.query.core;

import com.gws.utils.query.annotation.QBindAttrField;
import com.gws.utils.query.annotation.QBindEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.criteria.*;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * query转换builder类
 *
 * @version 
 * @author wangdong  2016年4月16日 下午3:54:41
 * 
 */
public class BaseQueryPredicateBuilder {
    private static Logger log = LogManager.getLogger(BaseQueryPredicateBuilder.class);

	public static <T> Predicate getPredicate2(Root<T> root, CriteriaBuilder cb, BaseQuery query){
		return getPredicate(root,cb,null,query);
	}
	
	public static <T> Predicate getPredicate(Root<T> root, CriteriaBuilder cb,
							CriteriaQuery<T> cquery, BaseQuery query){
		
        List<Predicate> predicatesAnd = new ArrayList<>();
		try {
			Class<?> entityClass = queryEntity(query);
			if (entityClass == null) {
				//是否返回NULL，待研究
				return null;
			}
			BeanInfo beanInfo = Introspector.getBeanInfo(query.getClass());
	        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
	        for (PropertyDescriptor pd : pds) {
	        	Method readMethod = pd.getReadMethod();	        	
				if ((pd.getName().indexOf("Page") == 0)
						|| (pd.getName().indexOf("Sort") == 0)) {
					continue;
				}		
	        	if (!pd.getName().equals("class")) {
					Object obj = readMethod.invoke(query);
					if(obj!=null){
						QBindAttrField fieldProp = (QBindAttrField) getBindFieldName(query, pd.getName());
						String bindAttrName = fieldProp.fieldName();
						if (bindAttrName == null) {
							//查询字段名称默认等于属性名称
							bindAttrName=pd.getName();
						}
						Path<?> from =root;
						Expression expression = from.get(bindAttrName);
						switch (fieldProp.where()) {
						case equal:
							predicatesAnd.add(cb.equal(expression, obj)) ;
							break;
						case greaterThanOrEqualTo:
							predicatesAnd.add(cb.greaterThanOrEqualTo(expression, (Comparable)obj));
							break;
						case lessThanOrEqualTo:
							predicatesAnd.add(cb.lessThanOrEqualTo(expression, (Comparable)obj));
							break;	
						case like:
							predicatesAnd.add(cb.like(expression, "%" + (Comparable)obj + "%"));
							break;
						case greaterThan:
							predicatesAnd.add(cb.greaterThan(expression, (Comparable) obj));
							break;
						case lessThan:
							predicatesAnd.add(cb.lessThan(expression, (Comparable) obj));
							break;
						case notEqual:
							predicatesAnd.add(cb.notEqual(expression, (Comparable) obj));
							break;
						case in:
							if (pd.getPropertyType().getName().indexOf("List") > 0) {
								List<?> value = (List<?>) obj;
								if (value.size() == 0) {
									// 防止生成LIST时，没有传入值，而查询条件会做全查处理，此处做特殊处理返回空条件
									((List<?>) obj).add(null);
								}
								if (value.size() > 20) {
									Set<Object> set = new HashSet<Object>(
											value.size());
									// 如果in超过20个要去重处理
									set.addAll(value);
									value = new ArrayList<Object>(set);
								}
								predicatesAnd.add(expression.in(value)) ;
							}
							else{
								List vList = new ArrayList<>();
								vList.add(obj);
								predicatesAnd.add(expression.in(vList)) ;
							}
							//特殊处理
							break;	
						default://默认等于equal
							break;
						}
					}
	        	}


	        }
		} catch (Exception e) {
			log.error(e);
		}  	
		
		//组合条件
		if (predicatesAnd.isEmpty()) {
			return cb.isTrue(cb.literal(true));
		}

		if (predicatesAnd.size() == 1) {
			return predicatesAnd.iterator().next();
		}
		return cb.and(predicatesAnd.toArray(new Predicate[predicatesAnd.size()]));
	}
	
	
	/**
	 * 
	 * 获取查询实体类名称
	 * 
	 * @author wangdong 2016年4月16日
	 * @param query
	 * @return
	 */
	public static Class<?> queryEntity(BaseQuery query) {
		Annotation anno = query.getClass().getAnnotation(QBindEntity.class);
		if (anno != null)
			return ((QBindEntity) anno).entityClass();
		return null;
	}
	
	/**
	 * 
	 * 获取绑定字段属性值
	 * 
	 * @author wangdong 2016年4月16日
	 * @param PropertyName
	 * @return
	 */
	public static Annotation getBindFieldName(BaseQuery query, String PropertyName){
		try {
			Field field = query.getClass().getDeclaredField(PropertyName);
			Annotation anno = field.getAnnotation(QBindAttrField.class);
			if (anno != null){
				return ((QBindAttrField) anno);
			}
		} catch (SecurityException e) {
			 log.error("[BaseQueryPredicateBuilder.getBindAttrName SecurityException:]"
								+ e.getMessage());
		} catch (NoSuchFieldException e) {
			 log.error("[BaseQueryPredicateBuilder.getBindAttrName NoSuchFieldException:]"
								+ e.getMessage());
		}
		return null;
	}

	
}
