package dao;

import model.Plat;
import util.SingletonConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlatDAO {

    Connection cnx = SingletonConnection.getConnection();

    // ================= INSERT =================
    public void insert(Plat p) {

        String sql = "INSERT INTO plat (nom, prix, disponible, idmenu, image) VALUES (?,?,?,?,?)";

        try {

            PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, p.getNom());
            ps.setDouble(2, p.getPrix());
            ps.setBoolean(3, p.isDisponible());
            ps.setInt(4, p.getIdmenu());
            ps.setString(5, p.getImage());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                p.setIdplat(rs.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= UPDATE =================
    public void update(Plat p) {

        String sql = "UPDATE plat SET nom=?, prix=?, disponible=?, idmenu=?, image=? WHERE idplat=?";

        try {

            PreparedStatement ps = cnx.prepareStatement(sql);

            ps.setString(1, p.getNom());
            ps.setDouble(2, p.getPrix());
            ps.setBoolean(3, p.isDisponible());
            ps.setInt(4, p.getIdmenu());
            ps.setString(5, p.getImage());
            ps.setInt(6, p.getIdplat());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= DELETE =================
    public void delete(int id) {

        String sql = "DELETE FROM plat WHERE idplat=?";

        try {

            PreparedStatement ps = cnx.prepareStatement(sql);

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= GET ALL =================
    public List<Plat> getAll() {

        List<Plat> list = new ArrayList<>();

        String sql = "SELECT * FROM plat";

        try {

            Statement st = cnx.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                Plat p = new Plat();

                p.setIdplat(rs.getInt("idplat"));
                p.setNom(rs.getString("nom"));
                p.setPrix(rs.getDouble("prix"));
                p.setDisponible(rs.getBoolean("disponible"));
                p.setIdmenu(rs.getInt("idmenu"));
                p.setImage(rs.getString("image"));

                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= FIND BY ID =================
    public Plat findById(int id) {

        String sql = "SELECT * FROM plat WHERE idplat=?";

        try {

            PreparedStatement ps = cnx.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Plat p = new Plat();

                p.setIdplat(rs.getInt("idplat"));
                p.setNom(rs.getString("nom"));
                p.setPrix(rs.getDouble("prix"));
                p.setDisponible(rs.getBoolean("disponible"));
                p.setIdmenu(rs.getInt("idmenu"));
                p.setImage(rs.getString("image"));

                return p;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}