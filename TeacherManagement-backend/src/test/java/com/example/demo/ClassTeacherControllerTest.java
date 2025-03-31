package com.example.demo;


import com.example.demo.group.ClassTeacher;
import com.example.demo.group.ClassTeacherController;
import com.example.demo.group.ClassTeacherService;
import com.example.demo.teacher.Teacher;
import com.example.demo.teacher.TeacherService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClassTeacherControllerTest {

    @Mock
    private ClassTeacherService classTeacherService;

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private ClassTeacherController classTeacherController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTeacherGroups() {
        List<ClassTeacher> mockGroups = List.of(new ClassTeacher(), new ClassTeacher());
        when(classTeacherService.getTeacherGroups()).thenReturn(mockGroups);

        List<ClassTeacher> result = classTeacherController.getTeacherGroups();

        assertEquals(mockGroups, result);
        verify(classTeacherService, times(1)).getTeacherGroups();
    }

    @Test
    void testAddTeacherGroup() throws BadRequestException {
        ClassTeacher group = new ClassTeacher();

        assertDoesNotThrow(() -> classTeacherController.addTeacherGroup(group));

        verify(classTeacherService, times(1)).addNewGroup(group);
    }

    @Test
    void testDeleteTeacherGroup() {
        Long groupId = 1L;

        classTeacherController.deleteTeacherGroup(groupId);

        verify(classTeacherService, times(1)).deleteGroup(groupId);
    }

    @Test
    void testGetTeachersFromGroup_Found() {
        Long groupId = 1L;
        List<Teacher> mockTeachers = List.of(new Teacher(), new Teacher());
        when(classTeacherService.getTeachersFromGroup(groupId)).thenReturn(mockTeachers);

        ResponseEntity<List<Teacher>> response = classTeacherController.getTeachersFromGroup(groupId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockTeachers, response.getBody());
        verify(classTeacherService, times(1)).getTeachersFromGroup(groupId);
    }

    @Test
    void testGetTeachersFromGroup_NotFound() {
        Long groupId = 1L;
        when(classTeacherService.getTeachersFromGroup(groupId)).thenReturn(Collections.emptyList());

        ResponseEntity<List<Teacher>> response = classTeacherController.getTeachersFromGroup(groupId);

        assertEquals(404, response.getStatusCodeValue());
        assertTrue(response.getBody().isEmpty());
        verify(classTeacherService, times(1)).getTeachersFromGroup(groupId);
    }

    @Test
    void testGetPercentageFilling() {
        Long groupId = 1L;
        Double mockPercentage = 75.0;
        when(teacherService.getPercentageFilling(groupId)).thenReturn(mockPercentage);

        ResponseEntity<Double> response = classTeacherController.getPercentageFilling(groupId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockPercentage, response.getBody());
        verify(teacherService, times(1)).getPercentageFilling(groupId);
    }
}
