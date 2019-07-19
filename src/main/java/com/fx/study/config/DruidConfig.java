package com.fx.study.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;

@Configuration
public class DruidConfig {
	@Value("${spring.datasource.driver-class-name}")  
    private String driverClassName;  
  
    @Value("${spring.datasource.initialSize}")  
    private int initialSize;  
  
    @Value("${spring.datasource.minIdle}")  
    private int minIdle;  
  
    @Value("${spring.datasource.maxActive}")  
    private int maxActive;  
  
    @Value("${spring.datasource.maxWait}")  
    private int maxWait;  
  
    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")  
    private int timeBetweenEvictionRunsMillis;  
  
    @Value("${spring.datasource.minEvictableIdleTimeMillis}")  
    private int minEvictableIdleTimeMillis;  
  
    @Value("${spring.datasource.validationQuery}")  
    private String validationQuery;  
  
    @Value("${spring.datasource.testWhileIdle}")  
    private boolean testWhileIdle;  
  
    @Value("${spring.datasource.testOnBorrow}")  
    private boolean testOnBorrow;  
  
    @Value("${spring.datasource.testOnReturn}")  
    private boolean testOnReturn;  
  
    @Value("${spring.datasource.filters}")  
    private String filters;  
  
    @Value("${spring.datasource.logSlowSql}")  
    private String logSlowSql;  
	
	//数据源
    @Bean  
    public DataSource druidDataSource() throws Exception {  
        DruidDataSource datasource = new DruidDataSource(); 
        datasource.setUrl(ReadProperties.url);  
        datasource.setUsername(ReadProperties.username);  
        datasource.setPassword(ReadProperties.password);  
        datasource.setDriverClassName(driverClassName);  
        datasource.setInitialSize(initialSize);  
        datasource.setMinIdle(minIdle);  
        datasource.setMaxActive(maxActive);  
        datasource.setMaxWait(maxWait);  
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);  
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);  
        datasource.setValidationQuery(validationQuery);  
        datasource.setTestWhileIdle(testWhileIdle);  
        datasource.setTestOnBorrow(testOnBorrow);  
        datasource.setTestOnReturn(testOnReturn);
        datasource.setFilters(filters); 
        return datasource;  
    }  
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
    public ServletRegistrationBean druidStatViewServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        Map<String, String> initParams = new HashMap<>();
        initParams.put("loginUsername", "admin");
        initParams.put("loginPassword", "123456");
        servletRegistrationBean.setInitParameters(initParams);
        return servletRegistrationBean;
    }
}
