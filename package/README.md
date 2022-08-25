# Configuring and running application

## Configuring application

Before starting application need to be configured via application.properies file. Properies are explained below. You have to set properties in next sections:
    - Superuser login and email
    - LDAP properies
    - HTTPS properties
    - Mail server settings
For other properties you can use default values.

Another thing you have to do before starting application is placing your server certificate file in PKCS12 format in ./keystore folder and set it in the corresponding property.

### Superuser login and email

Set superuser (admin user) login and email:
    superuser-param-name
    superuser-param-email

### Repository configuration

By default application uses embeded H2-based repository to host its metadata. 

Database URL:
    spring.datasource.url
By default H2 db files is placed in ./db folder:
    spring.datasource.url=jdbc:h2:file:./db/db
To change it change the path in the property. The folder doesn't need to exist - it would be created after first start (check the file system permition for the location).

    spring.datasource.initialization-mode=always
Don't change - this setting ensures creation of repository schema on first launch.

JDBC dirver clss name:
    spring.datasource.driverClassName
For H2 it is always:
    spring.datasource.driverClassName=org.h2.Driver
    
DB user settings:
    spring.datasource.username
    spring.datasource.password    
For H2-based repository before first time starting application on new repository you can set username and password and it would be the superuser account. By default it is
    spring.datasource.username=sa
    spring.datasource.password=
    
H2 DB connection pool size:
    spring.datasource.hikari.maximum-pool-size
By default:
    spring.datasource.hikari.maximum-pool-size=50
    
H2 web console enabling and disabling:
    spring.h2.console.enabled
By default it is enabled:
    spring.h2.console.enabled=true
Allowing and denying connection to H2 web console from remote machine:
    spring.h2.console.settings.web-allow-others
By default it is allowed:
    spring.h2.console.settings.web-allow-others=true
H2 web console URL is
    <protocol>//<server host and port>/h2-console
    
You can set other existing RDBMS to host repository. In this case you have to set properties:
    spring.datasource.url
    spring.datasource.driverClassName
    spring.datasource.username
    spring.datasource.password
    
### JPA and Hibernate properties
Name of the default repository schema: 
    spring.jpa.properties.hibernate.default_schema=REPOSITORY

Disable autocreation of schema objects based on entities definition:
    spring.jpa.hibernate.ddl-auto=none

Dialect of repository RDBMS (H2 by default):
    spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

Disable implicit controller-wide transactions:
    spring.jpa.open-in-view=false

Disable sql query logging:
    spring.jpa.show-sql=false

### Logging options

Log file name and location:
    logging.file.name
By default it is:
    logging.file.name=./log/magreport.log
But you can place it to your common log location folder.

### LDAP properties

Magreport uses only one way of user authentication -- **LDAP-based authentication**. No "inner" users. We believe that this approach corresponds enterprise-level applications best practices
and cuts off all the questions concerning personal data protection. Of course there are no default values for these properties.

LDAP URL:
    spring.ldap.url
For example:
    spring.ldap.url=ldap://ldap.example.com:389
LDAP search base:
    spring.ldap.base
For example:
    spring.ldap.base=DC=example,DC=com
LDAP domain:
    spring.ldap.domain=
For example:
    spring.ldap.domain=example.com
LDAP type:
    AD or LDAP
For example:
    ldap-settings.type=AD or ldap-settings.type=LDAP
Group-path:
    ldap-settings.group-path
For example:
    ldap-settings.group-path=
User-path:
    ldap-settings.user-path
For example:
    ldap-settings.user-path
**Only for type 'LDAP':**
User-template-dn:
    ldap-settings.user-template-dn
For example:
    ldap-settings.user-template-dn=uid={0},DC=example,DC=com


If you have to connect to LDAP via SSL (ldaps protocol) you need to add your LDAP SSL certificate to the java keystore.

### Jwt properties

Jwt token lifetime (in ms - default is 864000000 = 10 days):
    jwt.properties.validityDuration=864000000
Secrete phrase to generate jwt token (default is SecretKeyForIssuingJwtTokens, but it is recommended to change it):
    jwt.properties.secretKey=SecretKeyForIssuingJwtTokens

### HTTPS properties

Keystore file in PKCS12 format with server SSL certificate:
    server.ssl.key-store=
Keystore file password:
    server.ssl.key-store-password=
The alias mapped to the certificate
    server.ssl.key-alias=
Server port (default is 443):
    server.port=443
    
### Excel template settings

Report Excel template folder. Added report templates is stored here. Default is ./excel-templates:
    magreport.excel-template.folder=./excel-templates

### Reports folder

Folders to place report data files and exportes Excel files. You should be care of enough disk space for these files.

Folder for report data files, exported from DB, in AVRO format (default is ./data):
    magreport.reports.folder=./data
Income folder for exported Excel files (default is ./export):
    magreport.reports.rms-in-folder=./export
Outcome folder for exported Excel files (default is ./export):    
    magreport.reports.rms-out-folder=./export
   
You can set the same folder for rms-in-folder and rms-out-folder - as by default - if you don't assume any intermediate file processing. Or you can make Magreport interact with 
third-party service for proccessing files (for example with some kind of Right Management Service) via these folders - service should get files from rms-in-folder and place processed 
files to rms-out-folder.

### JobEngine properties

Maximum number of concurent jobs (default is 10):
    magreport.jobengine.thread-pool-size=10
Maximum number of rows in job result (default is 1000000):
    magreport.jobengine.max-rows=1000000
Maximum number of concurent exporting jobs (default is 20):
    magreport.mvc.excel-exporter.thread-pool-size=20
    
    
### ASM settings

Schedule for ASM refresh jobs (refreshes all ASM objects) in cron notation (default is every day at 7AM ):
    magreport.asm.refresh-schedule=0 0 7 * * *
    
### Job history clearing
Schedule for system job of deleting user jobs results (default is every day at 23):
    magreport.jobengine.history-clear-schedule=0 0 23 * * *
Report job life time in hours (default is 336 hours = 14 days):
    magreport.jobengine.job-retention-time=336
    
### Mail server settings

Mail server settings

Protocol (default emailServerProtocol):
    spring.mail.protocol=emailServerProtocol
SMTP-server host:
    spring.mail.host=
SMTP-port:
    mail.port=
User login:
    spring.mail.username=
User password:
    spring.mail.password=
Debug mode (default false):
    mail.debug=false
From address:
    mailAddressFrom=
