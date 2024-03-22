# **Backend-сервис для обмена статьями "Polonius"**
## **Описание**
***Polonius*** представляет собой полноценный backend-сервис для ведения блогов и публикации статей. Этот pet-проект призван продемонстрировать навыки работы с современными технологиями и архитектурными решениями. Основные особенности проекта:

![java:17](https://img.shields.io/badge/Java-17-red?style=for-the-badge&logo=java&labelColor=EEEBEB&color=C91B15
) ![Docker:4.28.0](https://img.shields.io/badge/Docker-4.28.0-blue?style=for-the-badge&logo=docker&labelColor=EEEBEB
) ![Maven:3.9.6](https://img.shields.io/badge/Maven-3.9.6-blue?style=for-the-badge&logo=maven&labelColor=EEEBEB
) ![Spring:3.0.12](https://img.shields.io/badge/Spring-3.0.12-27BD22?style=for-the-badge&logo=spring%20boot&labelColor=EEEBEB
) ![PostgreSql:16.2](https://img.shields.io/badge/PostgreSql-16.2-254BE5?style=for-the-badge&logo=postgresql&logoColor=blue&labelColor=EEEBEB
) ![AmazonS3:1.12.565](https://img.shields.io/badge/AmazonS3-1.12.565-red?style=for-the-badge&logo=amazon&logoColor=black&labelColor=E8E9E7&color=black
) ![Spring security](https://img.shields.io/badge/Spring%20Security-orange?style=for-the-badge&logo=spring%20security&logoColor=green&labelColor=EEEBEB&color=green
) ![Jwt:0.12.3](https://img.shields.io/badge/JWT-0.12.3-red?style=for-the-badge&labelColor=E8E9E7&color=red
) ![Junit:5.10.1](https://img.shields.io/badge/Junit-5.10.1-red?style=for-the-badge&logo=junit&logoColor=black&labelColor=E8E9E7&color=FC5C18
)

## **Установка**
Для начала работы с приложением blogapp-server пожалуйста, следуйте указаниям ниже:

**Важно!!! Для того чтобы приложение работало у вас должен быть установлен Docker, в противном случае приложение работать не будет.**

**Шаг 1**. Выполните авторизацию в Docker
1. Откройте командную строку или терминал на вашем компьютере.
2. Авторизуйтесь в своей учетной записи Docker Hub:
 ```shell
docker login
```
**Шаг 2**. Создайте docker-compose.yml в любой удобной для вас директори и впишите туда следующий код:
```yml
version: '3'
services:
  server:
    restart: always
    image: bosia/blogapp-server:1.1
    ports:
      - "8080:8080"
    environment:
      POSTGRES_HOST: postgreSqlDb
      POSTGRES_PORT: 5432
    depends_on:
      - postgreSqlDb

  postgreSqlDb:
    restart: on-failure
    container_name: postgreSqlDb
    image: postgres
    ports:
      - "5432:5432"
    environment:
      POSTGRES_DB: actualdb
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    volumes:
      - postgreSqlDbData:/var/lib/postgresql/
    
  pgadmin:
   restart: always
   container_name: pgadmin
   image: elestio/pgadmin
   environment:
     PGADMIN_DEFAULT_EMAIL: admin@email.com
     PGADMIN_DEFAULT_PASSWORD: admin
     PGADMIN_LISTEN_PORT: 5050
   ports:
     - "5050:5050"
   volumes:
     - ./servers.json:/pgadmin4/servers.json
      
volumes:
  postgreSqlDbData:
```
**Шаг 3**. С помощью терминала или командной строки перейдите в место где вы расположили docker-compose.yml и впишите следующию команду:

Для Linux
```
docker compose up -d
```

Для Windows
```
docker-compose up -d
```
**Шаг 4** Поключение базы данных к pg4.

1. Перейдите по адресу: ``http://localhost:5050/``

2. Введите email и пароль:

    + *Email*: admin@email.com

    + *Пароль*: admin
3. Подключение базы данных к pg4

+ Перейдите во вкладку "Add new Server".
+  После чего задайте любое имя во вкладке genеral. 
+  Далее перейдите во вкладку connection. Вам нужно вписать ip-адресс вашей базы данных, его можно получить следующим способом. Откройте консоль и введите команду:
```Shell
docker ps 
```
У вас должна появиться информация о ваших контейнерах. Например:
```shell
CONTAINER ID   IMAGE             COMMAND                  CREATED        STATUS         PORTS                                     NAMES
bf01e25b2905   blogapp-server    "java -jar ./app.jar"    31 hours ago   Up 5 minutes   0.0.0.0:8080->8080/tcp                    app_backend
77e71434ade2   elestio/pgadmin   "/entrypoint.sh"         31 hours ago   Up 4 hours     80/tcp, 443/tcp, 0.0.0.0:5050->5050/tcp   pgadmin
c4536e7ae490   postgres          "docker-entrypoint.s…"   31 hours ago   Up 5 minutes   0.0.0.0:5432->5432/tcp                    postgreSqlDb

```
+ Скопируйте CONTAINER ID у Image postgres.
+ Введите следующую команду
```Shell
docker inspect <Вставьте сюда Container Id>    
```
+ Во вкладке NetWorks найдите значение IPaddress, скопируйте его и вставьте в pg4
+ Введите в поле Username значение postgres
+ В поле Password также введите значение postgres 
+ Нажмите кнопку Save

**Установка завершена, приложение готово к работе**

## **Использование back-end сервиса**
Процесс работы с сервисом будет описан через инструкцию, отражающую каждый шаг общения с приложением.

