package com.example.demo.group;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClassTeacherRepository extends JpaRepository<ClassTeacher,Long> {
    @Query("SELECT s FROM ClassTeacher s WHERE s.name=?1")
    Optional<ClassTeacher> findGroupByName(String name);
}
