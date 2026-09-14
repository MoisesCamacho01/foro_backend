# Tarea 13 — Endpoint POST /comments

**Dificultad:** ⭐⭐⭐ (media)  
**Dependencias:** 07, 10  
**Estimación:** 45 min  
**Estado:** ✅ Completada

---

## Objetivo

Publicar una réplica (respuesta directa o sub-réplica) a un nodo existente.

---

## Contrato

| Campo | Valor |
|-------|-------|
| **Método** | `POST` |
| **Ruta** | `/comments` |
| **Auth** | Bearer token |

### Request

```json
{
  "parentId": "r1",
  "body": "Totalmente de acuerdo, parentId es clave para el árbol."
}
```

### Response 201

Objeto `ForumCommentResponse`:

- `parentId` = ID del padre
- `level` = parent.level + 1
- Badge según reglas (tarea 10)
- `origin: "user"`
- `children: []`

### Errores

| Código | Escenario |
|--------|-----------|
| 400 | body o parentId vacío |
| 401 | Sin auth |
| 404 | parentId no encontrado |
| 500 | Error persistencia |

> A diferencia del mock frontend (fallback silencioso), el backend **debe** responder 404.

---

## Implementación

```java
@PostMapping("/comments")
public ResponseEntity<?> createComment(
    @Valid @RequestBody CreateCommentRequest request,
    /* alias from JWT */
)
```

---

## Criterios de aceptación

- [x] Réplica insertada con `parentId` correcto
- [x] level y badge calculados según padre
- [x] parentId inexistente → 404
- [x] Réplica visible en árbol al hacer GET /questions

---

## Referencias

- `documentation/api-requests-flujo.md` — Publicar réplica
