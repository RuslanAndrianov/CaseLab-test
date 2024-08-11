# Тестовое задание CaseLab Java

Этот проект представляет собой решение тестового задания на [CaseLab Java](https://edu.rosatom.ru/caselab/JS1/).

## Запуск сервиса

1. Скачайте файлы проекта в виде zip-архива и распакуйте его. Или просто склонируйте репозиторий командой `git clone`. 
2. Находясь в корне проекта, в консоли введите команду `docker-compose build`.
3. Чтобы запустить контейнер с сервисом, в консоли введите команду `docker-compose up`.

## Описание решения

В рамках данной задачи был разработан микросервис, который выполняет функцию хранилища для различных файлов и их атрибутов. Микросервис предоставляет HTTP API, который обрабатывает запросы и ответы в формате JSON, позволяя пользователям взаимодействовать с системой через стандартные RESTful методы.

Из классов были написаны 1 REST-контроллер, 1 репозиторий и 1 класс-сущность "Файл".

## Примеры запросов к API

Запросы удобно проделать с помощью Postman.
После запуска контейнера с сервисом откройте Postman и проделайте следующие запросы.

1. POST http://localhost:8080/api/files
    
   Тело запроса:

       {
           "title":"Test File 1",
           "creationDate":"2023-10-01T12:00:00Z",
           "description":"This is a test file 1.",
           "fileData":"base64EncodedData1"
       }

   Ответ: <id файла>

2. GET http://localhost:8080/api/files/<id файла из п.1>

   Ответ:

       {
           "id":"<id файла из п.1>", 
           "title":"Test File 1",
           "creationDate":"2023-10-01T12:00:00Z",
           "description":"This is a test file 1.",
           "fileData":"base64EncodedData1"
       }

3. GET http://localhost:8080/api/files?page=&size=&sort=

   Ответ:

       [
           {
              "id":"<id файла из п.1>", 
              "title":"Test File 1",
              "creationDate":"2023-10-01T12:00:00Z",
              "description":"This is a test file 1.",
              "fileData":"base64EncodedData1"
           }
       ]

## Стек

Проект написан на `Java 21` и `Spring Boot 3`.

Для работы требуется БД `PostgreSQL`.