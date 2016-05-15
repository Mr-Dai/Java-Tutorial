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

<h3 id="establishing-a-connection">Establishing a Connection</h3>

First, you need to establish a connection with the data source you want to use. A data source can be a DBMS, a legacy file system, or some other source of data with a corresponding JDBC driver. Typically, a JDBC application connects to a target data source using one of two classes:

- `DriverManager`: This fully implemented class connects an application to a data source, which is specified by a database URL. When this class first attempts to establish a connection, it automatically loads any JDBC 4.0 drivers found within the class path. Note that your application must manually load any JDBC drivers prior to version 4.0.
- `DataSource`: This interface is preferred over `DriverManager` because it allows details about the underlying data source to be transparent to your application. A `DataSource` object's properties are set so that it represents a particular data source. See [Connecting with DataSource Objects](#connecting-with-datasource-objects) for more information. For more information about developing applications with the `DataSource` class, see the latest [The Java EE Tutorial](http://docs.oracle.com/javaee/6/tutorial/doc/).

**Note**: The samples in this tutorial use the `DriverManager` class instead of the `DataSource` class because it is easier to use and the samples do not require the features of the `DataSource` class.

#### Using the DriverManager Class

Connecting to your DBMS with the `DriverManager` class involves calling the method D`riverManager.getConnection`. The following method, <code><a href="http://docs.oracle.com/javase/tutorial/jdbc/basics/gettingstarted.html">JDBCTutorialUtilities.getConnection</a></code>, establishes a database connection:

```java
public Connection getConnection() throws SQLException {

    Connection conn = null;
    Properties connectionProps = new Properties();
    connectionProps.put("user", this.userName);
    connectionProps.put("password", this.password);

    if (this.dbms.equals("mysql")) {
        conn = DriverManager.getConnection(
                   "jdbc:" + this.dbms + "://" +
                   this.serverName +
                   ":" + this.portNumber + "/",
                   connectionProps);
    } else if (this.dbms.equals("derby")) {
        conn = DriverManager.getConnection(
                   "jdbc:" + this.dbms + ":" +
                   this.dbName +
                   ";create=true",
                   connectionProps);
    }
    System.out.println("Connected to database");
    return conn;
}
```

The method `DriverManager.getConnection` establishes a database connection. This method requires a database URL, which varies depending on your DBMS. The following are some examples of database URLs:

1. MySQL: `jdbc:mysql://localhost:3306/`, where `localhost` is the name of the server hosting your database, and `3306` is the port number.
2. Java DB: `jdbc:derby:testdb;create=true`, where `testdb` is the name of the database to connect to, and `create=true` instructs the DBMS to create the database.
   **Note**: This URL establishes a database connection with the Java DB Embedded Driver. Java DB also includes a Network Client Driver, which uses a different URL.

This method specifies the user name and password required to access the DBMS with a `Properties` object.

**Note**:

- Typically, in the database URL, you also specify the name of an existing database to which you want to connect. For example, the URL `jdbc:mysql://localhost:3306/mysql` represents the database URL for the MySQL database named `mysql`. The samples in this tutorial use a URL that does not specify a specific database because the samples create a new database.
- In previous versions of JDBC, to obtain a connection, you first had to initialize your JDBC driver by calling the method `Class.forName`. This methods required an object of type `java.sql.Driver`. Each JDBC driver contains one or more classes that implements the interface `java.sql.Driver`. The drivers for Java DB are `org.apache.derby.jdbc.EmbeddedDriver` and `org.apache.derby.jdbc.ClientDriver`, and the one for MySQL Connector/J is `com.mysql.jdbc.Driver`. See the documentation of your DBMS driver to obtain the name of the class that implements the interface `java.sql.Driver`.
  Any JDBC 4.0 drivers that are found in your class path are automatically loaded. (However, you must manually load any drivers prior to JDBC 4.0 with the method `Class.forName`.)

The method returns a `Connection` object, which represents a connection with the DBMS or a specific database. Query the database through this object.

#### Specifying Database Connection URLs

A database connection URL is a string that your DBMS JDBC driver uses to connect to a database. It can contain information such as where to search for the database, the name of the database to connect to, and configuration properties. The exact syntax of a database connection URL is specified by your DBMS.

##### Java DB Database Connection URLs

The following is the database connection URL syntax for Java DB:

```
jdbc:derby:[subsubprotocol:][databaseName]
    [;attribute=value]*
```

- `subsubprotocol` specifies where Java DB should search for the database, either in a directory, in memory, in a class path, or in a JAR file. It is typically omitted.
- `databaseName` is the name of the database to connect to.
- `attribute=value` represents an optional, semicolon-separated list of attributes. These attributes enable you to instruct Java DB to perform various tasks, including the following:
  * Create the database specified in the connection URL.
  * Encrypt the database specified in the connection URL.
  * Specify directories to store logging and trace information.
  * Specify a user name and password to connect to the database.

See *Java DB Developer's Guide* and *Java DB Reference Manual* from Java DB Technical Documentation for more information.

##### MySQL Connector/J Database URL

The following is the database connection URL syntax for MySQL Connector/J:

```
jdbc:mysql://[host][,failoverhost...]
    [:port]/[database]
    [?propertyName1][=propertyValue1]
    [&propertyName2][=propertyValue2]...
```

- `host:port` is the host name and port number of the computer hosting your database. If not specified, the default values of `host` and `port` are 127.0.0.1 and 3306, respectively.
- `database` is the name of the database to connect to. If not specified, a connection is made with no default database.
- `failover` is the name of a standby database (MySQL Connector/J supports failover).
- `propertyName=propertyValue` represents an optional, ampersand-separated list of properties. These attributes enable you to instruct MySQL Connector/J to perform various tasks.

