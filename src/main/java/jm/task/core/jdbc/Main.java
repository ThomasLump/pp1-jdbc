package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        // реализуйте алгоритм здесь
        UserService service = new UserServiceImpl();
        service.createUsersTable();
        for (int i = 1 ; i < 5 ; i++) {
            service.saveUser("name" + i, "lastname" + i, (byte) 1);
        }
        service.getAllUsers().forEach(System.out::println);
        service.cleanUsersTable();
        service.dropUsersTable();
    }
}
