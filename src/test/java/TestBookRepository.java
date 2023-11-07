import org.example.database.JDBConnectionWrapper;
import org.example.model.Book;
import org.example.model.builder.BookBuilder;
import org.example.repository.BookRepositoryMySQL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestBookRepository {
    @BeforeEach
    public void reinit() {
        JDBConnectionWrapper connectionWrapper = new JDBConnectionWrapper("test_library");


        String sql = "DROP DATABASE IF EXISTS test_library;" +
                "CREATE DATABASE test_library;" +
                "USE test_library;" +
                "CREATE TABLE book" +
                " (id BIGINT auto_increment not null unique primary key," +
                "author varchar(50) not null," +
                "title varchar(50) not null, " +
                "publishedDate Date unique not null);" +
                "INSERT INTO book VALUES(null, 'Ala Bala', 'Portocala','2022-2-2' );" +
                "INSERT INTO book VALUES(null, 'Maria', 'Galbeneaua', '2022-3-3');" +
                "INSERT INTO book VALUES(null, 'Codrin', 'A little ship', '2022-4-4');";
        try {
            Statement statement = connectionWrapper.getConnection().createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static List<Arguments> findAllArg() {
        List<Arguments> a = new ArrayList<>();
        List<Book> expected = new ArrayList<>();
        expected.add(new BookBuilder().setAuthor("Ala Bala").setTitle("Portocala").setPublishedDate(LocalDate.of(2022, 2, 2)).build());
        expected.add(new BookBuilder().setAuthor("Maria").setTitle("Galbeneaua").setPublishedDate(LocalDate.of(2022, 3, 3)).build());
        expected.add(new BookBuilder().setAuthor("Codrin").setTitle("A little ship").setPublishedDate(LocalDate.of(2022, 4, 4)).build());
        a.add(Arguments.of(expected));
        return a;
    }
    private static List<Arguments> removeAllArg() {
        List<Arguments> a = new ArrayList<>();
        List<Book> expected = new ArrayList<>();
        a.add(Arguments.of());
        return a;
    }

    @ParameterizedTest
    @MethodSource("findAllArg")
    public void test_findAll(List<Book> expected) {
        BookRepositoryMySQL b = new BookRepositoryMySQL(new JDBConnectionWrapper("test_library").getConnection());
        Assertions.assertArrayEquals(expected.toArray(), b.findAll().toArray());
    }
    @ParameterizedTest
    @MethodSource("findAllArg")
    public void test_removeall() {
        BookRepositoryMySQL b = new BookRepositoryMySQL(new JDBConnectionWrapper("test_library").getConnection());
        b.removeAll();
        Assertions.assertArrayEquals(new ArrayList<Book>().toArray(), b.findAll().toArray());
    }

}
