package model;

public class Session {
    private Admin admin;
    private Branch branch;

    public Session(Admin admin, Branch branch) {
        this.admin = admin;
        this.branch = branch;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public boolean isAdmin() {
        return branch != null;
    }
}
