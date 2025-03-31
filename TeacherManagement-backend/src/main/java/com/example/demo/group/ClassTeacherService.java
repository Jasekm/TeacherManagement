package com.example.demo.group;

import com.example.demo.teacher.Teacher;
import com.example.demo.teacher.TeacherRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassTeacherService {

        private final ClassTeacherRepository classTeacherRepository;
        private final TeacherRepository teacherRepository;

        @Autowired
        public ClassTeacherService(ClassTeacherRepository classTeacherRepository,TeacherRepository teacherRepository){
            this.classTeacherRepository=classTeacherRepository;
            this.teacherRepository=teacherRepository;
        }


        public List<ClassTeacher> getTeacherGroups(){
            return classTeacherRepository.findAll();
    }

    public void addNewGroup(ClassTeacher group) throws BadRequestException {
        Optional<ClassTeacher> studentOptional= classTeacherRepository.findGroupByName(group.getName());
        if(studentOptional.isPresent()){
            throw new BadRequestException("Jest juz grupa o takiej nazwie");
        }
        classTeacherRepository.save(group);
    }

    public void deleteGroup(Long groupId) {
            boolean exists = classTeacherRepository.existsById(groupId);
            if(!exists){
                throw new IllegalStateException("Grupa o id: "+ groupId+" nie istnieje");
            }
            classTeacherRepository.deleteById(groupId);
    }

    public List<Teacher> getTeachersFromGroup(Long groupId) {
         return  teacherRepository.findByClassTeacher_Id(groupId);
    }


}
