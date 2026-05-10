package dao;

import model.Commande;
import model.EtatCommande;
import util.SingletonConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeDAO implements Idao<Commande> {

    Connection cnx = SingletonConnection.getConnection();

    @Override
   
    public void insert(Commande c) {

        String sql = "INSERT INTO commande (etat, date_commande, id_client, id_serveur) VALUES (?,?,?,?)";

        try {
            PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, c.getEtat().name());
            ps.setTimestamp(2, Timestamp.valueOf(c.getDateCommande()));
            ps.setInt(3, c.getIdClient());
            ps.setInt(4, c.getIdServeur());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                c.setIdcommande(rs.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Commande c) {
        String sql = "UPDATE commande SET etat=?, date_commande=?, id_client=?, id_serveur=? WHERE idcommande=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setString(1, c.getEtat().name());
            ps.setTimestamp(2, Timestamp.valueOf(c.getDateCommande()));
            ps.setInt(3, c.getIdClient());
            ps.setInt(4, c.getIdServeur());
            ps.setInt(5, c.getIdcommande());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Commande c) {
        String sql = "DELETE FROM commande WHERE idcommande=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, c.getIdcommande());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Commande findById(int id) {
        String sql = "SELECT * FROM commande WHERE idcommande=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Commande(
                        rs.getInt("idcommande"),
                        EtatCommande.valueOf(rs.getString("etat")),
                        rs.getTimestamp("date_commande").toLocalDateTime(),
                        rs.getInt("id_client"),
                        rs.getInt("id_serveur")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Commande> getAll() {
        List<Commande> list = new ArrayList<>();
        String sql = "SELECT * FROM commande";

        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                list.add(new Commande(
                        rs.getInt("idcommande"),
                        EtatCommande.valueOf(rs.getString("etat")),
                        rs.getTimestamp("date_commande").toLocalDateTime(),
                        rs.getInt("id_client"),
                        rs.getInt("id_serveur")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}