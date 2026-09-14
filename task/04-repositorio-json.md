# Tarea 04 — Repositorio JSON

**Dificultad:** ⭐⭐ (media-baja)  
**Dependencias:** 02, 03  
**Estimación:** 1.5 h

---

## Objetivo

Implementar la capa de persistencia en archivos JSON con operaciones thread-safe.

---

## Repositorios

### `UserRepository` / `JsonUserRepository`

| Método | Descripción |
|--------|-------------|
| `findByAlias(String alias)` | Buscar usuario |
| `existsByAlias(String alias)` | Verificar duplicado |
| `save(UserModel user)` | Crear usuario |

Archivo: `data/users.json` → `List<UserModel>`

### `CommentRepository` / `JsonCommentRepository`

| Método | Descripción |
|--------|-------------|
| `findAll()` | Todos los comentarios/preguntas |
| `findById(String id)` | Por ID |
| `save(CommentModel comment)` | Crear |
| `update(CommentModel comment)` | Actualizar contadores de votos |

Archivo: `data/comments.json` → `List<CommentModel>`

### `VoteRepository` / `JsonVoteRepository` (si aplica)

| Método | Descripción |
|--------|-------------|
| `findByCommentIdAndUserAlias(...)` | Voto actual del usuario |
| `save(VoteModel vote)` | Crear/actualizar voto |
| `delete(VoteModel vote)` | Quitar voto (toggle off) |

Archivo: `data/votes.json`

---

## Implementación

- `@Repository` + interfaz + `Json*Repository` impl
- `ObjectMapper` inyectado
- `ReentrantReadWriteLock` por archivo
- `IOException` → `PersistenceException`
- Inicializar archivo vacío `[]` si no existe al arrancar (`@PostConstruct` o en constructor)

---

## Criterios de aceptación

- [ ] Lectura/escritura atómica (read → modify → write bajo lock)
- [ ] Archivos se crean automáticamente si no existen
- [ ] Operaciones concurrentes no corrompen el JSON
- [ ] Repositorios no conocen DTOs ni controladores

---

## Referencias

- `.cursor/for_backend.mdc` — Repositorio (DAO)