1. **URL-адрес запроса:** Здесь будет указан конкретный адрес сервиса, посредством которого выполняются все обменные операции.
   
2. **Тело запроса JSON:** JSON-запрос, который необходимо отправить на указанный URL.

3. **Тип запроса:** Обозначение метода запроса (например, GET, POST, PUT, DELETE), определяющего операцию, которую нужно отправить.

4. **Пример ответа:** Описание структуры отклика от сервиса, включающее возможные значения и типы данных, которые можно ожидать в ответе.
___
### Регистрация пользователя

***URL-адрес запроса:*** ```http://localhost:8080/registration```

***Тело запроса JSON:***
```JSON
{
    "username":"<Имя пользователя>",
    "password":"<Пароль>"
}
```
***Тип запроса:*** POST

***Пример ответа:***
```JSON
{
    "id": <id созданого пользователя>,
    "username": "<имя созданого пользователя>"
}
```

___
### Аутентификация пользователя
***URL-адрес запроса:*** ```http://localhost:8080/auth```

***Тело запроса JSON:***
```JSON
{
    "username":"<Имя пользователя>",
    "password":"<Пароль>"
}
```

***Тип запроса:*** POST

***Пример ответа:***
```JSON
{
    "token": "<Ваш уникальный токен>"
}
```
Этот токен надо будет использовать при следующих запросах пользователя
___

### Сброс пароля

***URL-адрес запроса:*** ```http://localhost:8080/user/resetPassword```

***Тип запроса:*** POST

***Пример ответа:***
```JSON
{
    "oldPassword":123,
    "newPassword":321
}
```
___
### Получение информации о профиле другого пользователя
***URL-адрес запроса:*** ```http://localhost:8080/article/<имя нужного вам пользователя>```

***Тип запроса:*** GET

**Пример ответа:**
```JSON
{
    "username": "<Имя пользователя>",
    "profileDescription": null,
    "articles": [
        {
            "id": "<Id пользователя>",
            "nameArticle": "<Имя статьи>",
            "dateOfCreation": "<Дата создания>",
            "likes": <Количество лайков>
        }
    ]
}
```
___
### Получение информации о профиле текущего пользователя
***URL-адрес запроса:*** ```http://localhost:8080/user/info```

***Тип запроса:*** GET

***Пример ответа:***
```JSON
{
    "username": "<Имя пользователя>",
    "profileDescription": null,
    "articles": [
        {
            "id": "<Id пользователя>",
            "nameArticle": "<Имя статьи>",
            "dateOfCreation": "<Дата создания>",
            "likes": <Количество лайков>
        }
    ]
}
```
___
### Получение информации о профиле другого пользователя
***URL-адрес запроса:*** ```http://localhost:8080/article/<имя нужного вам пользователя>```

***Тип запроса:*** GET

**Пример ответа:**
```JSON
{
    "username": "<Имя пользователя>",
    "profileDescription": null,
    "articles": [
        {
            "id": "<Id пользователя>",
            "nameArticle": "<Имя статьи>",
            "dateOfCreation": "<Дата создания>",
            "likes": <Количество лайков>
        }
    ]
}
```
___
### Изменение информации профиля

___
### Создание новой статьи
***URL-адрес запроса:*** ```http://localhost:8080/article/newArticle```

***Тело запроса JSON:***
```JSON
{
    "articleName":"<Имя вашей статьи>",
    "articleContent":"<Контент вашей статьи>"
}
```
***Тип запроса:*** PUT

***Пример ответа:***
Статус 200 Ok 
___
### Получение статьи
***URL-адрес запроса:*** ```http://localhost:8080/article/<id статьи>```

***Тип запроса:*** GET

**Пример ответа:**
```JSON
{
    "nameArticle": <Имя статьи>,
    "text": "<Контент вашей статьи>",
    "likes": <Количество лайков>,
    "dateOfCreation": "<Дата создания>",
    "Author": "<Имя автора>"
}
```
___

### Изменение имени вашей статьи
***URL-адрес запроса:*** ```http://localhost:8080/<id статьи>```

***Тело запроса JSON:***
```JSON
{
    "newArticleName": "<Новое имя вашей статьи>", 
    "newArticleContent": null
}
```

***Тип запроса:*** Patch

***Пример ответа:***
Статус 200 Ok 
___
### Изменение контента вашей статьи
***URL-адрес запроса:*** ```http://localhost:8080/<id статьи>```

***Тело запроса JSON:***
```JSON
{
    "newArticleName": null, 
    "newArticleContent": "<Новый контент вашей статьи>"
}
```
***Тип запроса:*** Patch

***Пример ответа:***
Статус 200 Ok 
___
### Удаление вашей статьи
***URL-адрес запроса:*** ```http://localhost:8080/<id статьи>```

***Тип запроса:*** Delete

***Пример ответа:***
Статус 200 Ok 
___

### Реализация поиска
***URL-адрес запроса:*** ```http://localhost:8080/search```

***Тело запроса JSON:***
```JSON
{
    "searchQuery":"<Запрос>",
    "searchId": <Критерий поиска: статьи = 0, пользователи = 1>
}
```
***Тип запроса:*** POST

***Пример ответа:***

При поиске пользователя:
```JSON
[
  {
    "username": "<имя пользователя>"
  }
]
```

При поиске статей:
```JSON
[
  {
     "articleId": <Id статьи>,
     "articleName": <Название статьи>,
     "username": <Имя владельца статьи>,
     "dateOfCreation": <Дата создания статьи>
  }
]
```
