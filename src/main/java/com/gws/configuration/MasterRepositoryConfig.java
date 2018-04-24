package com.gws.configuration;

import com.gws.utils.GlobalConstant;
import com.gws.utils.jpa.BaseSimpleJpaRepositoryEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;


@Configuration
@EnableJpaRepositories(entityManagerFactoryRef="entityManagerFactoryPrimary",
						basePackages= { "com.gws.repositories.master" },
						repositoryBaseClass=BaseSimpleJpaRepositoryEx.class)
public class MasterRepositoryConfig {
	@Autowired
    private JpaProperties jpaProperties;

    @Autowired @Qualifier("mastersds")
    private DataSource masterDS;
    
	@Value("${db.showsql}")
	private Boolean showSql=false;

    @Bean(name = "entityManagerPrimary")
    @Primary
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryPrimary(builder).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactoryPrimary")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryPrimary (EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(masterDS)
                .properties(getVendorProperties(masterDS))
                .packages("com.gws.entity") 
                .persistenceUnit("primaryPersistenceUnit")
                .build();
    }

    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }

}
