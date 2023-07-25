# Service for sharing things

## Functionality

The application allows users to search, upload and rent items for use, leave
reviews, make booking requests.

### The program has two microservices:

1. Server - contains all the logic, including working with the database, handling exceptional cases, etc.
2. Gateway - designed for validation of input requests

**_The following features are available to users:_**

- Adding an item to the catalog
- Adding a thing as a response to another user's request
- Editing information about a item by its owner
- Viewing a list of all the user's items
- Viewing information about a specific item by any user
- Search for items by keywords or text
- Adding a booking request for a certain period of time
- Confirmation or rejection of the booking request by the owner of the item
- Getting information about booking by owner or booker
- Getting a list of all bookings for the owner's items (all, rejected, approved, waiting or cancelled bookings)
- Getting a list of all bookings by booker (fall, rejected, approved, waiting or cancelled bookings)
- Adding comments after the expiration of the booking period
- Viewing comments from other users
- Adding a request for an item if it is not in the catalog. Other users can answer it.
- Viewing a list of all item requests
- Viewing a list of all requests for a specific user

---

## Instructions for start the program

1. Clone this repository
   `git clone https://github.com/isthatkirill/shareit-spring-boot.git`
2. Go to the directory with the program
   `cd shareit-spring-boot`
3. Build the app
   `mvn clean install`
4. Run docker containers using docker-compose
   `docker-compose up`
5. Good job! The application is running. Detailed information about the launch is available in the logs in the console.

## Technologies and libraries used

- Java
- Spring Boot
- JUnit
- Mockito
- Lombok
- Mapstruct
- H2
- PostgreSQL
- Hibernate
- Docker
- Postman
