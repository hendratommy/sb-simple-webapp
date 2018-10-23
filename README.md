# sb-simple-webapp
## Spring Boot Simple Webapp Template

Basic template to get started with Spring Boot Webapp.
This template have traditional Web Pages (Thymeleaf) for front end, exposed simple REST API Endpoint secured using Spring Security, the backend is using Spring-Data-JPA + Hibernate, and using Hibernate Envers to provide Audit Trail feature.

### Front End (Web Pages)
Using Thymeleaf and Semantic-UI
Secured using Username Password Authentication
File upload example for public files and private files

### REST API
Secured using Basic Authentication
User (Client) accessing API Endpoints have different credential with normal Users, and must be registered in table tbl_api_clients

### Spring Boot Actuator + Spring Boot Admin Client
The Actuator endpoint is secured using the same security schema as REST API
The Client must have ACTUATOR_CLIENT permission
