This repository contains the implementation of a microservice architecture for an existing API, now restructured into multiple Spring Boot applications. Each microservice operates independently, focusing on specific functionalities and communicating through defined interfaces and protocols. This approach enhances scalability, modularity, and maintenance efficiency.


Step 1: Clone the Repository
bash
Copy code
git clone [repository URL]
cd [repository directory]
Step 2: Build the Project
mvn clean install

Step 3: Running Instances on Different Ports
You can start multiple instances of the same application on different ports using one of the following methods:

Method 1: Command Line Argument
Run the following command, replacing [port_number] with your desired port number:

Copy code
java -jar target/[your-jar-file].jar --server.port=[port_number]
Method 2: Using application.properties File
For each instance, create or modify the application.properties file in the src/main/resources directory:


Copy code
server.port = [port_number]
Build the application again using mvn clean install, and then start each instance normally.

Method 3: IDE Configuration
If you are using an IDE, you can configure the run/debug configurations to specify the port:

Go to Run/Debug Configurations
Add or Edit a configuration for your application
Add the following in the VM options or Program arguments: --server.port=[port_number]
Apply changes and run the application
