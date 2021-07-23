package com.redbol.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StepNextJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    String requestDate=null;
    StepNextJobConfig(JobBuilderFactory jobBuilderFactory,StepBuilderFactory stepBuilderFactory){ 
		this.jobBuilderFactory=jobBuilderFactory;
		this.stepBuilderFactory=stepBuilderFactory; 
	}
    
    @Bean
    @Primary
    //@Qualifier("stepNextJob")
    public Job stepNextJob() {
        return jobBuilderFactory.get("stepNextJob")
                .start(step1(requestDate))
                .next(step2())
               // .next(step3())
                .build();
    }

    @Bean
    @JobScope
    public Step step1(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">>>>> This is Step1");
                    System.out.println(">>>>> requestDate="+requestDate);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet(
				/*
				 * (contribution, chunkContext) -> { System.out.println(">>>>> This is Step2");
				 * return RepeatStatus.FINISHED; }
				 */
                		scopeStep2Tasklet(requestDate)
                		)
                .build();
    }
    
    @Bean
    @StepScope //Tasklet에 대해서 설정
    public Tasklet scopeStep2Tasklet(@Value("#{jobParameters[requestDate]}") String requestDate) {
    	return (contribution,chunkContext) -> {
    		System.out.println(">>>>>>> This is scopeStep2Tasklet");
    		System.out.println(">>>>> requestDate="+requestDate);
    		return RepeatStatus.FINISHED;
    	};
    }
	/*
	 * @Bean
	 * 
	 * @StepScope //Tasklet에 대해서 설정 public Tasklet
	 * scopeStep2Tasklet(@Value("#{jobParameters[requestDate]}") String
	 * requestDate){ return (contribution, chunkContext) -> {
	 * System.out.println(">>>>>>> This is scopeStep2Tasklet");
	 * System.out.println(">>>>> requestDate="+requestDate); return
	 * RepeatStatus.FINISHED; }; }
	 */  

    @Bean
    public Step step3() {
        return stepBuilderFactory.get("step3")
                .tasklet((contribution, chunkContext) -> {
                	System.out.println(">>>>> This is Step3");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
