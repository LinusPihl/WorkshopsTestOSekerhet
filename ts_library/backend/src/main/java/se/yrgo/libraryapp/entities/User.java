package se.yrgo.libraryapp.entities;

public class User {
    private UserId id;
    private String name;
    private String realname;
    private String passwordHash; // New field to hold the password hash

    public User(UserId id, String name, String realname, String passwordHash) {
        this.id = id;
        this.name = name;
        this.realname = realname;
        this.passwordHash = passwordHash;
    }

    public User(UserId of, String user, String passwordHash2) {
        //TODO Auto-generated constructor stub
    }

    public UserId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRealname() {
        return realname;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((realname == null) ? 0 : realname.hashCode());
        result = prime * result + ((passwordHash == null) ? 0 : passwordHash.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (realname == null) {
            if (other.realname != null)
                return false;
        } else if (!realname.equals(other.realname))
            return false;
        if (passwordHash == null) {
            if (other.passwordHash != null)
                return false;
        } else if (!passwordHash.equals(other.passwordHash))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", user=" + name + ", realname=" + realname + ", passwordHash=" + passwordHash + "]";
    }
}
