package com.redbol.batch;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableBatchProcessing //
@SpringBootApplication
public class SpringBatchApplication {

	public static void main(String[] args) {
		/*int n=3;
		//람다
		Calculator cal=a -> n+1;
		System.out.println("calc==========="+cal.calc(n));
		
		//Stream
		List<Integer> list=Arrays.asList(1,2,3,4,5,6);
		Stream<Integer> stream=list.stream();
		stream.forEach(i -> {System.out.println("stream i::"+i);});
		
		List<String> names = Arrays.asList("son", "ryu", "kang","yang","tash","amazone");
		Stream<String> streams = names.stream()
						.map(String::toUpperCase)
		                .filter(name -> name.contains("A"));
		streams.forEach(name ->{System.out.println("name::::"+name);});
		*/
		SpringApplication.run(SpringBatchApplication.class, args);
	}

}
