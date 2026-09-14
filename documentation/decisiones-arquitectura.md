# Decisiones de arquitectura — ForumHub Backend

**Fecha:** 2026-09-13  
**Alcance:** Tareas 00–05

---

## 1. Contrato API — `ApiResponse<T>`

Todas las respuestas REST usan el envoltorio definido en `for_backend.mdc`:

```json
{
  "result": "SUCCESS",
  "message": "Descripción",
  "data": { },
  "timestamp": "2026-09-13T18:30:00"
}
```

El frontend Angular deberá adaptar su capa HTTP para extraer `data` del body.

---

## 2. Login — Get or Create

Flujo de `POST /auth/login`:

1. Normalizar alias (`trim` + `toLowerCase`)
2. Buscar en `users.json`
3. Si no existe → crear `UserModel` con UUID
4. Si existe → reutilizar
5. Generar JWT y retornar `LoginResponse` dentro de `ApiResponse`

**No aplica** código HTTP 409 por alias duplicado.

---

## 3. Logout — Stateless simple

- El servidor no mantiene blacklist de tokens
- `POST /auth/logout` retorna `ApiResponse` con `LogoutResponse { message: "Sesión cerrada" }`
- El cliente elimina `forumhub_user` y `forumhub_token` de `localStorage`
- El token sigue siendo válido técnicamente hasta expirar; la sesión se cierra en el cliente

---

## 4. Persistencia JSON

| Archivo | Contenido |
|---------|-----------|
| `data/users.json` | `List<UserModel>` |
| `data/comments.json` | `List<CommentModel>` |
| `data/votes.json` | `List<VoteModel>` |

Operaciones thread-safe con `ReentrantReadWriteLock`.
