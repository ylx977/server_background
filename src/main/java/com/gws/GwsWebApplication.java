package com.gws;

import com.gws.utils.GwsLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@ServletComponentScan
@SpringBootApplication
@ImportResource({"classpath:conf/dbconf.xml",
	 			"classpath:conf/masterShardingContext.xml",
	 			"classpath:conf/slaveShardingContext.xml",
	 			"classpath:conf/shardingConfig.xml"
				})
public class
GwsWebApplication {
    public static void main(String[] args) {
    	SpringApplication app = new SpringApplication(GwsWebApplication.class);
        SpringApplication.run(GwsWebApplication.class, args);
    	GwsLogger.info(" webApplication server is started!");
    }
}
