package fr.d2factory.libraryapp.library;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.member.Member;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


public class LibraryImpl implements Library {

    private BookRepository bookRepository;

    public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt)  {

        if( ChronoUnit.DAYS.between(borrowedAt,LocalDate.now()) > 30) {
            throw new HasLateBooksException();
        }

        Book bookFinded = bookRepository.findBook(isbnCode);

        if( bookFinded != null) {
            bookRepository.saveBookBorrow(bookFinded, borrowedAt);
            return bookFinded;
        }
        return null;
    }

    public void returnBook(Book book, Member member){
        member.payBook((int) ChronoUnit.DAYS.between(
                bookRepository.findBorrowedBookDate(book),LocalDate.now()));
    }
}
