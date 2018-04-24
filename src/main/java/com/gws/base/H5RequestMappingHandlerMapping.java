
package com.gws.base;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.gws.base.annotation.GwsRequestMaping;
import com.gws.base.annotation.H5Controller;


/**
 * h5controller请求映射处理
 *
 * @version 
 * @author wangdong  2017年4月7日 下午3:30:12
 * 
 */
public class H5RequestMappingHandlerMapping extends RequestMappingHandlerMapping {

	@Override
	protected boolean isHandler(Class<?> beanType) {
		return ((AnnotationUtils.findAnnotation(beanType, H5Controller.class) != null) || (
				AnnotationUtils.findAnnotation(beanType, GwsRequestMaping.class) != null));
	}

	private RequestMappingInfo createRequestMappingInfo(AnnotatedElement element) {
		GwsRequestMaping requestMapping = AnnotatedElementUtils
				.findMergedAnnotation(element, GwsRequestMaping.class);
		RequestCondition<?> condition = (element instanceof Class<?> ?
				getCustomTypeCondition((Class<?>) element) :
				getCustomMethodCondition((Method) element));
		if (requestMapping == null) {
			return null;
		}
		return RequestMappingInfo.paths(resolveEmbeddedValuesInPatterns(requestMapping.value()))
				.methods(requestMapping.method()).params(requestMapping.params()).headers(requestMapping.headers())
				.consumes(requestMapping.consumes()).produces(requestMapping.produces())
				.mappingName(requestMapping.name()).customCondition(condition).build();
	}

	@Override
	protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
		RequestMappingInfo info = createRequestMappingInfo(method);
		if (info != null) {
			RequestMappingInfo typeInfo = createRequestMappingInfo(handlerType);
			if (typeInfo != null) {
                info = typeInfo.combine(info);
			}
		}
		return info;
	}

}
