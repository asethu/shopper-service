# Instacart Shopper Service

RESTful service that exposes a bunch of APIs for Instacart shoppers registration flow. The service uses a SQLite database to persist data.

## Requirements

The project source is written in [Java](http://www.oracle.com/technetwork/java/javase) using [Jersey](https://jersey.java.net/) framework. [Apache Maven](http://maven.apache.org/) is used for dependency management.

You need to have Java SE (JDK) and Maven installed before processing further.

* Install [Java JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* Install [Apache Maven](http://maven.apache.org/download.cgi)

## Building and Running the Service

Once you have cloned the repository follow the steps below to build and run the service.

### Build the package

```bash
$ mvn clean compile
```

### Seed the database

```bash
$ mvn exec:java -Dexec.mainClass=com.instacart.shopper.SeedDatabase
```

The seed operation creates a SQLite database file under the directory `db/` with name `development.sqlite3`. The seed operation uses the sample data from the challenge question description (under part2's funnel report).

### Start the service

```bash
$ mvn exec:java -Dexec.mainClass=com.instacart.shopper.ServiceMain
...
Jersey app started with WADL available at http://localhost:9000/application.wadl
Hit enter to stop it...
```

Once the servce has been successfully started, you will see something like this the command line. You can hit the REST endpoint using the following URL,

```
http://localhost:9000/
```

The application log for the service can be found under the directory `target/`.

```
./target/application.log
```

## REST Endpoints

### Creating an Applicant

```
POST http://localhost:9000/applicants
{
    "firstName": ...,
    "lastName": ...,
    "region": ...,
    "phone": ...,
    "email": ...,
    "phoneType": ...,
    "over21": ...
}
```

### Getting an Applicant by email

```
GET http://localhost:9000/applicants/{email}
```

### Updating an Applicant's details by email

```
PATCH http://localhost:9000/applicants/{email}
{
    "region": ...,
    "phone": ...,
    ...
}
```

### Getting the Applicant's funnel report

```
GET http://localhost:9000/applicants/funnel.json?start_date=YYYY-MM-DD&end_date=YYYY-MM-DD
```

> For accessing the shopper webapp, set up [shopper client](https://github.com/asethu/shopper-client) application by following the instructions in the repository.