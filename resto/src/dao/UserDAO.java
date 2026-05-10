package dao;
import model.User;
import model.Role;
import util.SingletonConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements Idao<User> {

    Connection cnx = SingletonConnection.getConnection();

    @Override
    public void insert(User u) {
        String sql = "INSERT INTO user (name, motdepasse, role) VALUES (?,?,?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, u.getName());
            ps.setString(2, u.getMotdepasse());
            ps.setString(3, u.getRole().name());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) u.setId(rs.getInt(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(User u) {
        String sql = "UPDATE user SET name=?, motdepasse=?, role=? WHERE id=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setString(1, u.getName());
            ps.setString(2, u.getMotdepasse());
            ps.setString(3, u.getRole().name());
            ps.setInt(4, u.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(User u) {
        String sql = "DELETE FROM user WHERE id=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, u.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findById(int id) {
        String sql = "SELECT * FROM user WHERE id=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("motdepasse"),
                    Role.valueOf(rs.getString("role"))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM user";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                list.add(new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("motdepasse"),
                    Role.valueOf(rs.getString("role"))
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public User findByUsernameAndPassword(String name, String password) {
        String sql = "SELECT * FROM user WHERE name=? AND motdepasse=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("motdepasse"),
                    Role.valueOf(rs.getString("role"))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}