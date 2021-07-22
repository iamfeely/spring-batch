package com.redbol.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.redbol.batch.tasklets.PrimaryTasklet;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class PrimaryJobConfig {

    private final JobBuilderFactory jobBuilderFactory; // Job 빌더 생성용
    private final StepBuilderFactory stepBuilderFactory; // Step 빌더 생성용
    
    PrimaryJobConfig(JobBuilderFactory jobBuilderFactory,StepBuilderFactory stepBuilderFactory){ 
		this.jobBuilderFactory=jobBuilderFactory;
		this.stepBuilderFactory=stepBuilderFactory; 
	}
    // JobBuilderFactory를 통해서 tutorialJob을 생성
    @Bean
    @Qualifier("primaryJob")
    public Job primaryJob() {
        return jobBuilderFactory.get("primaryJob")
                .start(primaryStep1())  // Step 설정
                .next(primaryStep2())  // Step 설정
                .next(primaryStep3())  // Step 설정
                .build();
    }

    // StepBuilderFactory를 통해서 tutorialStep을 생성
    @Bean
    public Step primaryStep1() {
    	System.out.println("executed primaryStep1 !!");
        return stepBuilderFactory.get("primaryStep1")
                .tasklet(new PrimaryTasklet()) // Tasklet 설정
                .build();
    }
    @Bean
    public Step primaryStep2() {
    	System.out.println("executed primaryStep2 !!");
    	return stepBuilderFactory.get("primaryStep2")
    			.tasklet(new PrimaryTasklet()) // Tasklet 설정
    			.build();
    }
    @Bean
    public Step primaryStep3() {
    	System.out.println("executed primaryStep3 !!");
    	return stepBuilderFactory.get("primaryStep3")
    			.tasklet(new PrimaryTasklet()) // Tasklet 설정
    			.build();
    }
}