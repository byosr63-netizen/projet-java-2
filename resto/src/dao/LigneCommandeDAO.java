package dao;

import model.LigneCommande;
import util.SingletonConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LigneCommandeDAO implements Idao<LigneCommande> {

    Connection cnx = SingletonConnection.getConnection();

    public void insert(LigneCommande l) {

        String sql = "INSERT INTO ligcmd (idplat, idcommande, quantite, prix_unitaire) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, l.getIdplat());
            ps.setInt(2, l.getIdcommande());
            ps.setInt(3, l.getQuantite());
            ps.setDouble(4, l.getPrixUnitaire()); // 🔥 IMPORTANT

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void update(LigneCommande l) {
        String sql = "UPDATE ligcmd SET quantite=?, prix_unitaire=? WHERE idplat=? AND idcommande=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, l.getQuantite());
            ps.setDouble(2, l.getPrixUnitaire());
            ps.setInt(3, l.getIdplat());
            ps.setInt(4, l.getIdcommande());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(LigneCommande l) {
        String sql = "DELETE FROM ligcmd WHERE idplat=? AND idcommande=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, l.getIdplat());
            ps.setInt(2, l.getIdcommande());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public LigneCommande findById(int id) {
        // clé composite → pas utilisé
        return null;
    }

    @Override
    public List<LigneCommande> getAll() {

        List<LigneCommande> list = new ArrayList<>();

        String sql = "SELECT * FROM ligcmd";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                LigneCommande l = new LigneCommande(
                        rs.getInt("idcommande"),
                        rs.getInt("idplat"),
                        rs.getInt("quantite"),
                        rs.getDouble("prix_unitaire")
                );

                list.add(l);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}