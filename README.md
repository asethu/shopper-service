# Instacart Shopper Service

RESTful service that exposes a bunch of APIs for Instacart shoppers registration flow. The service uses a SQLite database to persist data.

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

The seed operation uses the sample data from the challenge question description (under part2's funnel report).

### Start the service

```bash
$ mvn exec:java -Dexec.mainClass=com.instacart.shopper.ServiceMain
...
Jersey app started with WADL available at http://localhost:9090/application.wadl
Hit enter to stop it...
```

Once the servce has been successfully started, you will see something like this the command line. You can hit the REST endpoint using the following URL,

```
http://localhost:9090/
```

The application log for the service can be found under the directory `target`.

```
./target/application.log
```

## REST Endpoints

### Creating an Applicant

```
POST http://localhost:9090/applicants
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
GET http://localhost:9090/applicants/{email}
```

### Updating an Applicant's details by email

```
PATCH http://localhost:9090/applicants/{email}
{
    "region": ...,
    "phone": ...,
    ...
}
```

### Getting the Applicant's funnel report

```
GET http://localhost:9090/applicants/funnel?start_date=YYYY-MM-DD&end_date=YYYY-MM-DD
```