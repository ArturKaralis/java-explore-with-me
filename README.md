# java-explore-with-me

Ссылка на пул-реквест 3 части работы: https://github.com/ArturKaralis/java-explore-with-me/pull/11

Template repository for ExploreWithMe project.
В рамках проекта разработано REST API для приложения ExploreWithMe. 
Проект предоставляет возможность делиться информацией об интересных событиях и помогать найти компанию для участия в них.
Технологический стек: Java, Spring Boot, Maven, REST, Lombok, SQL, JDBC, JPA, Docker Compose, Postman.

**Список эндпоинтов для фичи (рейтинги):**
1. Поставить лайк/дизлайк:
   PATCH http://localhost:8080/users/<userId>/events/<eventId>/like
   PATCH http://localhost:8080/users/<userId>/events/<eventId>/dislike
2. Получить лайк/дизлайк по id:
   GET http://localhost:8080/events/<id>
3. Удалить лайк/дизлайк:
   DELETE http://localhost:8080/users/<userId>/events/<eventId>/like
   DELETE http://localhost:8080/users/<userId>/events/<eventId>/dislike
4. Получение списка пользователей c рейтингом:
   GET http://localhost:8080/admin/users?ids=<eventOwnerUid>
