package com.redbol.batch.schedulers;

import java.util.List;

import javax.annotation.Resource;

import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;


public class QuartzStarter {
	 private static final Logger LOGGER = LoggerFactory.getLogger(QuartzStarter.class);
	   
	   @Autowired
	   private SchdulService schdulService;
	 
	   @Autowired
	   private SchedulerFactoryBean schedulerFactoryBean;
	   
	   @Autowired
	   private InitAbandonedJobService initAbandonedJobService;
	   
	   @Resource(name="scheduleListener")
	   private JobListener scheduleListener;
	   
	   private Scheduler scheduler;
	   
	 
	   /** 서버 launch 시 호출되는 메소드 
	    *  1. 서버 shutdown 시 running 중이던 job의 status를 ABANDONED 로 UPDATE
	    *  2. 스케쥴JOB 등록 및 스케쥴러 시작
	    */
	   public void init() throws Exception {
	      scheduler = schedulerFactoryBean.getScheduler();
	      addListener();
	      scheduler.clear();
	      List<SchdulVO> schdulList = getSchdulList();
	      registScheduleForScheduler(schdulList);
	      scheduler.start();
	   }
	 
	   
	   /** 스케쥴 리스트 조회 */
	   public List<SchdulVO> getSchdulList() throws Exception {
	      return schdulService.selectSchdulList();     
	   }
	   
	   
	   /** 스케쥴러에 스케쥴JOB 등록 */
	   private void registScheduleForScheduler(List<SchdulVO> schdulList) throws Exception {
	      for (SchdulVO schdul : schdulList) {
	            schdul.setParamtrList(schdulService.selectSchdulParamtr(schdul));
	            insertSchdul(schdul);
	      }
	   }
	   
	   /** scheduler 에 리스너 등록 */
	   private void addListener() throws SchedulerException {
	      scheduler.getListenerManager().addJobListener(scheduleListener);
	   }
	   
	   
	   /** scheduler 에 scheduleJob 등록 */
	   public void insertSchdul(SchdulVO schdulVO) throws Exception {
	      
	      JobDetail jobDetail;          // Job 상세 정보 VO
	      CronTrigger cronTrigger;      // Trigger 객체
	      
	      jobDetail = JobBuilder.newJob(SchedulerJob.class)
	            .withIdentity(new JobKey(schdulVO.getSchdulNo()))
	            .build();
	      
	      jobDetail.getJobDataMap().put("batchId"     , schdulVO.getBatchId    ());
	      jobDetail.getJobDataMap().put("batchProgrm" , schdulVO.getBatchNm    ());
	      jobDetail.getJobDataMap().put("paramtrList" , schdulVO.getParamtrList());
	      
	      cronTrigger = TriggerBuilder.newTrigger()
	             .withIdentity(schdulVO.getSchdulNo())
	             .withSchedule(CronScheduleBuilder.cronSchedule(schdulVO.getCronExpression()).withMisfireHandlingInstructionDoNothing())
	             .forJob(schdulVO.getSchdulNo())
	             .build();
	      
	      try {
	         scheduler.scheduleJob(jobDetail, cronTrigger);
	      } catch (SchedulerException e) {
	         LOGGER.error("ex while registering schedule {}", e.getMessage());
	      } catch (Exception e) {
	         LOGGER.error("ex while registering schedule {}", e.getMessage());
	      }
	      
	   }
	   
	   
	   //https://stackoverflow.com/questions/3650539/what-is-the-difference-between-schedulers-standby-and-pauseall
	   //stanby 는 scheduler 를 정지시키고 정지상태에서 misfire 된 job 들을 start가 된 이후 무시한다.
	   /** 스케쥴러 재시작
	    *  ADM 스케쥴 수정 및 삭제시 호출됨 
	    */
	   public void restart() {
	      try {
	         scheduler.standby();   
	         addListener();
	         scheduler.clear();
	         List<SchdulVO> schdulList = getSchdulList();
	         registScheduleForScheduler(schdulList);
	         scheduler.start();
	      } catch (Exception e) {
	         LOGGER.error("ex in restart() {}", e.getMessage());
	      }
	   }
	   
	   
	   /** 스케쥴러 종료
	    *  WAS SERVER shutdown
	    */
	   public void destroy() {  
	      try {
	         if (scheduler != null) {  
	            scheduler.shutdown();
	            schedulerFactoryBean.destroy();
	         }
	      } catch(Exception e) {
	         LOGGER.error("ex in destroy() {}", e.getMessage());
	      }
	   }
}
