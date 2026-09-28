# Seguridad

Este documento describe el sistema de seguridad implementado en Keventa, incluyendo el proceso de autenticación, autorización, generación y validación de tokens JWT, y el manejo de errores relacionados con seguridad.

Keventa utiliza **Spring Security** junto con **JSON Web Tokens (JWT)** para proteger los recursos de la aplicación y controlar el acceso según los roles asignados a cada usuario.

---

# Objetivos de seguridad

El sistema de seguridad de Keventa tiene como objetivos principales:

* Proteger los endpoints privados de la aplicación.
* Verificar la identidad de los usuarios mediante autenticación.
* Controlar el acceso a recursos según los roles asignados.
* Evitar accesos no autorizados.
* Mantener una comunicación segura entre cliente y servidor mediante tokens JWT.

---

# Tecnologías utilizadas

La seguridad de Keventa está construida utilizando:

* Spring Security.
* JWT (JSON Web Token).
* BCrypt para cifrado de contraseñas.
* Spring Authentication Manager.
* Spring Security Context.

---

# Flujo general de autenticación

El proceso de autenticación funciona de la siguiente manera:

```text id="6y8v5p"
Usuario
   │
   ▼
POST /auth/login
   │
   ▼
AuthController
   │
   ▼
AuthenticationManager
   │
   ▼
Validación de credenciales
   │
   ▼
Generación de JWT
   │
   ▼
Cliente recibe token
```

Después de autenticarse correctamente, el usuario recibe un token JWT que debe utilizar para acceder a los recursos protegidos.

---

# Login

El inicio de sesión es el único recurso disponible sin autenticación previa.

Endpoint:

```http id="31f4o8"
POST /auth/login
```

El usuario debe enviar sus credenciales:

```json id="b8tq56"
{
    "email": "usuario@correo.com",
    "password": "********"
}
```

Si las credenciales son correctas, el sistema genera un token:

```json id="n3k9bb"
{
    "name": "Administrador",
    "role": "ADMIN",
    "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

Este token será utilizado en las siguientes solicitudes.

---

# JWT

JWT es el mecanismo utilizado para mantener la autenticación del usuario entre solicitudes.

El cliente debe enviar el token mediante el encabezado HTTP:

```http id="w6y7gf"
Authorization: Bearer <token>
```

Ejemplo:

```http id="4z0d3h"
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

Cada solicitud protegida debe incluir este encabezado.

---

# Estructura del JWT

Un token JWT está compuesto por tres partes:

```text id="ps4b3f"
Header.Payload.Signature
```

Ejemplo:

```text id="5p8r1q"
xxxxx.yyyyy.zzzzz
```

## Header

Contiene información sobre el algoritmo utilizado.

Ejemplo:

```json id="3r1f2v"
{
    "alg": "HS256"
}
```

---

## Payload

Contiene información del usuario autenticado.

Ejemplo:

```json id="6g8n4q"
{
    "sub": "admin@keventa.com",
    "role": "ADMIN",
    "iat": 1785097539,
    "exp": 1785183939
}
```

Los datos utilizados por Keventa son principalmente:

* Email del usuario.
* Rol asignado.
* Fecha de creación.
* Fecha de expiración.

---

## Signature

Permite comprobar que el token no fue alterado.

Si la firma no coincide, el token es considerado inválido.

---

# Componentes de seguridad

La estructura del módulo de seguridad es:

```text id="4w0v2k"
security/
├── config/
├── handler/
├── jwt/
├── model/
└── service/
```

---

# SecurityConfig

Clase principal de configuración de Spring Security.

Responsabilidades:

* Configurar la cadena de filtros.
* Definir qué endpoints requieren autenticación.
* Configurar manejo de errores.
* Definir la política de sesiones.
* Registrar el filtro JWT.
* Habilitar seguridad basada en métodos.

Componentes principales:

```java id="4j4g5t"
@EnableMethodSecurity
@Configuration
public class SecurityConfig
```

---

# Política de sesión

Keventa utiliza autenticación basada en tokens, por lo que no mantiene sesiones del usuario.

Configuración:

```text id="u4a9p2"
SessionCreationPolicy.STATELESS
```

Esto significa:

* El servidor no almacena información de sesión.
* Cada solicitud debe incluir su propio token JWT.
* La autenticación se reconstruye en cada petición.

---

# JwtAuthenticationFilter

El filtro JWT se ejecuta antes del filtro estándar de autenticación de Spring Security.

Flujo:

```text id="3mx5q1"
Request HTTP
     │
     ▼
JwtAuthenticationFilter
     │
     ▼
Extraer token del Header
     │
     ▼
Validar JWT
     │
     ▼
Obtener usuario
     │
     ▼
Crear Authentication
     │
     ▼
SecurityContextHolder
```

Responsabilidades:

* Leer el encabezado `Authorization`.
* Extraer el token Bearer.
* Validar la firma del JWT.
* Obtener la información del usuario.
* Registrar el usuario autenticado dentro del contexto de seguridad.

---

# JwtService

Servicio encargado de las operaciones relacionadas con JWT.

Responsabilidades:

* Generar tokens.
* Validar tokens.
* Extraer información del usuario desde el token.
* Comprobar expiración.

Componentes:

```text id="k8v3f0"
JwtService
JwtServiceImp
```

---

# CustomUserDetails

Spring Security necesita una representación del usuario compatible con su modelo de autenticación.

Por esta razón Keventa implementa:

```text id="q0y5ck"
UserDetails
```

mediante:

```text id="f5f4o8"
CustomUserDetails
```

