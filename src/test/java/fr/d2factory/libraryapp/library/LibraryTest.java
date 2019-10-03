package fr.d2factory.libraryapp.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.book.ISBN;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.Resident;
import fr.d2factory.libraryapp.member.Student;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class LibraryTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private LibraryImpl library;

    private BookRepository bookRepository;

    private Book[] books;

    private Member member;

    Resident resident;

    Student student;

    @Before
    public void setup() {

        bookRepository = new BookRepository();

        library = new LibraryImpl();

        member = new Member() {
            @Override
            public void payBook(int numberOfDays) {}
        };

        resident = new Resident();

        student = new Student();


        try {

            JSONParser jsonParser = new JSONParser();

            String json = jsonParser.parse(new FileReader("src/test/resources/books.json")).toString();
            ObjectMapper mapper = new ObjectMapper();

            books = mapper.readValue(json, Book[].class);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        bookRepository.addBooks(Arrays.asList(books));
    }

    @Test
    public void member_can_borrow_a_book_if_book_is_available() {
        //Given
        final long isbn1 = 46578964513l;
        final long isbn2 = 3326456467846l;
        final long isbn3 = 968787565445l;
        final long isbn4 = 465789453149l;
        Book expectedBook1 = new Book("Harry Potter","J.K. Rowling",new ISBN(isbn1));
        Book expectedBook2 = new Book("Around the world in 80 days","Jules Verne",new ISBN(isbn2));
        Book expectedBook3 = new Book("Catch 22","Joseph Heller",new ISBN(isbn3));
        Book expectedBook4 = new Book("La peau de chagrin","Balzac",new ISBN(isbn4));

        //When
        Book resultBook1 = bookRepository.findBook(isbn1);
        Book resultBook2 = bookRepository.findBook(isbn2);
        Book resultBook3 = bookRepository.findBook(isbn3);
        Book resultBook4 = bookRepository.findBook(isbn4);

        //Then
        assertNotNull(resultBook1);
        assertNotNull(resultBook2);
        assertNotNull(resultBook3);
        assertNotNull(resultBook4);
        assertEquals(expectedBook1,resultBook1);
        assertEquals(expectedBook2,resultBook2);
        assertEquals(expectedBook3,resultBook3);
        assertEquals(expectedBook4,resultBook4);
        assertEquals(resultBook1.getAuthor(),"J.K. Rowling");
        assertEquals(resultBook1.getTitle(),"Harry Potter");
        assertEquals(resultBook1.getIsbn().getIsbnCode(),46578964513l);
    }

    @Test
    public void borrowed_book_is_no_longer_available(){
        //Given
        final long findedIsbn = 123456789l;
        final long expectedIsbn = 46578964513l;
        Book expectedBook1 = new Book("Harry Potter","J.K. Rowling",new ISBN(expectedIsbn));

        //When
        Book resultBook1 = bookRepository.findBook(findedIsbn);

        //Then
        assertNull(resultBook1);
        assertNotEquals(expectedBook1,resultBook1);
    }

    @Test
    public void residents_are_taxed_10cents_for_each_day_they_keep_a_book(){

        //Given
        resident.setWallet(100l);

        //When
        resident.payBook(20);

        //Then
        assertEquals(resident.getWallet(),98.0,0.0);
    }

    @Test
    public void students_pay_10_cents_the_first_30days(){

        //Given
        student.setWallet(100l);
        student.setStudentFirstYear(false);

        //When
        student.payBook(20);

        //Then
        assertEquals(student.getWallet(),98.0,0.0);
    }

    @Test
    public void students_in_1st_year_are_not_taxed_for_the_first_15days(){

        //Given
        student.setStudentFirstYear(true);
        student.setWallet(100l);

        //When
        student.payBook(10);

        //Then
        assertEquals(student.getWallet(),100.0,0.0);
    }

    @Test
    public void students_in_1st_year_are_taxed_10_cents_between_15days_and_30days(){

        //Given
        student.setStudentFirstYear(true);
        student.setWallet(100l);

        //When
        student.payBook(25);

        //Then
        assertEquals(student.getWallet(),99.0,0.0);
    }

    @Test
    public void students_in_1st_year_are_taxed_150_cents_between_15days_and_30days_and_15_cents_after_30days(){

        //Given
        student.setStudentFirstYear(true);
        student.setWallet(100l);

        //When
        student.payBook(45);

        //Then
        assertEquals(student.getWallet(),96.25,0.0);
    }

    @Test
    public void students_pay_15cents_for_each_day_they_keep_a_book_after_the_initial_30days(){

        //Given
        student.setWallet(100l);
        student.setStudentFirstYear(false);

        //When
        student.payBook(60);

        //Then
        assertEquals(student.getWallet(),92.5,0.0);
    }

    @Test
    public void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days(){

        //Given
        resident.setWallet(100l);

        //When
        resident.payBook(70);

        //Then
        assertEquals(resident.getWallet(),92.0,0.0);
    }

    @Test
    public void members_cannot_borrow_book_if_they_have_late_books(){

        expectedException.expect(HasLateBooksException.class);

        //Given
        final long isbn = 46578964513l;
        final LocalDate localDate = LocalDate.of(2017, Month.AUGUST, 15);

        //Then
        library.borrowBook(isbn,member,localDate);

    }
}
