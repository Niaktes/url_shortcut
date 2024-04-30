## url_shortcut

REST API использующийся для сокращения ссылок и ведения статистики по ним.

### Используемые технологии:
* Java 17
* Maven 3.8
* Spring 2.7.17
* * Web 
* * Data Jpa 
* * Security
* * Validation
* * Test
* PostgreSQL 14
* Liquibase 3.10.3
* Lombok 1.18.30
* Checkstyle


### Возможности:

1. Регистрация Вашего сайта (пользователя) происходит путём POST запроса на адрес /registration с 
   JSON, содержащим имя Вашего сайта. В ответ Вы получаете JSON, с подтверждением/отрицанием регистрации, 
   а также с уникальным логином и паролем (в случае удачной регистрации), необходимыми для авторизации:

_Request_
```
POST /registration
{
    "site": "example.ru"
}
```
_Response_
```
{
    "registration": true, "login": "exampleLogin", "password": "examplePassword"
}
```

2. Авторизация с использованием полученных ранее логина и пароля и получение JWT токена для дальнейшей 
   работы. Данное действие происходит также POST запросом на адрес /login. В ответ токен возвращается в 
   header ответа:

_Request_
```
POST /login
{
    "login": "exampleLogin", "password": "examplePassword"
}
```

3. После этого доступна дальнейшая работа, такая как регистрация URL страниц Вашего сайта и получение 
   коротких кодов на эти ссылки, как в примере ниже:
   
_Request_
```
POST /convert
{
    "url": "http://example.ru/someUrl.ru"
}
```
_Response_
```
{
    "code": "someCharacterSet"
}
```

4. Данный код можно использовать для перехода по закодированной ссылке. Причём перенаправление на страницу,
   скрытую под этим кодом, происходит автоматически:
   
_Request_
```
GET /reddirect/someCharacterSet
```

5. Последней возможностью является получение статистики переходов по закодированным Вами ссылкам. Каждый 
   раз, когда кто-либо переходит по ссылке с использованием кода, как в примере выше, это учитывается в 
   статистикe, ведущейся для каждого отдельного сайта (пользователя):

_Request_
```
GET /statistic
```
_Response_
```
{
    {"url": "http://example.ru/someUrl.ru", "total": 31}
    {"url": "http://example.ru/someOtherUrl.ru", "total": 322}
}
```

На этом возможности данного приложения заканчиваются.
#### Искренне благодарю за внимание к моему проекту! Всего Вам хорошего!

### Контакты для связи: 
> <a href="https://github.com/Niaktes/">Захаренко Сергей</a> <br>
> Телефон: +7 995 299 07 34 <br>
<a href="https://t.me/SZakharenko"><img src="https://seeklogo.com/images/T/telegram-logo-AD3D08A014-seeklogo.com.png" alt="Telegram" height="30"></a>
<a href="https://wa.me/89265900734"><img src="https://seeklogo.com/images/W/whatsapp-icon-logo-6E793ACECD-seeklogo.com.png" alt="Whatsapp" height="30"></a>
<a href="mailto:Sergei.Zakharenko.it@gmail.com"><img src="https://seeklogo.com/images/G/gmail-logo-0B5D69FF48-seeklogo.com.png" alt="Mail" height="30"></a>
