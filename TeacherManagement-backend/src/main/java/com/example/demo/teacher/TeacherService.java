package com.example.demo.teacher;


import com.example.demo.group.ClassTeacher;
import com.example.demo.group.ClassTeacherRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final ClassTeacherRepository classTeacherRepository;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository,ClassTeacherRepository classTeacherRepository){
        this.teacherRepository=teacherRepository;
        this.classTeacherRepository = classTeacherRepository;
    }

    public List<Teacher> getTeachers() {

        return teacherRepository.findAll();
    }
    public void addNewTeacher(Teacher teacher, Long classTeacherId) throws BadRequestException {
        ClassTeacher classTeacher = classTeacherRepository.findById(classTeacherId)
                .orElseThrow(() -> new IllegalArgumentException("ClassTeacher not found"));

        teacher.setClassTeacher(classTeacher);
        if (classTeacher.getTeachers().size() >= classTeacher.getMax_teachers()) {
            throw new BadRequestException("Nie można dodać nauczyciela, ponieważ grupa jest pełna.");
        }
        Optional<Teacher> teacherOptional= teacherRepository.findTeacherByName(teacher.getName());
        Optional<Teacher> teacherOptional1= teacherRepository.findTeacherBySurName(teacher.getSurname());
        if(teacherOptional.isPresent()&&teacherOptional1.isPresent()){
            throw new BadRequestException("Nauczyciel o tym imieniu i nazwisku juz istnieje");
        }
        teacherRepository.save(teacher);
    }

   public void deleteTeacher(Long teacherId) {
        boolean exists = teacherRepository.existsById(teacherId);
        if (!exists) {
            throw new IllegalStateException("Teacher with id " + teacherId + " does not exist");
        }

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalStateException("Teacher with id " + teacherId + " not found"));

        if (teacher.getClassTeacher() != null) {
            ClassTeacher classTeacher = teacher.getClassTeacher();
            classTeacher.getTeachers().remove(teacher);
            teacher.setClassTeacher(null);
            classTeacherRepository.save(classTeacher);
        }

        teacherRepository.deleteById(teacherId);


    }
    public Double getPercentageFilling(Long groupId) {
        ClassTeacher classTeacher = classTeacherRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("ClassTeacher not found"));

        int currentSize = teacherRepository.findByClassTeacher_Id(groupId).size();

        int capacity = classTeacher.getMax_teachers();

        if (capacity == 0) {
            throw new IllegalArgumentException("Capacity of the group cannot be zero.");
        }

        double percentage = (double) currentSize / capacity * 100;

        return percentage;
    }
}
