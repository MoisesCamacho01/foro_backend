Clona el proyecto de frontend: https://github.com/MoisesCamacho01/foro_frontend
Clona el proyecto de backend: https://github.com/MoisesCamacho01/foro_backend
## Recomendación:
- Se recomienda usar linux para no tener ningún inconveniente al momento de probar el proyecto
- El sistema requiere **dos terminales**: una para el backend y otra para el frontend.
# Instrucciones Backend

## Requisitos

| Requisito | Detalle |
|-----------|---------|
| **JDK** | Java 17 o superior |
| **Maven** | Opcional; el proyecto incluye `./mvnw` |
| **Base de datos** | No requerida (persistencia en JSON) |
| **Puerto** | `8080` libre por defecto (configurable) |
| **Sistema operativo** | Linux, macOS o Windows |
Verificar Java:
```bash
java -version
```
## Configuración
Las propiedades principales están en `src/main/resources/application.properties`:

| Propiedad                    | Valor por defecto | Descripción                                              |
| ---------------------------- | ----------------- | -------------------------------------------------------- |
| `server.port`                | `8080`            | Puerto HTTP del servidor                                 |
| `server.address`             | `0.0.0.0`         | Interfaz de escucha (accesible en red local)             |
| `app.data.dir`               | `./data`          | Carpeta donde se guardan los archivos JSON               |
| `app.jwt.secret`             | *(dev)*           | Clave para firmar tokens JWT                             |
| `app.jwt.expiration-ms`      | `86400000`        | Expiración del token (24 h)                              |
| `app.forum.max-reply-levels` | `3`               | Profundidad máxima de respuestas: `3`, `5` o `ilimitado` |
> En producción, cambiar obligatoriamente `app.jwt.secret` por un valor seguro.
### Datos iniciales
- Al arrancar, se crea automáticamente la carpeta `data/` y los archivos `users.json`, `comments.json` y `votes.json` si no existen.
- Si `comments.json` está vacío, se cargan preguntas de ejemplo desde `src/main/resources/seed/comments.json`.

## Cómo levantar el proyecto
### 1. Clonar o ubicarse en el directorio del proyecto
 
```bash
cd foro_backend
```
### 2. Compilar (opcional)

```bash
./mvnw clean package -DskipTests
```
### 3. Ejecutar en modo desarrollo
```bash
./mvnw spring-boot:run
```

En Windows:
```bash
mvnw.cmd spring-boot:run
```
### 4. Ejecutar el JAR compilado

```bash
java -jar target/foro_backend-0.0.1-SNAPSHOT.jar
```

### 5. Verificar que responde

El servidor queda disponible en `http://localhost:8080`.
Probar login (endpoint público):
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"alias":"demo"}'
```

La respuesta incluye un token JWT en `data.token`. Usarlo en rutas protegidas:
  
```bash
curl http://localhost:8080/questions \
  -H "Authorization: Bearer <token>"
```
### Pruebas

```bash
./mvnw test
```

31 pruebas cubren autenticación, endpoints del foro, persistencia JSON, límite de niveles de respuesta y concurrencia en votos.
# Instrucciones Frontend

## Requisitos

| Requisito     | Versión mínima | Notas                                                                    |
| ------------- | -------------- | ------------------------------------------------------------------------ |
| **Node.js**   | 20.x           | Requerido por Angular CLI 22                                             |
| **pnpm**      | 11.3.x         | Gestor de dependencias del proyecto (`packageManager` en `package.json`) |
| **Navegador** | Moderno        | Chrome, Firefox o Edge actualizado                                       |
| backend       | github         | En ejecucion                                                             |
### Opcional (pruebas E2E)

| Requisito      | Notas                                                                            |
| -------------- | -------------------------------------------------------------------------------- |
| **Playwright** | Se instala como dependencia del frontend; requiere Chromium para `pnpm test:e2e` |
### Configuración previa

Antes de levantar el frontend, verifica que `apiBaseUrl` en `src/environments/environment.ts` apunte al backend en ejecución:
```typescript
export const environment = {
  production: false,
  apiBaseUrl: 'http://localhost:8080',  // ajustar según tu entorno
};
```
> El backend debe estar corriendo antes de usar el foro. Sin él, el login y las acciones del foro fallarán.
## Cómo levantar el proyecto

### 2. Frontend

```bash
cd foro_frontend
pnpm install
pnpm start
```

- URL de la app: `http://localhost:4200`
- El servidor escucha en `0.0.0.0`, accesible desde la red local
- La recarga es automática al guardar cambios en desarrollo
### 3. Verificar que funciona
1. Abre `http://localhost:4200` en el navegador.
2. Ingresa un alias en `/login` y envía el formulario.
3. Tras el login, deberías ser redirigido a `/foro` con las preguntas cargadas.

Si el login falla, revisa que el backend esté activo y que `apiBaseUrl` coincida con su dirección.
### Comandos adicionales
```bash
# Frontend — build de producción
pnpm build
# Frontend — tests unitarios
pnpm test
# Frontend — tests E2E (requiere app corriendo en :4200)
pnpm test:e2e

```