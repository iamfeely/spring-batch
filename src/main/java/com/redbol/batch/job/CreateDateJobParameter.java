package com.redbol.batch.job;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor
public class CreateDateJobParameter {

  //  @Value("#{jobParameters[status]}") // (1)
  //  private ProductStatus status;

    private LocalDate createDate;

    @Value("#{jobParameters[createDate]}") // (2)
    public void setCreateDate(String createDate) {
       this.createDate = LocalDate.parse(createDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
   }
}