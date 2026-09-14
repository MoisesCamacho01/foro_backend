# Tarea 06 — Endpoint POST /auth/login

**Dificultad:** ⭐⭐ (media-baja)  
**Dependencias:** 04, 05  
**Estimación:** 1 h  
**Estado:** ✅ Completada

---

## Objetivo

Implementar el login por alias sin contraseña, creando el usuario si no existe o reutilizando sesión si ya existe.

---

## Contrato

| Campo | Valor |
|-------|-------|
| **Método** | `POST` |
| **Ruta** | `/auth/login` |
| **Auth** | No requerida |

### Request

```json
{ "alias": "moises_dev" }
```

### Response 200

```json
{ "alias": "moises_dev", "token": "eyJ..." }
```

### Errores

| Código | Escenario |
|--------|-----------|
| 400 | Alias vacío o inválido (validación) |
| 409 | Alias ya ocupado *(solo si se decide registro exclusivo — ver nota)* |
| 500 | Error de persistencia |

> **Nota de negocio:** El mock del frontend no valida alias duplicado; acepta cualquier alias. Decidir si el login es **registro automático** (primer alias = crear usuario, alias existente = login) o **registro exclusivo** (409 si ya existe). Documentar la decisión.

---

## Capas

### `AuthService` / `AuthServiceImpl`

1. Normalizar alias (`trim`)
2. Buscar usuario en `UserRepository`
3. Si no existe → crear `UserModel` con UUID
4. Generar JWT con claim `alias` (implementación completa en tarea 07; aquí puede ser stub)
5. Retornar `LoginResponse`

### `AuthController`

```java
@PostMapping("/auth/login")
public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request)
```

- `@CrossOrigin(origins = "*")`
- Sin try-catch (delegar a advice)

---

## Criterios de aceptación

- [x] `POST /auth/login` con alias válido retorna 200 + alias + token
- [x] Alias vacío retorna 400
- [x] Usuario persistido en `data/users.json`
- [ ] Probable con cURL:

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"alias":"moises_dev"}'
```

---

## Referencias

- `documentation/api-requests-flujo.md` — Flujo de autenticación
