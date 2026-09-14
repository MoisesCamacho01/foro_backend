# Tarea 00 — Decisiones de arquitectura

**Estado:** ✅ Cerrada  
**Fecha:** 2026-09-13

---

## Decisiones adoptadas

| Tema | Decisión | Implicación |
|------|----------|-------------|
| **Contrato API** | Mantener `ApiResponse<T>` | Todas las respuestas HTTP envuelven el DTO en `{ result, message, data, timestamp }`. El frontend deberá leer `response.data`. |
| **Login** | Registro automático (Get or Create) | `POST /auth/login` busca el alias; si no existe lo crea; si existe reutiliza la sesión. **No** se retorna 409. |
| **Logout** | Stateless simple | `POST /auth/logout` confirma al cliente; el token se descarta en `localStorage`. Sin blacklist en servidor. |

---

## Normalización de alias

- `trim()` + `toLowerCase()` al persistir y buscar
- Evita duplicados por mayúsculas/espacios

---

## Persistencia de votos

- Archivo separado: `data/votes.json`
- Modelo `VoteModel` con `commentId`, `userAlias`, `type`

---

## Referencias

- `documentation/decisiones-arquitectura.md` — copia extendida
- `task/README.md` — plan de tareas
