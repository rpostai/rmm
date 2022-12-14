1. The project was created using Maven. Therefore, in order to compile it just run
mvn clean install

2. As requested, my proposals to the project are:

2.1) Auditing
   --> Create an entity called "Audit" to keep track of all changes in the Inventory, Device and Service entities
   --> Another alternative would be use Hibernate Envers to same feature
2.2) Logging
   --> Using "Interceptors" or Aspected Oriented Programming (AOP), log every call made to the endpoints
2.3) Cache update
   --> In order to keep the cache updated among all other the application instances, it's necessary to implement a solution to update them.
   The solution adopted in the project initially was only to update local instance, but it's possible to extend this solution to update all instance's cache.
   My initial proposal is to post a message in a rabbitmq topic and implement a message reader in the application.
   Once the message is posted, the readers will read the message and trigger the cache update.
   Another approach would be using a cache server product like MemCache or Redis
2.4) Implement the linkage between "Customers" and their inventories
2.5) Implement security using Spring Security or Servlet Filters
