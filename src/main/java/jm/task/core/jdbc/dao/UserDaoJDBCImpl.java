package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS user (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL," +
                "lastname VARCHAR(255) NOT NULL," +
                "age TINYINT UNSIGNED NOT NULL)";
        Util.executeWithConnection(connection -> {
            Util.executeWithStatement(connection, statement -> {
                statement.execute(sql);
            });
        });
    }

    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS user";
        Util.executeWithConnection(connection -> {
            Util.executeWithStatement(connection, statement -> {
                statement.execute(sql);
            });
        });
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO user (name,lastname,age) VALUES (?,?,?)";
        Util.executeWithConnection(connection -> {
            Util.executeWithPreparedStatement(connection, sql, statement -> {
                statement.setString(1, name);
                statement.setString(2, lastName);
                statement.setByte(3, age);
                statement.executeUpdate();
                System.out.printf("User с именем - %s добавлен в базу данных\n", name);
            });
        });
    }

    public void removeUserById(long id) {
        String sql = "DELETE FROM user WHERE id = ?";
        Util.executeWithConnection(connection -> {
            Util.executeWithPreparedStatement(connection, sql, statement -> {
                statement.setLong(1, id);
                statement.executeUpdate();
            });
        });
    }

    public List<User> getAllUsers() {
        String sql = "SELECT * FROM user";
        List<User> users = new ArrayList<>();
        Util.executeWithConnection(connection -> {
            Util.executeWithStatement(connection, statement -> {
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    while (resultSet.next()) {
                        User addedUser = new User(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("lastname"),
                                resultSet.getByte("age"));
                        users.add(addedUser);
                    }
                }
            });
        });
        return users;
    }

    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE IF EXISTS user";
        Util.executeWithConnection(connection -> {
            Util.executeWithStatement(connection, statement -> {
                statement.execute(sql);
            });
        });
    }
}
