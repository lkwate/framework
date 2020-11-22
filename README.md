# lado
Object-Oriented Framework designed to efficiently operate on a Relational Database System. This framework is generic enough to interact with any RDBS without writing any code line and setting up with a few configurations. It automatically generates the form necessary for CRUD operations and takes care about the relations between the tables in the DB. That said, it automatically fills the dropdown list for foreign keys, automatically provides the way to navigate through all the tables related to a given one. It gives a friendly way to set up a multi-user system, it is easy with it to define the permission of a given user without any code line written...

# Getting Started
### Prerequisites
```
Java 9
Mysql 
```
### Create database
Launch Mysql server and execute the following script
```
* lado/database/securite Create.dll
* lado/database/insert.sql
```

### Modify the basic configurations
```
lado/config.properties
```
### Execution
```
git clone https://github.com/lkwate/lado.git
cd lado/out/artifacts/lado_jar
java -jar lado.jar
```

### Some show cases
#### login
<img src="https://github.com/lkwate/lado/blob/master/images/login-image.png" alt="lgoing" width="300" height="300"/>
#### Creation of a new user
<img src="https://github.com/lkwate/lado/blob/master/images/user-list.png" alt="user list" width="300" height="300" />
#### attempting to access to a rubric without a permession
<img src="https://github.com/lkwate/lado/blob/master/images/access-denied.png" alt="permission" width="400" height="400" />
#### Creation of operation
<img src="https://github.com/lkwate/lado/blob/master/images/operation-creation.png" alt="operation creation" width="300" height="300" />
#### Creation of Permission
<img src="https://github.com/lkwate/lado/blob/master/images/permission-creation.png" alt"permission createion" width="400" height="300" />
