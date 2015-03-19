package com.greencloud.website;

import javax.annotation.PreDestroy;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableAutoConfiguration(exclude=DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(TomcatPoolDataSourceProperties.class)
@MapperScan(basePackages="com.greencloud.website.db.mybatis.mapper")
@ComponentScan
//@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
public class Application {
	
	@Autowired
	private TomcatPoolDataSourceProperties tomcatPoolDataSourceProperties;
	
	private org.apache.tomcat.jdbc.pool.DataSource pool;	
	
	@Bean(destroyMethod="close")
	public DataSource dataSource() {
		
		TomcatPoolDataSourceProperties config = tomcatPoolDataSourceProperties;
		this.pool = new org.apache.tomcat.jdbc.pool.DataSource();
		this.pool.setDriverClassName(config.getDriverClassName());
		this.pool.setUrl(config.getUrl());
		this.pool.setUsername(config.getUsername());
		this.pool.setPassword(config.getPassword());
		this.pool.setInitialSize(config.getInitialSize());
		this.pool.setMaxActive(config.getMaxActive());
		this.pool.setMaxIdle(config.getMaxIdle());
		this.pool.setMinIdle(config.getMinIdle());
		this.pool.setTestOnBorrow(config.isTestOnBorrow());
		this.pool.setTestOnReturn(config.isTestOnReturn());
		this.pool.setValidationQuery(config.getValidationQuery());
		System.out.println(this.pool.getDriverClassName());
		return this.pool;
	}
	
	@PreDestroy
	public void close() {
		if (this.pool != null) {
			this.pool.close();
		}
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
		
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());
		sqlSessionFactoryBean.setTypeAliasesPackage("com.greencloud.website.model");
		
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sqlSessionFactoryBean
			.setMapperLocations(resolver.getResources("classpath:com/greencloud/website/db/mybatis/mapper/*.xml"));
		System.out.println("abc");
		return sqlSessionFactoryBean.getObject();
	}
	
	
	@Bean
	public SqlSessionTemplate sqlSessionTemplate() throws Exception {
		return new SqlSessionTemplate(sqlSessionFactoryBean());
	}
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}
	
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(new Object[] { Application.class }, args);
	}
}
