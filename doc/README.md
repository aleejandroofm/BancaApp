BancaApp

Proyecto basado en una aplicación de sistema bancario digital por consola, el cual incluye todos los servicios básicos de realizar transferencias, ingresar y retirar dinero desde la cuenta personal de cada cliente, además de la implementación de los métodos de pago y operativas esenciales que debe tener una app de estas características. En este caso, se hace uso de pago en efectivo, transferencia y Bizum.

El sistema cuenta con una diferenciación de roles estricta entre Clientes, Empleados y Administradores, y está diseñado bajo los principios de la metodología ágil Scrum y el patrón arquitectónico MVC (Modelo-Vista-Controlador).
Tecnologías Utilizadas
Tecnología	Tipo	Versión / Entorno
Java	Backend	JDK 17 o superior
MySQL	Base de datos	8.0
Trello	Gestión Ágil	Tablero Scrum (Sprints)
Git/GitHub	Control de Versiones	Estrategia Gitflow (main/dev)
🏗️ Arquitectura y Estructura del Proyecto

El código fuente está modularizado en paquetes específicos para garantizar la separación de responsabilidades:

    Modelo: Contiene el dominio del sistema (Usuario, Cliente, Empleado, Administrador, Cuenta, Bizum, Ingreso, Retirada). Implementación estricta de herencia y polimorfismo.

    Vista: Capa de interacción con el usuario mediante menús interactivos y seguros por consola (LoginView, ClienteView, EmpleadoView).

    Controlador: Orquestador que comunica las vistas con los servicios de negocio.

    Dao: Capa de persistencia encargada de las consultas e inserciones relacionales SQL (UsuarioDAO, CuentaDAO, OperacionDAO, LogAuditoriaDAO).

    Logica: Capa de servicios (CuentaService) encargada de validar las reglas de negocio y garantizar transacciones atómicas.

    Excepciones: Sistema de excepciones personalizadas jerárquico basado en una clase raíz común (BancaAppException).

🛡️ Robustez y Control de Errores

La aplicación implementa un sistema robusto de excepciones personalizadas no verificadas (Unchecked Exceptions). Cada error de negocio cuenta con un mensaje descriptivo y un código unificado inyectado dinámicamente para facilitar la auditoría técnica:

    UsuarioBloqueadoException [ERR-AUTH-403]: Detiene el acceso en el login si la cuenta del usuario está inactiva en la base de datos.

    DestinatarioInvalidoException [ERR-OPERACION-400]: Restringe operaciones de transferencia o Bizum hacia la misma cuenta de origen o el propio número de teléfono del cliente.

    PersistenciaException [ERR-DB-500]: Captura anomalías de conexión relacional o restricciones de claves foráneas en MySQL.

    CuentaNoEncontradaException [ERR-CUENTA-404]: Lanzada cuando un IBAN o teléfono no existe en los registros.

    SaldoInsuficienteException, LimiteExcedidoException y DatoInvalidoException.

Todas las vistas implementan la interfaz ErrorHandler.ErrorDisplay, lo que garantiza que los errores se capturen mediante bloques try-catch específicos y se muestren en cajas de texto limpias, erradicando los volcados de líneas rojas en la consola.
📋 Gestión de Auditoría Administrativa

El sistema cuenta con un servicio de trazabilidad integrado para operaciones en ventanilla y gestiones de personal. Operaciones como ALTA_USUARIO, BLOQUEAR_CUENTA o HABILITAR_CUENTA ejecutan una inserción atómica en los históricos de auditoría mediante bloques finally, asegurando el registro del DNI del empleado responsable, la fecha exacta y el estado del resultado de la acción.
🚀 Instalación y Despliegue
Requisitos Previos

    Poseer Java JDK 17 o superior.

    Tener instalado un IDE compatible (ej. Eclipse IDE).

    Servidor local MySQL Server 8.0 activo.

1. Clonar el repositorio
Bash

git clone https://github.com/aleejandroofm/BancaApp.git

2. Importar proyecto en Eclipse

    Abre Eclipse IDE.

    Ve a File > Import...

    Selecciona General > Existing Projects into Workspace y haz clic en Next.

    En Select root directory, busca la carpeta del proyecto clonado.

    Asegúrate de que el proyecto esté marcado y haz clic en Finish.

3. Configuración de la Base de Datos

    Ejecuta el script script.sql incluido en la raíz del proyecto dentro de tu gestor de MySQL (Workbench, phpMyAdmin o CLI) para crear el esquema y las tablas necesarias.

    Dirígete a la carpeta src/ en Eclipse y edita el archivo config.properties:

Properties

db.url=jdbc:mysql://localhost:3306/bancaapp_db
db.user=tu_usuario_mysql
db.password=tu_contraseña_mysql

4. Ejecución

    Localiza la clase principal Main dentro del paquete ejecutable (App).

    Haz clic derecho sobre el archivo -> Run As > Java Application.

📊 Estado Actual del Proyecto (Metodología Scrum)

    [x] Sprint 1: Configuración inicial, estructuración MVC y modelado del dominio con herencia de usuarios.

    [x] Sprint 2: Conexión a base de datos MySQL mediante carga dinámica con config.properties.

    [x] Sprint 3: Implementación del patrón DAO, lógica de negocio en servicios y transacciones.

    [x] Sprint 4: Robustez integral del software: creación del paquete de excepciones personalizadas, refactorización de vistas con bloques try-catch y maquetación final de auditorías.

