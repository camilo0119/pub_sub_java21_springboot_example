# Proyecto Spring Boot - Google Cloud Pub/Sub

Este proyecto es una API REST en Java usando Spring Boot que publica mensajes en un tópico de Google Cloud Pub/Sub.

## Requisitos

- Java 17+
- Maven 3.8+
- Cuenta de Google Cloud
- Proyecto en Google Cloud con Pub/Sub habilitado

## Configuración

2. **Habilita la API de Pub/Sub en tu proyecto de Google Cloud:**
   [Enlace directo](https://console.developers.google.com/apis/api/pubsub.googleapis.com/overview)

3. **Crea el tópico en Pub/Sub:**

4. **Configura las credenciales:**
   - Descarga el archivo de credenciales JSON desde Google Cloud Console.
   - Exporta la variable de entorno:
     ```
     export GOOGLE_APPLICATION_CREDENTIALS=/ruta/a/tu/credencial.json
     ```

5. **Configura el archivo `application.properties`:**
   Ajusta los valores según tu entorno.

6. **Compila y ejecuta la aplicación:**
   mvn clean install mvn spring-boot:run

## Uso

Para publicar un mensaje, realiza una petición POST: 
curl -X POST "http://localhost:8081/api/pubsub/publish?message=HolaPubSub"
