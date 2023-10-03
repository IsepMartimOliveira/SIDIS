# Supplementary Specification (FURPS+)

## Functionality

_Specifies functionalities that:_

- _are common across several US/UC;_
- _are not related to US/UC, namely: Audit, Reporting and Security._



(fill in here)

* Subscription Management: The system should provide the ability to manage different subscription plans, including creating new plans, promoting plans, deactivating plans, and migrating subscribers to different plans.
* Revenue Tracking: The system should track all revenue generated so far, both from monthly and annual subscriptions, broken down by plan.
* Cash Flow Management: The system should generate a cash flow map, showing the cash the company has received or will receive per month, based on active subscriptions.
* Metrics Tracking: The system should track various metrics, such as active subscriptions, new subscriptions, canceled subscriptions, and churn rate, at both the company level and plan level.

## Usability 

_Evaluates the user interface. It has several subcategories,
among them: error prevention; interface aesthetics and design; help and
documentation; consistency and standards._


(fill in here )

* Dashboard: The system should provide a user-friendly dashboard that displays the metrics and revenue information in a clear and organized manner.
* Frontend Integration: The backend service should be designed to easily integrate with any frontend solution chosen by the company in the future.
## Reliability
_Refers to the integrity, compliance and interoperability of the software. The requirements to be considered are: frequency and severity of failure, possibility of recovery, possibility of prediction, accuracy, average time between failures._


(fill in here )

* Data Consistency: The system should ensure data consistency by properly updating and synchronizing subscription information, revenue data, and metrics.
* Error Handling: The system should handle errors gracefully and provide appropriate error messages to users when necessary.
* Backup and Recovery: The system should have mechanisms in place to backup data and recover in case of any failures or data loss.

## Performance
_Evaluates the performance requirements of the software, namely: response time, start-up time, recovery time, memory consumption, CPU usage, load capacity and application availability._

(fill in here )

* Scalability: The system should be able to handle a large number of subscribers and perform well even with increasing data volume.
* Response Time: The system should provide fast response times when retrieving metrics or generating reports.
* Asynchronous Processing: The system should use asynchronous calls or event-based mechanisms to handle lengthy operations and avoid blocking the user interface.

## Supportability
_The supportability requirements gathers several characteristics, such as:
testability, adaptability, maintainability, compatibility,
configurability, installability, scalability and more._ 



(fill in here )
* Maintainability: The system should be designed in a modular and maintainable way to facilitate future updates and enhancements.
* Documentation: The system should have proper documentation, including API documentation, to assist developers and support staff.
* Logging and Monitoring: The system should have logging and monitoring capabilities to track system health, detect issues, and provide diagnostics when needed.
  
    

## +

### Design Constraints

_Specifies or constraints the system design process. Examples may include: programming languages, software process, mandatory standards/patterns, use of development tools, class library, etc._
  

(fill in here )
* Programming Languages: Java 17
* Framework: Spring Boot
* Object-Relational Mapping (ORM): JPA (Java Persistence API)
* Libraries: Lombok (for reducing boilerplate code)




### Implementation Constraints

_Specifies or constraints the code or construction of a system such
such as: mandatory standards/patterns, implementation languages,
database integrity, resource limits, operating system._


(fill in here )

* Database: H2 (an in-memory database)
* Database Integrity: Ensure data integrity and enforce constraints using appropriate JPA annotations and database schema design.

### Interface Constraints
_Specifies or constraints the features inherent to the interaction of the
system being developed with other external systems._


(fill in here )
* RESTful API: Design and implement the backend service as a RESTful API using Spring Boot's MVC capabilities.
* Swagger Integration: Incorporate Swagger into your Spring Boot project to generate API documentation and provide a user-friendly interface for testing the API endpoints.
### Physical Constraints

_Specifies a limitation or physical requirement regarding the hardware used to house the system, as for example: material, shape, size or weight._

(fill in here )
* Deployment Environment: Local machine or localhost.
* Server Configuration: The system should be configured to run on your local machine using appropriate server settings (e.g., port number, network configuration).
* Resource Limits: Consider the available resources (CPU, memory, disk space) on your local machine and ensure that the system operates within those limits.