# Tarea 02 — Infraestructura base

**Dificultad:** ⭐ (baja)  
**Dependencias:** 01  
**Estimación:** 45 min

---

## Objetivo

Implementar las clases transversales que usarán todos los endpoints: respuesta unificada, excepciones y manejo global de errores.

---

## Acciones

### 1. `ApiResponse<T>`

Implementar según `for_backend.mdc`:
- Campos: `result`, `message`, `data`, `timestamp`
- Métodos estáticos: `success(message, data)`, `error(message)`

### 2. Excepciones personalizadas

| Clase | Uso | HTTP |
|-------|-----|------|
| `ResourceNotFoundException` | Padre/comentario no encontrado | 404 |
| `AliasAlreadyExistsException` | Alias duplicado en login | 409 |
| `PersistenceException` | Fallo lectura/escritura JSON | 500 |
| `UnauthorizedException` | Token inválido o ausente | 401 |

### 3. `GlobalExceptionHandler`

- `@RestControllerAdvice`
- Handlers para: validación (`MethodArgumentNotValidException` → 400), excepciones anteriores, genérico → 500
- Sin `try-catch` en controladores

### 4. Configuración CORS

- `@Configuration` con `WebMvcConfigurer` o `@CrossOrigin` en controladores (`origins = "*"` según reglas)

### 5. Decisión de contrato API

Documentar en un comentario o mini-doc si las respuestas van envueltas en `ApiResponse` o como DTO directo (ver `task/README.md`).

---

## Criterios de aceptación

- [ ] `ApiResponse` serializa correctamente a JSON
- [ ] `GlobalExceptionHandler` devuelve JSON con estructura consistente
- [ ] CORS habilitado para el origen del frontend Angular
- [ ] Decisión de contrato documentada

---

## Referencias

- `.cursor/for_backend.mdc` — ApiResponse y GlobalExceptionHandler
