<h2>Учебный проект meOwO</h2>

<b>Цель</b>
</br>
Работа со стеком:
- front: VueJS
- back: Java Spring Boot
- DB: MongoDB

<b>Задание:</b>
   1) Создать БД
   2) Создать UI для работы с БД, с демонстрацией CRUD операций
   3) Создать минимум 3х пользователей с разными уровнями привилегий
   4) Создать продуманные индексы в коллекциях для наиболее часто используемых в поиске ключей документов
   5) Создать минимум 3 отчетные формы с использованием функций агрегации

<br>
<h2>Для запуска</h2>
<b>Добавить в application.properties следующее:</b>

spring.data.mongodb.database=your_db_name 
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017

suvorov.app.jwtSecret={your_key}
suvorov.app.jwtExpirationMs=86400000
