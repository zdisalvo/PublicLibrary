package com.bloomtech.library.services;

import com.bloomtech.library.exceptions.CheckableNotFoundException;
import com.bloomtech.library.exceptions.ResourceExistsException;
import com.bloomtech.library.models.Library;
import com.bloomtech.library.models.checkableTypes.*;
import com.bloomtech.library.repositories.CheckableRepository;
import net.jodah.typetools.TypeResolver;
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
    @Autowired
    CheckableService checkableService;

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
    void getByIsbn_returnsValidCheckable() {

        Checkable expected = new ScienceKit("2-0", "Anatomy Model");

        expected = checkables.get(5);


        Mockito.when(checkableRepository.findByIsbn("2-0")).thenReturn(Optional.of(expected));
        //Optional<Checkable> actualOptional = checkableRepository.findByIsbn("2-0");

        Checkable actual = checkableService.getByIsbn("2-0");

        //Checkable actual = null;

//        if (actualOptional.isPresent()) {
//            actual = actualOptional.get();
//        }

        assertEquals(expected, actual);

    }

    @Test
    void getByIsbn_invalidIsbn_throwsCheckableNotFoundException() {
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

        assertThrows(CheckableNotFoundException.class, () -> {checkableService.getByIsbn("0-0");});
    }

    @Test
    void getAll_returnsListOfCheckables() {

        List<Checkable> expected = checkables;

        when(checkableRepository.findAll()).thenReturn(expected);

        List<Checkable> actual = checkableService.getAll();

        assertEquals(actual, expected);
    }

    @Test
    void getByType_returnsACheckableOfCorrectType() {
        Checkable expected = checkables.get(0);

        when(checkableRepository.findByType(Media.class)).thenReturn(Optional.ofNullable(expected));

        Checkable actual = checkableService.getByType(Media.class);

        assertEquals(expected, actual);

    }

    @Test
    void getByType_throwsCheckableNotFoundException() {

        when(checkableRepository.findByType(TypeResolver.Unknown.class)).thenThrow(CheckableNotFoundException.class);

        assertThrows(CheckableNotFoundException.class, () -> {checkableService.getByType(TypeResolver.Unknown.class);});
    }

    @Test
    void save() {
        //Checkable expected = new ScienceKit("10-1", "1984");

        when(checkableRepository.findAll()).thenReturn(checkables);

        //checkableService.save(expected);

        checkableService.save(new ScienceKit("10-1", "1984"));

        Mockito.verify(checkableRepository).save(any(Checkable.class));
    }
}