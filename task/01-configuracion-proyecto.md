# Tarea 01 — Configuración del proyecto

**Dificultad:** ⭐ (baja)  
**Dependencias:** ninguna  
**Estimación:** 30 min

---

## Objetivo

Preparar el proyecto Maven con las dependencias y estructura de paquetes definidas en `for_backend.mdc`.

---

## Acciones

### 1. Agregar dependencias en `pom.xml`

- `spring-boot-starter-validation`
- `lombok` (scope provided)
- `jjwt-api`, `jjwt-impl`, `jjwt-jackson` (JWT para auth) — versión compatible con Java 17
- `spring-boot-starter-test` (MockMvc) — verificar si ya está cubierto por `webmvc-test`

### 2. Crear estructura de paquetes

```
com.example.foro_backend
├── config/
├── controller/
├── dto/
│   ├── auth/
│   └── forum/
├── exception/
├── model/
├── repository/
│   └── impl/
└── service/
    └── impl/
```

### 3. Configurar `application.properties`

```properties
server.port=8080
app.data.dir=./data
app.jwt.secret=<generar-secret-seguro>
app.jwt.expiration-ms=86400000
spring.jackson.serialization.write-dates-as-timestamps=false
```

### 4. Crear carpeta de datos

- `data/` en la raíz del proyecto (añadir a `.gitignore` si contiene datos de desarrollo)
- Definir rutas: `data/users.json`, `data/comments.json`, `data/votes.json`

---

## Criterios de aceptación

- [ ] `mvn compile` exitoso
- [ ] Lombok procesa anotaciones sin error
- [ ] Estructura de paquetes creada (aunque vacía)
- [ ] `application.properties` con propiedades documentadas

---

## Referencias

- `pom.xml`
- `.cursor/for_backend.mdc` — Stack tecnológico
