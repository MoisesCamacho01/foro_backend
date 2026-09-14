# Tarea 14 — Endpoint POST /comments/:id/vote

**Dificultad:** ⭐⭐⭐⭐ (media-alta)  
**Dependencias:** 07, 04, 10  
**Estimación:** 2 h  
**Estado:** ✅ Completada

---

## Objetivo

Implementar votos like/dislike con toggle mutuamente exclusivo, igual que el mock del frontend.

---

## Contrato

| Campo | Valor |
|-------|-------|
| **Método** | `POST` |
| **Ruta** | `/comments/{id}/vote` |
| **Auth** | Bearer token |

### Request

```json
{ "type": "like" }
```

o

```json
{ "type": "dislike" }
```

### Response 200

```json
{
  "liked": true,
  "disliked": false,
  "likes": 16,
  "dislikes": 1
}
```

> Retorna solo `VoteStateResponse`, no el comentario completo.

### Errores

| Código | Escenario |
|--------|-----------|
| 400 | type inválido |
| 401 | Sin auth |
| 404 | comment id no encontrado |
| 500 | Error persistencia |

---

## Reglas de toggle (replicar mock frontend)

| Acción | Efecto |
|--------|--------|
| Like sin voto previo | `liked=true`, `likes++` |
| Like con dislike previo | quita dislike (`dislikes--`), activa like (`likes++`) |
| Like ya activo | toggle off (`liked=false`, `likes--`) |
| Dislike sin voto previo | `disliked=true`, `dislikes++` |
| Dislike con like previo | quita like (`likes--`), activa dislike (`dislikes++`) |
| Dislike ya activo | toggle off (`disliked=false`, `dislikes--`) |

---

## Implementación

### `VoteService` / en `ForumServiceImpl`

1. Lock en repositorio (operación atómica)
2. Leer voto actual del usuario para ese comentario
3. Aplicar lógica toggle
4. Actualizar `CommentModel.likesCount` / `dislikesCount`
5. Persistir voto y comentario
6. Retornar `VoteStateResponse` con estado del usuario + totales

---

## Criterios de aceptación

- [x] Toggle like/dislike funciona según tabla de reglas
- [x] Like y dislike nunca activos simultáneamente para el mismo usuario
- [x] Contadores nunca negativos
- [x] Operaciones concurrentes no corrompen contadores (lock)
- [x] ID inexistente → 404

---

## Referencias

- `documentation/api-requests-flujo.md` — Foro votos
