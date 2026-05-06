package controller;

import dao.PlatDAO;
import model.Plat;

import java.util.List;

public class PlatController {

    private PlatDAO platDAO = new PlatDAO();

    public void ajouterPlat(Plat p) {
        platDAO.insert(p);
    }

    public void modifierPlat(Plat p) {
        platDAO.update(p);
    }

    public void supprimerPlat(int id) {
        Plat p = platDAO.findById(id);
        if (p != null) platDAO.delete(p);
    }

    public List<Plat> getAllPlats() {
        return platDAO.getAll();
    }

    public Plat getPlatById(int id) {
        return platDAO.findById(id);
    }
}