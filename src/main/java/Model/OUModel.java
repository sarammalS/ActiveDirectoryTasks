package Model;
public class OUModel {
    private String name;
    private String dn;   
    public OUModel(String name, String dn) {
        this.name = name;
        this.dn = dn;
    }

    public String getName() { return name; }
    public String getDn() { return dn; }
}