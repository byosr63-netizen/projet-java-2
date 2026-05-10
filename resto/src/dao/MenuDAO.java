package dao;

import model.Menu;
import util.SingletonConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {

    Connection cnx = SingletonConnection.getConnection();

  
    public void insert(Menu m) {

        String sql = "INSERT INTO menu (nom, description) VALUES (?, ?)";

        try {
            PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, m.getNom());
            ps.setString(2, m.getDescription());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                m.setIdmenu(rs.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 
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

 
    public Menu findById(int id) {

        String sql = "SELECT * FROM menu WHERE idmenu=?";

        try {

            PreparedStatement ps = cnx.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Menu m = new Menu();

                m.setIdmenu(rs.getInt("idmenu"));
                m.setNom(rs.getString("nom"));
                m.setDescription(rs.getString("description"));

                return m;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

   
    public List<Menu> getAll() {

        List<Menu> list = new ArrayList<>();

        String sql = "SELECT * FROM menu";

        try {

            Statement st = cnx.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                Menu m = new Menu();

                m.setIdmenu(rs.getInt("idmenu"));
                m.setNom(rs.getString("nom"));
                m.setDescription(rs.getString("description"));

                list.add(m);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}