See [MySQL Reference Manual](http://dev.mysql.com/doc/) for more information.

<h3 id="connecting-with-datasource-objects">Connecting with DataSource Objects</h3>

This section covers `DataSource` objects, which are the preferred means of getting a connection to a data source. In addition to their other advantages, which will be explained later, `DataSource` objects can provide connection pooling and distributed transactions. This functionality is essential for enterprise database computing. In particular, it is integral to Enterprise JavaBeans (EJB) technology.

This section shows you how to get a connection using the `DataSource` interface and how to use distributed transactions and connection pooling. Both of these involve very few code changes in your JDBC application.

The work performed to deploy the classes that make these operations possible, which a system administrator usually does with a tool (such as Apache Tomcat or Oracle WebLogic Server), varies with the type of `DataSource` object that is being deployed. As a result, most of this section is devoted to showing how a system administrator sets up the environment so that programmers can use a `DataSource` object to get connections.

#### Using DataSource Objects to Get a Connection

In [Establishing a Connection](#establishing-a-connection), you learned how to get a connection using the `DriverManager` class. This section shows you how to use a `DataSource` object to get a connection to your data source, which is the preferred way.

Objects instantiated by classes that implement the `DataSource` represent a particular DBMS or some other data source, such as a file. A `DataSource` object represents a particular DBMS or some other data source, such as a file. If a company uses more than one data source, it will deploy a separate `DataSource` object for each of them. The `DataSource` interface is implemented by a driver vendor. It can be implemented in three different ways:

- A basic `DataSource` implementation produces standard `Connection` objects that are not pooled or used in a distributed transaction.
- A `DataSource` implementation that supports connection pooling produces `Connection` objects that participate in connection pooling, that is, connections that can be recycled.
- A `DataSource` implementation that supports distributed transactions produces `Connection` objects that can be used in a distributed transaction, that is, a transaction that accesses two or more DBMS servers.

A JDBC driver should include at least a basic `DataSource` implementation. For example, the Java DB JDBC driver includes the implementation `org.apache.derby.jdbc.ClientDataSource` and for MySQL, `com.mysql.jdbc.jdbc2.optional.MysqlDataSource`. If your client runs on Java 8 compact profile 2, then the Java DB JDBC driver is `org.apache.derby.jdbc.BasicClientDataSource40`. The sample for this tutorial requires compact profile 3 or greater.

A `DataSource` class that supports distributed transactions typically also implements support for connection pooling. For example, a `DataSource` class provided by an EJB vendor almost always supports both connection pooling and distributed transactions.

Suppose that the owner of the thriving chain of The Coffee Break shops, from the previous examples, has decided to expand further by selling coffee over the Internet. With the large amount of online business expected, the owner will definitely need connection pooling. Opening and closing connections involves a great deal of overhead, and the owner anticipates that this online ordering system will necessitate a sizable number of queries and updates. With connection pooling, a pool of connections can be used over and over again, avoiding the expense of creating a new connection for every database access. In addition, the owner now has a second DBMS that contains data for the recently acquired coffee roasting company. This means that the owner will want to be able to write distributed transactions that use both the old DBMS server and the new one.

The chain owner has reconfigured the computer system to serve the new, larger customer base. The owner has purchased the most recent JDBC driver and an EJB application server that works with it to be able to use distributed transactions and get the increased performance that comes with connection pooling. Many JDBC drivers are available that are compatible with the recently purchased EJB server. The owner now has a three-tier architecture, with a new EJB application server and JDBC driver in the middle tier and the two DBMS servers as the third tier. Client computers making requests are the first tier.

#### Deploying Basic DataSource Objects

The system administrator needs to deploy `DataSource` objects so that The Coffee Break's programming team can start using them. Deploying a `DataSource` object consists of three tasks:

1. Creating an instance of the `DataSource` class
2. Setting its properties
3. Registering it with a naming service that uses the Java Naming and Directory Interface (JNDI) API

First, consider the most basic case, which is to use a basic implementation of the `DataSource` interface, that is, one that does not support connection pooling or distributed transactions. In this case there is only one `DataSource` object that needs to be deployed. A basic implementation of `DataSource` produces the same kind of connections that the `DriverManager` class produces.

##### Creating Instance of DataSource Class and Setting its Properties

Suppose a company that wants only a basic implementation of `DataSource` has bought a driver from the JDBC vendor DB Access, Inc. This driver includes the class `com.dbaccess.BasicDataSource` that implements the `DataSource` interface. The following code excerpt creates an instance of the class `BasicDataSource` and sets its properties. After the instance of `BasicDataSource` is deployed, a programmer can call the method `DataSource.getConnection` to get a connection to the company's database, `CUSTOMER_ACCOUNTS`. First, the system administrator creates the `BasicDataSource` object `ds` using the default constructor. The system administrator then sets three properties. Note that the following code is typically be executed by a deployment tool:

```java
com.dbaccess.BasicDataSource ds = new com.dbaccess.BasicDataSource();
ds.setServerName("grinder");
ds.setDatabaseName("CUSTOMER_ACCOUNTS");
ds.setDescription("Customer accounts database for billing");
```

The variable `ds` now represents the database `CUSTOMER_ACCOUNTS` installed on the server. Any connection produced by the `BasicDataSource` object ds will be a connection to the database `CUSTOMER_ACCOUNTS`.

##### Registering DataSource Object with Naming Service That Uses JNDI API

With the properties set, the system administrator can register the `BasicDataSource` object with a JNDI (Java Naming and Directory Interface) naming service. The particular naming service that is used is usually determined by a system property, which is not shown here. The following code excerpt registers the `BasicDataSource` object and binds it with the logical name `jdbc/billingDB`:

```java
Context ctx = new InitialContext();
ctx.bind("jdbc/billingDB", ds);
```

This code uses the JNDI API. The first line creates an `InitialContext` object, which serves as the starting point for a name, similar to root directory in a file system. The second line associates, or binds, the `BasicDataSource` object `ds` to the logical name `jdbc/billingDB`. In the next code excerpt, you give the naming service this logical name, and it returns the BasicDataSource object. The logical name can be any string. In this case, the company decided to use the name `billingDB` as the logical name for the `CUSTOMER_ACCOUNTS` database.

In the previous example, `jdbc` is a subcontext under the initial context, just as a directory under the root directory is a subdirectory. The name `jdbc/billingDB` is like a path name, where the last item in the path is analogous to a file name. In this case, `billingDB` is the logical name that is given to the `BasicDataSource` object `ds`. The subcontext `jdbc` is reserved for logical names to be bound to DataSource objects, so jdbc will always be the first part of a logical name for a data source.

##### Using Deployed DataSource Object

After a basic `DataSource` implementation is deployed by a system administrator, it is ready for a programmer to use. This means that a programmer can give the logical data source name that was bound to an instance of a `DataSource` class, and the JNDI naming service will return an instance of that `DataSource` class. The method `getConnection` can then be called on that `DataSource` object to get a connection to the data source it represents. For example, a programmer might write the following two lines of code to get a `DataSource` object that produces a connection to the database `CUSTOMER_ACCOUNTS`.

```java
Context ctx = new InitialContext();
DataSource ds = (DataSource)ctx.lookup("jdbc/billingDB");
```

The first line of code gets an initial context as the starting point for retrieving a `DataSource` object. When you supply the logical name `jdbc/billingDB` to the method `lookup`, the method returns the `DataSource` object that the system administrator bound to `jdbc/billingDB` at deployment time. Because the return value of the method `lookup` is a Java `Object`, we must cast it to the more specific `DataSource` type before assigning it to the variable `ds`.

The variable `ds` is an instance of the class `com.dbaccess.BasicDataSource` that implements the `DataSource` interface. Calling the method `ds.getConnection` produces a connection to the `CUSTOMER_ACCOUNTS` database.

```java
Connection con = ds.getConnection("fernanda","brewed");
```

The `getConnection` method requires only the user name and password because the variable ds has the rest of the information necessary for establishing a connection with the `CUSTOMER_ACCOUNTS` database, such as the database name and location, in its properties.

##### Advantages of DataSource Objects

Because of its properties, a `DataSource` object is a better alternative than the `DriverManager` class for getting a connection. Programmers no longer have to hard code the driver name or JDBC URL in their applications, which makes them more portable. Also, `DataSource` properties make maintaining code much simpler. If there is a change, the system administrator can update data source properties and not be concerned about changing every application that makes a connection to the data source. For example, if the data source were moved to a different server, all the system administrator would have to do is set the `serverName` property to the new server name.

Aside from portability and ease of maintenance, using a `DataSource` object to get connections can offer other advantages. When the `DataSource` interface is implemented to work with a `ConnectionPoolDataSource` implementation, all of the connections produced by instances of that `DataSource` class will automatically be pooled connections. Similarly, when the `DataSource` implementation is implemented to work with an `XADataSource` class, all of the connections it produces will automatically be connections that can be used in a distributed transaction. The next section shows how to deploy these types of `DataSource` implementations.

#### Deploying Other DataSource Implementations

A system administrator or another person working in that capacity can deploy a `DataSource` object so that the connections it produces are pooled connections. To do this, he or she first deploys a `ConnectionPoolDataSource` object and then deploys a `DataSource` object implemented to work with it. The properties of the `ConnectionPoolDataSource` object are set so that it represents the data source to which connections will be produced. After the `ConnectionPoolDataSource` object has been registered with a JNDI naming service, the `DataSource` object is deployed. Generally only two properties must be set for the `DataSource` object: `description` and `dataSourceName`. The value given to the `dataSourceName` property is the logical name identifying the `ConnectionPoolDataSource` object previously deployed, which is the object containing the properties needed to make the connection.

With the `ConnectionPoolDataSource` and `DataSource` objects deployed, you can call the method `DataSource.getConnection` on the `DataSource` object and get a pooled connection. This connection will be to the data source specified in the `ConnectionPoolDataSource` object's properties.

The following example describes how a system administrator for The Coffee Break would deploy a `DataSource` object implemented to provide pooled connections. The system administrator would typically use a deployment tool, so the code fragments shown in this section are the code that a deployment tool would execute.

To get better performance, The Coffee Break company has bought a JDBC driver from DB Access, Inc. that includes the class `com.dbaccess.ConnectionPoolDS`, which implements the `ConnectionPoolDataSource` interface. The system administrator creates create an instance of this class, sets its properties, and registers it with a JNDI naming service. The Coffee Break has bought its `DataSource` class, `com.applogic.PooledDataSource`, from its EJB server vendor, Application Logic, Inc. The class `com.applogic.PooledDataSource` implements connection pooling by using the underlying support provided by the `ConnectionPoolDataSource` class `com.dbaccess.ConnectionPoolDS`.

The `ConnectionPoolDataSource` object must be deployed first. The following code creates an instance of `com.dbaccess.ConnectionPoolDS` and sets its properties:

```java
com.dbaccess.ConnectionPoolDS cpds = new com.dbaccess.ConnectionPoolDS();
cpds.setServerName("creamer");
cpds.setDatabaseName("COFFEEBREAK");
cpds.setPortNumber(9040);
cpds.setDescription("Connection pooling for " + "COFFEEBREAK DBMS");
```

After the `ConnectionPoolDataSource` object has been deployed, the system administrator deploys the `DataSource` object. The following code registers the `com.dbaccess.ConnectionPoolDS` object `cpds` with a JNDI naming service. Note that the logical name being associated with the `cpds` variable has the subcontext `pool` added under the subcontext `jdbc`, which is similar to adding a subdirectory to another subdirectory in a hierarchical file system. The logical name of any instance of the class `com.dbaccess.ConnectionPoolDS` will always begin with `jdbc/pool`. Oracle recommends putting all `ConnectionPoolDataSource` objects under the subcontext `jdbc/pool`:

```java
Context ctx = new InitialContext();
ctx.bind("jdbc/pool/fastCoffeeDB", cpds);
```

Next, the `DataSource` class that is implemented to interact with the `cpds` variable and other instances of the `com.dbaccess.ConnectionPoolDS` class is deployed. The following code creates an instance of this class and sets its properties. Note that only two properties are set for this instance of `com.applogic.PooledDataSource`. The `description` property is set because it is always required. The other property that is set, `dataSourceName`, gives the logical JNDI name for `cpds`, which is an instance of the `com.dbaccess.ConnectionPoolDS` class. In other words, `cpds` represents the `ConnectionPoolDataSource` object that will implement connection pooling for the `DataSource` object.

The following code, which would probably be executed by a deployment tool, creates a `PooledDataSource` object, sets its properties, and binds it to the logical name `jdbc/fastCoffeeDB`:

```java
com.applogic.PooledDataSource ds = new com.applogic.PooledDataSource();
ds.setDescription("produces pooled connections to COFFEEBREAK");
ds.setDataSourceName("jdbc/pool/fastCoffeeDB");
Context ctx = new InitialContext();
ctx.bind("jdbc/fastCoffeeDB", ds);
```

At this point, a `DataSource` object is deployed from which an application can get pooled connections to the database `COFFEEBREAK`.

#### Getting and Using Pooled Connections

A *connection pool* is a cache of database connection objects. The objects represent physical database connections that can be used by an application to connect to a database. At run time, the application requests a connection from the pool. If the pool contains a connection that can satisfy the request, it returns the connection to the application. If no connections are found, a new connection is created and returned to the application. The application uses the connection to perform some work on the database and then returns the object back to the pool. The connection is then available for the next connection request.

Connection pools promote the reuse of connection objects and reduce the number of times that connection objects are created. Connection pools significantly improve performance for database-intensive applications because creating connection objects is costly both in terms of time and resources.

Now that these `DataSource` and `ConnectionPoolDataSource` objects are deployed, a programmer can use the `DataSource` object to get a pooled connection. The code for getting a pooled connection is just like the code for getting a nonpooled connection, as shown in the following two lines:

```java
ctx = new InitialContext();
ds = (DataSource)ctx.lookup("jdbc/fastCoffeeDB");
```

The variable `ds` represents a `DataSource` object that produces pooled connections to the database `COFFEEBREAK`. You need to retrieve this `DataSource` object only once because you can use it to produce as many pooled connections as needed. Calling the method `getConnection` on the `ds` variable automatically produces a pooled connection because the `DataSource` object that the `ds` variable represents was configured to produce pooled connections.

Connection pooling is generally transparent to the programmer. There are only two things you need to do when you are using pooled connections:

1. Use a `DataSource` object rather than the `DriverManager` class to get a connection. In the following line of code, `ds` is a `DataSource` object implemented and deployed so that it will create pooled connections and `username` and `password` are variables that represent the credentials of the user that has access to the database:
   ```java
   Connection con = ds.getConnection(username, password);
   ```
2. Use a `finally` statement to close a pooled connection. The following `finally` block would appear after the `try/catch` block that applies to the code in which the pooled connection was used:
   ```java
   try {
     Connection con = ds.getConnection(username, password);
     // ... code to use the pooled
     // connection con
   } catch (Exception ex {
     // ... code to handle exceptions
   } finally {
     if (con != null) con.close();
   }
   ```

Otherwise, an application using a pooled connection is identical to an application using a regular connection. The only other thing an application programmer might notice when connection pooling is being done is that performance is better.

The following sample code gets a `DataSource` object that produces connections to the database `COFFEEBREAK` and uses it to update a price in the table `COFFEES`:

```java
import java.sql.*;
import javax.sql.*;
import javax.ejb.*;
import javax.naming.*;

public class ConnectionPoolingBean implements SessionBean {

    // ...

    public void ejbCreate() throws CreateException {
        ctx = new InitialContext();
        ds = (DataSource)ctx.lookup("jdbc/fastCoffeeDB");
    }

    public void updatePrice(float price, String cofName,
                            String username, String password)
        throws SQLException{

        Connection con;
        PreparedStatement pstmt;
        try {
            con = ds.getConnection(username, password);
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("UPDATE COFFEES " +
                        "SET PRICE = ? " +
                        "WHERE COF_NAME = ?");
            pstmt.setFloat(1, price);
            pstmt.setString(2, cofName);
            pstmt.executeUpdate();

            con.commit();
            pstmt.close();

        } finally {
            if (con != null) con.close();
        }
    }

    private DataSource ds = null;
    private Context ctx = null;
}
```

The connection in this code sample participates in connection pooling because the following are true:

- An instance of a class implementing `ConnectionPoolDataSource` has been deployed.
- An instance of a class implementing `DataSource` has been deployed, and the value set for its `dataSourceName` property is the logical name that was bound to the previously deployed `ConnectionPoolDataSource` object.

Note that although this code is very similar to code you have seen before, it is different in the following ways:

- It imports the `javax.sql`, `javax.ejb`, and `javax.naming` packages in addition to `java.sql`.
  The `DataSource` and `ConnectionPoolDataSource` interfaces are in the `javax.sql` package, and the JNDI constructor `InitialContext` and method `Context.lookup` are part of the `javax.naming` package. This particular example code is in the form of an EJB component that uses API from the `javax.ejb` package. The purpose of this example is to show that you use a pooled connection the same way you use a nonpooled connection, so you need not worry about understanding the EJB API.
- It uses a `DataSource` object to get a connection instead of using the `DriverManager` facility.
- It uses a `finally` block to ensure that the connection is closed.

Getting and using a pooled connection is similar to getting and using a regular connection. When someone acting as a system administrator has deployed a `ConnectionPoolDataSource` object and a `DataSource` object properly, an application uses that `DataSource` object to get a pooled connection. An application should, however, use a `finally` block to close the pooled connection. For simplicity, the preceding example used a `finally` block but no `catch` block. If an exception is thrown by a method in the `try` block, it will be thrown by default, and the `finally` clause will be executed in any case.

#### Deploying Distributed Transactions

`DataSource` objects can be deployed to get connections that can be used in distributed transactions. As with connection pooling, two different class instances must be deployed: an `XADataSource` object and a `DataSource` object that is implemented to work with it.

Suppose that the EJB server that The Coffee Break entrepreneur bought includes the `DataSource` class `com.applogic.TransactionalDS`, which works with an `XADataSource` class such as `com.dbaccess.XATransactionalDS`. The fact that it works with any `XADataSource` class makes the EJB server portable across JDBC drivers. When the `DataSource` and `XADataSource` objects are deployed, the connections produced will be able to participate in distributed transactions. In this case, the class `com.applogic.TransactionalDS` is implemented so that the connections produced are also pooled connections, which will usually be the case for `DataSource` classes provided as part of an EJB server implementation.

The `XADataSource` object must be deployed first. The following code creates an instance of `com.dbaccess.XATransactionalDS` and sets its properties:

```java
com.dbaccess.XATransactionalDS xads = new com.dbaccess.XATransactionalDS();
xads.setServerName("creamer");
xads.setDatabaseName("COFFEEBREAK");
xads.setPortNumber(9040);
xads.setDescription("Distributed transactions for COFFEEBREAK DBMS");
```

The following code registers the `com.dbaccess.XATransactionalDS` object `xads` with a JNDI naming service. Note that the logical name being associated with `xads` has the subcontext `xa` added under `jdbc`. Oracle recommends that the logical name of any instance of the class `com.dbaccess.XATransactionalDS` always begin with `jdbc/xa`.

```java
Context ctx = new InitialContext();
ctx.bind("jdbc/xa/distCoffeeDB", xads);
```

Next, the `DataSource` object that is implemented to interact with `xads` and other `XADataSource` objects is deployed. Note that the `DataSource` class, `com.applogic.TransactionalDS`, can work with an `XADataSource` class from any JDBC driver vendor. Deploying the `DataSource` object involves creating an instance of the `com.applogic.TransactionalDS` class and setting its properties. The `dataSourceName` property is set to `jdbc/xa/distCoffeeDB`, the logical name associated with `com.dbaccess.XATransactionalDS`. This is the `XADataSource` class that implements the distributed transaction capability for the `DataSource` class. The following code deploys an instance of the `DataSource` class:

```java
com.applogic.TransactionalDS ds = new com.applogic.TransactionalDS();
ds.setDescription("Produces distributed transaction " +
                  "connections to COFFEEBREAK");
ds.setDataSourceName("jdbc/xa/distCoffeeDB");
Context ctx = new InitialContext();
ctx.bind("jdbc/distCoffeeDB", ds);
```

Now that instances of the classes `com.applogic.TransactionalDS` and `com.dbaccess.XATransactionalDS` have been deployed, an application can call the method `getConnection` on instances of the `TransactionalDS` class to get a connection to the `COFFEEBREAK` database that can be used in distributed transactions.

#### Using Connections for Distributed Transactions

To get a connection that can be used for distributed transactions, must use a `DataSource` object that has been properly implemented and deployed, as shown in the section Deploying Distributed Transactions. With such a `DataSource` object, call the method `getConnection` on it. After you have the connection, use it just as you would use any other connection. Because `jdbc/distCoffeesDB` has been associated with an `XADataSource` object in a JNDI naming service, the following code produces a `Connection` object that can be used in distributed transactions:

```java
Context ctx = new InitialContext();
DataSource ds = (DataSource)ctx.lookup("jdbc/distCoffeesDB");
Connection con = ds.getConnection();
```

There are some minor but important restrictions on how this connection is used while it is part of a distributed transaction. A transaction manager controls when a distributed transaction begins and when it is committed or rolled back; therefore, application code should never call the methods `Connection.commit` or `Connection.rollback`. An application should likewise never call `Connection.setAutoCommit(true)`, which enables the auto-commit mode, because that would also interfere with the transaction manager's control of the transaction boundaries. This explains why a new connection that is created in the scope of a distributed transaction has its auto-commit mode disabled by default. Note that these restrictions apply only when a connection is participating in a distributed transaction; there are no restrictions while the connection is not part of a distributed transaction.

For the following example, suppose that an order of coffee has been shipped, which triggers updates to two tables that reside on different DBMS servers. The first table is a new `INVENTORY` table, and the second is the `COFFEES` table. Because these tables are on different DBMS servers, a transaction that involves both of them will be a distributed transaction. The code in the following example, which obtains a connection, updates the `COFFEES` table, and closes the connection, is the second part of a distributed transaction.

Note that the code does not explicitly commit or roll back the updates because the scope of the distributed transaction is being controlled by the middle tier server's underlying system infrastructure. Also, assuming that the connection used for the distributed transaction is a pooled connection, the application uses a `finally` block to close the connection. This guarantees that a valid connection will be closed even if an exception is thrown, thereby ensuring that the connection is returned to the connection pool to be recycled.

The following code sample illustrates an enterprise Bean, which is a class that implements the methods that can be called by a client computer. The purpose of this example is to demonstrate that application code for a distributed transaction is no different from other code except that it does not call the `Connection` methods `commit`, `rollback`, or `setAutoCommit(true)`. Therefore, you do not need to worry about understanding the EJB API that is used.

```java
import java.sql.*;
import javax.sql.*;
import javax.ejb.*;
import javax.naming.*;

public class DistributedTransactionBean implements SessionBean {

    // ...

    public void ejbCreate() throws CreateException {

        ctx = new InitialContext();
        ds = (DataSource)ctx.lookup("jdbc/distCoffeesDB");
    }

    public void updateTotal(int incr, String cofName, String username,
                            String password)
        throws SQLException {

        Connection con;
        PreparedStatement pstmt;

        try {
            con = ds.getConnection(username, password);
            pstmt = con.prepareStatement("UPDATE COFFEES " +
                        "SET TOTAL = TOTAL + ? " +
                        "WHERE COF_NAME = ?");
            pstmt.setInt(1, incr);
            pstmt.setString(2, cofName);
            pstmt.executeUpdate();
            stmt.close();
        } finally {
            if (con != null) con.close();
        }
    }

    private DataSource ds = null;
    private Context ctx = null;
}
```

<h3 id="handling-sqlexceptions">Handling SQLExceptions</h3>

#### Overview of SQLException

When JDBC encounters an error during an interaction with a data source, it throws an instance of `SQLException` as opposed to `Exception`. (A data source in this context represents the database to which a `Connection` object is connected.) The `SQLException` instance contains the following information that can help you determine the cause of the error:

- A description of the error. Retrieve the `String` object that contains this description by calling the method `SQLException.getMessage`.
- A SQLState code. These codes and their respective meanings have been standardized by ISO/ANSI and Open Group (X/Open), although some codes have been reserved for database vendors to define for themselves. This String object consists of five alphanumeric characters. Retrieve this code by calling the method `SQLException.getSQLState`.
- An error code. This is an integer value identifying the error that caused the `SQLException` instance to be thrown. Its value and meaning are implementation-specific and might be the actual error code returned by the underlying data source. Retrieve the error by calling the method `SQLException.getErrorCode`.
- A cause. A `SQLException` instance might have a causal relationship, which consists of one or more `Throwable` objects that caused the `SQLException` instance to be thrown. To navigate this chain of causes, recursively call the method `SQLException.getCause` until a `null` value is returned.
- A reference to any *chained* exceptions. If more than one error occurs, the exceptions are referenced through this chain. Retrieve these exceptions by calling the method `SQLException.getNextException` on the exception that was thrown.

#### Retrieving Exceptions

The following method, `JDBCTutorialUtilities.printSQLException` outputs the SQLState, error code, error description, and cause (if there is one) contained in the `SQLException` as well as any other exception chained to it:

```java
public static void printSQLException(SQLException ex) {

    for (Throwable e : ex) {
        if (e instanceof SQLException) {
            if (ignoreSQLException(
                ((SQLException)e).
                getSQLState()) == false) {

                e.printStackTrace(System.err);
                System.err.println("SQLState: " +
                    ((SQLException)e).getSQLState());

                System.err.println("Error Code: " +
                    ((SQLException)e).getErrorCode());

                System.err.println("Message: " + e.getMessage());

                Throwable t = ex.getCause();
                while(t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
```

For example, if you call the method `CoffeesTable.dropTable` with Java DB as your DBMS, the table `COFFEES` does not exist, *and* you remove the call to `JDBCTutorialUtilities.ignoreSQLException`, the output will be similar to the following:

```
SQLState: 42Y55
Error Code: 30000
Message: 'DROP TABLE' cannot be performed on
'TESTDB.COFFEES' because it does not exist.
```

Instead of outputting `SQLException` information, you could instead first retrieve the `SQLState` then process the `SQLException` accordingly. For example, the method `JDBCTutorialUtilities.ignoreSQLException` returns `true` if the `SQLState` is equal to code `42Y55` (and you are using Java DB as your DBMS), which causes `JDBCTutorialUtilities.printSQLException` to ignore the `SQLException`:

```java
public static boolean ignoreSQLException(String sqlState) {

    if (sqlState == null) {
        System.out.println("The SQL state is not defined!");
        return false;
    }

    // X0Y32: Jar file already exists in schema
    if (sqlState.equalsIgnoreCase("X0Y32"))
        return true;

    // 42Y55: Table already exists in schema
    if (sqlState.equalsIgnoreCase("42Y55"))
        return true;

    return false;
}
```

#### Retrieving Warnings

`SQLWarning` objects are a subclass of `SQLException` that deal with database access warnings. Warnings do not stop the execution of an application, as exceptions do; they simply alert the user that something did not happen as planned. For example, a warning might let you know that a privilege you attempted to revoke was not revoked. Or a warning might tell you that an error occurred during a requested disconnection.

A warning can be reported on a `Connection` object, a `Statement` object (including `PreparedStatement` and `CallableStatement` objects), or a `ResultSet` object. Each of these classes has a `getWarnings` method, which you must invoke in order to see the first warning reported on the calling object. If `getWarnings` returns a warning, you can call the `SQLWarning` method `getNextWarning` on it to get any additional warnings. Executing a statement automatically clears the warnings from a previous statement, so they do not build up. This means, however, that if you want to retrieve warnings reported on a statement, you must do so before you execute another statement.

The following methods from `JDBCTutorialUtilities` illustrate how to get complete information about any warnings reported on `Statement` or `ResultSet` objects:

```java
public static void getWarningsFromResultSet(ResultSet rs)
    throws SQLException {
    JDBCTutorialUtilities.printWarnings(rs.getWarnings());
}

public static void getWarningsFromStatement(Statement stmt)
    throws SQLException {
    JDBCTutorialUtilities.printWarnings(stmt.getWarnings());
}

public static void printWarnings(SQLWarning warning)
    throws SQLException {

    if (warning != null) {
        System.out.println("\n---Warning---\n");

    while (warning != null) {
        System.out.println("Message: " + warning.getMessage());
        System.out.println("SQLState: " + warning.getSQLState());
        System.out.print("Vendor error code: ");
        System.out.println(warning.getErrorCode());
        System.out.println("");
        warning = warning.getNextWarning();
    }
}
```

The most common warning is a `DataTruncation` warning, a subclass of `SQLWarning`. All `DataTruncation` objects have a SQLState of `01004`, indicating that there was a problem with reading or writing data. `DataTruncation` methods let you find out in which column or parameter data was truncated, whether the truncation was on a read or write operation, how many bytes should have been transferred, and how many bytes were actually transferred.

#### Categorized SQLExceptions

Your JDBC driver might throw a subclass of `SQLException` that corresponds to a common SQLState or a common error state that is not associated with a specific SQLState class value. This enables you to write more portable error-handling code. These exceptions are subclasses of one of the following classes:

- `SQLNonTransientException`
- `SQLTransientException`
- `SQLRecoverableException`

See the latest Javadoc of the `java.sql` package or the documentation of your JDBC driver for more information about these subclasses.

#### Other Subclasses of SQLException

The following subclasses of `SQLException` can also be thrown:

- `BatchUpdateException` is thrown when an error occurs during a batch update operation. In addition to the information provided by `SQLException`, `BatchUpdateException` provides the update counts for all statements that were executed before the error occurred.
- `SQLClientInfoException` is thrown when one or more client information properties could not be set on a Connection. In addition to the information provided by `SQLException`, `SQLClientInfoException` provides a list of client information properties that were not set.

<h3 id="setting-up-tables">Setting Up Tables</h3>

#### COFFEES Table

The `COFFEES` table stores information about the coffees available for sale at The Coffee Break:

<table summary="COFFEES table">
<tr>
<th id="h1"><code>COF_NAME</code></th>
<th id="h2"><code>SUP_ID</code></th>
<th id="h3"><code>PRICE</code></th>
<th id="h4"><code>SALES</code></th>
<th id="h5"><code>TOTAL</code></th>
</tr>
<tr>
<td headers="h1">Colombian</td>
<td headers="h2">101</td>
<td headers="h3">7.99</td>
<td headers="h4">0</td>
<td headers="h5">0</td>
</tr>
<tr>
<td headers="h1">French_Roast</td>
<td headers="h2">49</td>
<td headers="h3">8.99</td>
<td headers="h4">0</td>
<td headers="h5">0</td>
</tr>
<tr>
<td headers="h1">Espresso</td>
<td headers="h2">150</td>
<td headers="h3">9.99</td>
<td headers="h4">0</td>
<td headers="h5">0</td>
</tr>
<tr>
<td headers="h1">Colombian_Decaf</td>
<td headers="h2">101</td>
<td headers="h3">8.99</td>
<td headers="h4">0</td>
<td headers="h5">0</td>
</tr>
<tr>
<td headers="h1">French_Roast_Decaf</td>
<td headers="h2">49</td>
<td headers="h3">9.99</td>
<td headers="h4">0</td>
<td headers="h5">0</td>
</tr>
</table>

The following describes each of the columns in the `COFFEES` table:

- `COF_NAME`: Stores the coffee name. Holds values with a SQL type of `VARCHAR` with a maximum length of 32 characters. Because the names are different for each type of coffee sold, the name uniquely identifies a particular coffee and serves as the primary key.
- `SUP_ID`: Stores a number identifying the coffee supplier. Holds values with a SQL type of `INTEGER`. It is defined as a foreign key that references the column `SUP_ID` in the `SUPPLIERS` table. Consequently, the DBMS will enforce that each value in this column matches one of the values in the corresponding column in the `SUPPLIERS` table.
- `PRICE`: Stores the cost of the coffee per pound. Holds values with a SQL type of `FLOAT` because it needs to hold values with decimal points. (Note that money values would typically be stored in a SQL type `DECIMAL` or `NUMERIC`, but because of differences among DBMSs and to avoid incompatibility with earlier versions of JDBC, the tutorial uses the more standard type `FLOAT`.)
- `SALES`: Stores the number of pounds of coffee sold during the current week. Holds values with a SQL type of `INTEGER`.
- `TOTAL`: Stores the number of pounds of coffee sold to date. Holds values with a SQL type of `INTEGER`.

#### SUPPLIERS Table

The `SUPPLIERS` stores information about each of the suppliers:

<table summary="SUPPLIERS table">
<tr>
<th id="h101"><code>SUP_ID</code></th>
<th id="h102"><code>SUP_NAME</code></th>
<th id="h103"><code>STREET</code></th>
<th id="h104"><code>CITY</code></th>
<th id="h105"><code>STATE</code></th>
<th id="h106"><code>ZIP</code></th>
</tr>
<tr>
<td headers="h101">101</td>
<td headers="h102">Acme, Inc.</td>
<td headers="h103">99 Market Street</td>
<td headers="h104">Groundsville</td>
<td headers="h105">CA</td>
<td headers="h106">95199</td>
</tr>
<tr>
<td headers="h101">49</td>
<td headers="h102">Superior Coffee</td>
<td headers="h103">1 Party Place</td>
<td headers="h104">Mendocino</td>
<td headers="h105">CA</td>
<td headers="h106">95460</td>
</tr>
<tr>
<td headers="h101">150</td>
<td headers="h102">The High Ground</td>
<td headers="h103">100 Coffee Lane</td>
<td headers="h104">Meadows</td>
<td headers="h105">CA</td>
<td headers="h106">93966</td>
</tr>
</table>

The following describes each of the columns in the `SUPPLIERS` table:

- `SUP_ID`: Stores a number identifying the coffee supplier. Holds values with a SQL type of `INTEGER`. It is the primary key in this table.
- `SUP_NAME`: Stores the name of the coffee supplier.
- `STREET`, `CITY`, `STATE`, and `ZIP`: These columns store the address of the coffee supplier.

#### COF_INVENTORY Table

The table `COF_INVENTORY` stores information about the amount of coffee stored in each warehouse:

<table summary="COF_INVENTORY table">
<tr>
<th id="h201"><code>WAREHOUSE_ID</code></th>
<th id="h202"><code>COF_NAME</code></th>
<th id="h203"><code>SUP_ID</code></th>
<th id="h204"><code>QUAN</code></th>
<th id="h205"><code>DATE_VAL</code></th>
</tr>
<tr>
<td headers="h201">1234</td>
<td headers="h202">House_Blend</td>
<td headers="h203">49</td>
<td headers="h204">0</td>
<td headers="h205">2006_04_01</td>
</tr>
<tr>
<td headers="h201">1234</td>
<td headers="h202">House_Blend_Decaf</td>
<td headers="h203">49</td>
<td headers="h204">0</td>
<td headers="h205">2006_04_01</td>
</tr>
<tr>
<td headers="h201">1234</td>
<td headers="h202">Colombian</td>
<td headers="h203">101</td>
<td headers="h204">0</td>
<td headers="h205">2006_04_01</td>
</tr>
<tr>
<td headers="h201">1234</td>
<td headers="h202">French_Roast</td>
<td headers="h203">49</td>
<td headers="h204">0</td>
<td headers="h205">2006_04_01</td>
</tr>
<tr>
<td headers="h201">1234</td>
<td headers="h202">Espresso</td>
<td headers="h203">150</td>
<td headers="h204">0</td>
<td headers="h205">2006_04_01</td>
</tr>
<tr>
<td headers="h201">1234</td>
<td headers="h202">Colombian_Decaf</td>
<td headers="h203">101</td>
<td headers="h204">0</td>
<td headers="h205">2006_04_01</td>
</tr>
</table>

The following describes each of the columns in the `COF_INVENTORY` table:

- `WAREHOUSE_ID`: Stores a number identifying a warehouse.
- `COF_NAME`: Stores the name of a particular type of coffee.
- `SUP_ID`: Stores a number identifying a supplier.
- `QUAN`: Stores a number indicating the amount of merchandise available.
- `DATE`: Stores a timestamp value indicating the last time the row was updated.

#### MERCH_INVENTORY Table

The table `MERCH_INVENTORY` stores information about the amount of non-coffee merchandise in stock:

<table summary="MERCH_INVETORY table">
<tr>
<th id="h301"><code>ITEM_ID</code></th>
<th id="h302"><code>ITEM_NAME</code></th>
<th id="h303"><code>SUP_ID</code></th>
<th id="h304"><code>QUAN</code></th>
<th id="h305"><code>DATE</code></th>
</tr>
<tr>
<td headers="h301">00001234</td>
<td headers="h302">Cup_Large</td>
<td headers="h303">00456</td>
<td headers="h304">28</td>
<td headers="h305">2006_04_01</td>
</tr>
<tr>
<td headers="h301">00001235</td>
<td headers="h302">Cup_Small</td>
<td headers="h303">00456</td>
<td headers="h304">36</td>
<td headers="h305">2006_04_01</td>
</tr>
<tr>
<td headers="h301">00001236</td>
<td headers="h302">Saucer</td>
<td headers="h303">00456</td>
<td headers="h304">64</td>
<td headers="h305">2006_04_01</td>
</tr>
<tr>
<td headers="h301">00001287</td>
<td headers="h302">Carafe</td>
<td headers="h303">00456</td>
<td headers="h304">12</td>
<td headers="h305">2006_04_01</td>
</tr>
<tr>
<td headers="h301">00006931</td>
<td headers="h302">Carafe</td>
<td headers="h303">00927</td>
<td headers="h304">3</td>
<td headers="h305">2006_04_01</td>
</tr>
<tr>
<td headers="h301">00006935</td>
<td headers="h302">PotHolder</td>
<td headers="h303">00927</td>
<td headers="h304">88</td>
<td headers="h305">2006_04_01</td>
</tr>
<tr>
<td headers="h301">00006977</td>
<td headers="h302">Napkin</td>
<td headers="h303">00927</td>
<td headers="h304">108</td>
<td headers="h305">2006_04_01</td>
</tr>
<tr>
<td headers="h301">00006979</td>
<td headers="h302">Towel</td>
<td headers="h303">00927</td>
<td headers="h304">24</td>
<td headers="h305">2006_04_01</td>
</tr>
<tr>
<td headers="h301">00004488</td>
<td headers="h302">CofMaker</td>
<td headers="h303">08732</td>
<td headers="h304">5</td>
<td headers="h305">2006_04_01</td>
</tr>
<tr>
<td headers="h301">00004490</td>
<td headers="h302">CofGrinder</td>
<td headers="h303">08732</td>
<td headers="h304">9</td>
<td headers="h305">2006_04_01</td>
</tr>
<tr>
<td headers="h301">00004495</td>
<td headers="h302">EspMaker</td>
<td headers="h303">08732</td>
<td headers="h304">4</td>
<td headers="h305">2006_04_01</td>
</tr>
<tr>
<td headers="h301">00006914</td>
<td headers="h302">Cookbook</td>
<td headers="h303">00927</td>
<td headers="h304">12</td>
<td headers="h305">2006_04_01</td>
</tr>
</table>

The following describes each of the columns in the `MERCH_INVENTORY` table:

- `ITEM_ID`: Stores a number identifying an item.
- `ITEM_NAME`: Stores the name of an item.
- `SUP_ID`: Stores a number identifying a supplier.
- `QUAN`: Stores a number indicating the amount of that item available.
- `DATE`: Stores a timestamp value indicating the last time the row was updated.

#### COFFEE_HOUSES Table

The table `COFFEE_HOUSES` stores locations of coffee houses:

<table summary="COFFEE_HOUSES table">
<tr>
<th id="h401"><code>STORE_ID</code></th>
<th id="h402"><code>CITY</code></th>
<th id="h403"><code>COFFEE</code></th>
<th id="h404"><code>MERCH</code></th>
<th id="h405"><code>TOTAL</code></th>
</tr>
<tr>
<td headers="h401">10023</td>
<td headers="h402">Mendocino</td>
<td headers="h403">3450</td>
<td headers="h404">2005</td>
<td headers="h405">5455</td>
</tr>
<tr>
<td headers="h401">33002</td>
<td headers="h402">Seattle</td>
<td headers="h403">4699</td>
<td headers="h404">3109</td>
<td headers="h405">7808</td>
</tr>
<tr>
<td headers="h401">10040</td>
<td headers="h402">SF</td>
<td headers="h403">5386</td>
<td headers="h404">2841</td>
<td headers="h405">8227</td>
</tr>
<tr>
<td headers="h401">32001</td>
<td headers="h402">Portland</td>
<td headers="h403">3147</td>
<td headers="h404">3579</td>
<td headers="h405">6726</td>
</tr>
<tr>
<td headers="h401">10042</td>
<td headers="h402">SF</td>
<td headers="h403">2863</td>
<td headers="h404">1874</td>
<td headers="h405">4710</td>
</tr>
<tr>
<td headers="h401">10024</td>
<td headers="h402">Sacramento</td>
<td headers="h403">1987</td>
<td headers="h404">2341</td>
<td headers="h405">4328</td>
</tr>
<tr>
<td headers="h401">10039</td>
<td headers="h402">Carmel</td>
<td headers="h403">2691</td>
<td headers="h404">1121</td>
<td headers="h405">3812</td>
</tr>
<tr>
<td headers="h401">10041</td>
<td headers="h402">LA</td>
<td headers="h403">1533</td>
<td headers="h404">1007</td>
<td headers="h405">2540</td>
</tr>
<tr>
<td headers="h401">33005</td>
<td headers="h402">Olympia</td>
<td headers="h403">2733</td>
<td headers="h404">1550</td>
<td headers="h405">4283</td>
</tr>
<tr>
<td headers="h401">33010</td>
<td headers="h402">Seattle</td>
<td headers="h403">3210</td>
<td headers="h404">2177</td>
<td headers="h405">5387</td>
</tr>
<tr>
<td headers="h401">10035</td>
<td headers="h402">SF</td>
<td headers="h403">1922</td>
<td headers="h404">1056</td>
<td headers="h405">2978</td>
</tr>
<tr>
<td headers="h401">10037</td>
<td headers="h402">LA</td>
<td headers="h403">2143</td>
<td headers="h404">1876</td>
<td headers="h405">4019</td>
</tr>
<tr>
<td headers="h401">10034</td>
<td headers="h402">San_Jose</td>
<td headers="h403">1234</td>
<td headers="h404">1032</td>
<td headers="h405">2266</td>
</tr>
<tr>
<td headers="h401">32004</td>
<td headers="h402">Eugene</td>
<td headers="h403">1356</td>
<td headers="h404">1112</td>
<td headers="h405">2468</td>
</tr>
</table>

The following describes each of the columns in the `COFFEE_HOUSES` table:

`STORE_ID`: Stores a number identifying a coffee house. It indicates, among other things, the state in which the coffee house is located. A value beginning with 10, for example, means that the state is California. `STORE_ID` values beginning with 32 indicate Oregon, and those beginning with 33 indicate the state of Washington.
`CITY`: Stores the name of the city in which the coffee house is located.
`COFFEE`: Stores a number indicating the amount of coffee sold.
`MERCH`: Stores a number indicating the amount of merchandise sold.
`TOTAL`: Stores a number indicating the total amount of coffee and merchandise sold.

#### DATA_REPOSITORY Table

The table DATA_REPOSITORY stores URLs that reference documents and other data of interest to The Coffee Break. The script `populate_tables.sql` does not add any data to this table. The following describes each of the columns in this table:

- `DOCUMENT_NAME`: Stores a string that identifies the URL.
- `URL`: Stores a URL.

#### Creating Tables

You can create tables with Apache Ant or JDBC API.

##### Creating Tables with Apache Ant

To create the tables used with the tutorial sample code, run the following command in the directory `<JDBC tutorial directory>`:

```
ant setup
```

This command runs several Ant targets, including the following, `build-tables` (from the `build.xml` file):

```xml
<target name="build-tables"
  description="Create database tables">
  <sql
    driver="${DB.DRIVER}"
    url="${DB.URL}"
    userid="${DB.USER}"
    password="${DB.PASSWORD}"
    classpathref="CLASSPATH"
    delimiter="${DB.DELIMITER}"
    autocommit="false" onerror="abort">
    <transaction src=
  "./sql/${DB.VENDOR}/create-tables.sql"/>
  </sql>
</target>
```

The sample specifies values for the following `sql` Ant task parameters:

<table summary="Ant parameters">
<tr>
<th id="h501">Parameter</th>
<th id="h502">Description</th>
</tr>
<tr>
<td headers="h501"><code>driver</code></td>
<td headers="h502">Fully qualified class name of your JDBC driver. This sample uses <code>org.apache.derby.jdbc.EmbeddedDriver</code> for Java DB and <code>com.mysql.jdbc.Driver</code> for MySQL Connector/J.</td>
</tr>
<tr>
<td headers="h501"><code>url</code></td>
<td headers="h502">Database connection URL that your DBMS JDBC driver uses to connect to a database.</td>
</tr>
<tr>
<td headers="h501"><code>userid</code></td>
<td headers="h502">Name of a valid user in your DBMS.</td>
</tr>
<tr>
<td headers="h501"><code>password</code></td>
<td headers="h502">Password of the user specified in <code>userid</code></td>
</tr>
<tr>
<td headers="h501"><code>classpathref</code></td>
<td headers="h502">Full path name of the JAR file that contains the class specified in <code>driver</code></td>
</tr>
<tr>
<td headers="h501"><code>delimiter</code></td>
<td headers="h502">String or character that separates SQL statements. This sample uses the semicolon (<code>;</code>).</td>
</tr>
<tr>
<td headers="h501"><code>autocommit</code></td>
<td headers="h502">Boolean value; if set to <code>false</code>, all SQL statements are executed as one transaction.</td>
</tr>
<tr>
<td headers="h501"><code>onerror</code></td>
<td headers="h502">Action to perform when a statement fails; possible values are <code>continue</code>, <code>stop</code>, and <code>abort</code>. The value <code>abort</code> specifies that if an error occurs, the transaction is aborted.</td>
</tr>
</table>

The sample stores the values of these parameters in a separate file. The build file `build.xml` retrieves these values with the `import` task:

```xml
<import file="${ANTPROPERTIES}"/>
```

The `transaction` element specifies a file that contains SQL statements to execute. The file `create-tables.sql` contains SQL statements that create all the tables described on this page. For example, the following excerpt from this file creates the tables `SUPPLIERS` and `COFFEES`:

```sql
create table SUPPLIERS
    (SUP_ID integer NOT NULL,
    SUP_NAME varchar(40) NOT NULL,
    STREET varchar(40) NOT NULL,
    CITY varchar(20) NOT NULL,
    STATE char(2) NOT NULL,
    ZIP char(5),
    PRIMARY KEY (SUP_ID));

create table COFFEES
    (COF_NAME varchar(32) NOT NULL,
    SUP_ID int NOT NULL,
    PRICE numeric(10,2) NOT NULL,
    SALES integer NOT NULL,
    TOTAL integer NOT NULL,
    PRIMARY KEY (COF_NAME),
    FOREIGN KEY (SUP_ID)
        REFERENCES SUPPLIERS (SUP_ID));
```

**Note**: The file `build.xml` contains another target named `drop-tables` that deletes the tables used by the tutorial. The `setup` target runs `drop-tables` before running the `build-tables` target.

##### Creating Tables with JDBC API

The following method, `SuppliersTable.createTable`, creates the `SUPPLIERS` table:

```java
public void createTable() throws SQLException {
    String createString =
        "create table " + dbName +
        ".SUPPLIERS " +
        "(SUP_ID integer NOT NULL, " +
        "SUP_NAME varchar(40) NOT NULL, " +
        "STREET varchar(40) NOT NULL, " +
        "CITY varchar(20) NOT NULL, " +
        "STATE char(2) NOT NULL, " +
        "ZIP char(5), " +
        "PRIMARY KEY (SUP_ID))";

    Statement stmt = null;
    try {
        stmt = con.createStatement();
        stmt.executeUpdate(createString);
    } catch (SQLException e) {
        JDBCTutorialUtilities.printSQLException(e);
    } finally {
        if (stmt != null) { stmt.close(); }
    }
}
```

The following method, `CoffeesTable.createTable`, creates the `COFFEES` table:

```java
public void createTable() throws SQLException {
    String createString =
        "create table " + dbName +
        ".COFFEES " +
        "(COF_NAME varchar(32) NOT NULL, " +
        "SUP_ID int NOT NULL, " +
        "PRICE float NOT NULL, " +
        "SALES integer NOT NULL, " +
        "TOTAL integer NOT NULL, " +
        "PRIMARY KEY (COF_NAME), " +
        "FOREIGN KEY (SUP_ID) REFERENCES " +
        dbName + ".SUPPLIERS (SUP_ID))";

    Statement stmt = null;
    try {
        stmt = con.createStatement();
        stmt.executeUpdate(createString);
    } catch (SQLException e) {
        JDBCTutorialUtilities.printSQLException(e);
    } finally {
        if (stmt != null) { stmt.close(); }
    }
}
```

In both methods, `con` is a `Connection` object and `dbName` is the name of the database in which you are creating the table.

To execute the SQL query, such as those specified by the `String createString`, use a `Statement` object. To create a `Statement` object, call the method `Connection.createStatement` from an existing `Connection` object. To execute a SQL query, call the method `Statement.executeUpdate`.

All `Statement` objects are closed when the connection that created them is closed. However, it is good coding practice to explicitly close `Statement` objects as soon as you are finished with them. This allows any external resources that the statement is using to be released immediately. Close a statement by calling the method `Statement.close`. Place this statement in a `finally` to ensure that it closes even if the normal program flow is interrupted because an exception (such as `SQLException`) is thrown.

**Note**: You must create the `SUPPLIERS` table before the `COFFEES` because `COFFEES` contains a foreign key, `SUP_ID` that references `SUPPLIERS`.

#### Populating Tables

Similarly, you can insert data into tables with Apache Ant or JDBC API.

##### Populating Tables with Apache Ant

In addition to creating the tables used by this tutorial, the command `ant setup` also populates these tables. This command runs the Ant target `populate-tables`, which runs the SQL script `populate-tables.sql`.

The following is an excerpt from `populate-tables.sql` that populates the tables `SUPPLIERS` and `COFFEES`:

```sql
insert into SUPPLIERS values(
    49, 'Superior Coffee', '1 Party Place',
    'Mendocino', 'CA', '95460');
insert into SUPPLIERS values(
    101, 'Acme, Inc.', '99 Market Street',
    'Groundsville', 'CA', '95199');
insert into SUPPLIERS values(
    150, 'The High Ground',
    '100 Coffee Lane', 'Meadows', 'CA', '93966');
insert into COFFEES values(
    'Colombian', 00101, 7.99, 0, 0);
insert into COFFEES values(
    'French_Roast', 00049, 8.99, 0, 0);
insert into COFFEES values(
    'Espresso', 00150, 9.99, 0, 0);
insert into COFFEES values(
    'Colombian_Decaf', 00101, 8.99, 0, 0);
insert into COFFEES values(
    'French_Roast_Decaf', 00049, 9.99, 0, 0);
```

##### Populating Tables with JDBC API

The following method, `SuppliersTable.populateTable`, inserts data into the table:

```java
public void populateTable() throws SQLException {

    Statement stmt = null;
    try {
        stmt = con.createStatement();
        stmt.executeUpdate(
            "insert into " + dbName +
            ".SUPPLIERS " +
            "values(49, 'Superior Coffee', " +
            "'1 Party Place', " +
            "'Mendocino', 'CA', '95460')");

        stmt.executeUpdate(
            "insert into " + dbName +
            ".SUPPLIERS " +
            "values(101, 'Acme, Inc.', " +
            "'99 Market Street', " +
            "'Groundsville', 'CA', '95199')");

        stmt.executeUpdate(
            "insert into " + dbName +
            ".SUPPLIERS " +
            "values(150, " +
            "'The High Ground', " +
            "'100 Coffee Lane', " +
            "'Meadows', 'CA', '93966')");
    } catch (SQLException e) {
        JDBCTutorialUtilities.printSQLException(e);
    } finally {
        if (stmt != null) { stmt.close(); }
    }
}
```

The following method, `CoffeesTable.populateTable`, inserts data into the table:

```java
public void populateTable() throws SQLException {

    Statement stmt = null;
    try {
        stmt = con.createStatement();
        stmt.executeUpdate(
            "insert into " + dbName +
            ".COFFEES " +
            "values('Colombian', 00101, " +
            "7.99, 0, 0)");

        stmt.executeUpdate(
            "insert into " + dbName +
            ".COFFEES " +
            "values('French_Roast', " +
            "00049, 8.99, 0, 0)");

        stmt.executeUpdate(
            "insert into " + dbName +
            ".COFFEES " +
            "values('Espresso', 00150, 9.99, 0, 0)");

        stmt.executeUpdate(
            "insert into " + dbName +
            ".COFFEES " +
            "values('Colombian_Decaf', " +
            "00101, 8.99, 0, 0)");

        stmt.executeUpdate(
            "insert into " + dbName +
            ".COFFEES " +
            "values('French_Roast_Decaf', " +
            "00049, 9.99, 0, 0)");
    } catch (SQLException e) {
        JDBCTutorialUtilities.printSQLException(e);
    } finally {
        if (stmt != null) {
          stmt.close();
        }
    }
}
```

<h3 id="retrieving-and-modifying-values-from-result-sets">Retrieving and Modifying Values from Result Sets</h3>

The following method, `CoffeesTable.viewTable` outputs the contents of the `COFFEES` tables, and demonstrates the use of `ResultSet` objects and cursors:

```java
public static void viewTable(Connection con, String dbName)
    throws SQLException {

    Statement stmt = null;
    String query =
        "select COF_NAME, SUP_ID, PRICE, " +
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

A `ResultSet` object is a table of data representing a database result set, which is usually generated by executing a statement that queries the database. For example, the `CoffeeTables.viewTable` method creates a `ResultSet`, `rs`, when it executes the query through the `Statement` object, `stmt`. Note that a `ResultSet` object can be created through any object that implements the `Statement` interface, including `PreparedStatement`, `CallableStatement`, and `RowSet`.

You access the data in a `ResultSet` object through a cursor. Note that this cursor is not a database cursor. This cursor is a pointer that points to one row of data in the `ResultSet`. Initially, the cursor is positioned before the first row. The method `ResultSet.next` moves the cursor to the next row. This method returns `false` if the cursor is positioned after the last row. This method repeatedly calls the `ResultSet.next` method with a `while` loop to iterate through all the data in the `ResultSet`.

#### ResultSet Interface

The `ResultSet` interface provides methods for retrieving and manipulating the results of executed queries, and `ResultSet` objects can have different functionality and characteristics. These characteristics are type, concurrency, and cursor *holdability*.

##### ResultSet Types

The type of a `ResultSet` object determines the level of its functionality in two areas: the ways in which the cursor can be manipulated, and how concurrent changes made to the underlying data source are reflected by the `ResultSet` object.

The sensitivity of a `ResultSet` object is determined by one of three different `ResultSet` types:

- `TYPE_FORWARD_ONLY`: The result set cannot be scrolled; its cursor moves forward only, from before the first row to after the last row. The rows contained in the result set depend on how the underlying database generates the results. That is, it contains the rows that satisfy the query at either the time the query is executed or as the rows are retrieved.
- `TYPE_SCROLL_INSENSITIVE`: The result can be scrolled; its cursor can move both forward and backward relative to the current position, and it can move to an absolute position. The result set is insensitive to changes made to the underlying data source while it is open. It contains the rows that satisfy the query at either the time the query is executed or as the rows are retrieved.
- `TYPE_SCROLL_SENSITIVE`: The result can be scrolled; its cursor can move both forward and backward relative to the current position, and it can move to an absolute position. The result set reflects changes made to the underlying data source while the result set remains open.

The default `ResultSet` type is `TYPE_FORWARD_ONLY`.

**Note**: Not all databases and JDBC drivers support all `ResultSet` types. The method `DatabaseMetaData.supportsResultSetType` returns `true` if the specified `ResultSet` type is supported and `false` otherwise.

##### ResultSet Concurrency

The concurrency of a `ResultSet` object determines what level of update functionality is supported.

There are two concurrency levels:

- `CONCUR_READ_ONLY`: The `ResultSet` object cannot be updated using the `ResultSet` interface.
- `CONCUR_UPDATABLE`: The `ResultSet` object can be updated using the `ResultSet` interface.

The default `ResultSet` concurrency is `CONCUR_READ_ONLY`.

**Note**: Not all JDBC drivers and databases support concurrency. The method `DatabaseMetaData.supportsResultSetConcurrency` returns `true` if the specified concurrency level is supported by the driver and false otherwise.

The method `CoffeesTable.modifyPrices` demonstrates how to use a `ResultSet` object whose concurrency level is `CONCUR_UPDATABLE`.

##### Cursor Holdability

Calling the method `Connection.commit` can close the `ResultSet` objects that have been created during the current transaction. In some cases, however, this may not be the desired behavior. The `ResultSet` property holdability gives the application control over whether ResultSet objects (cursors) are closed when commit is called.

The following `ResultSet` constants may be supplied to the `Connection` methods createStatement, prepareStatement, and prepareCall:

- `HOLD_CURSORS_OVER_COMMIT`: `ResultSet` cursors are not closed; they are *holdable*: they are held open when the method `commit` is called. Holdable cursors might be ideal if your application uses mostly read-only `ResultSet` objects.
- `CLOSE_CURSORS_AT_COMMIT`: `ResultSet` objects (cursors) are closed when the `commit` method is called. Closing cursors when this method is called can result in better performance for some applications.

The default cursor holdability varies depending on your DBMS.

**Note**: Not all JDBC drivers and databases support holdable and non-holdable cursors. The following method, `JDBCTutorialUtilities.cursorHoldabilitySupport`, outputs the default cursor holdability of `ResultSet` objects and whether `HOLD_CURSORS_OVER_COMMIT` and `CLOSE_CURSORS_AT_COMMIT` are supported:

```java
public static void cursorHoldabilitySupport(Connection conn)
    throws SQLException {

    DatabaseMetaData dbMetaData = conn.getMetaData();
    System.out.println("ResultSet.HOLD_CURSORS_OVER_COMMIT = " +
        ResultSet.HOLD_CURSORS_OVER_COMMIT);

    System.out.println("ResultSet.CLOSE_CURSORS_AT_COMMIT = " +
        ResultSet.CLOSE_CURSORS_AT_COMMIT);

    System.out.println("Default cursor holdability: " +
        dbMetaData.getResultSetHoldability());

    System.out.println("Supports HOLD_CURSORS_OVER_COMMIT? " +
        dbMetaData.supportsResultSetHoldability(
            ResultSet.HOLD_CURSORS_OVER_COMMIT));

    System.out.println("Supports CLOSE_CURSORS_AT_COMMIT? " +
        dbMetaData.supportsResultSetHoldability(
            ResultSet.CLOSE_CURSORS_AT_COMMIT));
}
```

#### Retrieving Column Values from Rows

The `ResultSet` interface declares getter methods (for example, `getBoolean` and `getLong`) for retrieving column values from the current row. You can retrieve values using either the index number of the column or the alias or name of the column. The column index is usually more efficient. Columns are numbered from 1. For maximum portability, result set columns within each row should be read in left-to-right order, and each column should be read only once.

For example, the following method, `CoffeesTable.alternateViewTable`, retrieves column values by number:

```java
public static void alternateViewTable(Connection con)
    throws SQLException {

    Statement stmt = null;
    String query =
        "select COF_NAME, SUP_ID, PRICE, " +
        "SALES, TOTAL from COFFEES";

    try {
        stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            String coffeeName = rs.getString(1);
            int supplierID = rs.getInt(2);
            float price = rs.getFloat(3);
            int sales = rs.getInt(4);
            int total = rs.getInt(5);
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

Strings used as input to getter methods are case-insensitive. When a getter method is called with a string and more than one column has the same alias or name as the string, the value of the first matching column is returned. The option to use a string as opposed to an integer is designed to be used when column aliases and names are used in the SQL query that generated the result set. For columns that are *not* explicitly named in the query (for example, `select * from COFFEES`) it is best to use column numbers. If column names are used, the developer should guarantee that they uniquely refer to the intended columns by using column aliases. A column alias effectively renames the column of a result set. To specify a column alias, use the SQL `AS` clause in the `SELECT` statement.

The getter method of the appropriate type retrieves the value in each column. For example, in the method `CoffeeTables.viewTable`, the first column in each row of the `ResultSet rs` is `COF_NAME`, which stores a value of SQL type `VARCHAR`. The method for retrieving a value of SQL type `VARCHAR` is `getString`. The second column in each row stores a value of SQL type `INTEGER`, and the method for retrieving values of that type is `getInt`.

Note that although the method `getString` is recommended for retrieving the SQL types `CHAR` and `VARCHAR`, it is possible to retrieve any of the basic SQL types with it. Getting all values with `getString` can be very useful, but it also has its limitations. For instance, if it is used to retrieve a numeric type, `getString` converts the numeric value to a Java `String` object, and the value has to be converted back to a numeric type before it can be operated on as a number. In cases where the value is treated as a string anyway, there is no drawback. Furthermore, if you want an application to retrieve values of any standard SQL type other than SQL3 types, use the `getString` method.

#### Cursors

As mentioned previously, you access the data in a `ResultSet` object through a cursor, which points to one row in the `ResultSet` object. However, when a `ResultSet` object is first created, the cursor is positioned before the first row. The method `CoffeeTables.viewTable` moves the cursor by calling the `ResultSet.next` method. There are other methods available to move the cursor:

- `next`: Moves the cursor forward one row. Returns `true` if the cursor is now positioned on a row and `false` if the cursor is positioned after the last row.
- `previous`: Moves the cursor backward one row. Returns `true` if the cursor is now positioned on a row and `false` if the cursor is positioned before the first row.
- `first`: Moves the cursor to the first row in the ResultSet object. Returns true if the cursor is now positioned on the first row and false if the ResultSet object does not contain any rows.
- `last`: Moves the cursor to the last row in the `ResultSet` object. Returns `true` if the cursor is now positioned on the last row and `false` if the ResultSet object does not contain any rows.
- `beforeFirst`: Positions the cursor at the start of the `ResultSet` object, before the first row. If the `ResultSet` object does not contain any rows, this method has no effect.
- `afterLast`: Positions the cursor at the end of the `ResultSet` object, after the last row. If the `ResultSet` object does not contain any rows, this method has no effect.
- `relative(int rows)`: Moves the cursor relative to its current position.
- `absolute(int row)`: Positions the cursor on the row specified by the parameter row.

Note that the default sensitivity of a `ResultSet` is `TYPE_FORWARD_ONLY`, which means that it cannot be scrolled; you cannot call any of these methods that move the cursor, except `next`, if your `ResultSet` cannot be scrolled. The method `CoffeesTable.modifyPrices`, described in the following section, demonstrates how you can move the cursor of a `ResultSet`.

#### Updating Rows in ResultSet Objects

You cannot update a default `ResultSet` object, and you can only move its cursor forward. However, you can create `ResultSet` objects that can be scrolled (the cursor can move backwards or move to an absolute position) and updated.

The following method, `CoffeesTable.modifyPrices`, multiplies the `PRICE` column of each row by the argument `percentage`:

```java
public void modifyPrices(float percentage) throws SQLException {

    Statement stmt = null;
    try {
        stmt = con.createStatement();
        stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                   ResultSet.CONCUR_UPDATABLE);
        ResultSet uprs = stmt.executeQuery(
            "SELECT * FROM " + dbName + ".COFFEES");

        while (uprs.next()) {
            float f = uprs.getFloat("PRICE");
            uprs.updateFloat( "PRICE", f * percentage);
            uprs.updateRow();
        }

    } catch (SQLException e ) {
        JDBCTutorialUtilities.printSQLException(e);
    } finally {
        if (stmt != null) { stmt.close(); }
    }
}
```

The field `ResultSet.TYPE_SCROLL_SENSITIVE` creates a `ResultSet` object whose cursor can move both forward and backward relative to the current position and to an absolute position. The field `ResultSet.CONCUR_UPDATABLE` creates a `ResultSet` object that can be updated. See the `ResultSet` Javadoc for other fields you can specify to modify the behavior of `ResultSet` objects.

The method `ResultSet.updateFloat` updates the specified column (in this example, `PRICE` with the specified `float` value in the row where the cursor is positioned. `ResultSet` contains various updater methods that enable you to update column values of various data types. However, none of these updater methods modifies the database; you must call the method `ResultSet.updateRow` to update the database.

#### Using Statement Objects for Batch Updates

`Statement`, `PreparedStatement` and `CallableStatement` objects have a list of commands that is associated with them. This list may contain statements for updating, inserting, or deleting a row; and it may also contain DDL statements such as `CREATE TABLE` and `DROP TABLE`. It cannot, however, contain a statement that would produce a `ResultSet` object, such as a `SELECT` statement. In other words, the list can contain only statements that produce an update count.

The list, which is associated with a `Statement` object at its creation, is initially empty. You can add SQL commands to this list with the method `addBatch` and empty it with the method `clearBatch`. When you have finished adding statements to the list, call the method `executeBatch` to send them all to the database to be executed as a unit, or batch.

For example, the following method `CoffeesTable.batchUpdate` adds four rows to the `COFFEES` table with a batch update:

```java
public void batchUpdate() throws SQLException {

    Statement stmt = null;
    try {
        this.con.setAutoCommit(false);
        stmt = this.con.createStatement();

        stmt.addBatch(
            "INSERT INTO COFFEES " +
            "VALUES('Amaretto', 49, 9.99, 0, 0)");

        stmt.addBatch(
            "INSERT INTO COFFEES " +
            "VALUES('Hazelnut', 49, 9.99, 0, 0)");

        stmt.addBatch(
            "INSERT INTO COFFEES " +
            "VALUES('Amaretto_decaf', 49, " +
            "10.99, 0, 0)");

        stmt.addBatch(
            "INSERT INTO COFFEES " +
            "VALUES('Hazelnut_decaf', 49, " +
            "10.99, 0, 0)");

        int [] updateCounts = stmt.executeBatch();
        this.con.commit();

    } catch(BatchUpdateException b) {
        JDBCTutorialUtilities.printBatchUpdateException(b);
    } catch(SQLException ex) {
        JDBCTutorialUtilities.printSQLException(ex);
    } finally {
        if (stmt != null) { stmt.close(); }
        this.con.setAutoCommit(true);
    }
}
```

The following line disables auto-commit mode for the `Connection` object con so that the transaction will not be automatically committed or rolled back when the method `executeBatch` is called.

```java
this.con.setAutoCommit(false);
```

To allow for correct error handling, you should always disable auto-commit mode before beginning a batch update.

The method `Statement.addBatch` adds a command to the list of commands associated with the `Statement` object `stmt`. In this example, these commands are all `INSERT INTO` statements, each one adding a row consisting of five column values. The values for the columns `COF_NAME` and `PRICE` are the name of the coffee and its price, respectively. The second value in each row is 49 because that is the identification number for the supplier, Superior Coffee. The last two values, the entries for the columns `SALE`S and `TOTAL`, all start out being zero because there have been no sales yet. (`SALES` is the number of pounds of this row's coffee sold in the current week; `TOTAL` is the total of all the cumulative sales of this coffee.)

The following line sends the four SQL commands that were added to its list of commands to the database to be executed as a batch:

```java
int [] updateCounts = stmt.executeBatch();
```

Note that `stmt` uses the method `executeBatch` to send the batch of insertions, not the method `executeUpdate`, which sends only one command and returns a single update count. The DBMS executes the commands in the order in which they were added to the list of commands, so it will first add the row of values for Amaretto, then add the row for Hazelnut, then Amaretto decaf, and finally Hazelnut decaf. If all four commands execute successfully, the DBMS will return an update count for each command in the order in which it was executed. The update counts that indicate how many rows were affected by each command are stored in the array `updateCounts`.

If all four of the commands in the batch are executed successfully, `updateCounts` will contain four values, all of which are 1 because an insertion affects one row. The list of commands associated with `stmt` will now be empty because the four commands added previously were sent to the database when `stmt` called the method `executeBatch`. You can at any time explicitly empty this list of commands with the method `clearBatch`.

The `Connection.commit` method makes the batch of updates to the `COFFEES` table permanent. This method needs to be called explicitly because the auto-commit mode for this connection was disabled previously.

The following line enables auto-commit mode for the current `Connection` object.

```java
this.con.setAutoCommit(true);
```

Now each statement in the example will automatically be committed after it is executed, and it no longer needs to invoke the method `commit`.

##### Performing Parameterized Batch Update

It is also possible to have a parameterized batch update, as shown in the following code fragment, where con is a `Connection` object:

```java
con.setAutoCommit(false);
PreparedStatement pstmt = con.prepareStatement(
                              "INSERT INTO COFFEES VALUES( " +
                              "?, ?, ?, ?, ?)");
pstmt.setString(1, "Amaretto");
pstmt.setInt(2, 49);
pstmt.setFloat(3, 9.99);
pstmt.setInt(4, 0);
pstmt.setInt(5, 0);
pstmt.addBatch();

pstmt.setString(1, "Hazelnut");
pstmt.setInt(2, 49);
pstmt.setFloat(3, 9.99);
pstmt.setInt(4, 0);
pstmt.setInt(5, 0);
pstmt.addBatch();

// ... and so on for each new
// type of coffee

int [] updateCounts = pstmt.executeBatch();
con.commit();
con.setAutoCommit(true);
```

##### Handling Batch Update Exceptions

You will get a `BatchUpdateException` when you call the method `executeBatch` if (1) one of the SQL statements you added to the batch produces a result set (usually a query) or (2) one of the SQL statements in the batch does not execute successfully for some other reason.

You should not add a query (a `SELECT` statement) to a batch of SQL commands because the method `executeBatch`, which returns an array of update counts, expects an update count from each SQL statement that executes successfully. This means that only commands that return an update count (commands such as `INSERT INTO, UPDATE, DELET`E) or that return 0 (such as `CREATE TABLE, DROP TABLE, ALTER TABLE`) can be successfully executed as a batch with the `executeBatch` method.

A `BatchUpdateException` contains an array of update counts that is similar to the array returned by the method `executeBatch`. In both cases, the update counts are in the same order as the commands that produced them. This tells you how many commands in the batch executed successfully and which ones they are. For example, if five commands executed successfully, the array will contain five numbers: the first one being the update count for the first command, the second one being the update count for the second command, and so on.

`BatchUpdateException` is derived from `SQLException`. This means that you can use all of the methods available to an `SQLException` object with it. The following method, `JDBCTutorialUtilities.printBatchUpdateException` prints all of the `SQLException` information plus the update counts contained in a `BatchUpdateException` object. Because `BatchUpdateException.getUpdateCounts` returns an array of `int`, the code uses a `for` loop to print each of the update counts:

```java
public static void printBatchUpdateException(BatchUpdateException b) {

    System.err.println("----BatchUpdateException----");
    System.err.println("SQLState:  " + b.getSQLState());
    System.err.println("Message:  " + b.getMessage());
    System.err.println("Vendor:  " + b.getErrorCode());
    System.err.print("Update counts:  ");
    int [] updateCounts = b.getUpdateCounts();

    for (int i = 0; i < updateCounts.length; i++) {
        System.err.print(updateCounts[i] + "   ");
    }
}
```

#### Inserting Rows in ResultSet Objects

**Note**: Not all JDBC drivers support inserting new rows with the `ResultSet` interface. If you attempt to insert a new row and your JDBC driver database does not support this feature, a `SQLFeatureNotSupportedException` exception is thrown.

The following method, `CoffeesTable.insertRow`, inserts a row into the `COFFEES` through a `ResultSet` object:

```java
public void insertRow(String coffeeName, int supplierID,
                      float price, int sales, int total)
    throws SQLException {

    Statement stmt = null;
    try {
        stmt = con.createStatement(
            ResultSet.TYPE_SCROLL_SENSITIVE
            ResultSet.CONCUR_UPDATABLE);

        ResultSet uprs = stmt.executeQuery(
            "SELECT * FROM " + dbName +
            ".COFFEES");

        uprs.moveToInsertRow();
        uprs.updateString("COF_NAME", coffeeName);
        uprs.updateInt("SUP_ID", supplierID);
        uprs.updateFloat("PRICE", price);
        uprs.updateInt("SALES", sales);
        uprs.updateInt("TOTAL", total);

        uprs.insertRow();
        uprs.beforeFirst();
    } catch (SQLException e ) {
        JDBCTutorialUtilities.printSQLException(e);
    } finally {
        if (stmt != null) { stmt.close(); }
    }
}
```

This example calls the `Connection.createStatement` method with two arguments, `ResultSet.TYPE_SCROLL_SENSITIVE` and `ResultSet.CONCUR_UPDATABLE`. The first value enables the cursor of the `ResultSet` object to be moved both forward and backward. The second value, `ResultSet.CONCUR_UPDATABLE`, is required if you want to insert rows into a `ResultSet` object; it specifies that it can be updatable.

The same stipulations for using strings in getter methods also apply to updater methods.

The method `ResultSet.moveToInsertRow` moves the cursor to the insert row. The insert row is a special row associated with an updatable result set. It is essentially a buffer where a new row can be constructed by calling the updater methods prior to inserting the row into the result set. For example, this method calls the method `ResultSet.updateString` to update the insert row's `COF_NAME` column to `Kona`.

The method `ResultSet.insertRow` inserts the contents of the insert row into the `ResultSet` object and into the database.

**Note**: After inserting a row with the `ResultSet.insertRow`, you should move the cursor to a row other than the insert row. For example, this example moves it to before the first row in the result set with the method `ResultSet.beforeFirst`. Unexpected results can occur if another part of your application uses the same result set and the cursor is still pointing to the insert row.
