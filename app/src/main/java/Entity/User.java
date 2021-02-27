package Entity;

public class User {
    private long id;
    private String email;
    private String passwd;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() { return email; }
}
