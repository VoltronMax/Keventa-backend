# Arquitectura

Este documento describe la estructura interna de Keventa, la organización de sus módulos y la responsabilidad de cada capa dentro de la aplicación.

Keventa utiliza una arquitectura organizada por dominios, donde cada módulo agrupa los componentes relacionados con una determinada parte del negocio.

## Principio de organización

La estructura principal del proyecto se encuentra organizada de la siguiente manera:

```text
src/main/java/com/keventa/
│
├── auth/
├── common/
├── product/
├── sale/
├── security/
└── user/
```

Cada dominio mantiene sus propias responsabilidades, evitando concentrar toda la lógica de la aplicación en paquetes globales como `controller`, `service` o `repository`.

Por ejemplo, los componentes relacionados con productos se encuentran dentro de `product/`, mientras que los relacionados con ventas se encuentran dentro de `sale/`.

Este enfoque facilita la navegación por el proyecto y permite que cada módulo mantenga un contexto claro.

---

# Estructura de los dominios

## `auth` — Autenticación

El módulo `auth` contiene los componentes relacionados con el proceso de inicio de sesión.

```text
auth/
├── controller/
├── dto/
└── service/
```

### Responsabilidades

* Recibir las solicitudes de inicio de sesión.
* Validar las credenciales proporcionadas.
* Coordinar el proceso de autenticación.
* Generar las credenciales necesarias para acceder al sistema.

Componentes principales:

* `AuthController`
* `LoginRequest`
* `AuthResponse`
* `AuthService`

La implementación de la seguridad y validación de JWT pertenece al módulo `security`.

---

## `product` — Productos

El módulo `product` concentra toda la lógica relacionada con los productos de la boutique.

```text
product/
├── controller/
├── dto/
├── entity/
├── mapper/
├── repository/
└── service/
```

### Responsabilidades

* Registrar productos.
* Consultar productos.
* Actualizar productos.
* Eliminar productos.
* Buscar productos.
* Mantener la información relacionada con el inventario.

Componentes principales:

* `ProductController`
* `Product`
* `ProductRepository`
* `ProductService`
* `ProductServiceImp`
* `ProductMapper`

---

## `sale` — Ventas

El módulo `sale` contiene la lógica relacionada con las ventas realizadas en la boutique.

```text
sale/
├── controller/
├── dto/
├── entity/
├── enums/
├── mapper/
├── repository/
└── service/
```

### Responsabilidades

* Registrar ventas.
* Consultar ventas.
* Cancelar ventas.
* Calcular el total de una venta.
* Calcular la utilidad.
* Actualizar el stock asociado a una venta.

Componentes principales:

* `SaleController`
* `Sale`
* `SaleStatus`
* `SaleRepository`
* `SaleService`
* `SaleServiceImp`
* `SaleMapper`

La lógica de ventas interactúa con el módulo `product` cuando una operación requiere modificar el stock.

---

## `user` — Usuarios

El módulo `user` contiene la gestión de las cuentas de usuario del sistema.

```text
user/
├── controller/
├── dto/
├── entity/
├── enums/
├── mapper/
├── repository/
└── service/
```

### Responsabilidades

* Registrar usuarios.
* Consultar usuarios.
* Actualizar usuarios.
* Eliminar usuarios.
* Cambiar roles.
* Gestionar las contraseñas de los usuarios.

Componentes principales:

* `UserController`
* `User`
* `UserRole`
* `UserRepository`
* `UserService`
* `UserServiceImp`
* `UserMapper`

---

# Seguridad

El módulo `security` contiene los componentes responsables de proteger los recursos de la aplicación.

```text
security/
├── config/
├── handler/
├── jwt/
├── model/
└── service/
```

### Responsabilidades

* Configurar Spring Security.
* Procesar tokens JWT.
* Validar tokens.
* Cargar usuarios autenticados.
* Crear el contexto de seguridad.
* Manejar errores de autenticación y autorización.

Componentes principales:

* `SecurityConfig`
* `JwtAuthenticationFilter`
* `JwtService`
* `JwtServiceImp`
* `CustomUserDetails`
* `CustomUserDetailsService`
* `JwtAuthenticationEntryPoint`
* `JwtAccessDeniedHandler`

La documentación detallada de este módulo se encuentra en:

**[Seguridad](seguridad.md)**

---

# Componentes compartidos

## `common`

El módulo `common` contiene componentes que pueden ser utilizados por diferentes dominios.

```text
common/
└── exception/
```

Actualmente se utiliza principalmente para el manejo centralizado de excepciones.

### Componentes principales

* `GlobalExceptionHandler`
* `ErrorResponse`

También contiene excepciones específicas de negocio:

* `EmailAlreadyRegistered`
* `InsufficientStockException`
* `ProductNotFoundException`
* `SaleAlreadyCancelledException`
* `SaleNotFoundException`
* `UserNotFoundException`

Estos componentes permiten que los diferentes dominios puedan comunicar errores de manera consistente sin duplicar el mismo mecanismo de manejo.

---

# Organización interna de un dominio

Los dominios siguen una estructura similar:

```text
product/
├── controller/
├── dto/
├── entity/
├── mapper/
├── repository/
└── service/
```

Cada paquete tiene una responsabilidad concreta.

