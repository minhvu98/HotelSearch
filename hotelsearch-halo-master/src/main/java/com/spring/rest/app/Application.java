package com.spring.rest.app;

import com.core.rest.common.RestApplication;
import com.spring.core.common.constant.EnviromentConstant;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;

/**
 * 
 * @author Thanh Luan
 *
 */

@RestApplication
@EnableEurekaClient
public class Application {
	private static final Logger logger = LogManager.getLogger(Application.class);
	private static final String SPRING_PROFILE_DEFAULT = "spring.profiles.default";
	private final Environment env;

	public Application(Environment env) {
		this.env = env;
	}

	@PostConstruct
	public void initApplication() {
		Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
		if (activeProfiles.contains(EnviromentConstant.SPRING_PROFILE_DEVELOPMENT)
				&& activeProfiles.contains(EnviromentConstant.SPRING_PROFILE_PRODUCTION)) {
			logger.error("You have misConfigured your application! It should not run "
					+ "with both the 'dev' and 'prod' profiles at the same time.");
		}

	}

	public static void main(String[] args) throws UnknownHostException {
		SpringApplication app = new SpringApplication(Application.class);
		Map<String, Object> defProperties = new HashMap<>();
		defProperties.put(SPRING_PROFILE_DEFAULT, EnviromentConstant.SPRING_PROFILE_DEVELOPMENT);
		app.setDefaultProperties(defProperties);
		Environment env = app.run(args).getEnvironment();
		   String protocol = "http";
	        if (env.getProperty("server.ssl.key-store") != null) {
	            protocol = "https";
	        }
		logger.info("\n----------------------------------------------------------\n\t" +
                "Application '{}' is running! Access URLs:\n\t" +
                "Local: \t\t{}://localhost:{}\n\t" +
                "External: \t{}://{}:{}\n\t" +
                "Profile(s): \t{}\n----------------------------------------------------------",
        env.getProperty("spring.application.name"),
        protocol,
        env.getProperty("server.port"),
        protocol,
        InetAddress.getLocalHost().getHostAddress(),
        env.getProperty("server.port"),
        env.getActiveProfiles());
	}

}
