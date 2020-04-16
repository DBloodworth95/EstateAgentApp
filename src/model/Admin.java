package model;
import java.io.Serializable;
import java.util.List;

public class Admin implements Serializable {
    private String username;
    private String password;
    private String accessLevel;
    private List<Branch> branchList;

    public Admin(String username, String password, String accessLevel, List<Branch> branchList) {
        this.username = username;
        this.password = password;
        this.accessLevel = accessLevel;
        this.branchList = branchList;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public List<Branch> getPropertyList() {
        return branchList;
    }

    public void setPropertyList(List<Branch> branchList) {
        this.branchList = branchList;
    }
}
