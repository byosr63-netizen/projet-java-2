package dao;

import model.Plat;
import util.SingletonConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlatDAO implements Idao<Plat> {

    Connection cnx = SingletonConnection.getConnection();

    @Override
    public void insert(Plat p) {
        String sql = "INSERT INTO plat VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, p.getIdplat());
            ps.setString(2, p.getNom());
            ps.setDouble(3, p.getPrix());
            ps.setBoolean(4, p.isDisponible());
            ps.setInt(5, p.getIdmenu());
            ps.setString(6, p.getImage());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
   
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

    @Override
    public void delete(Plat p) {
        String sql = "DELETE FROM plat WHERE idplat=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, p.getIdplat());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Plat findById(int id) {
        try {
            PreparedStatement ps = cnx.prepareStatement("SELECT * FROM plat WHERE idplat=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Plat(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getDouble(3),
                        rs.getBoolean(4),
                        rs.getInt(5),
                        rs.getString(6)
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Plat> getAll() {
        List<Plat> list = new ArrayList<>();
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM plat");

            while (rs.next()) {
                list.add(new Plat(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getDouble(3),
                        rs.getBoolean(4),
                        rs.getInt(5),
                        rs.getString(6)
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}