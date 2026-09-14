# Tarea 03 — Modelos de dominio JSON

**Dificultad:** ⭐⭐ (media-baja)  
**Dependencias:** 01  
**Estimación:** 45 min

---

## Objetivo

Definir los POJOs que mapean la estructura interna de los archivos JSON. **No se exponen al cliente** — solo los usa la capa repositorio/servicio.

---

## Modelos a crear

### `UserModel`

Persistencia de usuarios registrados por alias.

```java
// Campos sugeridos
String id;          // UUID v4
String alias;       // único, normalizado (trim, lowercase?)
LocalDateTime createdAt;
```

### `CommentModel`

Lista plana de preguntas y réplicas (mismo modelo, diferenciados por `parentId`).

```java
String id;              // UUID v4
String parentId;        // null = pregunta raíz
String authorAlias;     // referencia al usuario
String body;
LocalDateTime createdAt;
String origin;          // "seed" | "user"
int likesCount;
int dislikesCount;
```

### `VoteModel` (opcional — puede ir embebido)

Voto por usuario y comentario.

```java
String id;
String commentId;
String userAlias;
String type;            // "like" | "dislike"
```

> Alternativa: almacenar votos en `votes.json` separado o como mapa dentro de cada comentario. Elegir una estrategia y documentarla.

---

## Reglas

- Anotaciones Lombok: `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`
- Jackson: `@JsonInclude(NON_NULL)` donde aplique
- IDs generados con `UUID.randomUUID().toString()` antes de persistir

---

## Criterios de aceptación

- [ ] Modelos compilan y deserializan desde JSON de ejemplo
- [ ] `parentId` nullable para preguntas raíz
- [ ] Contadores `likesCount` / `dislikesCount` presentes en `CommentModel`

---

## Referencias

- `documentation/api-requests-flujo.md` — Modelos ForumComment, VoteState
- `.cursor/for_backend.mdc` — Modelos
