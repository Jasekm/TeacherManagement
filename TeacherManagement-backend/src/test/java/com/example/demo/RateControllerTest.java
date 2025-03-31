package com.example.demo;


import com.example.demo.group.ClassTeacher;
import com.example.demo.group.ClassTeacherRepository;
import com.example.demo.rating.RateController;
import com.example.demo.rating.RateRequest;
import com.example.demo.rating.RateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RateControllerTest {

    @Mock
    private RateService rateService;

    @Mock
    private ClassTeacherRepository classTeacherRepository;

    @InjectMocks
    private RateController rateController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddRate_Success() {
        Long classTeacherId = 1L;
        int value = 5;
        String comment = "Very good teacher";

        RateRequest rateRequest = new RateRequest();
        rateRequest.setValue(value);
        rateRequest.setClassTeacherId(classTeacherId);
        rateRequest.setComment(comment);

        ClassTeacher mockClassTeacher = new ClassTeacher();
        mockClassTeacher.setId(classTeacherId);

        when(classTeacherRepository.findById(classTeacherId)).thenReturn(Optional.of(mockClassTeacher));

        rateController.addRate(rateRequest);

        verify(classTeacherRepository, times(1)).findById(classTeacherId);
        verify(rateService, times(1)).addNewRate(value, mockClassTeacher, comment);
    }

    @Test
    void testAddRate_ClassTeacherNotFound() {
        // Given
        Long classTeacherId = 1L;
        int value = 3;
        String comment = "Needs improvement";

        RateRequest rateRequest = new RateRequest();
        rateRequest.setValue(value);
        rateRequest.setClassTeacherId(classTeacherId);
        rateRequest.setComment(comment);

        when(classTeacherRepository.findById(classTeacherId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> rateController.addRate(rateRequest));
        verify(classTeacherRepository, times(1)).findById(classTeacherId);
        verifyNoInteractions(rateService);
    }
}
