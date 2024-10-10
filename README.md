# RESTful Client Example with RESTEasy and Jackson
This project demonstrates how to create a RESTful client in a Java EE environment using **RestEasy** with **Jackson** for JSON processing. It includes examples of how to configure timeouts, send POST requests, and handle JSON responses, including pretty-printing and logging the HTTP status code.

## Features

- **RESTEasy** client configuration.
- Timeout configuration for **connection** and **read** operations.
- **Jackson** integration for JSON serialization/deserialization.
- Logging of HTTP status and response body in **pretty-print** format.
- Separation of concerns for easier maintainability and readability.
- Uses an external HTTP echo service via **Docker**.

## Technologies Used

- **Java EE** (with JSF ManagedBeans)
- **RestEasy** for the REST client.
- **Jackson** for JSON serialization/deserialization.
- **Lombok** to reduce boilerplate code.
- **WildFly** application server for deployment.
- **Docker** for external REST service.

## Prerequisites

Before deploying the project, you need to configure your WildFly server to prefer Jackson over JSON-B. This is necessary to ensure that RestEasy uses Jackson for JSON serialization and deserialization.

### Step 1: Modify `standalone.xml`

Add the following section to your `standalone.xml` configuration file inside your WildFly server:

```xml
<system-properties>
    <property name="resteasy.preferJacksonOverJsonB" value="true"/>
</system-properties>
```

### Step 2: Set up the External REST Service with Docker
We use an external HTTP echo service to simulate REST responses. To set this up, use the following Docker Compose configuration:

Create a docker-compose.yml file with the following content:
```yml
version: '3.8'
services:
   my-http-listener:
      image: mendhak/http-https-echo:31
      environment:
         - HTTP_PORT=8888
         - HTTPS_PORT=9999
      ports:
         - "8888:8888"
         - "9999:9999"
```
Run the following command to start the service:
```bash
docker-compose up -d
```
This service will run an HTTP echo server on port 8888 and HTTPS on port 9999, which will be used by the application to send requests and receive responses.

### Step 3: Clone the repository
```bash
git clone https://github.com/arianmtzcu/resteasy-with-jackson-jee.git
```

### Step 4: Build the project using Maven
```bash
mvn clean install
```

### Step 5: Deploy the application to WildFly
- Copy the generated .war file to the deployments folder of your WildFly instance.
- Start WildFly and access the application from your browser or API client.

### Contributing
Contributions are welcome! Feel free to open a pull request or issue to suggest improvements.

### License
This project is licensed under the MIT License - see the LICENSE file for details.
