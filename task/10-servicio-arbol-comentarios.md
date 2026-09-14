# Tarea 10 — Servicio árbol de comentarios

**Dificultad:** ⭐⭐⭐ (media)  
**Dependencias:** 04, 08  
**Estimación:** 2 h  
**Estado:** ✅ Completada

---

## Objetivo

Implementar la lógica de negocio del foro: transformar lista plana → árbol anidado, crear preguntas/réplicas y enriquecer con datos del usuario autenticado.

---

## `ForumService` / `ForumServiceImpl`

### `List<ForumCommentResponse> getAllQuestions(String currentUserAlias)`

1. Leer todos los `CommentModel` del repositorio
2. Filtrar raíces (`parentId == null`)
3. Construir árbol recursivo con `children[]`
4. Para cada nodo:
   - Mapear autor → `ForumUserResponse` (tone según level)
   - Calcular `createdLabel` y `relativeLabel`
   - Resolver `VoteStateResponse` del usuario actual (liked/disliked + totales)
   - Asignar `badge` según reglas
5. Orden: preguntas raíz — **user-created prepended** (más recientes primero), seed después (o por `createdAt` desc)

### `ForumCommentResponse createQuestion(String alias, CreateQuestionRequest req)`

1. Crear `CommentModel` con `parentId=null`, `origin=user`, contadores en 0
2. Persistir
3. Retornar DTO con `badge: "Nueva pregunta"`, `tone: current` para autor

### `ForumCommentResponse createReply(String alias, CreateCommentRequest req)`

1. Buscar padre por `parentId` → 404 si no existe
2. `level = parent.level + 1` (calcular level del padre desde árbol o almacenar en modelo)
3. Badge:
   - `"Respuesta directa"` si padre es raíz (`parent.parentId == null`)
   - `"Sub-réplica escalonada"` en otro caso
4. Persistir y retornar DTO

---

## Mapper / utilidades

- `CommentMapper` — Model → Response
- `TreeBuilder` — lista plana → bosque de DTOs
- `UserPresentationHelper` — alias → initials, handle, displayName

---

## Criterios de aceptación

- [x] Lista plana con `parentId` se convierte en árbol con `children[]`
- [x] Nodos huérfanos (parentId inválido) no rompen el árbol — omitir o loguear
- [x] Level calculado correctamente en réplicas
- [x] Servicio retorna solo DTOs, nunca Models

---

## Referencias

- `.cursor/for_backend.mdc` — Construcción del árbol
- `documentation/api-requests-flujo.md` — Reglas de badge y level
