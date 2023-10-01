package com.bloomtech.library.services;

import com.bloomtech.library.datastore.Datastore;
import com.bloomtech.library.exceptions.CheckableNotFoundException;
import com.bloomtech.library.exceptions.LibraryNotFoundException;
import com.bloomtech.library.exceptions.ResourceExistsException;
import com.bloomtech.library.models.*;
import com.bloomtech.library.models.checkableTypes.Checkable;
import com.bloomtech.library.models.checkableTypes.Media;
import com.bloomtech.library.models.checkableTypes.ScienceKit;
import com.bloomtech.library.repositories.CheckableRepository;
import com.bloomtech.library.repositories.LibraryCardRepository;
import com.bloomtech.library.repositories.LibraryRepository;
import com.bloomtech.library.models.CheckableAmount;
import com.bloomtech.library.repositories.PatronRepository;
import com.bloomtech.library.views.LibraryAvailableCheckouts;
import com.bloomtech.library.views.OverdueCheckout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibraryService {

    //TODO: Implement behavior described by the unit tests in tst.com.bloomtech.library.services.LibraryService

    @Autowired
    private LibraryRepository libraryRepository;
    @Autowired
    private CheckableRepository checkableRepository;
    @Autowired
    private CheckableService checkableService;
    @Autowired
    private Datastore datastore;
    @Autowired
    private LibraryCardRepository libraryCardRepository;

    


    public List<Library> getLibraries() {
        return libraryRepository.findAll();
        //return new ArrayList<>();
    }

    public Library getLibraryByName(String name) {
        List<Library> libraries = libraryRepository.findAll();
        Optional<Library> libraryOptional = libraryRepository.findByName(name);
        if (libraryOptional.isPresent()) {
            return libraryOptional.get();
        } else {
            throw new LibraryNotFoundException("Non-existent library");
        }
//        for (Library library : libraries) {
//            if (library.getName().equals(name)) {
//                System.out.println(library.getName());
//                return library;
//            }
//        }

        //return null;
    }

    public void save(Library library) {
        List<Library> libraries = libraryRepository.findAll();
        if (libraries.stream().filter(p -> p.getName().equals(library.getName())).findFirst().isPresent()) {
            throw new ResourceExistsException("Library with name: " + library.getName() + " already exists!");
        }
        libraryRepository.save(library);
    }

    public CheckableAmount getCheckableAmount(String libraryName, String checkableIsbn) {
        Optional<Library> libraryOptional = libraryRepository.findByName(libraryName);

        Library library = new Library(null);
        if (libraryOptional.isPresent()) {
            library = libraryOptional.get();
        }

        Checkable checkable = checkableService.getByIsbn(checkableIsbn);

        for (CheckableAmount checkableAmount : library.getCheckables()) {
            if (checkableAmount.getCheckable().getIsbn().equals(checkableIsbn)) {
                return new CheckableAmount(checkableAmount.getCheckable(), checkableAmount.getAmount());
            }
        }



        return new CheckableAmount(checkable, 0);

    }

    public List<LibraryAvailableCheckouts> getLibrariesWithAvailableCheckout(String isbn) {
        List<LibraryAvailableCheckouts> available = new ArrayList<>();
        Checkable checkable = checkableService.getByIsbn(isbn);

        for (Library library : libraryRepository.findAll()) {
            if (library.getCheckables().stream().filter(p -> p.getCheckable().getIsbn().equals(isbn)).findFirst().isPresent()) {
                for (CheckableAmount checkableAmount : library.getCheckables()) {
                    if (checkableAmount.getCheckable().equals(checkable)) {
                        available.add(new LibraryAvailableCheckouts(checkableAmount.getAmount(), library.getName()));
                        System.out.println(checkableAmount.getAmount() + " " + library.getName());
                    }
                    //}

                }
            }
        }
            //for (CheckableAmount checkableAmount : library.getCheckables()) {
//            Checkable checkable =
//
//                if (checkableAmount.getCheckable().getIsbn().equals(isbn)) {
//                    available.add(new LibraryAvailableCheckouts(checkableAmount.getAmount(), library.getName()));
//                    System.out.println(checkableAmount.getAmount() + " " + library.getName());
//                }
//            //}
//        }

    return available;
}


    public List<OverdueCheckout> getOverdueCheckouts(String libraryName) {
        List<OverdueCheckout> overdueCheckouts = new ArrayList<>();

        Optional<Library> libraryOptional = libraryRepository.findByName(libraryName);

        Library library = new Library(null);

        if (libraryOptional.isPresent()) {
            library = libraryOptional.get();
        }


        List<LibraryCard> libraryCards = new ArrayList<>();

        for (LibraryCard libraryCard : libraryCardRepository.findAll()) {
            Library library1 = libraryCard.getLibrary();
            if (libraryCard.getLibrary().getName().equals(libraryName)) {
                libraryCards.add(libraryCard);
            }
        }


        for (LibraryCard libraryCard : libraryCards) {
            for (Checkout checkout : libraryCard.getCheckouts()) {
                if (checkout.getDueDate().compareTo(LocalDateTime.now()) < 0 ) {
                    //if (library.getCheckables().stream().filter(p -> p.getCheckable().getIsbn().equals(checkout.getCheckable().getIsbn())).findFirst().isPresent()) {
                        overdueCheckouts.add(new OverdueCheckout(libraryCard.getPatron(), checkout));
                        System.out.println(checkout.getCheckable().getTitle());
                    //}
                }
            }
        }




        return overdueCheckouts;
    }
}
