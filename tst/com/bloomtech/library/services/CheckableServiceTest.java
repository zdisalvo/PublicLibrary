package com.bloomtech.library.services;

import com.bloomtech.library.exceptions.CheckableNotFoundException;
import com.bloomtech.library.exceptions.ResourceExistsException;
import com.bloomtech.library.models.Library;
import com.bloomtech.library.models.checkableTypes.*;
import com.bloomtech.library.repositories.CheckableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CheckableServiceTest {

    //TODO: Inject dependencies and mocks
    @MockBean
    private CheckableRepository checkableRepository;

    private List<Checkable> checkables;

    @BeforeEach
    void init() {
        //Initialize test data
        checkables = new ArrayList<>();

        checkables.addAll(
                Arrays.asList(
                        new Media("1-0", "The White Whale", "Melvin H", MediaType.BOOK),
                        new Media("1-1", "The Sorcerer's Quest", "Ana T", MediaType.BOOK),
                        new Media("1-2", "When You're Gone", "Complaining at the Disco", MediaType.MUSIC),
                        new Media("1-3", "Nature Around the World", "DocuSpecialists", MediaType.VIDEO),
                        new ScienceKit("2-0", "Anatomy Model"),
                        new ScienceKit("2-1", "Robotics Kit"),
                        new Ticket("3-0", "Science Museum Tickets"),
                        new Ticket("3-1", "National Park Day Pass")
                )
        );
    }

    //TODO: Write Unit Tests for all CheckableService methods and possible Exceptions

    @Test
    void findByIsbn_returnsValidCheckable() {

        Checkable expected = new ScienceKit("2-0", "Anatomy Model");

        expected = checkables.get(5);


        Mockito.when(checkableRepository.findByIsbn("2-0")).thenReturn(Optional.of(expected));
        Optional<Checkable> actualOptional = checkableRepository.findByIsbn("2-0");

        Checkable actual = null;

        if (actualOptional.isPresent()) {
            actual = actualOptional.get();
        }

        assertEquals(expected, actual);

    }

    @Test
    void findByIsbn_invalidIsbn_throwsCheckableNotFoundException() {
        Checkable expected = null;

        Optional<Checkable> expectedOptional;

        when(checkableRepository.findByIsbn("0-0")).thenThrow(CheckableNotFoundException.class);
        //Optional<Checkable> actualOptional = checkableRepository.findByIsbn("0-0");

        Checkable actual = null;

//        if (actualOptional.isPresent()) {
//            actual = actualOptional.get();
//        } else {
//            actual = null;
//        }

        assertThrows(CheckableNotFoundException.class, () -> {checkableRepository.findByIsbn("0-0");});
    }

    @Test
    void findAll_returnsListOfCheckables() {

        List<Checkable> expected = checkables;

        when(checkableRepository.findAll()).thenReturn(expected);

        List<Checkable> actual = checkableRepository.findAll();

        assertEquals(actual, expected);
    }
}