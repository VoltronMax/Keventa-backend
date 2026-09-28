# Base de datos

Este documento describe la estructura de la base de datos utilizada por Keventa, sus entidades principales, atributos y relaciones.

Keventa utiliza **PostgreSQL** como sistema gestor de base de datos y **Spring Data JPA con Hibernate** como tecnología de persistencia.

---

# Tecnologías de persistencia

Las tecnologías utilizadas para la capa de datos son:

* PostgreSQL.
* Spring Data JPA.
* Hibernate ORM.
* Jakarta Persistence API (JPA).

La comunicación entre la aplicación y la base de datos se realiza mediante entidades Java que representan las tablas almacenadas.

Flujo general:

```text id="7v2p8m"
Entidad Java
      │
      ▼
Hibernate / JPA
      │
      ▼
PostgreSQL
```

---

# Modelo de datos actual

Actualmente Keventa cuenta con tres entidades principales:

```text id="x9m4ab"
User
 │
 ├── Usuarios del sistema


Product
 │
 ├── Productos disponibles en inventario


Sale
 │
 └── Ventas realizadas
```

---

# Entidad User

La entidad `User` representa a las personas autorizadas para ingresar al sistema.

Ubicación:

```text id="a7w3kc"
user/entity/User.java
```

## Responsabilidad

Almacenar la información necesaria para autenticar usuarios y asignar permisos dentro del sistema.

---

## Atributos

| Campo      | Tipo     | Descripción                              |
| ---------- | -------- | ---------------------------------------- |
| `id`       | Long     | Identificador único del usuario          |
| `name`     | String   | Nombre del usuario                       |
| `email`    | String   | Correo utilizado para iniciar sesión     |
| `password` | String   | Contraseña almacenada en formato cifrado |
| `role`     | UserRole | Rol asignado al usuario                  |

---

## Rol del usuario

El atributo `role` utiliza un enumerado:

```text id="u2k8sm"
UserRole
```

Valores disponibles:

```text id="5q7c1p"
ADMIN
EMPLOYEE
```

Este valor es utilizado posteriormente por Spring Security para determinar los permisos del usuario.

---

## Seguridad de la contraseña

La contraseña nunca se almacena directamente.

Antes de persistirse, pasa por:

```text id="w5m2s8"
PasswordEncoder
        │
        ▼
BCrypt
        │
        ▼
Hash almacenado
```

Ejemplo:

```text id="c7p4x1"
Contraseña:
password123

Base de datos:
$2a$10$...
```

---

# Entidad Product

La entidad `Product` representa los productos disponibles dentro del inventario de la boutique.

Ubicación:

```text id="m8x4d2"
product/entity/Product.java
```

---

## Responsabilidad

Almacenar la información comercial y de inventario de cada producto.

---

## Atributos

| Campo           | Tipo       | Descripción                       |
| --------------- | ---------- | --------------------------------- |
| `id`            | Long       | Identificador único del producto  |
| `name`          | String     | Nombre del producto               |
| `purchasePrice` | BigDecimal | Precio de compra del producto     |
| `salePrice`     | BigDecimal | Precio de venta del producto      |
| `stock`         | Integer    | Cantidad disponible en inventario |

---

# Entidad Sale

La entidad `Sale` representa una venta realizada dentro del sistema.

Ubicación:

```text id="v3c8n1"
sale/entity/Sale.java
```

---

## Responsabilidad

Registrar la información histórica de una operación de venta.

---

## Atributos

| Campo           | Tipo          | Descripción                                |
| --------------- | ------------- | ------------------------------------------ |
| `id`            | Long          | Identificador único de la venta            |
| `productId`     | Long          | Identificador del producto vendido         |
| `productName`   | String        | Nombre del producto al momento de la venta |
| `purchasePrice` | BigDecimal    | Precio de compra registrado                |
| `salePrice`     | BigDecimal    | Precio de venta registrado                 |
| `quantity`      | Integer       | Cantidad vendida                           |
| `total`         | BigDecimal    | Valor total de la venta                    |
| `profit`        | BigDecimal    | Utilidad generada                          |
| `saleDate`      | LocalDateTime | Fecha y hora de la venta                   |
| `status`        | SaleStatus    | Estado actual de la venta                  |

