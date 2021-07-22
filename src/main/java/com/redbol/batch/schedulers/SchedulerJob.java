package com.redbol.batch.schedulers;

import java.util.List;

import javax.annotation.Resource;
import javax.batch.runtime.JobExecution;

import org.apache.logging.log4j.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Transactional;

@Transactional
//@DisallowConcurrentExecution   //클러스터링 환경에선 해당 어노테이션 작동하지 않음
public class SchedulerJob  extends QuartzJobBean {
	 
	   private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerJob.class);
	      
	   @Autowired
	   private JobLauncher jobLauncher;
	   
	   @Resource(name="QuartzUtils")
	   private QuartzUtils quartzUtils;
	   
	   @Override
	   protected void executeInternal(JobExecutionContext jobContext) throws org.quartz.JobExecutionException {
	      LOGGER.info("executeInternal()");
	      try {
	          JobDataMap dataMap = jobContext.getJobDetail().getJobDataMap();
	          
	          JobParametersBuilder jpb = new JobParametersBuilder();
	          for (BatchParamtrVO paramtr : (List<BatchParamtrVO>)dataMap.get("paramtrList")) {
	             jpb.addString(paramtr.getParamtrNm(), paramtr.getParamtr());
	          }
	          
	          String schdulNo = jobContext.getJobDetail().getKey().getName();
	          String currentTime = Long.toString(System.currentTimeMillis());
	          String schdulResultNo = dataMap.getString("schdulResultNo");
	          
	          jpb.addString("schdulNo", schdulNo);
	          jpb.addString("currentTime", currentTime);
	          jpb.addString("schdulResultNo", schdulResultNo);
	 
	          JobExecution je = null;                
	          je = jobLauncher.run((Job)BeanUtils.getBean(dataMap.getString("batchProgrm")), jpb.toJobParameters());
	          result = je.getId(); //jobExecutionId 가 생성되는 시점에 유의 (배치 실행 후 리턴되며 실행 전 알 수 없음)
	             
	          jobContext.setResult(result);
	         
	      } catch (JobExecutionAlreadyRunningException e) {
	         LOGGER.info("ex while excute : {}", e.getMessage());
	      } catch (Exception e) {
	         LOGGER.info("ex while excute : {}", e.getMessage()); 
	      }
	   }
	   
	}

