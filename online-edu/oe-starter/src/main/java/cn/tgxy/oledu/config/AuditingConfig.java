package cn.tgxy.oledu.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import cn.tgxy.tgbase.dto.BsUserDto;
import cn.tgxy.tgbase.service.BsAuthService;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditingConfig {

	@Autowired
	private BsAuthService authService;

    @Bean
    AuditorAware<Long> auditorAware() {
    	return () -> {
    		try {
				BsUserDto userDto = authService.getCurrentUser();
				return Optional.of(userDto.getId());
			} catch (Exception e) {
	    		return Optional.of(0L);
			}
    	};

	}
}