---

# Estado de una venta

El atributo:

```text id="f6k1pd"
status
```

utiliza el enumerado:

```text id="z4m8xa"
SaleStatus
```

Valores actuales:

```text id="8m1q6y"
COMPLETED
CANCELLED
```

---

## Razón del diseño

Cuando una venta ocurre, es importante conservar la información histórica del producto vendido.

Por esta razón la entidad `Sale` guarda una copia de datos importantes:

```text id="v8c2qx"
Product
 ├── name
 ├── purchasePrice
 └── salePrice


Sale
 ├── productName
 ├── purchasePrice
 └── salePrice
```

Esto evita que una modificación futura del producto altere la información histórica de una venta antigua.

Ejemplo:

```text id="n7m2vp"
Producto vendido:

Camisa
Precio venta: $50.000


Después:

Producto actualizado:
Precio venta: $60.000


La venta antigua mantiene:

Precio venta: $50.000
```

---

# Repositorios

Cada entidad cuenta con un repositorio encargado de interactuar con la base de datos.

Estructura:

```text id="g5m8xz"
repository/

├── UserRepository
├── ProductRepository
└── SaleRepository
```

Proporcionado por Spring Data JPA.

---

# Generación de IDs

Las entidades utilizan identificadores:

```java id="z9v2mc"
Long
```

para sus claves primarias.

La generación de IDs se realiza mediante estrategia automática de base de datos.

Ejemplo:

```text id="p8m3kv"
Nuevo registro
      │
      ▼
PostgreSQL genera ID
      │
      ▼
Entidad persistida
```

---

# Estructura conceptual de tablas

Representación simplificada:

```text id="m4z8qx"
┌───────────────┐
│     USER      │
├───────────────┤
│ id            │
│ name          │
│ email         │
│ password      │
│ role          │
└───────────────┘


┌───────────────┐
│   PRODUCT     │
├───────────────┤
│ id            │
│ name          │
│ purchasePrice │
│ salePrice     │
│ stock         │
└───────────────┘


┌───────────────┐
│     SALE      │
├───────────────┤
│ id            │
│ productId     │
│ productName   │
│ purchasePrice │
│ salePrice     │
│ quantity      │
│ total         │
│ profit        │
│ saleDate      │
│ status        │
└───────────────┘
```

---

# Flujo de persistencia de una venta

Cuando se registra una venta:

```text id="t5m7qz"
Solicitud de venta
        │
        ▼
SaleService
        │
        ├── Buscar Product
        │
        ├── Validar stock
        │
        ├── Actualizar stock
        │
        ├── Crear Sale
        │
        ▼
Repositories
        │
        ▼
PostgreSQL
```

La operación se ejecuta dentro de una transacción para mantener la consistencia entre inventario y ventas.

---

# Validaciones de datos

Las validaciones principales se aplican mediante DTOs antes de llegar a la persistencia.

Ejemplos:

## Producto

* Nombre obligatorio.
* Precio de compra positivo.
* Precio de venta positivo.
* Stock mayor o igual a cero.

## Venta

* Identificador de producto válido.
* Cantidad positiva.

## Usuario

* Nombre obligatorio.
* Email válido.
* Contraseña requerida.

---

# Decisiones importantes de diseño

## Uso de DTOs

Las entidades no son expuestas directamente mediante la API.

El flujo utilizado es:

```text id="r4n8yv"
Request DTO
      ↓
Entity
      ↓
Database


Database
      ↓
Entity
      ↓
Response DTO
```

Esto evita acoplar la estructura de la API con la base de datos.

---

## Separación por dominios

Cada módulo administra sus propias entidades:

```text id="w8c4pz"
product → Product

sale → Sale

user → User
```

Esto mantiene organizado el código y facilita futuras modificaciones.

---

## Estado actual del modelo

Actualmente Keventa cuenta con:

* Gestión de usuarios.
* Gestión de productos.
* Control de inventario.
* Registro de ventas.
* Cancelación de ventas.
* Cálculo de utilidad.

---

# Documentos relacionados

* [Arquitectura](arquitectura.md)
* [Seguridad](seguridad.md)
* [Roles y permisos](roles-y-permisos.md)
* [Reglas de negocio](reglas-de-negocio.md)

