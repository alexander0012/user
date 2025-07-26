# API de Gestión de Usuarios

Esta es una API RESTful para la gestión de usuarios, construida con Spring Boot 3. El proyecto implementa un sistema de registro y autenticación seguro utilizando JSON Web Tokens (JWT).

## Características Principales

-   **Registro de Usuarios:** Permite a nuevos usuarios registrarse en el sistema.
-   **Autenticación con JWT:** Utiliza Spring Security y JWT para proteger los endpoints.
-   **Documentación con Swagger:** Genera automáticamente una documentación interactiva de la API con SpringDoc (OpenAPI 3).
-   **Persistencia de Datos:** Usa Spring Data JPA y Hibernate para interactuar con la base de datos.
-   **Migraciones de Base de Datos:** Gestiona el esquema de la base de datos de forma versionada con Flyway.
-   **Base de Datos en Memoria:** Configurado para usar H2 para un desarrollo y pruebas rápidas.
-   **Manejo de Excepciones:** Implementa un manejador de excepciones global para devolver respuestas de error consistentes y seguras.

## Tecnologías Utilizadas

-   **Java 21**
-   **Spring Boot 3.3.1**
-   **Spring Security**
-   **Spring Data JPA**
-   **H2 Database**
-   **Flyway** (Para migraciones de base de datos)
-   **JJWT** (Java JWT)
-   **Lombok**
-   **SpringDoc (Swagger/OpenAPI 3)**
-   **Gradle**

## Configuración

El proyecto está preconfigurado para ejecutarse con una base de datos H2 en memoria. El archivo `application.properties` contiene toda la configuración necesaria.

**Nota sobre la clave JWT:** La clave en `application.properties` es para desarrollo. En un entorno de producción, debe ser reemplazada por una clave segura y gestionada como un secreto.

## Cómo Ejecutar el Proyecto

### Prerrequisitos

-   JDK 21 o superior.
-   Gradle 8.x.

### Pasos

1.  **Clona el repositorio:** `https://github.com/alexander0012/user`

2.  **Construye el proyecto:**
    Usa el wrapper de Gradle para compilar el proyecto y descargar las dependencias: `./gradlew clean build`
 
3.  **Corre el proyecto.**
    Corre el comando para correr la aplicacion: `./gradlew bootRun`

La aplicación estará disponible en `http://localhost:8080`.

## Endpoints de la API

La mejor forma de explorar y probar la API es a través de la documentación interactiva de Swagger.

### Documentación Interactiva (Swagger)

Una vez que la aplicación esté en ejecución, accede a la siguiente URL en tu navegador:
-   **http://localhost:8080/swagger-ui.html**

### Endpoints Públicos

Estos endpoints no requieren autenticación.

#### Registrar un nuevo usuario

-   **Endpoint:** `POST /v1/usuario/register`
-   **Descripción:** Crea un nuevo usuario en el sistema y devuelve un token JWT para futuras interacciones.
-   **Request Body:**
```
{
  "name": "Juan Rodriguez",
  "email": "juan.rodriguez@example.cl",
  "password": "aA123456!",
  "phones": [
    {
      "number": "1234567",
      "cityCode": "1",
      "contryCode": "57"
    },
    {
      "number": "9876543",
      "cityCode": "2",
      "contryCode": "56"
    }
  ]
}
```
-   **Success Response (200 OK):**
```
{
    "id": "15d1f3b9-e538-4d68-a4fa-6908b1913f2b",
    "created": "2025-07-25T21:13:52.852630Z",
    "modified": "2025-07-25T21:13:52.852630Z",
    "lastLogin": "2025-07-25T21:13:52.816550Z",
    "token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqdWFuLnJvZHJpZ3VlekBleGFtcGxlLmNvbSIsImlhdCI6MTc1MzQ3ODAzMiwiZXhwIjoxNzUzNTY0NDMyfQ.6zdJ0nPtncDmwDtPqRAS075QSR4S57V_TJdA_PpBub4SizPY3pFsx8xZU8an9vQE",
    "isactive": true
}
```

### Endpoints Protegidos (Requieren Token)

Para acceder a estos endpoints, debes incluir el token JWT en la cabecera `Authorization`.

-   **Header:** `Authorization: Bearer <TU_TOKEN_JWT>`

#### Obtener todos los usuarios

-   **Endpoint:** `GET /v1/usuario/all`
-   **Descripción:** Devuelve una lista de todos los usuarios registrados.
-   **Success Response (200 OK):**
```
[
    {
        "id": "6fa2483c-0b80-43ad-9171-428596ef186d",
        "name": "Juan Rodriguez",
        "email": "juan.rodriguez@example.cl",
        "created": "2025-07-26T18:39:36.635906Z",
        "modified": "2025-07-26T18:39:36.635906Z",
        "lastLogin": "2025-07-26T18:39:36.608945Z",
        "token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqdWFuLnJvZHJpZ3VlekBleGFtcGxlLmNsIiwiaWF0IjoxNzUzNTU1MTc2LCJleHAiOjE3NTM2NDE1NzZ9.5bntToJtYL_U4LOI4kW-BbsydnOCBSQGi_yToUCW1GOMReSYrkZgC83FAOW8aElM",
        "active": true
    }
]
```

#### Obtener los detalles de todos los usuarios

-   **Endpoint:** `GET /v1/usuario/allDetails`
-   **Descripción:** Devuelve una lista de todos los usuarios registrados con sus detalles de registro, incluyendo telefonos.
-   **Success Response (200 OK):**
```
[
    {
        "id": "6fa2483c-0b80-43ad-9171-428596ef186d",
        "name": "Juan Rodriguez",
        "created": "2025-07-26T18:39:36.635906Z",
        "modified": "2025-07-26T18:39:36.635906Z",
        "lastLogin": "2025-07-26T18:39:36.608945Z",
        "token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqdWFuLnJvZHJpZ3VlekBleGFtcGxlLmNsIiwiaWF0IjoxNzUzNTU1MTc2LCJleHAiOjE3NTM2NDE1NzZ9.5bntToJtYL_U4LOI4kW-BbsydnOCBSQGi_yToUCW1GOMReSYrkZgC83FAOW8aElM",
        "isactive": true,
        "email": "juan.rodriguez@example.cl",
        "phones": [
            {
                "number": "1234567",
                "cityCode": "1",
                "contryCode": "57"
            },
            {
                "number": "9876543",
                "cityCode": "2",
                "contryCode": "56"
            }
        ]
    }
]
```

## Flujo de Autenticación

1.  **Registro:** El usuario se registra a través del endpoint `POST /v1/auth/register`. La aplicación devuelve un token JWT.
2.  **Acceso a Recursos Protegidos:** Para cualquier petición a un endpoint protegido (ej: `GET /v1/usuario/all`), el cliente debe incluir el token en la cabecera `Authorization`.
3.  **Inicio de Sesión (Login):** Cuando el token expire, el usuario deberá usar el endpoint `POST /v1/usuario/login` con su email y contraseña para obtener un nuevo token.

## Diagrama de solucion

A continuación se muestra un diagrama de alto nivel que ilustra la arquitectura de la aplicación y el flujo de comunicación entre sus componentes.

![Diagrama de Arquitectura de la Solución](readme/resources/diagrama.png)