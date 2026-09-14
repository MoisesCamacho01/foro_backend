# Tarea 15 — Endpoint POST /auth/logout

**Dificultad:** ⭐⭐ (media-baja)  
**Dependencias:** 07  
**Estimación:** 30 min  
**Estado:** ✅ Completada

---

## Objetivo

Implementar logout opcional. El frontend elimina `localStorage` localmente; el backend puede invalidar token o simplemente confirmar.

---

## Contrato

| Campo | Valor |
|-------|-------|
| **Método** | `POST` |
| **Ruta** | `/auth/logout` |
| **Auth** | Bearer token |

### Request

Sin body.

### Response 200

```json
{ "message": "Sesión cerrada" }
```

(o `ApiResponse` vacío según decisión de contrato)

### Errores

| Código | Escenario |
|--------|-----------|
| 401 | Sin token |

---

## Estrategias de implementación

| Estrategia | Complejidad | Notas |
|------------|-------------|-------|
| **Stateless simple** | Baja | Solo retorna 200; el cliente descarta el token |
| **Blacklist JWT** | Media | Guardar tokens invalidados en memoria/JSON hasta expiración |

Para MVP, **stateless simple** es suficiente (el frontend no valida token con backend en cada navegación).

---

## Criterios de aceptación

- [x] `POST /auth/logout` con token válido retorna 200
- [x] Sin token retorna 401
- [x] Documentada la estrategia elegida (stateless simple)

---

## Referencias

- `documentation/api-requests-flujo.md` — Logout opcional
