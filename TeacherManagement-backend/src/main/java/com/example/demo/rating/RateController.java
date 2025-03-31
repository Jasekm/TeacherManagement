package com.example.demo.rating;

import com.example.demo.group.ClassTeacher;
import com.example.demo.group.ClassTeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="api/rating")
public class RateController {

    private final RateService rateService;
    private final ClassTeacherRepository classTeacherRepository;

    @Autowired
    public RateController(RateService rateService, ClassTeacherRepository classTeacherRepository){
        this.rateService=rateService;
        this.classTeacherRepository=classTeacherRepository;
    }
    @PostMapping
    public void addRate(@RequestBody RateRequest rateRequest) {
        ClassTeacher classTeacher = classTeacherRepository.findById(rateRequest.getClassTeacherId())
                .orElseThrow(() -> new IllegalArgumentException("ClassTeacher not found"));

        Rate rate = rateService.addNewRate(rateRequest.getValue(), classTeacher, rateRequest.getComment());
    }
}
