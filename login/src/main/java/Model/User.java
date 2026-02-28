package Model;
public class User {

        private  String username;
        private  String firstName;
        private  String lastName;
        
        private  String email;
        private  String displayName;
        private  String initials;
        private  String description;
        private  String office;
        private  String telephone;
        private  String webpage;
      
        public User(String username,
                    String firstName,
                    String lastName,
                    String email,
                    String displayName,
                    String initials,
                    String description,
                    String office,
                    String telephone,
                    String webpage) {

            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.displayName = displayName;
            this.initials = initials;
            this.description = description;
            this.office = office;
            this.telephone = telephone;
            this.webpage = webpage;

        }

        public String getUsername()   { return username; }
        public String getFirstName()  { return firstName; }
        public String getLastName()   { return lastName; }
        public String getEmail()      { return email; }
        public String getDisplayName(){ return displayName; }
        public String getInitials()   { return initials; }
        public String getDescription(){ return description; }
        public String getOffice()     { return office; }
        public String getTelephone()  { return telephone; }
        public String getWebpage()    { return webpage; }
    }
