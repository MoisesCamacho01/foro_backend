# Tarea 07 — JWT y filtro de seguridad

**Dificultad:** ⭐⭐⭐ (media)  
**Dependencias:** 06  
**Estimación:** 1.5 h  
**Estado:** ✅ Completada

---

## Objetivo

Proteger los endpoints del foro validando el header `Authorization: Bearer <token>` que enviará el frontend.

---

## Componentes

### `JwtService`

- `generateToken(String alias)` — firmar con secret de `application.properties`
- `extractAlias(String token)` — leer claim
- `isTokenValid(String token)` — expiración y firma

### `JwtAuthFilter` o interceptor

- Leer header `Authorization`
- Extraer Bearer token
- Validar y poblar contexto de usuario (p. ej. `SecurityContext` o `ThreadLocal` simple)
- Rutas públicas: `/auth/login`, `/actuator/**` (si aplica)
- Rutas protegidas: `/questions`, `/comments/**`, `/auth/logout`

### Configuración de seguridad

Opciones:
- **Spring Security** (filtro JWT stateless) — recomendado
- **Filter manual** registrado en `WebMvcConfigurer` — más simple

---

## Endpoints protegidos

| Ruta | Requiere JWT |
|------|--------------|
| `GET /questions` | Sí |
| `POST /questions` | Sí |
| `POST /comments` | Sí |
| `POST /comments/:id/vote` | Sí |
| `POST /auth/logout` | Sí |
| `POST /auth/login` | No |

---

## Errores

| Código | Escenario |
|--------|-----------|
| 401 | Token ausente, expirado o inválido |

> El frontend manejará 401 con interceptor (redirigir a `/login`) — pendiente en frontend.

---

## Criterios de aceptación

- [x] Token generado en login es aceptado en requests protegidos
- [x] Request sin token a `/questions` retorna 401
- [x] Alias del token disponible en servicios (para autoría de preguntas/réplicas/votos)
- [x] `/auth/login` sigue siendo público

---

## Referencias

- `documentation/api-requests-flujo.md` — Headers Authorization
