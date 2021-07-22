package com.redbol.batch.schedulers;
import java.util.Properties;

import javax.sql.DataSource;
 
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
 
import com.redbol.batch.schedulers.QuartzStarter;
 
@Configuration
public class QuartzConfig {
	private static final Logger logger = LoggerFactory.getLogger(QuartzConfig.class);
	   
	   @Autowired
	   private DataSource dataSource;
	   
	   @Autowired
	   private PlatformTransactionManager transactionManager;
	   
	   @Autowired
	   private ApplicationContext applicationContext;
	   
	   @Bean
	   public SchedulerFactoryBean schedulerFactory() throws SchedulerException {
	       logger.info("SchedulerFactoryBean created!");
	       SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
	       AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
	       jobFactory.setApplicationContext(applicationContext);
	       schedulerFactoryBean.setJobFactory(jobFactory);
	       schedulerFactoryBean.setTransactionManager(transactionManager);
	       schedulerFactoryBean.setDataSource(dataSource);
	       schedulerFactoryBean.setOverwriteExistingJobs(true);
	       schedulerFactoryBean.setAutoStartup(true);
	       schedulerFactoryBean.setQuartzProperties(quartzProperties());
	       
	       return schedulerFactoryBean;
	   }
	 
	 
	   @Bean
	   public Properties quartzProperties() {
	       PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
	       propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
	 
	       Properties properties = null;
	       try {
	           propertiesFactoryBean.afterPropertiesSet();
	           properties = propertiesFactoryBean.getObject();
	       } catch (Exception e) {
	          logger.warn("Cannot load quartz.properties");
	       }
	       return properties;
	   }
	   
	   
	   @Bean(initMethod="init", destroyMethod="destroy")
	   public QuartzStarter quartzStarter() {
	      return new QuartzStarter();
	   }
}
