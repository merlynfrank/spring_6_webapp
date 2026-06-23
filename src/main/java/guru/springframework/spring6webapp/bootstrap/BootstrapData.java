package guru.springframework.spring6webapp.bootstrap;

import guru.springframework.spring6webapp.domain.Author;
import guru.springframework.spring6webapp.domain.Book;
import guru.springframework.spring6webapp.domain.Publisher;
import guru.springframework.spring6webapp.repositories.AuthorRepository;
import guru.springframework.spring6webapp.repositories.BookRepository;
import guru.springframework.spring6webapp.repositories.PublisherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


/*
This is explicitly Component stereotype to indicate that Spring Context to detect and pick it up as Spring bean
So when the Spring Context loads it will be detected.

CommandLineRunner - when detected in the context/classpath - this will get picked up and run at startup
 */

@Component
public class BootstrapData implements CommandLineRunner {
    /*
    We want to work with below repos so define a constructor whihc has below repos so at startup
    it will autowire in the implementations for the repositories that are being provided by Spring data JPA
     */
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;

    public BootstrapData(AuthorRepository authorRepository, BookRepository bookRepository,
                         PublisherRepository publisherRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Author eric = new Author();
        eric.setFirstName("Eric");
        eric.setLastName("Evans");

        Book ddd = new Book();
        ddd.setTitle("Domain Driven Design");
        ddd.setIsbn("123456");

        // Best practise to always store the object that was saved to the DB
        // for operations if any down the line
        Author ericSaved = authorRepository.save(eric);
        Book dddSaved = bookRepository.save(ddd);

        Author rod = new Author();
        rod.setFirstName("Rod");
        rod.setLastName("Johnson");

        Book noEJB = new Book();
        noEJB.setTitle("J2EE Development without EJB");
        noEJB.setIsbn("4563495");

        Author rodSaved = authorRepository.save(rod);
        Book noEJBSaved = bookRepository.save(noEJB);

        //Build Associations
        ericSaved.getBooks().add(dddSaved);
        rodSaved.getBooks().add(noEJBSaved);

        //Build Book to author association/relationship
        // for this to reflect in the join table
        // both sides of the assoc is necessary

        ddd.getAuthors().add(ericSaved);
        noEJBSaved.getAuthors().add(rodSaved);

        Publisher penguin = new Publisher();
        penguin.setPublisherName("Penguin");
        penguin.setAddress("1562 Eastern");
        penguin.setCity("Chicago");
        penguin.setZip("12345");

        Publisher penguinSaved = publisherRepository.save(penguin);

        dddSaved.setPublisher(penguinSaved);
        noEJBSaved.setPublisher(penguinSaved);

        authorRepository.save(ericSaved);
        authorRepository.save(rodSaved);
        bookRepository.save(dddSaved);
        bookRepository.save(noEJBSaved);

        System.out.println("In Bootstrap");
        System.out.println("Author count: " + authorRepository.count());
        System.out.println("Book count: " + bookRepository.count());
        System.out.println("Publisher count: " + publisherRepository.count());
    }
}
