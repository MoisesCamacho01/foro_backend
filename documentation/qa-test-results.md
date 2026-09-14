# Reporte QA — ForumHub Backend

**Fecha:** 2026-09-13  
**Comando:** `./mvnw test`  
**Resultado global:** ✅ PASS (22/22)

---

## Resumen por endpoint

| Endpoint | Tests | Resultado |
|----------|-------|-----------|
| `POST /auth/login` | 3 | ✅ PASS |
| `POST /auth/logout` | 2 | ✅ PASS |
| `GET /questions` | 2 | ✅ PASS |
| `POST /questions` | 2 | ✅ PASS |
| `POST /comments` | 2 | ✅ PASS |
| `POST /comments/{id}/vote` | 4 | ✅ PASS |
| Persistencia JSON | 1 | ✅ PASS |
| Concurrencia votos | 1 | ✅ PASS |
| Contexto Spring | 1 | ✅ PASS |
| ForumService (unit) | 3 | ✅ PASS |
| Login público | 1 | ✅ PASS |

---

## Detalle de pruebas

### Auth (`AuthControllerTest`)

| Test | HTTP | Resultado |
|------|------|-----------|
| `loginWithValidAliasReturnsToken` | 200 | ✅ |
| `loginWithEmptyAliasReturnsBadRequest` | 400 | ✅ |
| `loginReusesExistingUser` | 200 (Get or Create) | ✅ |
| `logoutWithValidTokenReturnsOk` | 200 | ✅ |
| `logoutWithoutTokenReturnsUnauthorized` | 401 | ✅ |

### Questions (`QuestionControllerTest`)

| Test | HTTP | Resultado |
|------|------|-----------|
| `getQuestionsWithoutTokenReturnsUnauthorized` | 401 | ✅ |
| `getQuestionsWithValidTokenReturnsSeedTree` | 200 | ✅ |
| `createQuestionReturnsCreatedResponse` | 201 | ✅ |
| `createQuestionWithEmptyBodyReturnsBadRequest` | 400 | ✅ |
| `loginRemainsPublicWithoutToken` | 200 | ✅ |

### Comments (`CommentControllerTest`)

| Test | HTTP | Resultado |
|------|------|-----------|
| `createCommentWithValidParentReturnsCreated` | 201 | ✅ |
| `createCommentWithMissingParentReturnsNotFound` | 404 | ✅ |
| `voteLikeIncrementsLikes` | 200 | ✅ |
| `voteLikeToggleOffDecrementsLikes` | 200 | ✅ |
| `voteDislikeAfterLikeIsMutuallyExclusive` | 200 | ✅ |
| `voteOnMissingCommentReturnsNotFound` | 404 | ✅ |

### Integración

| Test | Resultado |
|------|-----------|
| `ForumPersistenceTest.createQuestionPersistsToCommentsJson` | ✅ |
| `VoteConcurrencyTest.concurrentLikesKeepConsistentCounts` | ✅ |
| `ForumServiceTest` (3 tests) | ✅ |
| `ForoBackendApplicationTests.contextLoads` | ✅ |

---

## Contrato JSON validado

- Respuestas envueltas en `ApiResponse<T>` con `result`, `message`, `data`, `timestamp`
- `LoginResponse`: `{ alias, token }`
- `LogoutResponse`: `{ message }`
- `ForumCommentResponse`: árbol con `children[]`, `votes`, `author`
- `VoteStateResponse`: `{ liked, disliked, likes, dislikes }`

---

## cURL de verificación manual

```bash
# Login
curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"alias":"moises_dev"}'

# Logout
curl -s -X POST http://localhost:8080/auth/logout \
  -H "Authorization: Bearer <token>"

# GET questions
curl -s http://localhost:8080/questions \
  -H "Authorization: Bearer <token>"

# POST question
curl -s -X POST http://localhost:8080/questions \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"body":"Nueva pregunta"}'

# POST comment
curl -s -X POST http://localhost:8080/comments \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"parentId":"q1","body":"Réplica"}'

# POST vote
curl -s -X POST http://localhost:8080/comments/q1/vote \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"type":"like"}'
```
