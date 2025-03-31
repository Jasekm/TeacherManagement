package com.example.demo.rating;



import com.example.demo.group.ClassTeacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RateService {

    private final RateRepository rateRepository;

    @Autowired
    public RateService(RateRepository rateRepository){
        this.rateRepository=rateRepository;
    }


    public Rate addNewRate(int value, ClassTeacher classTeacher, String comment) {
        LocalDate currentDate = LocalDate.now();

        Rate rate = new Rate(value, classTeacher, currentDate, comment);

        return rateRepository.save(rate);
    }
}
