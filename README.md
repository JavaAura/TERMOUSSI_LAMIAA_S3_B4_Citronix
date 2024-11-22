Citronix - Lemon Farm Management System

Citronix is a REST API developed in Java Spring Boot, designed to manage lemon farms. It allows farmers to effectively track production from planting to sale.
Features:
    
    - Farm Management :  Create, update, and manage farm details (name, location, area).
    - Field Management : CRUD, Associate fields with farms, ensuring area consistency
    - Tree Management : CRUD, Track trees, calculate age, and determine productivity based on tree age
    - Harvest Managemet : CRUD, Track seasonal harvests, including quantities harvested per tree
    - Sales Management : CRUD, Record sales, calculate revenue based on harvest quantities and unit prices
Technologies Used
      
    Java 1.8
    Spring Boot
    Maven
    Spring Data JPA
    MapStruct
    Lombok
    JUnit & Mockito
    Swagger for API documentation
    MySQL database
Prerequisites

    JDK 1.8 or higher
    Maven 2.7.18 or higher
    PostgreSQL for the database
Installation
  Clone the repository:

     git clone https://github.com/TERMOUSSI-LAMIAA/citronix.git
  Navigate to the project folder:

    cd citronix
  Install dependencies:

    mvn install
  Configure  database (PostgreSQL):

    Update application.properties with database connection details
  The API will be accessible at http://localhost:8080
API Documentation

    http://localhost:8080/swagger-ui/index.html
Author and Contact Information

    Termoussi Lamiaa 
    Email: lamiaa3105@gmail.com