## Controller

Se encarga de recibir las solicitudes HTTP y exponer los endpoints de la API.

No debe contener lógica de negocio compleja.

Ejemplo:

```text
HTTP Request
     ↓
Controller
     ↓
Service
```

---

## DTO

Los Data Transfer Objects representan los datos que entran o salen de la aplicación.

Se utilizan para evitar exponer directamente las entidades de persistencia mediante la API.

Ejemplos:

```text
CreateProductRequest
UpdateProductRequest
ProductResponse
```

Los DTO de entrada también permiten aplicar validaciones sobre los datos recibidos.

---

## Entity

Representa los objetos que se almacenan en la base de datos.

Ejemplos:

```text
Product
Sale
User
```

Las entidades representan el estado persistente de los objetos del sistema.

---

## Mapper

Se encarga de convertir entre entidades y DTOs.

Por ejemplo:

```text
CreateProductRequest
        ↓
ProductMapper
        ↓
Product
```

y:

```text
Product
   ↓
ProductMapper
   ↓
ProductResponse
```

Esto permite mantener separadas las estructuras utilizadas por la API de las utilizadas para persistencia.

---

## Repository

Es la capa responsable de interactuar con la base de datos.

Los repositories utilizan Spring Data JPA.

Ejemplo:

```text
ProductRepository
        ↓
Base de datos
```

Los controllers y services no deberían encargarse directamente de construir consultas SQL.

---

## Service

Contiene la lógica de negocio del dominio.

Por ejemplo, en el módulo de ventas:

```text
SaleController
      ↓
SaleService
      ↓
Validaciones y reglas de negocio
      ↓
SaleRepository / ProductRepository
```

El Service es la capa donde se coordinan las operaciones que involucran diferentes componentes.

---

# Flujo de una solicitud

Una solicitud típica sigue el siguiente flujo:

```text
                    HTTP Request
                         │
                         ▼
                   ┌───────────┐
                   │ Controller│
                   └─────┬─────┘
                         │
                         ▼
                   ┌───────────┐
                   │  Service  │
                   └─────┬─────┘
                         │
                         ▼
                   ┌───────────┐
                   │ Repository│
                   └─────┬─────┘
                         │
                         ▼
                    PostgreSQL
```

Cuando la operación necesita transformar información:

```text
Request DTO
     │
     ▼
Controller
     │
     ▼
Service
     │
     ▼
Mapper
     │
     ▼
Entity
     │
     ▼
Repository
     │
     ▼
Database
```

Y para devolver información:

```text
Database
    │
    ▼
Entity
    │
    ▼
Mapper
    │
    ▼
Response DTO
    │
    ▼
Controller
    │
    ▼
HTTP Response
```

---

# Comunicación entre dominios

Aunque los dominios están separados, algunos pueden necesitar colaborar entre sí.

Un ejemplo importante es el registro de una venta.

El módulo `sale` necesita consultar y modificar información perteneciente a `product`.

```text
sale
 │
 ├── SaleService
 │
 └──────────────► product
                       │
                       └── ProductRepository
```

Esto permite que una venta pueda:

1. Buscar el producto.
2. Verificar su stock.
3. Calcular el total.
4. Calcular la utilidad.
5. Actualizar el stock.
6. Registrar la venta.

La separación por dominios no significa que estos sean completamente independientes, sino que cada uno mantiene una responsabilidad clara.

---

# Principios aplicados

La arquitectura actual de Keventa busca aplicar principalmente los siguientes principios:

### Separación de responsabilidades

Cada componente tiene una función específica.

Por ejemplo:

```text
Controller → HTTP
Service    → Negocio
Repository → Persistencia
Mapper     → Conversión
Entity     → Persistencia
DTO        → Transferencia de datos
```

### Encapsulamiento por dominio

Los componentes relacionados con una misma funcionalidad permanecen juntos.

```text
product/
sale/
user/
auth/
security/
```

Esto evita que el proyecto dependa de una estructura global excesivamente fragmentada.

### Bajo acoplamiento

Los controllers no interactúan directamente con la base de datos.

La comunicación se realiza principalmente mediante las capas correspondientes:

```text
Controller
    ↓
Service
    ↓
Repository
```

### Reutilización

Los componentes compartidos, como el manejo global de excepciones, se encuentran en `common` para evitar duplicar código entre dominios.

---

# Estructura completa actual

La estructura actual del código fuente es:

```text
src/main/java/com/keventa/
│
├── auth/
│   ├── controller/
│   ├── dto/
│   └── service/
│
├── common/
│   └── exception/
│
├── product/
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── mapper/
│   ├── repository/
│   └── service/
│
├── sale/
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── enums/
│   ├── mapper/
│   ├── repository/
│   └── service/
│
├── security/
│   ├── config/
│   ├── handler/
│   ├── jwt/
│   ├── model/
│   └── service/
│
└── user/
    ├── controller/
    ├── dto/
    ├── entity/
    ├── enums/
    ├── mapper/
    ├── repository/
    └── service/
```

Esta estructura busca mantener Keventa organizada por **dominios y responsabilidades**, permitiendo que el proyecto pueda crecer sin convertir todos sus componentes en una única estructura difícil de mantener.
