# Tarea 05 — DTOs de autenticación

**Dificultad:** ⭐⭐ (media-baja)  
**Dependencias:** 02  
**Estimación:** 20 min

---

## Objetivo

Definir los records de entrada/salida para el flujo de login, compatibles con el frontend.

---

## DTOs

### `LoginRequest`

```java
public record LoginRequest(
    @NotBlank @Size(min = 1, max = 50) String alias
) {
    public LoginRequest {
        alias = alias != null ? alias.trim() : null;
        if (alias != null && alias.isBlank()) {
            throw new IllegalArgumentException("El alias no puede estar vacío");
        }
    }
}
```

### `LoginResponse`

Compatible con `LoginResponse` del frontend:

```java
public record LoginResponse(
    String alias,
    String token
) {}
```

> El frontend marca `token` como opcional en TypeScript, pero el backend **debe** generarlo para endpoints protegidos.

---

## Criterios de aceptación

- [ ] Validación `@NotBlank` rechaza alias vacío → 400
- [ ] `LoginResponse` serializa `{ "alias": "...", "token": "..." }`
- [ ] Records con constructor compacto para trim/validación

---

## Referencias

- `documentation/api-requests-flujo.md` — LoginRequest / LoginResponse
