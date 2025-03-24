# 🌦️ **Weather Info API**  

This is a Spring Boot REST API to fetch and store weather information based on a pincode.  
- 🌍 Uses **Google Maps API** and **OpenWeather API** for geolocation and weather data.  
- 📂 Stores data in **MySQL** for optimized future retrieval.  
- 🧪 Fully tested with **JUnit** and **Mockito**.  
- 📝 API documentation with **Swagger**.  

---

## 🌐 **API Documentation (Swagger)**
- **Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 🧪 **Postman Collection**
- **Import Postman Collection:**  
   `src/main/resources/WeatherPincodeTask.postman_collection.json`

---

## 🚀 **Endpoints**
| Method | Endpoint | Description |
|--------|----------|-------------|
| **POST** | `/api/weather` | Fetch weather data for a pincode and date |
| **GET** | `/api/weather?page=0&size=10` | Fetch all weather data with pagination |

---

### 📁 **Setup and Configuration**

## 1. **Clone the Repository**
```bash
git clone https://github.com/your-repo/weather-info-api.git
cd weather-info-api
```

## 2. Configure application.properties

## 🌐 SERVER CONFIGURATION
server.port=8080

## 🌍 DATABASE CONFIGURATION
spring.datasource.url=jdbc:mysql://localhost:3306/weather_db
spring.datasource.username=root
spring.datasource.password=your-password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

## 🌐 GOOGLE MAPS API KEY (for geocoding)
google.maps.api.key=YOUR_GOOGLE_MAPS_API_KEY

## ☀️ OPENWEATHER API KEY (for fallback geocoding)
openweather.api.key=YOUR_OPENWEATHER_API_KEY

## 🏢 JPA CONFIGURATION
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.open-in-view=false

## ✅ TEST CONFIGURATION
spring.test.database.replace=none
spring.jpa.defer-datasource-initialization=true



# Note:  Prefer to use Intellij Idea
