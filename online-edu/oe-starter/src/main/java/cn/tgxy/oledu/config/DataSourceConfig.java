package cn.tgxy.oledu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.pool.DruidDataSource;

import jakarta.annotation.Resource;

@Configuration
public class DataSourceConfig {
    @Resource
    private DruidProperties druidProperties;


	@Primary
	@Bean("dataSource")
    DruidDataSource singleDatasource() {
		
        DruidDataSource dataSource = new DruidDataSource();
        druidProperties.config(dataSource);
        return dataSource;
    }
	
    
}