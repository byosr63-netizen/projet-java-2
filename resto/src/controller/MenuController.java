package controller;

import dao.MenuDAO;
import model.Menu;

import java.util.List;

public class MenuController {

    private MenuDAO menuDAO = new MenuDAO();

    // ================= AJOUTER =================
    public void ajouterMenu(String nom, String description) {

        Menu menu = new Menu();

        menu.setNom(nom);
        menu.setDescription(description);

        menuDAO.insert(menu);
    }

    // ================= MODIFIER =================
    public void modifierMenu(int id,
                             String nom,
                             String description) {

        Menu menu = new Menu();

        menu.setIdmenu(id);
        menu.setNom(nom);
        menu.setDescription(description);

        menuDAO.update(menu);
    }

    // ================= SUPPRIMER =================
    public void supprimerMenu(int id) {

        Menu menu = menuDAO.findById(id);

        if (menu != null) {
            menuDAO.delete(menu);
        }
    }

    // ================= GET ALL =================
    public List<Menu> getAllMenus() {

        return menuDAO.getAll();
    }

    // ================= GET BY ID =================
    public Menu getMenuById(int id) {

        return menuDAO.findById(id);
    }
}