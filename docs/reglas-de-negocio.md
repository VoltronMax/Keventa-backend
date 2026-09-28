# Reglas de negocio 

Este documento describe las reglas que determinan cómo debe comportarse Keventa durante las operaciones principales del sistema.

Estas reglas representan las condiciones y restricciones que debe respetar el sistema para mantener la información consistente.

---

## Productos

Los productos representan los artículos disponibles en el inventario de la boutique.

### Registro de productos

Para registrar un producto se debe proporcionar la información necesaria para identificarlo y establecer sus valores comerciales.

Un producto contiene, entre otros datos:

* Nombre.
* Precio de compra.
* Precio de venta.
* Stock disponible.

El sistema valida que los valores recibidos sean válidos antes de registrar el producto.

### Actualización de productos

La información de un producto puede ser modificada posteriormente.

La actualización se realiza de manera parcial, por lo que no es necesario proporcionar todos los campos del producto.

Los campos que no sean enviados mantienen su valor actual.

### Eliminación de productos

Un producto puede ser eliminado del sistema.

La eliminación de un producto requiere que este exista previamente.

### Consulta de productos

El sistema permite:

* Obtener todos los productos.
* Obtener un producto por su ID.
* Obtener un producto por su nombre.
* Buscar productos cuyo nombre contenga un texto determinado.

La búsqueda por nombre no requiere que el texto coincida exactamente con el nombre completo del producto.

---

# Inventario

El stock representa la cantidad disponible de unidades de un producto.

El inventario se modifica principalmente como consecuencia de las operaciones de venta.

## Registro de una venta

Antes de registrar una venta, el sistema debe comprobar que:

1. El producto exista.
2. La cantidad solicitada sea válida.
3. Exista suficiente stock disponible.

Si el stock disponible es menor que la cantidad solicitada, la venta no puede realizarse.

### Actualización del stock

Cuando una venta se registra correctamente:

```text
Stock nuevo = Stock actual - Cantidad vendida
```

Por ejemplo:

```text
Stock actual:       10
Cantidad vendida:   3
----------------------
Stock restante:     7
```

El cambio de stock forma parte de la misma operación de registro de la venta.

---

# Ventas

Las ventas representan las transacciones realizadas sobre los productos de la boutique.

## Registro de ventas

Una venta debe estar asociada a un producto existente.

Al registrar una venta, el sistema almacena información relacionada con la operación, incluyendo:

* Producto vendido.
* Nombre del producto.
* Precio de compra.
* Precio de venta.
* Cantidad vendida.
* Total de la venta.
* Utilidad obtenida.
* Fecha de la venta.
* Estado de la venta.

Una venta registrada correctamente comienza con el estado:

```text
COMPLETED
```

## Cálculo del total

El total de una venta se obtiene multiplicando el precio de venta del producto por la cantidad vendida.

```text
Total = Precio de venta × Cantidad
```

Por ejemplo:

```text
Precio de venta:  $50.000
Cantidad:              3
---------------------------
Total:             $150.000
```

## Cálculo de la utilidad

La utilidad representa la diferencia entre el valor total de la venta y el costo total de los productos vendidos.

```text
Costo total = Precio de compra × Cantidad

Utilidad = Total de venta - Costo total
```

Por ejemplo:

```text
Precio de compra: $30.000
Precio de venta:  $50.000
Cantidad:                 2

Total:             $100.000
Costo total:        $60.000
Utilidad:           $40.000
```

La utilidad se calcula utilizando los precios registrados en el momento de realizar la venta.

---

# Cancelación de ventas

Una venta puede ser cancelada posteriormente.

La cancelación está disponible únicamente para el rol `ADMIN`.

## Condiciones

Para cancelar una venta:

1. La venta debe existir.
2. La venta no debe estar previamente cancelada.
3. El producto asociado debe existir.

Si la venta ya se encuentra cancelada, el sistema rechaza la operación.

## Restauración del stock

Cuando una venta es cancelada, las unidades vendidas vuelven a estar disponibles en el inventario.

```text
Stock nuevo = Stock actual + Cantidad de la venta
```

Ejemplo:

```text
Stock actual:             7
Cantidad de venta:        3
----------------------------
Stock restaurado:        10
```

De esta manera, cancelar una venta revierte el efecto que tuvo la operación sobre el inventario.

## Estado de una venta cancelada

Cuando la cancelación se realiza correctamente, el estado de la venta cambia a:

```text
CANCELLED
```

Una venta que se encuentre en estado `CANCELLED` no puede volver a cancelarse.

---

# Devoluciones

Actualmente Keventa **no contempla un proceso independiente de devoluciones**.

Las devoluciones no forman parte de las operaciones disponibles en la versión actual del sistema.

Si posteriormente se incorpora esta funcionalidad, deberá definirse un conjunto específico de reglas para determinar cómo afectan las devoluciones al inventario, las ventas y la utilidad.

---

# Usuarios

Los usuarios representan las personas autorizadas para utilizar el sistema.

Cada usuario posee:

* Nombre.
* Correo electrónico.
* Contraseña.
* Rol.

## Registro de usuarios

No se permite registrar dos usuarios con el mismo correo electrónico.

