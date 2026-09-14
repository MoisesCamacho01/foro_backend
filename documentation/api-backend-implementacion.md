# API Backend — Implementación ForumHub

**Fecha:** 2026-09-13  
**Referencia frontend:** `documentation/api-requests-flujo.md`

---

## Mapping endpoint → capas

| Método | Ruta | Controller | Service | Repositorio |
|--------|------|------------|---------|-------------|
| `POST` | `/auth/login` | `AuthController` | `AuthServiceImpl` | `UserRepository` |
| `POST` | `/auth/logout` | `AuthController` | `AuthServiceImpl` | — (stateless) |
| `GET` | `/questions` | `QuestionController` | `ForumServiceImpl` | `CommentRepository`, `VoteRepository` |
| `POST` | `/questions` | `QuestionController` | `ForumServiceImpl` | `CommentRepository` |
| `POST` | `/comments` | `CommentController` | `ForumServiceImpl` | `CommentRepository` |
| `POST` | `/comments/{id}/vote` | `CommentController` | `ForumServiceImpl` | `CommentRepository`, `VoteRepository` |

---

## Contrato de respuesta

Todas las respuestas usan `ApiResponse<T>`:

```json
{
  "result": "SUCCESS",
  "message": "...",
  "data": { },
  "timestamp": "2026-09-13T18:58:00"
}
```

> El frontend debe leer `response.data` para obtener el DTO.

---

## Autenticación

- **Login:** Get or Create por alias (sin 409)
- **JWT:** Header `Authorization: Bearer <token>`
- **Logout:** Stateless — confirma al cliente; token descartado en `localStorage`
- **Interceptor:** `JwtAuthInterceptor` protege rutas del foro

### Rutas públicas

- `POST /auth/login`

### Rutas protegidas

- `/questions`, `/comments/**`, `/auth/logout`

---

## Persistencia JSON

| Archivo | Modelo | Contenido |
|---------|--------|-----------|
| `data/users.json` | `UserModel` | Usuarios registrados |
| `data/comments.json` | `CommentModel` | Preguntas y réplicas (lista plana) |
| `data/votes.json` | `VoteModel` | Votos por usuario/comentario |

Seed inicial: `src/main/resources/seed/comments.json` → cargado por `DataInitializer`.

---

## Árbol de comentarios

El servicio transforma la lista plana (`parentId`) en árbol anidado (`children[]`) mediante `TreeBuilder` + `CommentMapper`.

---

## Decisiones de diseño

Ver: `documentation/decisiones-arquitectura.md`
