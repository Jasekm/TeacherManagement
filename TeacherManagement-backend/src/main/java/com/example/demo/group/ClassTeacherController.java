package com.example.demo.group;


import com.example.demo.teacher.Teacher;
import com.example.demo.teacher.TeacherController;
import com.example.demo.teacher.TeacherService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(path="api/group")
public class ClassTeacherController {

        private final ClassTeacherService classTeacherService;
        private final TeacherService teacherService;

        @Autowired
        public ClassTeacherController(ClassTeacherService classTeacherService, TeacherService teacherService){
            this.classTeacherService=classTeacherService;
            this.teacherService=teacherService;

        }

        @GetMapping
        public List<ClassTeacher> getTeacherGroups(){
            return classTeacherService.getTeacherGroups();
        }

        @PostMapping
        public void addTeacherGroup(@RequestBody ClassTeacher group) throws BadRequestException {
            classTeacherService.addNewGroup(group);
        }
        @DeleteMapping(path="{groupId}")
        public void deleteTeacherGroup(@PathVariable("groupId") Long groupId){
            classTeacherService.deleteGroup(groupId);
        }
        @GetMapping(path="{groupId}/teacher")
        public ResponseEntity<List<Teacher>> getTeachersFromGroup(@PathVariable("groupId") Long groupId) {
            List<Teacher> teachers = classTeacherService.getTeachersFromGroup(groupId);

            if (teachers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.emptyList());
            }

            return ResponseEntity.ok(teachers);
        }
        @GetMapping(path ="{groupId}/fill")
        public ResponseEntity<Double> getPercentageFilling(@PathVariable("groupId") Long groupId){
            Double percentageFilling = teacherService.getPercentageFilling(groupId);
            return ResponseEntity.ok(percentageFilling);
        }


}