Responsabilidades:

* Exponer información del usuario a Spring Security.
* Proporcionar las autoridades asociadas al usuario.
* Integrar la entidad `User` con el sistema de seguridad.

---

# Roles y Authorities

Keventa utiliza roles para controlar los permisos.

Los roles disponibles son:

```text id="y5r3f8"
ADMIN
EMPLOYEE
```

Spring Security internamente trabaja con authorities.

Por esta razón los roles son transformados:

```text id="8n6xk2"
ADMIN
     ↓
ROLE_ADMIN


EMPLOYEE
     ↓
ROLE_EMPLOYEE
```

Ejemplo:

```java id="k3m1x9"
new SimpleGrantedAuthority(
    "ROLE_" + user.getRole()
)
```

Esto permite utilizar expresiones como:

```java id="7z9q0s"
@PreAuthorize("hasRole('ADMIN')")
```

o:

```java id="4g3q5m"
@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')");
```

---

# Autorización mediante @PreAuthorize

La autorización se realiza a nivel de método mediante:

```java id="r2x5f6"
@PreAuthorize()
```

Ejemplo:

```java id="4v7n9c"
@PreAuthorize("hasRole('ADMIN')")
@DeleteMapping("/{id}")
public void eliminarUsuario(){
}
```

Antes de ejecutar el método, Spring Security verifica si el usuario autenticado posee los permisos necesarios.

---

# SecurityContextHolder

`SecurityContextHolder` almacena temporalmente la información del usuario autenticado durante la ejecución de una solicitud.

Cuando el JWT es válido:

```text id="6s7q1x"
JWT válido
    ↓
Authentication creado
    ↓
SecurityContextHolder
    ↓
Usuario autenticado disponible
```

Este contexto es utilizado posteriormente por Spring Security para evaluar permisos.

---

# Manejo de errores de seguridad

Keventa diferencia dos situaciones:

## 401 Unauthorized

Ocurre cuando el usuario no está autenticado correctamente.

Ejemplos:

* Token inexistente.
* Token inválido.
* Token expirado.

Este caso es manejado por:

```text id="1q4y7m"
JwtAuthenticationEntryPoint
```

Ejemplo de respuesta:

```json id="v6w4s1"
{
    "timestamp": "...",
    "status": 401,
    "error": "Unauthorized",
    "message": "Token invalido, o inexistente"
}
```

---

## 403 Forbidden

Ocurre cuando el usuario está autenticado pero no tiene permisos suficientes.

Ejemplo:

```text id="6j2n0p"
Usuario EMPLOYEE
        ↓
Intenta cancelar una venta
        ↓
Acceso rechazado
```

Este caso es manejado por:

```text id="x3p8v7"
JwtAccessDeniedHandler
```

Ejemplo:

```json id="4q7m1x"
{
    "timestamp": "...",
    "status": 403,
    "error": "Forbidden",
    "message": "Sin permisos para ejecutar el recurso"
}
```

---

# Contraseñas

Las contraseñas nunca se almacenan directamente.

Antes de guardarlas en la base de datos son procesadas mediante BCrypt.

Flujo:

```text id="9r3k2v"
Contraseña original
        │
        ▼
BCryptPasswordEncoder
        │
        ▼
Hash almacenado en BD
```

Ejemplo:

```text id="1v7m5z"
password123

↓

$2a$10$7Hk...
```

Cuando un usuario inicia sesión, Spring Security compara la contraseña proporcionada con el hash almacenado.

---

# Protección de endpoints

La configuración general establece:

```text id="0m5c8r"
POST /auth/login
        ↓
Permitido sin autenticación


Cualquier otro endpoint
        ↓
Requiere autenticación
```

Después de autenticarse, los permisos específicos son controlados mediante `@PreAuthorize`.

Ejemplo:

```text id="3x8v2q"
Endpoint protegido
        ↓
Usuario autenticado
        ↓
Verificación de rol
        ↓
Acceso permitido o rechazado
```

---

# Flujo completo de una petición protegida

```text id="n4c8y1"
Cliente
  │
  │ Authorization: Bearer JWT
  ▼
JwtAuthenticationFilter
  │
  ▼
Validación del token
  │
  ▼
CustomUserDetailsService
  │
  ▼
SecurityContextHolder
  │
  ▼
@PreAuthorize
  │
  ▼
Controller
  │
  ▼
Service
  │
  ▼
Respuesta
```

---

# Resumen

El sistema de seguridad de Keventa está compuesto por:

| Componente                  | Responsabilidad                                     |
| --------------------------- | --------------------------------------------------- |
| Spring Security             | Protección general de recursos                      |
| JWT                         | Mantener la autenticación entre solicitudes         |
| SecurityConfig              | Configuración central de seguridad                  |
| JwtAuthenticationFilter     | Validación del token en cada petición               |
| JwtService                  | Creación y validación de JWT                        |
| CustomUserDetails           | Adaptación del usuario para Spring Security         |
| SecurityContextHolder       | Mantener el usuario autenticado durante la petición |
| @PreAuthorize               | Control de permisos por rol                         |
| JwtAuthenticationEntryPoint | Manejo de errores 401                               |
| JwtAccessDeniedHandler      | Manejo de errores 403                               |
| BCrypt                      | Protección de contraseñas                           |

---

# Documentos relacionados

* [Arquitectura](arquitectura.md)
* [API](api.md)
* [Roles y permisos](roles-y-permisos.md)
* [Reglas de negocio](reglas-de-negocio.md)
* [Base de datos](bd.md)
