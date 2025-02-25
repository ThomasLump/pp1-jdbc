package jm.task.core.jdbc.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Util {
    // реализуйте настройку соеденения с БД
    private static final DataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:7777/mysql");
        config.setUsername("root");
        config.setPassword("");
        dataSource = new HikariDataSource(config);
    }

    public interface SQLConsumer<T> {
        void accept(T t) throws SQLException;
    }

    public interface SQLFunction<T, R> {
        R apply(T t) throws SQLException;
    }

    public static void executeWithConnection(SQLConsumer<Connection> cons) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            cons.accept(connection);
        } catch (Exception e) {
        }
    }


    public static void executeWithStatement(Connection connection, SQLConsumer<Statement> cons) {
        try (Statement statement = connection.createStatement()) {
            cons.accept(statement);
            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {}
        }
    }

    public static void executeWithPreparedStatement(Connection connection, String sql, SQLConsumer<PreparedStatement> cons) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            cons.accept(statement);
            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {}
        }
    }


}
