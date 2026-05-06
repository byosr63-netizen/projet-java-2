package model;

public class User {
    private int id;
    private String name;
    private String motdepasse;
    private Role role; // ENUM

    public User() {}

    public User(int id, String name, String motdepasse, Role role) {
        this.id = id;
        this.name = name;
        this.motdepasse = motdepasse;
        this.role = role;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMotdepasse() { return motdepasse; }
    public void setMotdepasse(String motdepasse) { this.motdepasse = motdepasse; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}