👥 Integrantes del Equipo y Gestión Ágil

El flujo y evolución temporal del desarrollo del software ha sido monitorizado de forma descriptiva a través de un tablero Kanban/Scrum en Trello, dividiendo los hitos de control en Sprints con sus respectivas fechas de vencimiento estimadas correlativas al histórico de Git.
Rol	Nombre	Especialidad
Product Owner / Scrum Master	Alejandro Ferrándiz Martínez	Backend, Arquitectura de Datos y Persistencia
Co-Owner	Pablo Ariel Mathieu Ruiz	Diseño de Interfaz y Lógica de Consola# BancaApp

Proyecto basado en una aplicación de sistema bancario digital por consola, el cual incluye todos los servicios básicos de realizar **transferencias, ingresar y retirar dinero** desde la cuenta personal de cada cliente, además de la implementación de los métodos de pago y operativas esenciales que debe tener una app de estas características. En este caso, se hace uso de **pago en efectivo, transferencia y Bizum**. 

El sistema cuenta con una diferenciación de roles estricta entre **Clientes, Empleados y Administradores**, y está diseñado bajo los principios de la metodología ágil **Scrum** y el patrón arquitectónico **MVC (Modelo-Vista-Controlador)**.

---

### Tecnologías Utilizadas

| **Tecnología** | **Tipo** | **Versión / Entorno** |
| -------------- | ----------------- | --------------------- |
| **Java** | Backend           | JDK 17 o superior     |
| **MySQL** | Base de datos     | 8.0                   |
| **Trello** | Gestión Ágil      | Tablero Scrum (Sprints)|
| **Git/GitHub** | Control de Versiones | Estrategia Gitflow (main/dev) |

---

## 🏗️ Arquitectura y Estructura del Proyecto

El código fuente está modularizado en paquetes específicos para garantizar la separación de responsabilidades:

* `Modelo`: Contiene el dominio del sistema (`Usuario`, `Cliente`, `Empleado`, `Administrador`, `Cuenta`, `Bizum`, `Ingreso`, `Retirada`). Implementación estricta de herencia y polimorfismo.
* `Vista`: Capa de interacción con el usuario mediante menús interactivos y seguros por consola (`LoginView`, `ClienteView`, `EmpleadoView`).
* `Controlador`: Orquestador que comunica las vistas con los servicios de negocio.
* `Dao`: Capa de persistencia encargada de las consultas e inserciones relacionales SQL (`UsuarioDAO`, `CuentaDAO`, `OperacionDAO`, `LogAuditoriaDAO`).
* `Logica`: Capa de servicios (`CuentaService`) encargada de validar las reglas de negocio y garantizar transacciones atómicas.
* `Excepciones`: Sistema de excepciones personalizadas jerárquico basado en una clase raíz común (`BancaAppException`).

---

## 🛡️ Robustez y Control de Errores

La aplicación implementa un sistema robusto de excepciones personalizadas no verificadas (*Unchecked Exceptions*). Cada error de negocio cuenta con un mensaje descriptivo y un código unificado inyectado dinámicamente para facilitar la auditoría técnica:

* **`UsuarioBloqueadoException`** `[ERR-AUTH-403]`: Detiene el acceso en el login si la cuenta del usuario está inactiva en la base de datos.
* **`DestinatarioInvalidoException`** `[ERR-OPERACION-400]`: Restringe operaciones de transferencia o Bizum hacia la misma cuenta de origen.
* **`PersistenciaException`** `[ERR-DB-500]`: Captura anomalías de conexión relacional o restricciones de claves foráneas en MySQL.
* **`CuentaNoEncontradaException`** `[ERR-CUENTA-404]`: Lanzada cuando un IBAN o teléfono no existe en los registros.
* **`SaldoInsuficienteException`**, **`LimiteExcedidoException`** y **`DatoInvalidoException`**.

Todas las vistas implementan la interfaz `ErrorHandler.ErrorDisplay`, lo que garantiza que los errores se capturen mediante bloques `try-catch` estratégicos y se muestren en cajas de texto limpias, erradicando los volcados de líneas rojas en la consola.

---

## 📋 Gestión de Auditoría Administrativa

El sistema cuenta con un servicio de trazabilidad integrado para operaciones en ventanilla y gestiones de personal. Operaciones como `ALTA_USUARIO`, `BLOQUEAR_CUENTA` o `HABILITAR_CUENTA` ejecutan una inserción atómica en los históricos de auditoría mediante bloques `finally`, asegurando el registro del DNI del empleado responsable, la fecha exacta y el estado del resultado de la acción.

---

## 🚀 Instalación y Despliegue

### Requisitos Previos
- Poseer **Java JDK 17 o superior**.
- Tener instalado un IDE compatible (ej. **Eclipse IDE**).
- Servidor local **MySQL Server 8.0** activo.

### 1. Clonar el repositorio
```bash
git clone [https://github.com/aleejandroofm/BancaApp.git](https://github.com/aleejandroofm/BancaApp.git)
