# Tarea 08 — DTOs del foro

**Dificultad:** ⭐⭐ (media-baja)  
**Dependencias:** 02  
**Estimación:** 45 min  
**Estado:** ✅ Completada

---

## Objetivo

Definir los records de request/response que replica el contrato TypeScript del frontend.

---

## DTOs de respuesta

### `ForumUserResponse`

```java
public record ForumUserResponse(
    String displayName,
    String handle,       // opcional
    String initials,
    String tone          // "author"|"level1"|"level2"|"level3"|"current"
) {}
```

### `VoteStateResponse`

```java
public record VoteStateResponse(
    boolean liked,
    boolean disliked,
    int likes,
    int dislikes
) {}
```

### `ForumCommentResponse`

```java
public record ForumCommentResponse(
    String id,
    String parentId,
    ForumUserResponse author,
    String body,
    String createdLabel,
    String relativeLabel,
    int level,
    String badge,
    String origin,       // "seed" | "user"
    VoteStateResponse votes,
    List<ForumCommentResponse> children
) {}
```

> El frontend usa `children[]`; las reglas del backend mencionan `replies`. **Usar `children`** para compatibilidad directa con el frontend.

---

## DTOs de request

### `CreateQuestionRequest`

```java
public record CreateQuestionRequest(
    @NotBlank String body
) {}
```

### `CreateCommentRequest`

```java
public record CreateCommentRequest(
    @NotBlank String parentId,
    @NotBlank String body
) {}
```

### `VoteRequest`

```java
public record VoteRequest(
    @NotBlank String type   // "like" | "dislike"
) {
    public VoteRequest {
        if (type != null && !type.equals("like") && !type.equals("dislike")) {
            throw new IllegalArgumentException("type debe ser 'like' o 'dislike'");
        }
    }
}
```

---

## Helpers (servicio o mapper)

- `buildForumUser(String alias, String tone)` → calcula `displayName`, `handle`, `initials`
- `formatCreatedLabel(LocalDateTime)` / `formatRelativeLabel(LocalDateTime)` → etiquetas legibles

---

## Criterios de aceptación

- [x] DTOs serializan JSON idéntico al ejemplo de `api-requests-flujo.md`
- [x] Validaciones en records de request
- [x] Campo del árbol se llama `children`, no `replies`

---

## Referencias

- `documentation/api-requests-flujo.md` — Modelos de datos
