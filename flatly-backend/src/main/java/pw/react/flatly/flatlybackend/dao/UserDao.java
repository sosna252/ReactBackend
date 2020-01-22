package pw.react.flatly.flatlybackend.dao;

import pw.react.flatly.flatlybackend.model.User;

import java.util.List;

public interface UserDao {
    List<User> findAll();
    void insertUser(User usr);
    void updateUser(User usr);
    void executeUpdateUser(User usr);
    void deleteUser(User usr);
}
