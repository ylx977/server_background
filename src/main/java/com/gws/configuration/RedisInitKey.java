package com.gws.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.Entity;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;

@Configuration
public class RedisInitKey implements CommandLineRunner {
	@Autowired
	@Resource(name = "initSeqId")
	private RedisValConfig initSeqId;

	@Override
	public void run(String... arg0) throws Exception {
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
		Set<BeanDefinition> entitySet = scanner.findCandidateComponents("com.gws.entity");
		initSeqId.setPrefix("CACHE_ID_GEN_");

		List<String> beanNames = new ArrayList<>();
		for (BeanDefinition entityBean : entitySet) {
			String entityName = entityBean.getBeanClassName();
			if (!StringUtils.isEmpty(entityName)) {
				String[] className = entityName.split("\\.");
				beanNames.add(className[className.length - 1]);
			}
		}
		initSeqId.setKey(toString(beanNames.toArray()));
	}

	public static String toString(Object[] a) {
		if (a == null)
			return "";

		int iMax = a.length - 1;
		if (iMax == -1)
			return "";

		StringBuilder b = new StringBuilder();
		b.append("");
		for (int i = 0;; i++) {
			b.append(String.valueOf(a[i]));
			if (i == iMax)
				return b.append("").toString();
			b.append(", ");
		}
	}
}
