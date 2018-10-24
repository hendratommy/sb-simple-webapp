# sb-simple-webapp
## Spring Boot Simple Webapp Template

Basic template to get started with Spring Boot Webapp.
This template have traditional Web Pages (Thymeleaf) for front end, exposed simple REST API Endpoint secured using Spring Security, the backend is using Spring-Data-JPA + Hibernate, Hibernate Envers to provide Audit Trail feature, and Spring Boot Admin Client for monitoring.

### Front End (Web Pages)
Using Thymeleaf and Semantic-UI
Secured using Username Password Authentication
File upload example for public files and private files
#### Scenario
The scenario this webapp demonstrate is:
- Anyone can see and upload files in Public page. The uploaded files is shared
- Authenticated user can access both Public and Private pages. In private files, user can upload and see uploaded files privately to that user only

### REST API
Secured using Basic Authentication
User (Client) accessing API Endpoints have different credential with normal Users, and must be registered in table tbl_api_clients

### Spring Boot Actuator + Spring Boot Admin Client
The Actuator endpoint is secured using the same security schema as REST API
The Client must have ACTUATOR_CLIENT permission to access actuator endpoints

## Getting Started
1. Clone/download this repository
2. Import the project
3. Create two new Schema in MariaDB named sb_simple_webapp and sb_simple_webapp_audit
4. Open and edit config/application.properties accordingly (such as datasource username and password, default is sa)
5. Run the project with argument `--install`
This will setup the table entries (required for the first run only). See SystemService.java for details
6. Open web browser and browse to http://localhost:8080
7. (Optional) Spring Boot Admin Server is required for monitoring purposed, take a look at https://codecentric.github.io/spring-boot-admin/current/ if you want to install it
