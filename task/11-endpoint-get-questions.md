# Tarea 11 — Endpoint GET /questions

**Dificultad:** ⭐⭐⭐ (media)  
**Dependencias:** 07, 09, 10  
**Estimación:** 45 min  
**Estado:** ✅ Completada

---

## Objetivo

Exponer la carga inicial del foro: árbol completo de preguntas con réplicas anidadas.

---

## Contrato

| Campo | Valor |
|-------|-------|
| **Método** | `GET` |
| **Ruta** | `/questions` |
| **Auth** | `Authorization: Bearer <token>` |

### Response 200

Array de `ForumCommentResponse` (preguntas raíz con `children[]` anidados).

Ver ejemplo completo en `documentation/api-requests-flujo.md` líneas 207–247.

### Errores

| Código | Escenario |
|--------|-----------|
| 401 | Sin token o token inválido |
| 500 | Error de persistencia |

---

## Implementación

### `QuestionController` (o `ForumController`)

```java
@GetMapping("/questions")
public ResponseEntity<?> getQuestions(/* alias from JWT */)
```

- Extraer alias del contexto JWT
- Delegar a `forumService.getAllQuestions(alias)`
- Retornar lista (directa o envuelta en `ApiResponse` según decisión tarea 02)

---

## Criterios de aceptación

- [x] Retorna árbol seed `q1` con réplicas anidadas
- [x] Estructura JSON compatible con `ForumComment[]` del frontend
- [x] `votes.liked` / `votes.disliked` reflejan estado del usuario autenticado
- [ ] cURL de prueba documentado en tarea 16

---

## Referencias

- `documentation/api-requests-flujo.md` — Foro carga inicial
