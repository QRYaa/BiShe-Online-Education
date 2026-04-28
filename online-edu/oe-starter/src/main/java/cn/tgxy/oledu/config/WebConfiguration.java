package cn.tgxy.oledu.config;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import cn.tgxy.oledu.interceptor.FileSecurityInterceptor;
import cn.tgxy.oledu.interceptor.SecurityInterceptor;

/**
 * 
 * @author chrisdeng 2021-08-13
 */
@Configuration("webConfig")
@Primary
public class WebConfiguration implements WebMvcConfigurer {

	@Autowired
	private SecurityInterceptor securityInterceptor;
	
	@Autowired
	private FileSecurityInterceptor fileSecurityInterceptor;

	@Bean
	PropertiesFactoryBean configProperties() throws Exception {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		propertiesFactoryBean.setLocations(resolver.getResources("classpath*:application.properties"));
		return propertiesFactoryBean;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOriginPatterns("*").allowCredentials(true)
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").maxAge(3600);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 添加用户权限验证Intercepter
		InterceptorRegistration registration = registry.addInterceptor(securityInterceptor);
		registration.addPathPatterns("/admin/**");
		
		registration=registry.addInterceptor(fileSecurityInterceptor);
		registration.addPathPatterns("/fadmin/**");
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new ByteArrayHttpMessageConverter());
		converters.add(new StringHttpMessageConverter());
		converters.add(new ResourceHttpMessageConverter());
		converters.add(new AllEncompassingFormHttpMessageConverter());
		converters.add(jackson2HttpMessageConverter());
	}
	
	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		configurer.setDefaultTimeout(3600_000);
	}

	@Bean
	MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		ObjectMapper mapper = new ObjectMapper();

		// 日期格式转换
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		mapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));

		// Long/Integer/Byte类型转String类型
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
		simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

		mapper.registerModule(simpleModule);

		converter.setObjectMapper(mapper);
		return converter;
	}
}
