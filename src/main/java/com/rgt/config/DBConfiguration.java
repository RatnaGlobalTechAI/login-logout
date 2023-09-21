package com.rgt.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.rgt.utils.DBEncryptDecrypt;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


@Configuration
@EnableJpaRepositories(basePackages = "com.rgt.repository" , entityManagerFactoryRef = "entityManager" , transactionManagerRef ="transactionManager" )
public class DBConfiguration {
	
private static final Logger logger = LoggerFactory.getLogger(DBConfiguration.class);
	
	@Autowired
	private Environment env;

    @Primary
    @Bean(name = "entityManager")
    public LocalContainerEntityManagerFactoryBean entityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.rgt.entity");
        em.setPersistenceUnitName("rgtPersistence");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "none");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Primary
    @Bean(name = "dataSource")
    public DataSource dataSource() {
        DataSource dataSource = null;
        try {

        	
        	String url = env.getProperty("jdbc.url");
        	String username = env.getProperty("jdbc.username");	
        	String password = env.getProperty("jdbc.password");

    

            if (StringUtils.isNotBlank(password)) {
                DBEncryptDecrypt decryptObj = new DBEncryptDecrypt();
                password = decryptObj.decrypt(password);
            }
            if (StringUtils.isNotBlank(password) && StringUtils.isNotBlank(username) &&
            		StringUtils.isNotBlank(url)) {
                String dbMaxPoolZizeConfigVal = System.getProperty("dbmaxpoolsize");
                String dbMinPoolZizeConfigVal = System.getProperty("dbminpoolsize");
                HikariConfig jdbcConfig = new HikariConfig();
                int dbMaxPoolSize = 50;
                int dbMinPoolSize = 25;
                if (StringUtils.isNotBlank(dbMaxPoolZizeConfigVal)) {
                    dbMaxPoolSize = Integer.parseInt(dbMaxPoolZizeConfigVal);
                }
                if (StringUtils.isNotBlank(dbMinPoolZizeConfigVal)) {
                    dbMinPoolSize = Integer.parseInt(dbMinPoolZizeConfigVal);
                }
                jdbcConfig.setMaximumPoolSize(dbMaxPoolSize);
                jdbcConfig.setMinimumIdle(dbMinPoolSize);
                jdbcConfig.setPoolName("PSJPGMyJioDBPool");
                jdbcConfig.setJdbcUrl(url);
                jdbcConfig.setUsername(username);
                jdbcConfig.setPassword(password);
                dataSource = new HikariDataSource(jdbcConfig);
            }
        } catch (Exception exe) {
            logger.error("Exception ===>>>>{}", exe.getMessage());
        }
        return dataSource;
    }
    
   

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManager().getObject());
        return transactionManager;
    }


}
