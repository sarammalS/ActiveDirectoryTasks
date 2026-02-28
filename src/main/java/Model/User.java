package Model;

public class User {

    private String username;
    private String firstName;
    private String lastName;
    private String fullName;      
    private String email;
    private String displayName;
    private String initials;
    private String description;
    private String office;
    private String telephone;
    private String webpage;
    private String homePhone;
    private String pager;
    private String mobile;
    private String fax;
    private String ipPhone;
    private String notes;
    private String street;
    private String poBox;
    private String city;
    private String state;
    private String zip;
    private String country;

    private String jobTitle;
    private String department;
    private String company;
    private String manager;

    public User(
            
            String username, String firstName, String lastName, String fullName,
            String email, String displayName, String initials, String description,
            String office, String telephone, String webpage,
            String homePhone, String pager, String mobile,
            String fax, String ipPhone, String notes,
            String street, String poBox, String city,
            String state, String zip, String country,
            String jobTitle, String department, String company, String manager) {

        this.username    = username;
        this.firstName   = firstName;
        this.lastName    = lastName;
        this.fullName    = fullName;
        this.email       = email;
        this.displayName = displayName;
        this.initials    = initials;
        this.description = description;
        this.office      = office;
        this.telephone   = telephone;
        this.webpage     = webpage;
        this.homePhone   = homePhone;
        this.pager       = pager;
        this.mobile      = mobile;
        this.fax         = fax;
        this.ipPhone     = ipPhone;
        this.notes       = notes;
        this.street      = street;
        this.poBox       = poBox;
        this.city        = city;
        this.state       = state;
        this.zip         = zip;
        this.country     = country;
        this.jobTitle    = jobTitle;
        this.department  = department;
        this.company     = company;
        this.manager     = manager;
    }

	public String getUsername() {
		return username;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFullName() {
		return fullName;
	}

	public String getEmail() {
		return email;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getInitials() {
		return initials;
	}

	public String getDescription() {
		return description;
	}

	public String getOffice() {
		return office;
	}

	public String getTelephone() {
		return telephone;
	}

	public String getWebpage() {
		return webpage;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public String getPager() {
		return pager;
	}

	public String getMobile() {
		return mobile;
	}

	public String getFax() {
		return fax;
	}

	public String getIpPhone() {
		return ipPhone;
	}

	public String getNotes() {
		return notes;
	}

	public String getStreet() {
		return street;
	}

	public String getPoBox() {
		return poBox;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZip() {
		return zip;
	}

	public String getCountry() {
		return country;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public String getDepartment() {
		return department;
	}

	public String getCompany() {
		return company;
	}

	public String getManager() {
		return manager;
	}
    
}
    
//    }
//    public String getUsername()    { return username; }
//    public String getFirstName()   { return firstName; }
//    public String getLastName()    { return lastName; }
//    public String getFullName()    { return fullName; }
//    public String getEmail()       { return email; }
//    public String getDisplayName() { return displayName; }
//    public String getInitials()    { return initials; }
//    public String getDescription() { return description; }
//    public String getOffice()      { return office; }
//    public String getTelephone()   { return telephone; }
//    public String getWebpage()     { return webpage; }
//
//    public String getHomePhone()   { return homePhone; }
//    public String getPager()       { return pager; }
//    public String getMobile()      { return mobile; }
//    public String getFax()         { return fax; }
//    public String getIpPhone()     { return ipPhone; }
//    public String getNotes()       { return notes; }
//    public String getStreet()      { return street; }
//    public String getPoBox()       { return poBox; }
//    public String getCity()        { return city; }
//    public String getState()       { return state; }
//    public String getZip()         { return zip; }
//    public String getCountry()     { return country; }
//
//    public String getJobTitle()    { return jobTitle; }
//    public String getDepartment()  { return department; }
//    public String getCompany()     { return company; }
//    public String getManager()     { return manager; }
//}