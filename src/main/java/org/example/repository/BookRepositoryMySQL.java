package org.example.repository;

import org.example.model.AudioBook;
import org.example.model.Book;
import org.example.model.EBook;
import org.example.model.builder.AudioBookBuilder;
import org.example.model.builder.BookBuilder;
import org.example.model.builder.EBookBuilder;

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
                "    a.runtime AS audiobook_runtime,\n" +
                "    e.document_type AS ebook_document_type\n" +
                "FROM\n" +
                "    book\n" +
                "        LEFT JOIN\n" +
                "    audiobook a ON book.id = a.book_id\n" +
                "        LEFT JOIN\n" +
                "    ebook e ON book.id = e.book_id";

        List<Book> books = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                if(resultSet.getLong("audiobook_runtime")!=0){
                    books.add(getAudiobookFromResultSet(resultSet));
                }
                else if(resultSet.getString("ebook_document_type")!=null){
                    books.add(getEbookFromResultSet(resultSet));
                }
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return books;
    }
    public List<AudioBook> findAllAudioBook() {
        String sql = "SELECT\n" +
                "    book.id,\n" +
                "    book.author,\n" +
                "    book.title,\n" +
                "    book.publishedDate,\n" +
                "    a.runtime AS audiobook_runtime\n" +
                "FROM\n" +
                "    book\n" +
                "    JOIN\n" +
                "    audiobook a ON book.id = a.book_id\n";

        List<AudioBook> audioBooks = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                audioBooks.add(getAudiobookFromResultSet(resultSet));
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

        return audioBooks;
    }
    public List<EBook> findAllEBook() {
        String sql = "SELECT\n" +
                "    book.id,\n" +
                "    book.author,\n" +
                "    book.title,\n" +
                "    book.publishedDate,\n" +
                "    e.document_type AS ebook_document_type\n" +
                "FROM\n" +
                "    book\n" +
                "         JOIN\n" +
                "    ebook e ON book.id = e.book_id";

        List<EBook> ebooks = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                ebooks.add(getEbookFromResultSet(resultSet));
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        List<Book> books = findAll();
        for(Book book: books){
            for(EBook ebook: ebooks){
                if(book.getId().equals(ebook.getId())){
                    ebook.setAuthor(book.getAuthor());
                    ebook.setTitle(book.getTitle());
                    ebook.setPublishedDate(book.getPublishedDate());
                }
            }
        }
        return ebooks;
    }

    //todo: findAll +fiecare baza de date

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM Book WHERE id = ?";
        Optional<Book> optionalBook = Optional.empty();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);


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
        String sql = "INSERT INTO Book VALUES(null, ?, ?, ?);";
        String sqlAudiobook = "INSERT INTO AudioBook VALUES(?, ?);";
        String sqlEbook = "INSERT INTO Ebook VALUES(?, ?);";
        String sqlLastInserted = "SELECT LAST_INSERT_ID() as last_insert;";
        PreparedStatement preparedStatement;

        try{
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, book.getAuthor());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setDate(3,java.sql.Date.valueOf(book.getPublishedDate()));
            boolean isBookInserted = preparedStatement.execute();

            if(!isBookInserted){
                return false;
            }

            if(book instanceof AudioBook){
                preparedStatement = connection.prepareStatement(sqlLastInserted);
                ResultSet resultSet = preparedStatement.executeQuery(sql);
                if(resultSet.next()){
                    preparedStatement = connection.prepareStatement(sqlAudiobook);
                    preparedStatement.setLong(1, resultSet.getLong("last_insert"));
                    preparedStatement.setLong(2, ((AudioBook) book).getRuntime());

                    return preparedStatement.execute();
                }
            } else if (book instanceof EBook) {
                preparedStatement = connection.prepareStatement(sqlLastInserted);
                ResultSet resultSet = preparedStatement.executeQuery(sql);
                if(resultSet.next()) {
                    preparedStatement = connection.prepareStatement(sqlEbook);
                    preparedStatement.setLong(1, resultSet.getLong("last_insert"));
                    preparedStatement.setString(2, ((EBook) book).getFormat());

                    return preparedStatement.execute();
                }
            }

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

    private EBook getEbookFromResultSet(ResultSet resultSet)throws SQLException {
        return new EBookBuilder()
                .setId(resultSet.getLong("id"))
                .setAuthor(resultSet.getString("author"))
                .setTitle(resultSet.getString("title"))
                .setPublishedDate(new java.sql.Date((resultSet.getDate("publishedDate")).getTime()).toLocalDate())
                .setFormat(resultSet.getString("ebook_document_type"))
                .build();
    }

    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException {
        return new BookBuilder()
                .setId(resultSet.getLong("id"))
                .setAuthor(resultSet.getString("author"))
                .setTitle(resultSet.getString("title"))
                .setPublishedDate(new java.sql.Date((resultSet.getDate("publishedDate")).getTime()).toLocalDate())
                .build();
    }

    private AudioBook getAudiobookFromResultSet(ResultSet resultSet) throws SQLException {
        return new AudioBookBuilder()
                .setId(resultSet.getLong("id"))
                .setAuthor(resultSet.getString("author"))
                .setTitle(resultSet.getString("title"))
                .setPublishedDate(new java.sql.Date((resultSet.getDate("publishedDate")).getTime()).toLocalDate())
                .setRuntime(resultSet.getLong("audiobook_runtime"))
                .build();
    }
}