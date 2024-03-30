# 🏦 Bank Application 

# Project Description
The Bank Application is a simple yet comprehensive banking system designed to facilitate user interactions with their accounts securely. Users can perform various operations including registration, login, account management, transactions, and transaction history viewing. This project aims to provide a seamless banking experience for users while maintaining the security and integrity of their financial data. Additionally, it utilizes the _app.freecurrencyapi.com_ open API to fetch the latest exchange rate data based on the Canadian dollar (CAD) for currency conversion purposes.

# Backend Project
This is a backend project that develops server-side functionality to support the booking system. This includes database management, API development, authentication, and business logic implementation.

# Testing
The project employs JUnit 5 for testing purposes. Unit tests are implemented to ensure the correctness and reliability of backend functionalities. Testing covers various scenarios including positive and negative cases, boundary conditions, and error handling.


# ERD
![ERD](./img/bank_erd.png)

# Features 
## 🦱 User Management
- **Register:** Users can create accounts to access the booking system.

- **Login/Logout:**
  - Secure authentication for users using Spring Security.
  - Upon login, members receive JWT token for subsequent requests.
  
- **JWT Authentication and Authorization:**
  - JWT Token Usage: Clients include the JWT token in the Authorization header of requests.
  - Authentication: The server verifies the token's signature to authenticate users.
  - Authorization: Certain endpoints require specific roles or permissions, which are encoded in the JWT token.
  - Token Expiry: JWT tokens have a limited validity period for enhanced security.

- **Update User Information:** Users can update their email, first name, and last name. 


## 💳 Account Management
- **Account Creation:**
  - Users can create new accounts.
  - The system will create a random, unique account number.
- **Account Retrieval**: Users can retrieve details of specific accounts.
- **Account List Retrieval**: Provides users with a list of all their accounts.
- **Account Deletion**: Users can delete their accounts.

## 💰 Account Transactions
- **Deposit**: Deposit funds into accounts via an ATM transaction.
- **Withdrawal**: Withdraw funds from accounts via an ATM transaction.
- **Transfer**: Transfer funds between user accounts securely.

## 🔁 Transaction Management
- **Transaction List Retrieval**: Users can retrieve a list of their account transactions based on specified filters.
- **Transaction Details**: Users can view detailed information about a specific transaction.

## 💱 Currency Management System
The bank app provides users with access to the latest exchange rate information based on the Canadian dollar (CAD). 
This system utilizes the _app.freecurrencyapi.com_ open API to fetch and display exchange rates for various currencies.

- **View All Exchange Rates**: users can retrieve all available exchange rates based on the Canadian dollar.
- **View Exchange Rate for a Single Currency**: users can get the exchange rate for a specific currency based on the Canadian dollar.

  
# Tech Stack
### Programming Languages
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 

### Frameworks
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
  
### Persistence
<img src="https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge">

### Databases
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 

### Testing
<img src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white">

### Development Tools
<img src="https://img.shields.io/badge/H2-376E99?style=for-the-badge&logo=h2&logoColor=white"> 

### Version Control  
<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">

### Tools & Utilities
<img src="https://img.shields.io/badge/gradle-2D4999?style=for-the-badge&logo=gradle&logoColor=white"> <img src="https://img.shields.io/badge/json%20web%20tokens-323330?style=for-the-badge&logo=json-web-tokens&logoColor=pink"> <img src="https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white">


