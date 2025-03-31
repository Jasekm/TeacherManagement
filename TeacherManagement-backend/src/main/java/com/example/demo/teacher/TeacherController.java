package com.example.demo.teacher;


import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "api/teacher")
public class TeacherController {


    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService){
        this.teacherService=teacherService;
    }

    @GetMapping
    public List<Teacher> getTeachers(){
        return teacherService.getTeachers();

    }
    @PostMapping
    public void addNewTeacher(@RequestBody Teacher teacher, @RequestParam Long classTeacherId) throws BadRequestException {
         teacherService.addNewTeacher(teacher, classTeacherId);
    }
    @DeleteMapping(path="{teacherId}")
    public void deleteTeacher(@PathVariable("teacherId") Long teacherId){
        teacherService.deleteTeacher(teacherId);
    }
    @GetMapping("/csv")
    public void getAllTeachersCSV(HttpServletResponse response) {
        List<Teacher> teachers = teacherService.getTeachers();
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=teachers.csv");

        try(ServletOutputStream outputStream = response.getOutputStream()){
            outputStream.println("ID,NAME,SURNAME,CONDITION,SALARY,YEAR_OF_BIRTH");
            //System.out.println("ID,NAME,SURNAME,CONDITION,SALARY,YEAR_OF_BIRTH");

            for (Teacher teacher : teachers) {
                outputStream.println(teacher.getId() + "," + teacher.getName() + "," + teacher.getSurname()+","+teacher.getCondition()+","+teacher.getSalary()+","+teacher.getYob());
                //System.out.println(teacher.getId() + "," + teacher.getName() + "," + teacher.getSurname()+","+teacher.getCondition()+","+teacher.getSalary()+","+teacher.getYob());
            }
            outputStream.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
