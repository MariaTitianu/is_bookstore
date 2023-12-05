package org.example.repository.book;

import org.example.model.book.Book;
import org.example.model.builder.BookBuilder;
import org.example.model.validator.Notification;

import java.rmi.NoSuchObjectException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMySQL implements BookRepository {
    private final Connection connection;

    public BookRepositoryMySQL(Connection connection){
        this.connection = connection;
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT\n" +
                "    book.id,\n" +
                "    book.author,\n" +
                "    book.title,\n" +
                "    book.publishedDate,\n" +
                "    book.quantity\n"+
                "    FROM\n" +
                "    book\n";

        List<Book> books = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                books.add(getBookFromResultSet(resultSet));
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM book WHERE id = ?";
        Optional<Book> optionalBook = Optional.empty();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                optionalBook = Optional.of(getBookFromResultSet(resultSet));
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return optionalBook;
    }

    /**
     *
     * How to reproduce a sql injection attack on insert statement
     *
     *
     * 1) Uncomment the lines below and comment out the PreparedStatement part
     * 2) For the Insert Statement DROP TABLE SQL Injection attack to succeed we will need multi query support to be added to our connection
     * Add to JDBConnectionWrapper the following flag after the DB_URL + schema concatenation: + "?allowMultiQueries=true"
     * 3) book.setAuthor("', '', null); DROP TABLE book; -- "); // this will delete the table book
     * 3*) book.setAuthor("', '', null); SET FOREIGN_KEY_CHECKS = 0; SET GROUP_CONCAT_MAX_LEN=32768; SET @tables = NULL; SELECT GROUP_CONCAT('`', table_name, '`') INTO @tables FROM information_schema.tables WHERE table_schema = (SELECT DATABASE()); SELECT IFNULL(@tables,'dummy') INTO @tables; SET @tables = CONCAT('DROP TABLE IF EXISTS ', @tables); PREPARE stmt FROM @tables; EXECUTE stmt; DEALLOCATE PREPARE stmt; SET FOREIGN_KEY_CHECKS = 1; --"); // this will delete all tables. You are not required to know the table name anymore.
     * 4) Run the program. You will get an exception on findAll() method because the test_library.book table does not exist anymore
     */


    // ALWAYS use PreparedStatement when USER INPUT DATA is present
    // DON'T CONCATENATE Strings!

    @Override
    public boolean save(Book book) {
        String sql = "INSERT INTO Book VALUES(null, ?, ?, ?, ?);";
        PreparedStatement preparedStatement;

        try{
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, book.getAuthor());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setDate(3,java.sql.Date.valueOf(book.getPublishedDate()));
            preparedStatement.setInt(4, book.getQuantity());
            preparedStatement.execute();

            return true;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public void removeAll() {
        String sql = "TRUNCATE book;";

        try{
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public Notification<Book> updateBook(Book book) {
        Notification<Book> notifyBook = new Notification<>();
        String sql = "UPDATE book set title = ?,author=?,publishedDate = ?, quantity = ? WHERE id = ? ";
        PreparedStatement preparedStatement;

        try{
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setDate(3,java.sql.Date.valueOf(book.getPublishedDate()));
            preparedStatement.setInt(4, book.getQuantity());
            preparedStatement.setLong(5, book.getId());
            preparedStatement.execute();
            preparedStatement.getResultSet();
            notifyBook.setResult(book);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return notifyBook;
    }

    @Override
    public Notification<Book> deleteBook(Long id) {
        Notification<Book> notifyBook = new Notification<>();
        String sql = "DELETE FROM book WHERE id = ?";
        PreparedStatement preparedStatement;

        try{
            Optional<Book> bookOptional = findById(id);
            if(bookOptional.isPresent()){
                notifyBook.setResult(bookOptional.get());
            }
            else{
                throw new NoSuchObjectException("Cartea cu id-ul " + id + " nu exista!");
            }

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException | NoSuchObjectException e){
            notifyBook.addError(e.toString());
        }

        return notifyBook;
    }

    @Override
    public Notification<Boolean> updateEmployeeActivity(Long userId, Long bookId, Integer bookQuantity) {
        Notification<Boolean> updateEmployeeActivityNotification = new Notification<>();
        String sql = "INSERT INTO employee_sales(id_employee, id_book, quantity) VALUE (?, ?, ?)";
        PreparedStatement preparedStatement;

        try{

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, Math.toIntExact(userId));
            preparedStatement.setInt(2, Math.toIntExact(bookId));
            preparedStatement.setInt(3, bookQuantity);
            preparedStatement.execute();
        } catch (SQLException e){
            updateEmployeeActivityNotification.addError(e.toString());
        }

        return updateEmployeeActivityNotification;
    }

    @Override
    public Notification<List<String>> getEmployeeReport(Long id) {
        Notification<List<String>> reportNotification = new Notification<>();
        List<String> report = new ArrayList<>();
        report.add("Employee " + id + " report: ");
        String getEmployeeReportSql = "SELECT u.username, es.quantity, b.* FROM user u\n" +
                "JOIN employee_sales es on u.id = es.id_employee\n" +
                "JOIN book b on b.id = es.id_book\n" +
                "WHERE u.id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(getEmployeeReportSql);

            statement.setInt(1, Math.toIntExact(id));

            ResultSet employeeReportResultSet = statement.executeQuery();
            while(employeeReportResultSet.next()){
                report.add("Employee username: " + employeeReportResultSet.getString(1)
                        + ", books sold: " + employeeReportResultSet.getInt(2)
                        + ", book id: " + employeeReportResultSet.getInt(3)
                        + ", book author: " + employeeReportResultSet.getString(4)
                        + ", book title: " + employeeReportResultSet.getString(5)
                        + ", book published date: " + employeeReportResultSet.getDate(6)
                        + ", book remaining quantity: " + employeeReportResultSet.getInt(7));
            }
            reportNotification.setResult(report);

        } catch (SQLException e) {
            reportNotification.addError(e.toString());
        }
        return reportNotification;
    }

    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException {
        return new BookBuilder()
                .setId(resultSet.getLong("id"))
                .setAuthor(resultSet.getString("author"))
                .setTitle(resultSet.getString("title"))
                .setPublishedDate(new java.sql.Date((resultSet.getDate("publishedDate")).getTime()).toLocalDate())
                .setQuantity(resultSet.getInt("quantity"))
                .build();
    }
}