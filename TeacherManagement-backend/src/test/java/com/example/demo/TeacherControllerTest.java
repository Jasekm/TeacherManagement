package com.example.demo;

import com.example.demo.teacher.Teacher;
import com.example.demo.teacher.TeacherCondition;
import com.example.demo.teacher.TeacherController;
import com.example.demo.teacher.TeacherService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private TeacherController teacherController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTeachers() {
        List<Teacher> mockTeachers = List.of(new Teacher(), new Teacher());
        when(teacherService.getTeachers()).thenReturn(mockTeachers);

        List<Teacher> result = teacherController.getTeachers();

        assertEquals(mockTeachers, result);
        verify(teacherService, times(1)).getTeachers();
    }

    @Test
    void testAddNewTeacher() throws BadRequestException {
        Teacher teacher = new Teacher();
        Long classTeacherId = 1L;

        assertDoesNotThrow(() -> teacherController.addNewTeacher(teacher, classTeacherId));

        verify(teacherService, times(1)).addNewTeacher(teacher, classTeacherId);
    }

    @Test
    void testDeleteTeacher() {
        Long teacherId = 1L;

        teacherController.deleteTeacher(teacherId);

        verify(teacherService, times(1)).deleteTeacher(teacherId);
    }

    @Test
    void testGetAllTeachersCSV() throws Exception {
        // Given
        List<Teacher> mockTeachers = List.of(
                new Teacher(1L, "Mateusz", "Jasek", TeacherCondition.OBECNY, 1980, 5000.0),
                new Teacher(2L, "Wojtek", "Kieliszek", TeacherCondition.NIEOBECNY, 1985, 4700.0)
        );

        when(teacherService.getTeachers()).thenReturn(mockTeachers);

        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(byteArrayOutputStream);

        when(mockResponse.getOutputStream()).thenReturn(new ServletOutputStreamAdapter(byteArrayOutputStream));

        teacherController.getAllTeachersCSV(mockResponse);

        verify(mockResponse).setContentType("text/csv");
        verify(teacherService, times(1)).getTeachers();

        writer.flush();
        String csvOutput = byteArrayOutputStream.toString();
        assertTrue(csvOutput.contains("ID,NAME,SURNAME,CONDITION,SALARY,YEAR_OF_BIRTH"));
        assertTrue(csvOutput.contains("1,Mateusz,Jasek,OBECNY,5000.0,1980"));
        assertTrue(csvOutput.contains("2,Wojtek,Kieliszek,NIEOBECNY,4700.0,1985"));
    }


    static class ServletOutputStreamAdapter extends jakarta.servlet.ServletOutputStream {

        private final ByteArrayOutputStream outputStream;

        public ServletOutputStreamAdapter(ByteArrayOutputStream outputStream) {
            this.outputStream = outputStream;
        }

        @Override
        public void write(int b) throws IOException {
            outputStream.write(b);
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(jakarta.servlet.WriteListener writeListener) {
        }
    }
}
