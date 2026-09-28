# Keventa

Keventa es un sistema de gestión para una boutique, desarrollado con el objetivo de centralizar y facilitar el manejo de productos, ventas, inventario y usuarios.

El proyecto nace como una alternativa al manejo tradicional de la información mediante hojas de cálculo, buscando ofrecer una solución más organizada, segura y fácil de mantener.

## Objetivo

El objetivo principal de Keventa es proporcionar una herramienta que permita gestionar las operaciones principales de una boutique desde un único sistema, reduciendo la dependencia de procesos manuales y facilitando el control de la información.

## Funcionalidades actuales

Actualmente Keventa cuenta con tres módulos principales:

* **Productos:** gestión y consulta del inventario.
* **Ventas:** registro, consulta y cancelación de ventas, junto con el control automático del stock y cálculo de utilidad.
* **Usuarios:** administración de las cuentas que tienen acceso al sistema.

El sistema también cuenta con autenticación mediante JWT y autorización basada en roles.

## Roles

Keventa actualmente maneja dos roles:

* **ADMIN:** acceso completo a las funcionalidades administrativas del sistema.
* **EMPLOYEE:** acceso limitado a las operaciones necesarias para las actividades habituales de la boutique.

La descripción detallada de cada rol y sus permisos se encuentra en la documentación del proyecto.

## Tecnologías

### Backend

* Java 21
* Spring Boot
* Spring Data JPA
* Hibernate
* Spring Security
* JWT
* PostgreSQL
* BCrypt

### Herramientas

* Git
* GitHub
* Postman
* DataGrip

## Arquitectura

Keventa utiliza una arquitectura organizada por dominios, separando las responsabilidades relacionadas con usuarios, productos, ventas, autenticación y seguridad.

Cada módulo mantiene sus propias capas de Controller, Service, Repository, DTO y Mapper cuando son necesarias.

La estructura y las decisiones arquitectónicas del proyecto se encuentran documentadas en:

**[Arquitectura](/arquitectura.md)**

## Seguridad

El sistema utiliza Spring Security y JWT para proteger los recursos de la aplicación.

La autenticación se realiza mediante credenciales de usuario y posteriormente se utiliza un token JWT para acceder a los recursos protegidos.

La autorización se controla mediante los roles asignados a cada usuario.

Para conocer en detalle el funcionamiento del sistema de seguridad:

**[Seguridad](/seguridad.md)**

## API

Keventa expone una API REST versionada `/api/v1`.

Los principales recursos son:

```text
/api/v1/productos
/api/v1/ventas
/api/v1/usuarios
```

La documentación detallada de los endpoints se encuentra en:

**[API](/api.md)**

## Reglas de negocio

Las operaciones del sistema siguen determinadas reglas relacionadas con el inventario, las ventas, los usuarios y los permisos de cada rol.

Estas reglas se encuentran documentadas en:

**[Reglas de negocio](/reglas-de-negocio.md)**

## Base de datos

Keventa utiliza PostgreSQL como sistema de gestión de base de datos y Spring Data JPA/Hibernate para la persistencia de las entidades.

La documentación de las entidades, atributos y relaciones se encuentra en:

**[Base de datos](/bd.md)**

## Documentación

La documentación detallada del proyecto se encuentra organizada en la carpeta `docs/`:

```text
docs/
├── arquitectura.md
├── api.md
├── seguridad.md
├── roles-y-permisos.md
├── reglas-de-negocio.md
└── bd.md
```

Cada documento aborda una responsabilidad específica del sistema para evitar concentrar toda la información en un único archivo.

## Estado del proyecto

El backend actualmente cuenta con:

* Gestión de productos.
* Gestión de ventas.
* Gestión de usuarios.
* Control de inventario.
* Cálculo de utilidad.
* Cancelación de ventas.
* Validaciones de datos.
* DTOs y Mappers.
* Manejo global de excepciones.
* Autenticación mediante JWT.
* Autorización basada en roles.
* Protección mediante `@PreAuthorize`.
* Manejo diferenciado de errores `401` y `403`.
* Pruebas manuales mediante Postman.

## Licencia

Este proyecto se encuentra actualmente en desarrollo.
