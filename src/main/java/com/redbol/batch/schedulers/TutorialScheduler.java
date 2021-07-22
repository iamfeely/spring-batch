package com.redbol.batch.schedulers;

import java.time.LocalDateTime;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.redbol.batch.job.SecondJobConfig;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TutorialScheduler {

    private final Job job;  // primaryJob
    private final JobLauncher jobLauncher;
    
    TutorialScheduler(Job job,JobLauncher jobLauncher){
    	this.job=job;
    	this.jobLauncher=jobLauncher;
    }

    // 5초마다 실행
    @Scheduled(fixedDelay = 30 * 1000L)
    public void executeJob () {
        try {
            jobLauncher.run(
                    job,
                    new JobParametersBuilder()
                    .addString("Time", LocalDateTime.now().toString())
                    .toJobParameters()  // job parameter 설정
            );
        } catch (JobExecutionException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
