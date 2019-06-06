package com.yuanwang.common.startup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.CannotReadScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.jdbc.datasource.init.UncategorizedScriptException;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;


/**项目启动类，启动后进行操作
 * @author crj
 *
 */
@Component
public class StartUpRunner implements ApplicationRunner{
	
	private static final Logger LOGGER=LoggerFactory.getLogger(StartUpRunner.class);
	
	@Autowired
	DataSource dataSource;
	
	@Value("${spring.datasource.name}")
	private String schema;
	
	@Value("${spring.datasource.url}")
	private String url;
	
	@Value("${spring.datasource.username}")
	private String username;
	
	@Value("${spring.datasource.password}")
	private String password;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
		Connection connection=null;
		try{
			LOGGER.info("升级");
			LOGGER.info(url+"=="+username+"=="+password);
			connection=DriverManager.getConnection(url, username,password);
			String fixPath=ResourceUtils.getURL("classpath:").getPath();
			FileSystemResource rc = new FileSystemResource(fixPath+"db/update/update.sql");
			EncodedResource er = new EncodedResource(rc,"utf-8");
			ScriptUtils.executeSqlScript(connection, er);
		}catch(MySQLSyntaxErrorException e){
			LOGGER.info("初始化数据库");
			connection=DriverManager.getConnection(url.replace(schema, "mysql"), username,password);
			String sql="create database "+schema+" character set utf8 collate utf8_general_ci";
			LOGGER.info(sql);
			PreparedStatement p=connection.prepareStatement(sql);
			p.execute(sql);
			runsqlBySpringUtils("db/init/init.sql");
		}catch(UncategorizedScriptException e){
			LOGGER.info("",e);
		}catch(CannotReadScriptException e){
			LOGGER.info("",e);
		}catch(Exception e){
			LOGGER.info("",e);
		}finally {
			connection.close();
		}
	}

	
	public void runsqlBySpringUtils(String sqlPath) throws SQLException{
		Connection connection=null;
		try {
			String fixPath=ResourceUtils.getURL("classpath:").getPath();
			connection=dataSource.getConnection();
			LOGGER.info(connection.getSchema());
			FileSystemResource rc = new FileSystemResource(fixPath+sqlPath);
			EncodedResource er = new EncodedResource(rc,"utf-8");
			ScriptUtils.executeSqlScript(connection, er);
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			connection.close();
		}
	}
	
}
