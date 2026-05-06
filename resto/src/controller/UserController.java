package controller;

import dao.UserDAO;
import model.User;

public class UserController {

    private UserDAO userDAO = new UserDAO();
    private User currentUser; // ❌ PAS static

    // ================= LOGIN =================
    public boolean login(String name, String password) {

        User u = userDAO.findByUsernameAndPassword(name, password);

        if (u != null) {
            currentUser = u;
            return true;
        }

        return false;
    }

    // ================= CURRENT USER =================
    public User getCurrentUser() {
        return currentUser;
    }

    // ================= ROLE =================
    public String getRole() {
        return (currentUser != null) ? currentUser.getRole().name() : null;
    }
}