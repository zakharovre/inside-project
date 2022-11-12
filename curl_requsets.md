1. Авторизация
   curl -X POST localhost:8081/api/login -H 'Content-Type: application/json' -d '{"username":"admin", "password":"admin"}'
   
2. Добавление сообщения, вместо token подставить полученный из первого запроса токен
   curl -X POST localhost:8081/api/message -H 'Content-Type: application/json' -H 'Authorization: Bearer_token' -d '{"username":"username", "message":"message"}'
   
3. Получение последних сообщений, вместо token подставить полученный из первого запроса токен, вместо N число сообщений
   curl -X POST localhost:8081/api/message -H 'Content-Type: application/json' -H 'Authorization: Bearer_token' -d '{"username":"username", "message":"history N"}'