Si el correo ya se encuentra registrado, el sistema rechaza la operación.

Las contraseñas no se almacenan directamente. Antes de persistirlas, son transformadas mediante BCrypt.

## Primer usuario

Cuando no existen usuarios registrados en el sistema, el primer usuario creado recibe automáticamente el rol:

```text
ADMIN
```

Los usuarios creados posteriormente reciben inicialmente el rol:

```text
EMPLOYEE
```

Esto permite que el sistema disponga de un administrador inicial sin requerir una asignación manual del primer rol.

## Actualización de usuarios

La información de un usuario puede actualizarse parcialmente.

Los campos no enviados mantienen su valor actual.

Si se modifica el correo electrónico, el nuevo correo no puede pertenecer a otro usuario existente.

Si se modifica la contraseña, esta debe volver a almacenarse utilizando BCrypt.

## Cambio de rol

El rol de un usuario puede ser modificado.

Esta operación está reservada al rol `ADMIN`.

Los roles disponibles actualmente son:

```text
ADMIN
EMPLOYEE
```

## Eliminación de usuarios

Un usuario puede ser eliminado del sistema siempre que exista previamente.

La administración de usuarios corresponde exclusivamente al rol `ADMIN`.

---

# Roles y operaciones

Las reglas de negocio y los permisos de acceso son conceptos relacionados, pero diferentes.

Los roles determinan quién puede ejecutar determinadas operaciones.

## ADMIN

El administrador tiene acceso completo a las operaciones administrativas disponibles actualmente.

Puede:

* Administrar productos.
* Administrar usuarios.
* Registrar ventas.
* Consultar ventas.
* Cancelar ventas.

## EMPLOYEE

El empleado tiene acceso a las operaciones necesarias para el funcionamiento cotidiano de la boutique.

Puede:

* Consultar productos.
* Buscar productos.
* Registrar ventas.
* Consultar ventas.

No puede:

* Registrar productos.
* Modificar productos.
* Eliminar productos.
* Administrar usuarios.
* Cambiar roles.
* Cancelar ventas.

La descripción completa de los permisos se encuentra en:

**[Roles y permisos](roles-and-permissions.md)**

---

# Consistencia de las operaciones

Las operaciones que modifican información relacionada deben mantener los datos consistentes.

Por ejemplo, registrar una venta implica modificar simultáneamente:

```text
Venta
  +
Stock del producto
```

De la misma manera, cancelar una venta implica:

```text
Estado de la venta
  +
Stock del producto
```

Estas operaciones se ejecutan como una única transacción para evitar que una parte de la operación se complete mientras otra falla.

---

# Validación de datos

Keventa valida los datos recibidos antes de ejecutar las operaciones correspondientes.

Las validaciones se aplican principalmente sobre los DTO utilizados como entrada.

Entre los valores que se controlan se encuentran:

* Campos obligatorios.
* Valores positivos.
* Valores que no pueden ser negativos.
* Formato de los datos.
* Correos electrónicos.
* Contraseñas.

Las reglas específicas de validación dependen del DTO utilizado por cada operación.

---

# Manejo de situaciones inválidas

Cuando una operación no puede ejecutarse debido a una condición del negocio, Keventa genera una excepción específica.

Algunos ejemplos son:

```text
Producto inexistente
        ↓
ProductNotFoundException

Venta inexistente
        ↓
SaleNotFoundException

Stock insuficiente
        ↓
InsufficientStockException

Venta ya cancelada
        ↓
SaleAlreadyCancelledException

Correo ya registrado
        ↓
EmailAlreadyRegistered
```

Estas excepciones son procesadas por el mecanismo global de manejo de errores de Keventa.

---

# Resumen de reglas principales

| Operación            | Regla                                        |
| -------------------- | -------------------------------------------- |
| Registrar producto   | Debe proporcionar información válida         |
| Actualizar producto  | La actualización puede ser parcial           |
| Eliminar producto    | El producto debe existir                     |
| Registrar venta      | El producto debe existir                     |
| Registrar venta      | Debe existir stock suficiente                |
| Registrar venta      | El stock se reduce según la cantidad vendida |
| Registrar venta      | Se calcula total y utilidad                  |
| Cancelar venta       | Solo puede hacerlo `ADMIN`                   |
| Cancelar venta       | La venta debe existir                        |
| Cancelar venta       | No puede estar previamente cancelada         |
| Cancelar venta       | El stock se restaura                         |
| Registrar usuario    | El correo debe ser único                     |
| Registrar usuario    | El primer usuario obtiene `ADMIN`            |
| Registrar usuario    | Los siguientes usuarios obtienen `EMPLOYEE`  |
| Cambiar rol          | Solo puede hacerlo `ADMIN`                   |
| Administrar usuarios | Solo puede hacerlo `ADMIN`                   |
| Devoluciones         | No están implementadas actualmente           |

---

# Estado de estas reglas

Las reglas descritas corresponden al comportamiento actualmente implementado en Keventa.

A medida que el sistema evolucione, este documento deberá actualizarse cuando se incorporen nuevas operaciones o cambien las reglas existentes.

El objetivo es que este documento represente **qué debe hacer el sistema**, independientemente de cómo se encuentre implementado técnicamente.
