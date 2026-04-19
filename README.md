# Task Manager API

API Rest para gestión de tareas construida con Spring Boot. Este proyecto incluye autenticación mediante JWT y uso de Spring Security, persistencia de datos con PostgreSQL y contenerización utilizando Docker. **PROYECTO EN DESARROLLO**

## Tecnologías

- **Java 21**
- **Spring Boot 3.x / 4.x** (Web, Data JPA, Security, Validation)
- **PostgreSQL** (Base de datos)
- **JJWT** (Generación y validación de tokens JWT)
- **Springdoc OpenAPI (Swagger)** (Documentación)
- **Docker y Docker Compose**

## Requisitos Previos

- [Docker](https://www.docker.com/get-started) y Docker Compose instalados.
- *(Opcional, solo para desarrollo local sin Docker)* Java 21 y Maven.

---

## Ejecución con Docker Compose (recomendado)

El archivo `compose.yaml` levanta dos contenedores:
1. **postgres** — Base de datos PostgreSQL en el puerto `5432`.
2. **app** — La aplicación Spring Boot en el puerto `8080`.

### 1. Clonar el repositorio

```bash
git clone https://github.com/imaugar/task-manager-api.git
cd task-manager-api
```

### 2. (Opcional) Configurar variables de entorno

Por defecto la aplicación usa valores seguros para desarrollo. Si quieres personalizarlos, crea un archivo `.env` en la raíz del proyecto:

```env
JWT_SECRET=tu_clave_secreta_aqui
JWT_EXPIRATION=86400000
```

> Si no creas el `.env`, se usarán los valores predeterminados definidos en `compose.yaml`.

### 3. Construir y levantar los contenedores

```bash
docker compose up --build -d
```

- `--build` compila la imagen de la aplicación (necesario la primera vez o tras cambios en el código).
- `-d` ejecuta los contenedores en segundo plano.

La aplicación tardará unos segundos en arrancar mientras espera a que PostgreSQL esté listo.

### 4. Verificar que está corriendo

```bash
docker compose logs -f app
```

Cuando veas una línea como `Started TaskManagerApiApplication`, la API estará lista.

### 5. Detener los contenedores

```bash
docker compose down
```

Para eliminar también los datos persistidos de la base de datos:

```bash
docker compose down -v
```

---

## Ejecución en Desarrollo Local (sin Docker para la app)

Si prefieres ejecutar la aplicación directamente desde tu IDE o Maven, necesitas levantar solo la base de datos:

```bash
docker compose up postgres -d
```

Luego ejecuta la aplicación:

```bash
./mvnw spring-boot:run
```

La configuración en `application.properties` apunta a `localhost:5432` con usuario `admin` y contraseña `admin`, que coincide con el contenedor de postgres.

---

## Documentación de la API (Swagger)

Una vez que la aplicación esté corriendo, accede a Swagger UI en:

```
http://localhost:8080/swagger-ui.html
```

---

## Autenticación y Seguridad

Esta API está protegida con **Spring Security** y **JWT**. El flujo de autenticación es el siguiente:

1. Registrar un usuario via `POST /api/auth/register`.
2. Iniciar sesión via `POST /api/auth/login` y obtener el token JWT de la respuesta.
3. Incluir el token en la cabecera de todas las peticiones siguientes:
   ```
   Authorization: Bearer <token>
   ```

---

## Funcionalidad de la API

La API gestiona **Usuarios**, **Proyectos** y **Tareas** con control de acceso basado en roles (RBAC).

### Roles

- **ADMIN**: Control total. Puede crear proyectos, añadir miembros, crear tareas y asignarlas.
- **USER**: Puede ver sus proyectos, consultar sus tareas asignadas y actualizar el estado de las mismas.

> **Nota:** En Spring Boot 4 no está activado por defecto el trailing slash. Usar siempre las rutas **sin** barra final (ej. `/api/projects`, no `/api/projects/`).

### Credenciales de administrador por defecto

El sistema crea automáticamente dos usuarios admin al arrancar:

| Username | Password |
|----------|----------|
| `admin`  | `admin`  |
| `admin2` | `admin2` |

Los usuarios registrados via `/api/auth/register` siempre obtienen rol `USER`. Para obtener rol `ADMIN` hay que usar las credenciales anteriores o hardcodear unas nuevas en el DataInitializer.

### Endpoints

#### Autenticación (`/api/auth`)
| Método | Ruta | Body | Acceso |
|--------|------|------|--------|
| `POST` | `/api/auth/register` | `{"username","email","password"}` | Público |
| `POST` | `/api/auth/login` | `{"username","password"}` | Público |

#### Proyectos (`/api/projects`)
| Método | Ruta | Body / Params | Acceso |
|--------|------|---------------|--------|
| `POST` | `/api/projects` | `{"name","description"}` | ADMIN |
| `GET` | `/api/projects` | — | Autenticado |
| `POST` | `/api/projects/{projectId}/addmember` | `?username=<username>` | ADMIN |

#### Tareas (`/api/tasks`)
| Método | Ruta | Body / Params | Acceso |
|--------|------|---------------|--------|
| `POST` | `/api/tasks` | `{"name","description","projectId"}` | ADMIN |
| `GET` | `/api/tasks/project/{projectId}` | — | Autenticado |
| `GET` | `/api/tasks/mytasks` | — | Autenticado |
| `PUT` | `/api/tasks/{taskId}/assign` | Body: `<userId>` (número) | ADMIN |
| `PUT` | `/api/tasks/{taskId}/status` | `{"status":"PENDING\|IN_PROGRESS\|COMPLETED"}` | Usuario asignado |
