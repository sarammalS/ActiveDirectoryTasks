# Active Directory User Management Portal

A Java Servlet-based web application for managing users in Microsoft Active Directory via LDAPS.

This project provides a secure administrative interface for performing full user lifecycle operations including creation, update, deletion, password reset, and search.

---

## Overview

This application integrates with Microsoft Active Directory using JNDI (Java Naming and Directory Interface) over LDAPS (SSL, port 636).

It is designed for internal administrative environments where domain users are delegated permission to manage objects within a specific Organizational Unit (OU).

---

## Features

* Domain authentication via LDAP bind
* Create Active Directory users
* Update user attributes
* Delete users
* Reset passwords
* Enable / disable accounts
* List and search users
* Session-based authentication
* Rollback handling on failed operations

---

## Technology Stack

* Java (Servlet API)
* JSP
* JNDI (javax.naming.ldap)
* Microsoft Active Directory
* LDAPS (SSL – port 636)
* Apache Tomcat

---

## Project Structure

```
src/
 ├── servlet/
 │     ├── LoginServlet.java
 │     ├── CreateUserServlet.java
 │     ├── UpdateUserServlet.java
 │     ├── DeleteUserServlet.java
 │     ├── ResetPasswordServlet.java
 │     ├── ListUsersServlet.java
 │
 ├── util/
 │     └── LdapUtil.java
 │
WebContent/
 ├── login.jsp
 ├── dashboard.jsp
 ├── createUser.jsp
 ├── updateUser.jsp
 ├── confirmation.jsp
 └── error.jsp
```

---

## How It Works

### Authentication

Users authenticate using their domain credentials.
The application attempts an LDAP bind against Active Directory.

If successful:

* A session is created
* User credentials are stored temporarily in session

---

### User Creation Workflow

1. Create user in disabled state (userAccountControl = 544)
2. Set password (unicodePwd, UTF-16LE, quoted)
3. Enable account (userAccountControl = 512)
4. Rollback user if password setting fails

LDAPS is mandatory for password operations.

---

## Configuration

### 1. LDAP Connection (LdapUtil.java)

Ensure LDAPS is used:

```
ldaps://your-domain-controller:636
```

Example environment setup:

```java
env.put(Context.PROVIDER_URL, "ldaps://dc.mydomain.local:636");
env.put(Context.SECURITY_AUTHENTICATION, "simple");
env.put(Context.SECURITY_PRINCIPAL, username);
env.put(Context.SECURITY_CREDENTIALS, password);
```

---

### 2. Base DN

Set your Organizational Unit:

```
OU=Employees,DC=mydomain,DC=local
```

---

### 3. JVM Truststore

The Domain Controller SSL certificate must be trusted by the JVM.

Import certificate:

```
keytool -import -alias adcert -file dc-cert.cer -keystore cacerts
```

---

## Deployment

1. Deploy WAR file to Apache Tomcat
2. Ensure port 636 is open
3. Verify SSL certificate is trusted
4. Ensure delegated AD permissions are configured

---

## Required Active Directory Permissions

The authenticated user must have permission to:

* Create user objects in target OU
* Modify user attributes
* Reset passwords
* Delete users
* Enable/disable accounts

---

## Common Errors

| Error                        | Cause                                |
| ---------------------------- | ------------------------------------ |
| WILL_NOT_PERFORM             | Not using LDAPS                      |
| Constraint violation         | Password does not meet domain policy |
| Unwilling to perform         | Insufficient permissions             |
| User created but error shown | Password modification failed         |

---

## Security Considerations

* Always use LDAPS
* Never log plain text passwords
* Use HTTPS for web interface
* Restrict OU permissions
* Implement role-based access control in production

---

## Future Improvements

* Group management module
* Bulk CSV import
* Role-based access control
* Audit logging
* Spring Boot migration
* REST API interface

---

## License

This project is for educational and internal enterprise use.

---

## Author

Developed as a Java–Active Directory integration project for enterprise user lifecycle management.
