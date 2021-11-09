# Investiments Manager API

An API for investiments management build with Java, Spring Boot and Spring Framework.
Investments Manager API can be use by beginning investors to diversify their investments.

## Features

* Create a Company to invest: `POST/api/v1/companies` **[admin access]**
* Get all companies or by activation status: `GET/api/v1/companies?fields=status`
  * `status` **Boolean(true|false)** *(optional)*: Filter companies by activation status
* Find a company by id: `GET/api/v1/companies/{companyId}`
* Update a company by id: `PUT/api/v1/companies/{companyId}` **[admin access]**
* Delete a company by id: `DELETE/api/v1/companies/{companyId}` **[admin access]**
* Create a Client account: `POST/api/v1/clients` **[admin access]**
* Get all clients: `GET/api/v1/clients` **[admin access]**
* Find a client by id: `GET/api/v1/clients/{clientId}`
* Update a client by id: `PUT/api/v1/clients/{clientId}` **[admin access]**
* Delete a client by id: `DELETE/api/v1/clients/{clientId}` **[admin access]**
* List all client acquired actions: `GET/api/v1/clients/{clientId}/actions`
* Client acquire actions distributed by active companies: `POST/api/v1/clients/{clientId}/actions?fields=totalAmount,totalCompanies`
  * `totalAmount` **Number** *(mandatory)*: The total amount to invest.
  * `totalCompanies` **Number** *(optional)*: Client can choose how many companies they want to invest in.

## Requirements

* Java 16 (AdoptOpenJDK 16.0.1.hs-adpt)
* Maven 3.6.3
* Git 2.7+
* Apache Tomcat 9+


## Instalation

* Clone project from Gitlab

```bash
git clone git@gitlab.com:liviacjbvilaca/investimentsmanager.git
```


### Compile and Package

* Inside the project directory, generate `jar`

```bash
mvn package
```

### Test

* For unit test phase, you can run:

```bash
mvn test
```

* To run all tests (including Integration Tests):

```bash
mvn integration-test
```

### Execution

* Before running, edit `resources/data.sql` with your admin user information.  

* To run:

```bash
java -jar target/investimentsmanager-0.0.1-SNAPSHOT.jar
```

or using `Foreman`

```bash
foreman start
```

## Documentation

* Swagger (development environment): [http://localhost:8080/swagger-ui](http://localhost:8080/swagger-ui)
