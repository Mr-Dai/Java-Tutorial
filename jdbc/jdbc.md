# JDBC(TM) Database Access

The JDBC™ API was designed to keep simple things simple. This means that the JDBC makes everyday database tasks easy. This trail walks you through examples of using JDBC to execute common SQL statements, and perform other objectives common to database applications.

This trail is divided into these lessons:

- [JDBC Introduction](http://docs.oracle.com/javase/tutorial/jdbc/overview/index.html) Lists JDBC features, describes JDBC Architecture and reviews SQL commands and Relational Database concepts.
- [JDBC Basics](http://docs.oracle.com/javase/tutorial/jdbc/basics/index.html) covers the JDBC API, which is included in the Java™ SE 6 release.

By the end of the first lesson, you will know how to use the basic JDBC API to create tables, insert values into them, query the tables, retrieve the results of the queries, and update the tables. In this process, you will learn how to use simple statements and prepared statements, and you will see an example of a stored procedure. You will also learn how to perform transactions and how to catch exceptions and warnings.

## JDBC Introduction

The JDBC API is a Java API that can access any kind of tabular data, especially data stored in a [Relational Database](#a-relational-database-overview).

JDBC helps you to write Java applications that manage these three programming activities:

1. Connect to a data source, like a database
2. Send queries and update statements to the database
3. Retrieve and process the results received from the database in answer to your query

The following simple code fragment gives a simple example of these three steps:

```java
public void connectToAndQueryDatabase(String username, String password) {

    Connection con = DriverManager.getConnection(
                         "jdbc:myDriver:myDatabase",
                         username,
                         password);

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT a, b, c FROM Table1");

    while (rs.next()) {
        int x = rs.getInt("a");
        String s = rs.getString("b");
        float f = rs.getFloat("c");
    }
}
```

This short code fragment instantiates a `DriverManager` object to connect to a database driver and log into the database, instantiates a `Statement` object that carries your SQL language query to the database; instantiates a `ResultSet` object that retrieves the results of your query, and executes a simple `while` loop, which retrieves and displays those results. It's that simple.

#### JDBC Product Components

JDBC includes four components:

1. **The JDBC API** —  The JDBC™ API provides programmatic access to relational data from the Java™ programming language. Using the JDBC API, applications can execute SQL statements, retrieve results, and propagate changes back to an underlying data source. The JDBC API can also interact with multiple data sources in a distributed, heterogeneous environment.

   The JDBC API is part of the Java platform, which includes the *Java™ Standard Edition* (Java™ SE) and the *Java™ Enterprise Edition* (Java™ EE). The JDBC 4.0 API is divided into two packages: `java.sql` and `javax.sql`. Both packages are included in the Java SE and Java EE platforms.

2. **JDBC Driver Manager** —  The JDBC `DriverManager` class defines objects which can connect Java applications to a JDBC driver. `DriverManager` has traditionally been the backbone of the JDBC architecture. It is quite small and simple.

   The Standard Extension packages `javax.naming` and `javax.sql` let you use a `DataSource` object registered with a *Java Naming and Directory Interface™* (JNDI) naming service to establish a connection with a data source. You can use either connecting mechanism, but using a `DataSource` object is recommended whenever possible.

3. **JDBC Test Suite** —  The JDBC driver test suite helps you to determine that JDBC drivers will run your program. These tests are not comprehensive or exhaustive, but they do exercise many of the important features in the JDBC API.

4. **JDBC-ODBC Bridge** —  The Java Software bridge provides JDBC access via ODBC drivers. Note that you need to load ODBC binary code onto each client machine that uses this driver. As a result, the ODBC driver is most appropriate on a corporate network where client installations are not a major problem, or for application server code written in Java in a three-tier architecture.

This Trail uses the first two of these these four JDBC components to connect to a database and then build a java program that uses SQL commands to communicate with a test Relational Database. The last two components are used in specialized environments to test web applications, or to communicate with ODBC-aware DBMSs.

#### JDBC Architecture

**Two-tier and Three-tier Processing Models**

The JDBC API supports both two-tier and three-tier processing models for database access.

![](http://docs.oracle.com/javase/tutorial/jdbc/overview/intro.anc2.gif)

In the two-tier model, a Java applet or application talks directly to the data source. This requires a JDBC driver that can communicate with the particular data source being accessed. A user's commands are delivered to the database or other data source, and the results of those statements are sent back to the user. The data source may be located on another machine to which the user is connected via a network. This is referred to as a client/server configuration, with the user's machine as the client, and the machine housing the data source as the server. The network can be an intranet, which, for example, connects employees within a corporation, or it can be the Internet.

In the three-tier model, commands are sent to a "middle tier" of services, which then sends the commands to the data source. The data source processes the commands and sends the results back to the middle tier, which then sends them to the user. MIS directors find the three-tier model very attractive because the middle tier makes it possible to maintain control over access and the kinds of updates that can be made to corporate data. Another advantage is that it simplifies the deployment of applications. Finally, in many cases, the three-tier architecture can provide performance advantages.

![](http://docs.oracle.com/javase/tutorial/jdbc/overview/intro.anc1.gif)

Until recently, the middle tier has often been written in languages such as C or C++, which offer fast performance. However, with the introduction of optimizing compilers that translate Java bytecode into efficient machine-specific code and technologies such as Enterprise JavaBeans™, the Java platform is fast becoming the standard platform for middle-tier development. This is a big plus, making it possible to take advantage of Java's robustness, multithreading, and security features.

With enterprises increasingly using the Java programming language for writing server code, the JDBC API is being used more and more in the middle tier of a three-tier architecture. Some of the features that make JDBC a server technology are its support for connection pooling, distributed transactions, and disconnected rowsets. The JDBC API is also what allows access to a data source from a Java middle tier.

#### A Relational Database Overview

A database is a means of storing information in such a way that information can be retrieved from it. In simplest terms, a relational database is one that presents information in tables with rows and columns. A table is referred to as a relation in the sense that it is a collection of objects of the same type (rows). Data in a table can be related according to common keys or concepts, and the ability to retrieve related data from a table is the basis for the term relational database. A Database Management System (DBMS) handles the way data is stored, maintained, and retrieved. In the case of a relational database, a Relational Database Management System (RDBMS) performs these tasks. DBMS as used in this book is a general term that includes RDBMS.

**Integrity Rules**

Relational tables follow certain integrity rules to ensure that the data they contain stay accurate and are always accessible. First, the rows in a relational table should all be distinct. If there are duplicate rows, there can be problems resolving which of two possible selections is the correct one. For most DBMSs, the user can specify that duplicate rows are not allowed, and if that is done, the DBMS will prevent the addition of any rows that duplicate an existing row.

A second integrity rule of the traditional relational model is that column values must not be repeating groups or arrays. A third aspect of data integrity involves the concept of a null value. A database takes care of situations where data may not be available by using a null value to indicate that a value is missing. It does not equate to a blank or zero. A blank is considered equal to another blank, a zero is equal to another zero, but two null values are not considered equal.

When each row in a table is different, it is possible to use one or more columns to identify a particular row. This unique column or group of columns is called a primary key. Any column that is part of a primary key cannot be null; if it were, the primary key containing it would no longer be a complete identifier. This rule is referred to as entity integrity.

The `Employees` table illustrates some of these relational database concepts. It has five columns and six rows, with each row representing a different employee.

<table summary="Employees, sample database table">
<tr>
<th id="h1"><code>Employee_Number</code></th>
<th id="h2"><code>First_name</code></th>
<th id="h3"><code>Last_Name</code></th>
<th id="h4"><code>Date_of_Birth</code></th>
<th id="h5"><code>Car_Number</code></th>
</tr>
<tr>
<td headers="h1">10001</td>
<td headers="h2">Axel</td>
<td headers="h3">Washington</td>
<td headers="h4">28-Aug-43</td>
<td headers="h5">5</td>
</tr>
<tr>
<td headers="h1">10083</td>
<td headers="h2">Arvid</td>
<td headers="h3">Sharma</td>
<td headers="h4">24-Nov-54</td>
<td headers="h5">null</td>
</tr>
<tr>
<td headers="h1">10120</td>
<td headers="h2">Jonas</td>
<td headers="h3">Ginsberg</td>
<td headers="h4">01-Jan-69</td>
<td headers="h5">null</td>
</tr>
<tr>
<td headers="h1">10005</td>
<td headers="h2">Florence</td>
<td headers="h3">Wojokowski</td>
<td headers="h4">04-Jul-71</td>
<td headers="h5">12</td>
</tr>
<tr>
<td headers="h1">10099</td>
<td headers="h2">Sean</td>
<td headers="h3">Washington</td>
<td headers="h4">21-Sep-66</td>
<td headers="h5">null</td>
</tr>
<tr>
<td headers="h1">10035</td>
<td headers="h2">Elizabeth</td>
<td headers="h3">Yamaguchi</td>
<td headers="h4">24-Dec-59</td>
<td headers="h5">null</td>
</tr>
</table>

The primary key for this table would generally be the employee number because each one is guaranteed to be different. (A number is also more efficient than a string for making comparisons.) It would also be possible to use `First_Name` and `Last_Name` because the combination of the two also identifies just one row in our sample database. Using the last name alone would not work because there are two employees with the last name of "Washington." In this particular case the first names are all different, so one could conceivably use that column as a primary key, but it is best to avoid using a column where duplicates could occur. If Elizabeth Yamaguchi gets a job at this company and the primary key is `First_Name`, the RDBMS will not allow her name to be added (if it has been specified that no duplicates are permitted). Because there is already an Elizabeth in the table, adding a second one would make the primary key useless as a way of identifying just one row. Note that although using `First_Name` and `Last_Name` is a unique composite key for this example, it might not be unique in a larger database. Note also that the `Employee` table assumes that there can be only one car per employee.

**SELECT Statements**

SQL is a language designed to be used with relational databases. There is a set of basic SQL commands that is considered standard and is used by all RDBMSs. For example, all RDBMSs use the `SELECT` statement.

A `SELECT` statement, also called a query, is used to get information from a table. It specifies one or more column headings, one or more tables from which to select, and some criteria for selection. The RDBMS returns rows of the column entries that satisfy the stated requirements. A `SELECT` statement such as the following will fetch the first and last names of employees who have company cars:

```sql
SELECT First_Name, Last_Name
FROM Employees
WHERE Car_Number IS NOT NULL
```

The result set (the set of rows that satisfy the requirement of not having null in the `Car_Number` column) follows. The first name and last name are printed for each row that satisfies the requirement because the `SELECT` statement (the first line) specifies the columns `First_Name` and `Last_Name`. The `FROM` clause (the second line) gives the table from which the columns will be selected.

<table summary="Result set of not having null in the Car_Number column">
<tr>
<th id="h101">FIRST_NAME</th>
<th id="h102">LAST_NAME</th>
</tr>
<tr>
<td headers="h101">Axel</td>
<td headers="h102">Washington</td>
</tr>
<tr>
<td headers="h101">Florence</td>
<td headers="h102">Wojokowski</td>
</tr>
</table>

The following code produces a result set that includes the whole table because it asks for all of the columns in the table Employees with no restrictions (no `WHERE` clause). Note that `SELECT *` means "SELECT all columns."

```sql
SELECT *
FROM Employees
```

**WHERE Clauses**

The `WHERE` clause in a `SELECT` statement provides the criteria for selecting values. For example, in the following code fragment, values will be selected only if they occur in a row in which the column `Last_Name` begins with the string 'Washington'.

```sql
SELECT First_Name, Last_Name
FROM Employees
WHERE Last_Name LIKE 'Washington%'
```

The keyword `LIKE` is used to compare strings, and it offers the feature that patterns containing wildcards can be used. For example, in the code fragment above, there is a percent sign (`%`) at the end of 'Washington', which signifies that any value containing the string 'Washington' plus zero or more additional characters will satisfy this selection criterion. So 'Washington' or 'Washingtonian' would be matches, but 'Washing' would not be. The other wildcard used in `LIKE` clauses is an underbar (`_`), which stands for any one character. For example,

```sql
WHERE Last_Name LIKE 'Ba_man'
```

would match 'Batman', 'Barman', 'Badman', 'Balman', 'Bagman', 'Bamman', and so on.

The code fragment below has a `WHERE` clause that uses the equal sign (`=`) to compare numbers. It selects the first and last name of the employee who is assigned car 12.

```sql
SELECT First_Name, Last_Name
FROM Employees
WHERE Car_Number = 12
```

The next code fragment selects the first and last names of employees whose employee number is greater than 10005:

```sql
SELECT First_Name, Last_Name
FROM Employees
WHERE Employee_Number > 10005
```

`WHERE` clauses can get rather elaborate, with multiple conditions and, in some DBMSs, nested conditions. This overview will not cover complicated `WHERE` clauses, but the following code fragment has a `WHERE` clause with two conditions; this query selects the first and last names of employees whose employee number is less than 10100 and who do not have a company car.

```sql
SELECT First_Name, Last_Name
FROM Employees
WHERE Employee_Number < 10100 and Car_Number IS NULL
```

A special type of `WHERE` clause involves a join, which is explained in the next section.

**Joins**

A distinguishing feature of relational databases is that it is possible to get data from more than one table in what is called a join. Suppose that after retrieving the names of employees who have company cars, one wanted to find out who has which car, including the make, model, and year of car. This information is stored in another table, `Cars`:

<table summary="Cars table">
<tr>
<th id="h201"><code>Car_Number</code></th>
<th id="h202"><code>Make</code></th>
<th id="h203"><code>Model</code></th>
<th id="h204"><code>Year</code></th>
</tr>
<tr>
<td headers="h201">5</td>
<td headers="h202">Honda</td>
<td headers="h203">Civic DX</td>
<td headers="h204">1996</td>
</tr>
<tr>
<td headers="h201">12</td>
<td headers="h202">Toyota</td>
<td headers="h203">Corolla</td>
<td headers="h204">1999</td>
</tr>
</table>

There must be one column that appears in both tables in order to relate them to each other. This column, which must be the primary key in one table, is called the foreign key in the other table. In this case, the column that appears in two tables is `Car_Number`, which is the primary key for the table `Cars` and the foreign key in the table Employees. If the 1996 Honda Civic were wrecked and deleted from the `Cars` table, then `Car_Number` 5 would also have to be removed from the Employees table in order to maintain what is called referential integrity. Otherwise, the foreign key column (`Car_Number`) in the `Employees` table would contain an entry that did not refer to anything in `Cars`. A foreign key must either be null or equal to an existing primary key value of the table to which it refers. This is different from a primary key, which may not be null. There are several null values in the `Car_Number` column in the table `Employees` because it is possible for an employee not to have a company car.

The following code asks for the first and last names of employees who have company cars and for the make, model, and year of those cars. Note that the `FROM` clause lists both Employees and Cars because the requested data is contained in both tables. Using the table name and a dot (.) before the column name indicates which table contains the column.

```sql
SELECT Employees.First_Name, Employees.Last_Name,
    Cars.Make, Cars.Model, Cars.Year
FROM Employees, Cars
WHERE Employees.Car_Number = Cars.Car_Number
```

This returns a result set that will look similar to the following:

<table summary="Result set of first and last names of employees who have company cars and for the make, model, and year of those cars">

<tr>
<th id="h301"><code>FIRST_NAME</code></th>
<th id="h302"><code>LAST_NAME</code></th>
<th id="h303"><code>MAKE</code></th>
<th id="h304"><code>MODEL</code></th>
<th id="h305"><code>YEAR</code></th>
</tr>

<tr>
<td headers="h301">Axel</td>
<td headers="h302">Washington</td>
<td headers="h303">Honda</td>
<td headers="h304">Civic DX</td>
<td headers="h305">1996</td>
</tr>

<tr>
<td headers="h301">Florence</td>
<td headers="h302">Wojokowski</td>
<td headers="h303">Toyota</td>
<td headers="h304">Corolla</td>
<td headers="h305">1999</td>
</tr>

</table>

**Common SQL Commands**

SQL commands are divided into categories, the two main ones being Data Manipulation Language (DML) commands and Data Definition Language (DDL) commands. DML commands deal with data, either retrieving it or modifying it to keep it up-to-date. DDL commands create or change tables and other database objects such as views and indexes.

A list of the more common DML commands follows:

- `SELECT` —  used to query and display data from a database. The `SELECT` statement specifies which columns to include in the result set. The vast majority of the SQL commands used in applications are `SELECT` statements.
- `INSERT` —  adds new rows to a table. `INSERT` is used to populate a newly created table or to add a new row (or rows) to an already-existing table.
- `DELETE` —  removes a specified row or set of rows from a table.
- `UPDATE` —  changes an existing value in a column or group of columns in a table.

The more common DDL commands follow:

- `CREATE TABLE` —  creates a table with the column names the user provides. The user also needs to specify a type for the data in each column. Data types vary from one RDBMS to another, so a user might need to use metadata to establish the data types used by a particular database. `CREATE TABLE` is normally used less often than the data manipulation commands because a table is created only once, whereas adding or deleting rows or changing individual values generally occurs more frequently.
- `DROP TABLE` —  deletes all rows and removes the table definition from the database. A JDBC API implementation is required to support the `DROP TABLE` command as specified by SQL92, Transitional Level. However, support for the `CASCADE` and `RESTRICT` options of `DROP TABLE` is optional. In addition, the behavior of DROP TABLE is implementation-defined when there are views or integrity constraints defined that reference the table being dropped.
- `ALTER TABLE` —  adds or removes a column from a table. It also adds or drops table constraints and alters column attributes

**Result Sets and Cursors**

The rows that satisfy the conditions of a query are called the result set. The number of rows returned in a result set can be zero, one, or many. A user can access the data in a result set one row at a time, and a cursor provides the means to do that. A cursor can be thought of as a pointer into a file that contains the rows of the result set, and that pointer has the ability to keep track of which row is currently being accessed. A cursor allows a user to process each row of a result set from top to bottom and consequently may be used for iterative processing. Most DBMSs create a cursor automatically when a result set is generated.

Earlier JDBC API versions added new capabilities for a result set's cursor, allowing it to move both forward and backward and also allowing it to move to a specified row or to a row whose position is relative to another row.

**Transactions**

When one user is accessing data in a database, another user may be accessing the same data at the same time. If, for instance, the first user is updating some columns in a table at the same time the second user is selecting columns from that same table, it is possible for the second user to get partly old data and partly updated data. For this reason, DBMSs use transactions to maintain data in a consistent state (data consistency) while allowing more than one user to access a database at the same time (data concurrency).

A transaction is a set of one or more SQL statements that make up a logical unit of work. A transaction ends with either a commit or a rollback, depending on whether there are any problems with data consistency or data concurrency. The commit statement makes permanent the changes resulting from the SQL statements in the transaction, and the rollback statement undoes all changes resulting from the SQL statements in the transaction.

A lock is a mechanism that prohibits two transactions from manipulating the same data at the same time. For example, a table lock prevents a table from being dropped if there is an uncommitted transaction on that table. In some DBMSs, a table lock also locks all of the rows in a table. A row lock prevents two transactions from modifying the same row, or it prevents one transaction from selecting a row while another transaction is still modifying it.

**Stored Procedures**

A stored procedure is a group of SQL statements that can be called by name. In other words, it is executable code, a mini-program, that performs a particular task that can be invoked the same way one can call a function or method. Traditionally, stored procedures have been written in a DBMS-specific programming language. The latest generation of database products allows stored procedures to be written using the Java programming language and the JDBC API. Stored procedures written in the Java programming language are bytecode portable between DBMSs. Once a stored procedure is written, it can be used and reused because a DBMS that supports stored procedures will, as its name implies, store it in the database.

The following code is an example of how to create a very simple stored procedure using the Java programming language. Note that the stored procedure is just a static Java method that contains normal JDBC code. It accepts two input parameters and uses them to change an employee's car number.

Do not worry if you do not understand the example at this point. The code example below is presented only to illustrate what a stored procedure looks like. You will learn how to write the code in this example in the tutorials that follow.

```java
import java.sql.*;

public class UpdateCar {

    public static void UpdateCarNum(int carNo, int empNo)
        throws SQLException {

        Connection con = null;
        PreparedStatement pstmt = null;   
      
        try {
            con = DriverManager.getConnection(
                      "jdbc:default:connection");

            pstmt = con.prepareStatement(
                        "UPDATE EMPLOYEES " +
                        "SET CAR_NUMBER = ? " +
                        "WHERE EMPLOYEE_NUMBER = ?");

            pstmt.setInt(1, carNo);
            pstmt.setInt(2, empNo);
            pstmt.executeUpdate();
        }
        finally {
            if (pstmt != null) pstmt.close();
        }
    }
}
```

**Metadata**

Databases store user data, and they also store information about the database itself. Most DBMSs have a set of system tables, which list tables in the database, column names in each table, primary keys, foreign keys, stored procedures, and so forth. Each DBMS has its own functions for getting information about table layouts and database features. JDBC provides the interface `DatabaseMetaData`, which a driver writer must implement so that its methods return information about the driver and/or DBMS for which the driver is written. For example, a large number of methods return whether or not the driver supports a particular functionality. This interface gives users and tools a standardized way to get metadata.

In general, developers writing tools and drivers are the ones most likely to be concerned with metadata.

## JDBC Basics

In this lesson you will learn the basics of the JDBC API.

- [Getting Started](#getting-started) sets up a basic database development environment and shows you how to compile and run the JDBC tutorial samples.
- [Processing SQL Statements with JDBC](#processing-sql-statements-with-jdbc) outlines the steps required to process any SQL statement. The pages that follow describe these steps in more detail:
  + [Establishing a Connection](#establishing-a-connection) connects you to your database.
  + [Connecting with DataSource Objects](#connecting-with-datasource-objects) shows you how to connect to your database with `DataSource` objects, the preferred way of getting a connection to a data source.
  + [Handling SQLExceptions](#handling-sqlexceptions) shows you how to handle exceptions caused by database errors.
  + [Setting Up Tables](#setting-up-tables) describes all the database tables used in the JDBC tutorial samples and how to create and populate tables with JDBC API and SQL scripts.
  + [Retrieving and Modifying Values from Result Sets](#retrieving-and-modifying-values-from-result-sets) develop the process of configuring your database, sending queries, and retrieving data from your database.
  + [Using Prepared Statements](#using-prepared-statements) describes a more flexible way to create database queries.
  + [Using Transactions](#using-transactions) shows you how to control when a database query is actually executed.
- [Using RowSet Objects](#using-rowset-objects) introduces you to `RowSet` objects; these are objects that hold tabular data in a way that make it more flexible and easier to use than result sets. The pages that follow describe the different kinds of `RowSet` objects available:
  + [Using JdbcRowSet Objects](#using-jdbcrowset-objects)
  + [Using CachedRowSet Objects](#using-cachedrowset-objects)
  + [Using JoinRowSet Objects](#using-joinrowset-objects)
  + [Using FilteredRowSet Objects](#using-filteredrowset-objects)
  + [Using WebRowSet Objects](#using-webrowset-objects)
- [Using Advanced Data Types](#using-advanced-data-types) introduces you to other data types; the pages that follow describe these data types in further detail:
  + [Using Large Objects](#using-large-objects)
  + [Using SQLXML Objects](#using-sqlxml-objects)
  + [Using Array Objects](#using-array-objects)
  + [Using DISTINCT Data Type](#using-distinct-data-type)
  + [Using Structured Objects](#using-structured-objects)
  + [Using Customized Type Mappings](#using-customized-type-mappings)
  + [Using Datalink Objects](#using-datalink-objects)
  + [Using RowId Objects](#using-rowid-objects)
- [Using Stored Procedures](#using-stored-procedures) shows you how to create and use a stored procedure, which is a group of SQL statements that can be called like a Java method with variable input and output parameters.
- [Using JDBC with GUI API](#using-jdbc-with-gui-api) demonstrates how to integrate JDBC with the Swing API.

### Getting Started

The sample code that comes with this tutorial creates a database that is used by a proprietor of a small coffee house called The Coffee Break, where coffee beans are sold by the pound and brewed coffee is sold by the cup.

The following steps configure a JDBC development environment with which you can compile and run the tutorial samples:

 1. Install the latest version of the Java SE SDK on your computer
 2. Install your database management system (DBMS) if needed
 3. Install a JDBC driver from the vendor of your database
 4. Install Apache Ant
 5. Install Apache Xalan
 6. Download the sample code
 7. Modify the `build.xml` file
 8. Modify the tutorial properties file
 9. Compile and package the samples
10. Create databases, tables, and populate tables
11. Run the samples

**Install the latest version of the Java SE SDK on your computer**

Install the latest version of the Java SE SDK on your computer.

Ensure that the full directory path of the Java SE SDK `bin` directory is in your `PATH` environment variable so that you can run the Java compiler and the Java application launcher from any directory.

**Install your database management system (DBMS) if needed**

You may use Java DB, which comes with the latest version of Java SE SDK. This tutorial has been tested for the following DBMS:

- [Java DB](http://www.oracle.com/technetwork/java/javadb/overview/index.html)
- [MySQL](http://www.mysql.com/)

Note that if you are using another DBMS, you might have to alter the code of the tutorial samples.

**Install a JDBC driver from the vendor of your database**

If you are using Java DB, it already comes with a JDBC driver. If you are using MySQL, install the latest version of [Connector/J](http://www.mysql.com/products/connector/).

Contact the vendor of your database to obtain a JDBC driver for your DBMS.

There are many possible implementations of JDBC drivers. These implementations are categorized as follows:

- **Type 1**: Drivers that implement the JDBC API as a mapping to another data access API, such as ODBC (Open Database Connectivity). Drivers of this type are generally dependent on a native library, which limits their portability. The JDBC-ODBC Bridge is an example of a Type 1 driver.
  **Note**: The JDBC-ODBC Bridge should be considered a transitional solution. It is not supported by Oracle. Consider using this only if your DBMS does not offer a Java-only JDBC driver.
- **Type 2**: Drivers that are written partly in the Java programming language and partly in native code. These drivers use a native client library specific to the data source to which they connect. Again, because of the native code, their portability is limited. Oracle's OCI (Oracle Call Interface) client-side driver is an example of a Type 2 driver.
- **Type 3**: Drivers that use a pure Java client and communicate with a middleware server using a database-independent protocol. The middleware server then communicates the client's requests to the data source.
- **Type 4**: Drivers that are pure Java and implement the network protocol for a specific data source. The client connects directly to the data source.

Check which driver types comes with your DBMS. Java DB comes with two Type 4 drivers, an Embedded driver and a Network Client Driver. MySQL Connector/J is a Type 4 driver.

Installing a JDBC driver generally consists of copying the driver to your computer, then adding the location of it to your class path. In addition, many JDBC drivers other than Type 4 drivers require you to install a client-side API. No other special configuration is usually needed.

**Install Apache Ant**

These steps use Apache Ant, a Java-based tool, to build, compile, and run the JDBC tutorial samples. Go to the following link to download Apache Ant:

[http://ant.apache.org/](http://ant.apache.org/)

Ensure that the Apache Ant executable file is in your PATH environment variable so that you can run it from any directory.

**Install Apache Xalan**

The sample `RSSFeedsTable.java`, which is described in [Using SQLXML Objects](#using-sqlxml-objects), requires Apache Xalan if your DBMS is Java DB. The sample uses Apache Xalan-Java. Go to the following link to download it:

[http://xml.apache.org/xalan-j/](http://xml.apache.org/xalan-j/)

**Download the sample code**

The sample code, [JDBCTutorial.zip](http://docs.oracle.com/javase/tutorial/jdbc/basics/examples/zipfiles/JDBCTutorial.zip), consists of the following files:

- properties
  + javadb-build-properties.xml
  + javadb-sample-properties.xml
  + mysql-build-properties.xml
  + mysql-sample-properties.xml
- sql
  + javadb
    - create-procedures.sql
    - create-tables.sql
    - drop-tables.sql
    - populate-tables.sql
  + mysql
    - create-procedures.sql
    - create-tables.sql
    - drop-tables.sql
    - populate-tables.sql
- src/com/oracle/tutorial/jdbc
  + CachedRowSetSample.java
  + CityFilter.java
  + ClobSample.java
  + CoffeesFrame.java
  + CoffeesTable.java
  + CoffeesTableModel.java
  + DatalinkSample.java
  + ExampleRowSetListener.java
  + FilteredRowSetSample.java
  + JdbcRowSetSample.java
  + JDBCTutorialUtilities.java
  + JoinSample.java
  + ProductInformationTable.java
  + RSSFeedsTable.java
  + StateFilter.java
  + StoredProcedureJavaDBSample.java
  + StoredProcedureMySQLSample.java
  + SuppliersTable.java
  + WebRowSetSample.java
- txt
  + colombian-description.txt
- xml
  + rss-coffee-industry-news.xml
  + rss-the-coffee-break-blog.xml
- build.xml

Create a directory to contain all the files of the sample. These steps refer to this directory as *`<JDBC tutorial directory>`*. Unzip the contents of `JDBCTutorial.zip` into *`<JDBC tutorial directory>`*.

**Modify the build.xml file**

The `build.xml` file is the build file that Apache Ant uses to compile and execute the JDBC samples. The files `properties/javadb-build-properties.xml` and `properties/mysql-build-properties.xml` contain additional Apache Ant properties required for Java DB and MySQL, respectively. The files `properties/javadb-sample-properties.xml` and `properties/mysql-sample-properties.xml` contain properties required by the sample.

Modify these XML files as follows:

In the `build.xml` file, modify the property `ANTPROPERTIES` to refer to either `properties/javadb-build-properties.xml` or `properties/mysql-build-properties.xml`, depending on your DBMS. For example, if you are using Java DB, your `build.xml` file would contain this:

```xml
<property
  name="ANTPROPERTIES"
  value="properties/javadb-build-properties.xml"/>

  <import file="${ANTPROPERTIES}"/>
```

Similarly, if you are using MySQL, your `build.xml` file would contain this:

```xml
<property
  name="ANTPROPERTIES"
  value="properties/mysql-build-properties.xml"/>

  <import file="${ANTPROPERTIES}"/>
```

In the `properties/javadb-build-properties.xml` or `properties/mysql-build-properties.xml` file (depending on your DBMS), modify the following properties, as described in the following table:


<table summary="DBMS-specific build properties">
<tr>
<th id="h1">Property</th>
<th id="h2">Description</th>
</tr>
<tr>
<td headers="h1"><code>JAVAC</code></td>
<td headers="h2">The full path name of your Java compiler, <code>javac</code></td>
</tr>
<tr>
<td headers="h1"><code>JAVA</code></td>
<td headers="h2">The full path name of your Java runtime executable, <code>java</code></td>
</tr>
<tr>
<td headers="h1"><code>PROPERTIESFILE</code></td>
<td headers="h2">The name of the properties file, either <code>properties/javadb-sample-properties.xml</code> or <code>properties/mysql-sample-properties.xml</code></td>
</tr>
<tr>
<td headers="h1"><code>MYSQLDRIVER</code></td>
<td headers="h2">The full path name of your MySQL driver. For Connector/J, this is typically <code><em>&lt;Connector/J installation directory&gt;</em>/mysql-connector-java-<em>version-number</em>.jar</code>.</td>
</tr>
<tr>
<td headers="h1"><code>JAVADBDRIVER</code></td>
<td headers="h2">The full path name of your Java DB driver. This is typically <code><em>&lt;Java DB installation directory&gt;</em>/lib/derby.jar</code>.</td>
</tr>
<tr>
<td headers="h1"><code>XALANDIRECTORY</code></td>
<td headers="h2">The full path name of the directory that contains Apache Xalan.</td>
</tr>
<tr>
<td headers="h1"><code>CLASSPATH</code></td>
<td headers="h2">The class path that the JDBC tutorial uses. <em>You do not need to change this value.</em></td>
</tr>
<tr>
<td headers="h1"><code>XALAN</code></td>
<td headers="h2">The full path name of the file <code>xalan.jar</code>.</td>
</tr>
<tr>
<td headers="h1"><code>DB.VENDOR</code></td>
<td headers="h2">A value of either <code>derby</code> or <code>mysql</code> depending on whether you are using Java DB or MySQL, respectively. The tutorial uses this value to construct the URL required to connect to the DBMS and identify DBMS-specific code and SQL statements.</td>
</tr>
<tr>
<td headers="h1"><code>DB.DRIVER</code></td>
<td headers="h2">The fully qualified class name of the JDBC driver. For Java DB, this is <code>org.apache.derby.jdbc.EmbeddedDriver</code>. For MySQL, this is <code>com.mysql.jdbc.Driver</code>.</td>
</tr>
<tr>
<td headers="h1"><code>DB.HOST</code></td>
<td headers="h2">The host name of the computer hosting your DBMS.</td>
</tr>
<tr>
<td headers="h1"><code>DB.PORT</code></td>
<td headers="h2">The port number of the computer hosting your DBMS.</td>
</tr>
<tr>
<td headers="h1"><code>DB.SID</code></td>
<td headers="h2">The name of the database the tutorial creates and uses.</td>
</tr>
<tr>
<td headers="h1"><code>DB.URL.NEWDATABASE</code></td>
<td headers="h2">The connection URL used to connect to your DBMS when creating a new database. <em>You do not need to change this value.</em></td>
</tr>
<tr>
<td headers="h1"><code>DB.URL</code></td>
<td headers="h2">The connection URL used to connect to your DBMS. <em>You do not need to change this value.</em></td>
</tr>
<tr>
<td headers="h1"><code>DB.USER</code></td>
<td headers="h2">The name of the user that has access to create databases in the DBMS.</td>
</tr>
<tr>
<td headers="h1"><code>DB.PASSWORD</code></td>
<td headers="h2">The password of the user specified in <code>DB.USER</code>.</td>
</tr>
<tr>
<td headers="h1"><code>DB.DELIMITER</code></td>
<td headers="h2">The character used to separate SQL statements. <em>Do not change this value.</em> It should be the semicolon character (<code>;</code>).</td>
</tr>
</table>

**Modify the tutorial properties file**

The tutorial samples use the values in either the properties/javadb-sample-properties.xml file or properties/mysql-sample-properties.xml file (depending on your DBMS) to connect to the DBMS and initialize databases and tables, as described in the following table:

<table summary="DBMS-specific sample properties">
<tr>
<th id="h101">Property</th>
<th id="h102">Description</th>
</tr>
<tr>
<td headers="h101"><code>dbms</code></td>
<td headers="h102">A value of either <code>derby</code> or <code>mysql</code> depending on whether you are using Java DB or MySQL, respectively. The tutorial uses this value to construct the URL required to connect to the DBMS and identify DBMS-specific code and SQL statements.</td>
</tr>
<tr>
<td headers="h101"><code>jar_file</code></td>
<td headers="h102">The full path name of the JAR file that contains all the class files of this tutorial.</td>
</tr>
<tr>
<td headers="h101"><code>driver</code></td>
<td headers="h102">The fully qualified class name of the JDBC driver. For Java DB, this is <code>org.apache.derby.jdbc.EmbeddedDriver</code>. For MySQL, this is <code>com.mysql.jdbc.Driver</code>.</td>
</tr>
<tr>
<td headers="h101"><code>database_name</code></td>
<td headers="h102">The name of the database the tutorial creates and uses.</td>
</tr>
<tr>
<td headers="h101"><code>user_name</code></td>
<td headers="h102">The name of the user that has access to create databases in the DBMS.</td>
</tr>
<tr>
<td headers="h101"><code>password</code></td>
<td headers="h102">The password of the user specified in <code>user_name</code>.</td>
</tr>
<tr>
<td headers="h101"><code>server_name</code></td>
<td headers="h102">The host name of the computer hosting your DBMS.</td>
</tr>
<tr>
<td headers="h101"><code>port_number</code></td>
<td headers="h102">The port number of the computer hosting your DBMS.</td>
</tr>
</table>

**Note**: For simplicity in demonstrating the JDBC API, the JDBC tutorial sample code does not perform the password management techniques that a deployed system normally uses. In a production environment, you can follow the Oracle Database password management guidelines and disable any sample accounts. See the section [Securing Passwords](http://docs.oracle.com/cd/B28359_01/network.111/b28531/app_devs.htm#CJADABGG) in [Application Design](http://docs.oracle.com/cd/B28359_01/network.111/b28531/app_devs.htm) in [Managing Security for Application Developers](http://docs.oracle.com/cd/B28359_01/network.111/b28531/app_devs.htm) in [Oracle Database Security Guide](http://docs.oracle.com/cd/B28359_01/network.111/b28531/toc.htm) for password management guidelines and other security recommendations.

**Compile and package the samples**

At a command prompt, change the current directory to *`<JDBC tutorial directory>`*. From this directory, run the following command to compile the samples and package them in a jar file:

```
ant jar
```

**Create databases, tables, and populate tables**

If you are using MySQL, then run the following command to create a database:

```
ant create-mysql-database
```

**Note**: No corresponding Ant target exists in the `build.xml` file that creates a database for Java DB. The database URL for Java DB, which is used to establish a database connection, includes the option to create the database (if it does not already exist). See [Establishing a Connection](#establishing-a-connection) for more information.

If you are using either Java DB or MySQL, then from the same directory, run the following command to delete existing sample database tables, recreate the tables, and populate them. For Java DB, this command also creates the database if it does not already exist:

```
ant setup
```

**Note**: You should run the command `ant setup` every time before you run one of the Java classes in the sample. Many of these samples expect specific data in the contents of the sample's database tables.

**Run the samples**

Each target in the `build.xml` file corresponds to a Java class or SQL script in the JDBC samples. The following table lists the targets in the `build.xml` file, which class or script each target executes, and other classes or files each target requires:

<table summary="build.xml targets">
<tr>
<th id="h201">Ant Target</th>
<th id="h202">Class or SQL Script</th>
<th id="h203">Other Required Classes or Files</th>
</tr>
<tr>
<td headers="h201"><code>javadb-create-procedure</code></td>
<td headers="h202"><code>javadb/create-procedures.sql</code>; see the <code>build.xml</code> file to view other SQL statements that are run</td>
<td headers="h203">No other required files</td>
</tr>
<tr>
<td headers="h201"><code>mysql-create-procedure</code></td>
<td headers="h202"><code>mysql/create-procedures.sql</code>.</td>
<td headers="h203">No other required files</td>
</tr>
<tr>
<td headers="h201"><code>run</code></td>
<td headers="h202"><code>JDBCTutorialUtilities</code></td>
<td headers="h203">No other required classes</td>
</tr>
<tr>
<td headers="h201"><code>runct</code></td>
<td headers="h202"><code>CoffeesTable</code></td>
<td headers="h203"><code>JDBCTutorialUtilities</code></td>
</tr>
<tr>
<td headers="h201"><code>runst</code></td>
<td headers="h202"><code>SuppliersTable</code></td>
<td headers="h203"><code>JDBCTutorialUtilities</code></td>
</tr>
<tr>
<td headers="h201"><code>runjrs</code></td>
<td headers="h202"><code>JdbcRowSetSample</code></td>
<td headers="h203"><code>JDBCTutorialUtilities</code></td>
</tr>
<tr>
<td headers="h201"><code>runcrs</code></td>
<td headers="h202"><code>CachedRowSetSample</code>, <code>ExampleRowSetListener</code></td>
<td headers="h203"><code>JDBCTutorialUtilities</code></td>
</tr>
<tr>
<td headers="h201"><code>runjoin</code></td>
<td headers="h202"><code>JoinSample</code></td>
<td headers="h203"><code>JDBCTutorialUtilities</code></td>
</tr>
<tr>
<td headers="h201"><code>runfrs</code></td>
<td headers="h202"><code>FilteredRowSetSample</code></td>
<td headers="h203"><code>JDBCTutorialUtilities</code>, <code>CityFilter</code>, <code>StateFilter</code></td>
</tr>
<tr>
<td headers="h201"><code>runwrs</code></td>
<td headers="h202"><code>WebRowSetSample</code></td>
<td headers="h203"><code>JDBCTutorialUtilities</code></td>
</tr>
<tr>
<td headers="h201"><code>runclob</code></td>
<td headers="h202"><code>ClobSample</code></td>
<td headers="h203"><code>JDBCTutorialUtilities</code>, <code>txt/colombian-description.txt</code></td>
</tr>
<tr>
<td headers="h201"><code>runrss</code></td>
<td headers="h202"><code>RSSFeedsTable</code></td>
<td headers="h203"><code>JDBCTutorialUtilities</code>, the XML files contained in the <code>xml</code> directory</td>
</tr>
<tr>
<td headers="h201"><code>rundl</code></td>
<td headers="h202"><code>DatalinkSample</code></td>
<td headers="h203"><code>JDBCTutorialUtilities</code></td>
</tr>
<tr>
<td headers="h201"><code>runspjavadb</code></td>
<td headers="h202"><code>StoredProcedureJavaDBSample</code></td>
<td headers="h203"><code>JDBCTutorialUtilities</code>, <code>SuppliersTable</code>, <code>CoffeesTable</code></td>
</tr>
<tr>
<td headers="h201"><code>runspmysql</code></td>
<td headers="h202"><code>StoredProcedureMySQLSample</code></td>
<td headers="h203"><code>JDBCTutorialUtilities</code>, <code>SuppliersTable</code>, <code>CoffeesTable</code></td>
</tr>
<tr>
<td headers="h201"><code>runframe</code></td>
<td headers="h202"><code>CoffeesFrame</code></td>
<td headers="h203"><code>JDBCTutorialUtilities</code>, <code>CoffeesTableModel</code></td>
</tr>
</table>

For example, to run the class `CoffeesTable`, change the current directory to *`<JDBC tutorial directory>`*, and from this directory, run the following command:

```
ant runct
```

### Processing SQL Statements with JDBC

In general, to process any SQL statement with JDBC, you follow these steps:

1. Establishing a connection.
2. Create a statement.
3. Execute the query.
4. Process the ResultSet object.
5. Close the connection.

This page uses the following method, `CoffeesTables.viewTable`, from the tutorial sample to demonstrate these steps. This method outputs the contents of the table `COFFEES`. This method will be discussed in more detail later in this tutorial:

```java
public static void viewTable(Connection con, String dbName)
    throws SQLException {

    Statement stmt = null;
    String query = "select COF_NAME, SUP_ID, PRICE, " +
                   "SALES, TOTAL " +
                   "from " + dbName + ".COFFEES";
    try {
        stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            String coffeeName = rs.getString("COF_NAME");
            int supplierID = rs.getInt("SUP_ID");
            float price = rs.getFloat("PRICE");
            int sales = rs.getInt("SALES");
            int total = rs.getInt("TOTAL");
            System.out.println(coffeeName + "\t" + supplierID +
                               "\t" + price + "\t" + sales +
                               "\t" + total);
        }
    } catch (SQLException e ) {
        JDBCTutorialUtilities.printSQLException(e);
    } finally {
        if (stmt != null) { stmt.close(); }
    }
}
```

#### Establishing Connections

First, establish a connection with the data source you want to use. A data source can be a DBMS, a legacy file system, or some other source of data with a corresponding JDBC driver. This connection is represented by a `Connection` object. See [Establishing a Connection](#establishing-a-connection) for more information.

#### Creating Statements

A `Statement` is an interface that represents a SQL statement. You execute `Statement` objects, and they generate `ResultSet` objects, which is a table of data representing a database result set. You need a `Connection` object to create a `Statement` object.

For example, `CoffeesTables.viewTable` creates a `Statement` object with the following code:

```java
stmt = con.createStatement();
```

There are three different kinds of statements:

- `Statement`: Used to implement simple SQL statements with no parameters.
- `PreparedStatement`: (Extends `Statement`.) Used for precompiling SQL statements that might contain input parameters. See [Using Prepared Statements](#using-prepared-statements) for more information.
- `CallableStatement`: (Extends `PreparedStatement`.) Used to execute stored procedures that may contain both input and output parameters. See [Stored Procedures](#stored-procedures) for more information.

#### Executing Queries

To execute a query, call an `execute` method from `Statement` such as the following:

- `execute`: Returns `true` if the first object that the query returns is a `ResultSet` object. Use this method if the query could return one or more `ResultSet` objects. Retrieve the `ResultSet` objects returned from the query by repeatedly calling `Statement.getResultSet`.
- `executeQuery`: Returns one `ResultSet` object.
- `executeUpdate`: Returns an integer representing the number of rows affected by the SQL statement. Use this method if you are using `INSERT`, `DELETE`, or `UPDATE` SQL statements.

For example, `CoffeesTables.viewTable` executed a `Statement` object with the following code:

```java
ResultSet rs = stmt.executeQuery(query);
```

See [Retrieving and Modifying Values from Result Sets](#retrieving-and-modifying-values-from-result-sets) for more information.

#### Processing ResultSet Objects

You access the data in a `ResultSet` object through a cursor. Note that this cursor is not a database cursor. This cursor is a pointer that points to one row of data in the `ResultSet` object. Initially, the cursor is positioned before the first row. You call various methods defined in the `ResultSet` object to move the cursor.

For example, `CoffeesTables.viewTable` repeatedly calls the method `ResultSet.next` to move the cursor forward by one row. Every time it calls next, the method outputs the data in the row where the cursor is currently positioned:

```java
try {
    stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery(query);
    while (rs.next()) {
        String coffeeName = rs.getString("COF_NAME");
        int supplierID = rs.getInt("SUP_ID");
        float price = rs.getFloat("PRICE");
        int sales = rs.getInt("SALES");
        int total = rs.getInt("TOTAL");
        System.out.println(coffeeName + "\t" + supplierID +
                           "\t" + price + "\t" + sales +
                           "\t" + total);
    }
}
// ...
```

See [Retrieving and Modifying Values from Result Sets](#retrieving-and-modifying-values-from-result-sets) for more information.

#### Closing Connections

When you are finished using a `Statement`, call the method `Statement.close` to immediately release the resources it is using. When you call this method, its `ResultSet` objects are closed.

For example, the method `CoffeesTables.viewTable` ensures that the `Statement` object is closed at the end of the method, regardless of any `SQLException` objects thrown, by wrapping it in a `finally` block:

```java
} finally {
    if (stmt != null) { stmt.close(); }
}
```

JDBC throws an `SQLException` when it encounters an error during an interaction with a data source. See [Handling SQL Exceptions](#handling-sql-exceptions) for more information.

In JDBC 4.1, which is available in Java SE release 7 and later, you can use a try-with-resources statement to automatically close `Connection`, `Statement`, and `ResultSet` objects, regardless of whether an `SQLException` has been thrown. An automatic resource statement consists of a `try` statement and one or more declared resources. For example, you can modify `CoffeesTables.viewTable` so that its `Statement` object closes automatically, as follows:

```java
public static void viewTable(Connection con) throws SQLException {

    String query = "select COF_NAME, SUP_ID, PRICE, " +
                   "SALES, TOTAL " +
                   "from COFFEES";

    try (Statement stmt = con.createStatement()) {

        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            String coffeeName = rs.getString("COF_NAME");
            int supplierID = rs.getInt("SUP_ID");
            float price = rs.getFloat("PRICE");
            int sales = rs.getInt("SALES");
            int total = rs.getInt("TOTAL");
            System.out.println(coffeeName + ", " + supplierID +
                               ", " + price + ", " + sales +
                               ", " + total);
        }
    } catch (SQLException e) {
        JDBCTutorialUtilities.printSQLException(e);
    }
}
```

The following statement is an try-with-resources statement, which declares one resource, `stmt`, that will be automatically closed when the `try` block terminates:

```java
try (Statement stmt = con.createStatement()) {
    // ...
}
```


