# Task Manager API

API Rest para gestión de tareas construida con Spring Boot. Este proyecto incluye autenticación mediante JWT y uso de Spring Security, persistencia de datos con PostgreSQL y contenerización utilizando docker. **PROYECTO NO ACABADO**

## Tecnologías

- **Java 21**
- **Spring Boot 3.x / 4.x** (Web, Data JPA, Security, Validation)
- **PostgreSQL** (Base de datos)
- **JJWT** (Generación y validación de tokens JWT)
- **Springdoc OpenAPI (Swagger)** (Documentación)
- **Docker y Docker Compose** 

## Requisitos Previos

- [Docker](https://www.docker.com/get-started) y Docker Compose instalados en tu sistema.
- *(Opcional, para desarrollo local)* Java 21 y Maven.

## Ejecución del Proyecto (Docker Compose)

El archivo `compose.yaml` (o `docker-compose.yml`) está diseñado para orquestar **dos contenedores**:
1. **postgres**: Contenedor con la base de datos relacional PostgreSQL.
2. **api**: Contenedor con la aplicación Spring Boot.

Para construir la imagen de la API y levantar ambos servicios, ejecuta el siguiente comando en la raíz del proyecto:

```bash
docker compose up -d --build
```

Esto iniciará:
- La base de datos PostgreSQL expuesta en el puerto `5432`.
- La API de Spring Boot, típicamente expuesta en el puerto `8080`.

Para detener y eliminar los contenedores:

```bash
docker compose down
```

## Entorno de Desarrollo Local

El proyecto cuenta con la dependencia `spring-boot-docker-compose`, lo que facilita enormemente el desarrollo local. 

Si ejecutas la aplicación desde tu IDE o mediante Maven:

```bash
./mvnw spring-boot:run
```

Spring Boot detectará automáticamente el archivo `compose.yaml`, levantará el contenedor de base de datos necesario y configurará las credenciales automáticamente para la aplicación. 

### Variables de entorno por defecto en PostgreSQL:
- **Base de datos:** `task_manager`
- **Usuario:** `admin`
- **Contraseña:** `admin`

## Documentación de la API

La aplicación incluye Swagger UI para explorar y probar los endpoints de la API de forma interactiva. 

Una vez que la aplicación esté ejecutándose, visita:
```text
http://localhost:8080/swagger-ui.html
```
*(El puerto `8080` es el predeterminado, asegúrate de utilizar el puerto correspondiente si lo has modificado).*

## Autenticación y Seguridad

Esta API está protegida con **Spring Security** y **JWT**.
Por lo general, el flujo es el siguiente:
1. Registrar un usuario o iniciar sesión a través del endpoint de enrutamiento público (ej. `/api/auth/login`).
2. Obtener un token JWT de la respuesta.
3. Incluir el token en la cabecera HTTP de las siguientes peticiones como: `Authorization: Bearer <tu_token>`.

## Funcionalidad de la API

La API gestiona **Usuarios**, **Proyectos** y **Tareas**, y aplica un sistema de control de acceso basado en roles (RBAC).

### Roles Principales:
- **ADMIN**: Tiene control total. Puede crear proyectos, asignar usuarios a proyectos, crear tareas y asignar tareas a miembros.
- **USER**: Puede ver los proyectos a los que pertenece, consultar las tareas que tiene asignadas y modificar el estado de las tareas que se le han asignado.

### Endpoints Principales:

#### 🔐 Autenticación (`/api/auth`)
- `POST /register`: Registro de un nuevo usuario. Devuelve un token JWT.
- `POST /login`: Inicio de sesión de usuario y obtención del token.

#### 📂 Proyectos (`/api/projects`)
- `POST /`: Crear un nuevo proyecto *(solo ADMIN)*.
- `GET /`: Obtener los proyectos a los que el usuario logueado pertenece.
- `POST /{projectId}/addmember`: Añadir un miembro a un proyecto *(solo ADMIN)*.

#### 📝 Tareas (`/api/tasks`)
- `POST /`: Crear una nueva tarea en el sistema *(solo ADMIN)*.
- `GET /project/{projectId}`: Mostrar todas las tareas que pertenecen al proyecto.
- `GET /mytasks`: Obtener todas las tareas asignadas al usuario logueado.
- `PUT /{taskId}/assign`: Asignar una tarea a un usuario *(solo ADMIN)*.
- `PUT /{taskId}/status`: Actualizar el estado de una tarea *(solo el usuario asignado)*.
