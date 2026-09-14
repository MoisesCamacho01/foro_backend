# Tarea 12 — Endpoint POST /questions

**Dificultad:** ⭐⭐⭐ (media)  
**Dependencias:** 07, 10  
**Estimación:** 45 min  
**Estado:** ✅ Completada

---

## Objetivo

Permitir publicar una nueva pregunta raíz. El autor se infiere del JWT.

---

## Contrato

| Campo | Valor |
|-------|-------|
| **Método** | `POST` |
| **Ruta** | `/questions` |
| **Auth** | Bearer token |

### Request

```json
{ "body": "¿Cómo estructurar el estado del árbol de comentarios?" }
```

### Response 201

Objeto `ForumCommentResponse` completo:

- `parentId: null`
- `level: 0`
- `badge: "Nueva pregunta"`
- `origin: "user"`
- `author.tone: "current"`
- `votes`: todos en 0, liked/disliked false
- `children: []`
- `createdLabel` / `relativeLabel`: `"recién publicado"`

### Errores

| Código | Escenario |
|--------|-----------|
| 400 | body vacío |
| 401 | Sin auth |
| 500 | Error persistencia |

---

## Implementación

```java
@PostMapping("/questions")
public ResponseEntity<?> createQuestion(
    @Valid @RequestBody CreateQuestionRequest request,
    /* alias from JWT */
)
```

- Delegar a `forumService.createQuestion(alias, request)`
- HTTP 201 Created

---

## Criterios de aceptación

- [x] Pregunta persistida en `comments.json` con UUID
- [x] Response coincide con contrato frontend
- [x] Nueva pregunta aparece en subsiguiente `GET /questions` (al inicio del listado)
- [x] body vacío → 400

---

## Referencias

- `documentation/api-requests-flujo.md` — Publicar pregunta
