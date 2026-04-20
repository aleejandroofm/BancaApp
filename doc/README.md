# BancaApp
Proyecto basado en una aplicación de sistema bancario digital, el cuál incluye todos los servicios básicos de realizar **transferencias, ingresar y retirar dinero** desde la cuenta personal de cada cliente, además de la implementación de los métodos de pago básicos que debe tener una app de estas características. En este caso, se hace uso de **pago en efectivo, por tarjeta, transferencia y Bizum**. También se visualiza una diferenciación de roles entre **Clientes, Empleados y Administradores.**

### Para este proyecto, hacemos uso de las siguientes tecnologías:

| **Tecnología** | **Tipo**          | **Versión**   |
| -------------- | ----------------- | ------------- |
| Java           | Backend           | 1.8           |
| MySQL          | Base de datos     | 8.0           |
| Pendiente      | Framework         | Pendiente     |

## *Instalación del proyecto*
Para la instalación del proyecto, será necesario cumplir con una serie de requisitos previos al mismo, los cuales serán los siguientes:

- Poseer **Java JDK 17 o superior**.
- Tener instalado cualquier tipo de IDE permitido, por ejemplo, **Eclipse IDE**.

### *Clonar repositorio*
```bash
git clone https://github.com/aleejandroofm/BancaApp.git
```
### *Importar proyecto a Eclipse*

```markdown

### Importar en Eclipse

1. Abrir Eclipse
2. Ir a *File > Import*
3. Seleccionar "Existing Projects into Workspace"
4. Buscar la carpeta del proyecto clonado
5. Hacer clic en "Finish"

### Configuración

Editar el archivo `config.properties`:

- URL de la base de datos
- Usuario
- Contraseña

### Ejecutar la aplicación

1. Click derecho en la clase principal
2. Seleccionar *Run As > Java Application*

```

### Estados del proyecto

- [X] Configuración Inicial
- [X] Conexión a base de datos
- [ ] Implementación de autenticación
- [ ] Tests unitarios del código

### Integrantes del equipo 

| **Rol**       | **Nombre**                   | **Encargo**  |
| ------------- | ---------------------------- | ------------ |
| Product Owner | Alejandro Ferrándiz Martínez | **Backend**  |