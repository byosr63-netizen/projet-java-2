package dao;

import model.Menu;
import util.SingletonConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO implements Idao<Menu> {

    Connection cnx = SingletonConnection.getConnection();

    @Override
    public void insert(Menu m) {
        String sql = "INSERT INTO menu VALUES (?,?,?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, m.getIdmenu());
            ps.setString(2, m.getNom());
            ps.setString(3, m.getDescription());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Menu m) {
        String sql = "UPDATE menu SET nom=?, description=? WHERE idmenu=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setString(1, m.getNom());
            ps.setString(2, m.getDescription());
            ps.setInt(3, m.getIdmenu());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Menu m) {
        String sql = "DELETE FROM menu WHERE idmenu=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, m.getIdmenu());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Menu findById(int id) {
        try {
            PreparedStatement ps = cnx.prepareStatement("SELECT * FROM menu WHERE idmenu=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Menu(rs.getInt(1), rs.getString(2), rs.getString(3));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Menu> getAll() {
        List<Menu> list = new ArrayList<>();
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM menu");

            while (rs.next()) {
                list.add(new Menu(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}