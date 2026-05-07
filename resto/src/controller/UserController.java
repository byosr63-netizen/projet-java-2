package controller;

import dao.UserDAO;
import model.User;

public class UserController {

    private UserDAO userDAO = new UserDAO();
    private User currentUser;

    public boolean login(String name, String password) {

        User u = userDAO.findByUsernameAndPassword(name, password);

        if (u != null) {
            currentUser = u;
            return true;
        }

        return false;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public String getRole() {
        return (currentUser != null) ? currentUser.getRole().name() : null;
    }

    public void logout() {
        currentUser = null;
    }
}