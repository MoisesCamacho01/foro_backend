# Tarea 09 — Seed data inicial

**Dificultad:** ⭐⭐ (media-baja)  
**Dependencias:** 03, 04  
**Estimación:** 45 min  
**Estado:** ✅ Completada

---

## Objetivo

Poblar `data/comments.json` con el hilo canónico que usa el mock del frontend, para que `GET /questions` devuelva contenido desde el primer arranque.

---

## Datos seed (IDs fijos para compatibilidad con frontend)

| ID | parentId | Autor | level | origin |
|----|----------|-------|-------|--------|
| `q1` | null | Carlos Rodríguez (@carlos_dev) | 0 | seed |
| `r1` | q1 | Mariana López | 1 | seed |
| `r1_1` | r1 | *(según mock frontend)* | 2 | seed |
| `r1_1_1` | r1_1 | *(según mock frontend)* | 3 | seed |

> Revisar el seed exacto en el frontend (`forum.service.ts`) para copiar textos, contadores de votos y fechas relativas.

---

## Implementación

### Opción A — Archivo JSON estático

- `src/main/resources/seed/comments.json`
- `@PostConstruct` en repositorio o `DataInitializer`: si `data/comments.json` está vacío, copiar seed

### Opción B — `ApplicationRunner`

- Clase `@Component` que inserta seed al arrancar si no hay datos

---

## Usuarios seed

Crear entradas en `data/users.json` para autores del seed (carlos_dev, mariana, etc.) o resolver autores solo en mapper sin persistir usuarios seed.

---

## Criterios de aceptación

- [x] Al arrancar app limpia, `GET /questions` devuelve árbol `q1 → r1 → r1_1 → r1_1_1`
- [x] IDs coinciden con los del frontend mock
- [x] Contadores de votos iniciales correctos (p. ej. q1: likes=15, dislikes=1)
- [x] Seed no se duplica en reinicios sucesivos

---

## Referencias

- `documentation/api-requests-flujo.md` — Ejemplo GET /questions
- Frontend: `src/app/core/services/forum.service.ts` (seed canónico)
