# Tarea 16 — Pruebas QA

**Dificultad:** ⭐⭐⭐⭐ (media-alta)  
**Dependencias:** 06–15  
**Estimación:** 3 h  
**Estado:** ✅ Completada

---

## Objetivo

Validar el contrato JSON y códigos HTTP de todos los endpoints con JUnit 5 + MockMvc (y opcionalmente REST Assured).

---

## Suite de pruebas

### Auth

| Test | Esperado |
|------|----------|
| Login alias válido | 200, body con alias + token |
| Login alias vacío | 400 |
| Logout con token | 200 |
| Logout sin token | 401 |

### Questions

| Test | Esperado |
|------|----------|
| GET /questions sin token | 401 |
| GET /questions con token | 200, array con seed q1 y children |
| POST /questions body válido | 201, ForumCommentResponse |
| POST /questions body vacío | 400 |

### Comments

| Test | Esperado |
|------|----------|
| POST /comments parentId válido | 201, level/badge correctos |
| POST /comments parentId inválido | 404 |

### Votes

| Test | Esperado |
|------|----------|
| Like inicial | liked=true, likes incrementado |
| Like toggle off | liked=false, likes decrementado |
| Like con dislike previo | mutuamente exclusivo |
| Dislike — mismas reglas | idem |
| Vote en id inexistente | 404 |

### Persistencia

| Test | Esperado |
|------|----------|
| POST question → leer JSON | registro presente en archivo |
| Voto concurrente simulado | contadores consistentes |

---

## Configuración de pruebas

- Usar `@SpringBootTest` + `@AutoConfigureMockMvc`
- Directorio temporal para JSON (`@TempDir` o `application-test.properties`)
- Limpiar datos entre tests

---

## Entregable

Reporte en `documentation/qa-test-results.md` con:
- Fecha de ejecución
- Comando usado (`mvn test`)
- Resultado por endpoint (PASS/FAIL)
- Ejemplos cURL verificados manualmente

---

## Criterios de aceptación

- [x] 100% tests PASS (22/22)
- [x] Estructura JSON validada contra DTOs
- [x] Códigos HTTP exactos según especificación
- [x] Reporte generado en `/documentation`

---

## Referencias

- `.cursor/for_backend.mdc` — Reglas QA
