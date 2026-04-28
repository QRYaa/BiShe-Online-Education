package cn.tgxy;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

@EnableTransactionManagement
@EnableScheduling
@SpringBootApplication
public class OnlineEduApplication {
    private static Logger log = LoggerFactory.getLogger(OnlineEduApplication.class);

	public static void main(String[] args) throws Exception{
		ConfigurableApplicationContext application = SpringApplication.run(OnlineEduApplication.class, args);
		Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        if (!StringUtils.hasText(path)) {
            path = "";
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "Online-edu  is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + path + "\n\t" +
                "External: \thttp://" + ip + ":" + port + path + "\n" +
                "----------------------------------------------------------");
	}
	

}
