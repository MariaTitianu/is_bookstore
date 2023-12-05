package org.example.service.book;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.example.model.book.Book;
import org.example.model.validator.Notification;
import org.example.repository.book.BookRepository;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Book with id: %d not found".formatted(id)));
    }

    @Override
    public Notification<Book> decrementByAmount(long bookId, int amount) {
        Notification<Book> notification = new Notification<>();
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if(bookOptional.isPresent()) {
            Book book = bookOptional.get();
            if(book.getQuantity() - amount >= 0) {
                book.setQuantity(book.getQuantity() - amount);
                notification = bookRepository.updateBook(book);
            }
            else{
                notification.addError("Cannot buy books! (Insufficient quantity)");
            }
        }
        else{
            notification.addError("Cannot buy books! (Insufficient quantity)");
        }
        return notification;
    }

    @Override
    public boolean save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public int getAgeOfBook(Long id) {
        Book book = this.findById(id);

        LocalDate now = LocalDate.now();

        return (int) ChronoUnit.YEARS.between(book.getPublishedDate(), now);
    }

    @Override
    public Notification<Book> update(Book book) {
        return bookRepository.updateBook(book);
    }

    @Override
    public Notification<Book> delete(Long id) {
        return bookRepository.deleteBook(id);
    }

    @Override
    public Notification<Book> sellBook(Long userId, Long bookId, Integer bookQuantity) {
        Notification<Book> decrementBookNotification = decrementByAmount(bookId, bookQuantity);
        if(!decrementBookNotification.hasErrors()){
            Notification<Boolean> updateEmployeeActivityNotification = bookRepository.updateEmployeeActivity(userId, bookId, bookQuantity);
            if(updateEmployeeActivityNotification.hasErrors()) {
                decrementBookNotification.addError(updateEmployeeActivityNotification.getFormattedErrors());
            }
        }
        else{
            decrementBookNotification.addError("Cannot update employee activity! (decrement error)");
        }
        return decrementBookNotification;
    }

    @Override
    public Notification<String> generateEmployeeReport(Long userId) {
        String employeeReportName = "employee" + userId + "Report.pdf";
        Notification<String> employeeReportNotification = new Notification<>();
        Notification<List<String>> reportNotification = bookRepository.getEmployeeReport(userId);
        if(!reportNotification.hasErrors()){
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(employeeReportName));

                document.open();
                Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, BaseColor.BLACK);

                for(String s : reportNotification.getResult()) {
                    Paragraph paragraph = new Paragraph(s, font);
                    document.add(paragraph);
                }

                document.close();

                employeeReportNotification.setResult(employeeReportName);
            } catch (DocumentException | FileNotFoundException e) {
                employeeReportNotification.addError(e.toString());
            }
        }
        else{
            employeeReportNotification.addError(reportNotification.getFormattedErrors());
        }
        return employeeReportNotification;
    }
}