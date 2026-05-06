package dao;

import model.Facture;
import util.SingletonConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FactureDAO implements Idao<Facture> {

    Connection cnx = SingletonConnection.getConnection();

    // ================= INSERT =================
    @Override
    public void insert(Facture f) {

        String sql = "INSERT INTO facture (montant_total, idcommande, date_facture) VALUES (?,?,?)";

        try {
            PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setDouble(1, f.getMontantTotal());
            ps.setInt(2, f.getIdcommande());
            ps.setTimestamp(3, Timestamp.valueOf(f.getDateFacture()));

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                f.setIdfacture(rs.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= UPDATE =================
    @Override
    public void update(Facture f) {

        String sql = "UPDATE facture SET montant_total=?, idcommande=?, date_facture=? WHERE idfacture=?";

        try {
            PreparedStatement ps = cnx.prepareStatement(sql);

            ps.setDouble(1, f.getMontantTotal());
            ps.setInt(2, f.getIdcommande());
            ps.setTimestamp(3, Timestamp.valueOf(f.getDateFacture()));
            ps.setInt(4, f.getIdfacture());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= DELETE =================
    @Override
    public void delete(Facture f) {

        String sql = "DELETE FROM facture WHERE idfacture=?";

        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, f.getIdfacture());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= FIND BY ID =================
    @Override
    public Facture findById(int id) {

        String sql = "SELECT * FROM facture WHERE idfacture=?";

        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Facture(
                        rs.getInt("idfacture"),
                        rs.getDouble("montant_total"),
                        rs.getInt("idcommande"),
                        rs.getTimestamp("date_facture").toLocalDateTime()
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ================= FIND BY COMMANDE =================
    public Facture findByCommandeId(int idCommande) {

        String sql = "SELECT * FROM facture WHERE idcommande=?";

        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, idCommande);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Facture(
                        rs.getInt("idfacture"),
                        rs.getDouble("montant_total"),
                        rs.getInt("idcommande"),
                        rs.getTimestamp("date_facture").toLocalDateTime()
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ================= GET ALL =================
    @Override
    public List<Facture> getAll() {

        List<Facture> list = new ArrayList<>();

        String sql = "SELECT * FROM facture";

        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                list.add(new Facture(
                        rs.getInt("idfacture"),
                        rs.getDouble("montant_total"),
                        rs.getInt("idcommande"),
                        rs.getTimestamp("date_facture").toLocalDateTime()
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}