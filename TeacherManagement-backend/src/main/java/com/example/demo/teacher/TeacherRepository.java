package com.example.demo.teacher;


import com.example.demo.group.ClassTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher,Long> {
    List<Teacher> findByClassTeacher_Id(Long groupId);

    @Query("SELECT t FROM Teacher t WHERE t.classTeacher.id = :groupId")
    List<Teacher> findTeachersByGroupId(@Param("groupId") Long groupId);

    @Query("SELECT s FROM Teacher s WHERE s.name=?1")
    Optional<Teacher> findTeacherByName(String name);
    @Query("SELECT s FROM Teacher s WHERE s.surname=?1")
    Optional<Teacher> findTeacherBySurName(String surname);
}
