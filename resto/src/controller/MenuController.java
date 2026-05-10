package controller;

import dao.MenuDAO;
import model.Menu;

import java.util.List;

public class MenuController {

    private MenuDAO menuDAO = new MenuDAO();

    
    public void ajouterMenu(String nom, String description) {

        Menu menu = new Menu();

        menu.setNom(nom);
        menu.setDescription(description);

        menuDAO.insert(menu);
    }

   
    public void modifierMenu(int id,
                             String nom,
                             String description) {

        Menu menu = new Menu();

        menu.setIdmenu(id);
        menu.setNom(nom);
        menu.setDescription(description);

        menuDAO.update(menu);
    }

    public void supprimerMenu(int id) {

        Menu menu = menuDAO.findById(id);

        if (menu != null) {
            menuDAO.delete(menu);
        }
    }

 
    public List<Menu> getAllMenus() {

        return menuDAO.getAll();
    }

    public Menu getMenuById(int id) {

        return menuDAO.findById(id);
    }
}