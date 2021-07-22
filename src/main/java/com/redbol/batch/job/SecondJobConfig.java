package com.redbol.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j // log 사용을 위한 lombok 어노테이션
@RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
@Configuration
public class SecondJobConfig {
    private final JobBuilderFactory jobBuilderFactory; // 생성자 DI 받음
    private final StepBuilderFactory stepBuilderFactory; // 생성자 DI 받음
    
    SecondJobConfig(JobBuilderFactory jobBuilderFactory,StepBuilderFactory stepBuilderFactory){ 
		this.jobBuilderFactory=jobBuilderFactory;
		this.stepBuilderFactory=stepBuilderFactory; 
	}
    
    @Bean
    @Qualifier("secondJob")
    public Job secondJob() {
        return jobBuilderFactory.get("secondJob")
                .start(secondStep1())
                .next(secondStep2())
                .build();
    }

    @Bean
    public Step secondStep1() {
        return stepBuilderFactory.get("secondStep1")
                .tasklet((contribution, chunkContext) -> {
                	System.out.println(">>>>> This is secondStep1");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    
    @Bean
    public Step secondStep2() {
    	return stepBuilderFactory.get("secondStep2")
    			.tasklet((contribution, chunkContext) -> {
    				System.out.println(">>>>> This is secondStep2");
    				return RepeatStatus.FINISHED;
    			})
    			.build();
    }
}