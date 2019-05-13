package com.yuanwang.common.mybitas;

import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import com.github.pagehelper.PageHelper;

//@Configuration
//开启事务支持
//@EnableTransactionManagement
public class MybitasConfig implements TransactionManagementConfigurer{
	@Resource
	DataSource dataSource;
	
	private static Logger LOGGER=LoggerFactory.getLogger(MySqlSessionFactoryBean.class);
	
	@Bean(name="sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory() throws Exception{
		LOGGER.info("进入mybitas配置");
		MySqlSessionFactoryBean sqlSessionFactoryBean = new MySqlSessionFactoryBean();
//		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
//		//设置默认枚举转换器
//		configuration.setDefaultEnumTypeHandler(EnumOrdinalTypeHandler.class);
//		//全局映射器启用缓存 
//		configuration.setCacheEnabled(true);
//		//关联对象即时延迟加载
//		configuration.setLazyLoadingEnabled(true);
//		//设置关联对象加载的形态，此处为按需加载字段(加载字段由SQL指 定)，不会加载关联表的所有字段，以提高性能
//		configuration.setAggressiveLazyLoading(true);
//		//对于未知的SQL查询，允许返回不同的结果集以达到通用的效果
//		configuration.setMultipleResultSetsEnabled(true);
//		//允许使用列标签代替列名
//		configuration.setUseColumnLabel(true);
//		//对于批量更新操作缓存SQL以提高性能 BATCH,SIMPLE
//		configuration.setDefaultExecutorType(ExecutorType.SIMPLE);
//		//数据库超过25000秒仍未响应则超时
//		configuration.setDefaultStatementTimeout(25000);
//		
//		
//		PageHelper pageHelper=new PageHelper();
//		Properties properties=new Properties();
//		properties.setProperty("dialect", "mysql");
//		properties.setProperty("params", "count=countSql");
//		pageHelper.setProperties(properties);
//		sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageHelper});
//		sqlSessionFactoryBean.setConfiguration((org.apache.ibatis.session.Configuration) configuration);
		sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
		org.springframework.core.io.Resource[] mapperResources = new PathMatchingResourcePatternResolver()
				.getResources("classpath:com/yuanwang/*/dao/*.xml");
		sqlSessionFactoryBean.setMapperLocations(mapperResources);
		sqlSessionFactoryBean.setTypeAliasesPackage("com.yuanwang");
		return sqlSessionFactoryBean.getObject();
	}
	
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory){
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	
	@Bean
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}

}
