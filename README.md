# SocialNetworkRESTAPI
## Техническое задание:

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/53e5bad6-bd9e-4d4d-93c7-b020ac98a3a6)


### Для реализации данного ТЗ было использовано следующее:
    
    1)Spring Boot Starter Data JPA
    
    2)Spring Boot Starter Security
    
    3)Spring Boot Starter Web
    
    4)PostgreSQL (для работы с базой данных PostgreSQL)
    
    5)Lombok (дополнительная библиотека для упрощения разработки)
    
    6)Spring Boot Starter Test (зависимость для тестирования)
    
    7)Spring Security Test (зависимость для тестирования Spring Security)
    
    8)Spring Boot Starter Validation (зависимость для валидации данных)
    
    9)ModelMapper (для маппинга объектов)
    
    10)JavaFaker (для генерации фейковых данных)
    
    11)Hibernate Validator (для валидации данных с использованием Hibernate)
    
    12)Java JWT (для работы с JSON Web Tokens)
    
    13)JUnit(для тестирования)
    
    14)Mockito(для тестирования)

### Для успешного запуска проекта необходимо:

    1)Создать БД по имени SocialNet по пути jdbc:postgresql://localhost:5432/SocialNet,логин и пароль от БД в данном случае = postgres.
    
    2)Выполнить SQL-код для создания таблиц БД,который можно найти по пути src/main/java/ru/Vlad/Spring/SocialNet/SocialNetwork/utils/SQL/Tables-Creating.txt.
    
    3)Для запуска проекта можно в командной строке ввести mvn spring-boot:run или просто нажать на кнопку запуска в IntellijIDEA/Eclipse:)
    
### Вывод при запуске через терминал:

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/5ca9c24f-9538-474c-975d-06bf803cdcec)


### Параметры проекта в application.properties:

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/4b56e0b3-84a1-4fbd-a253-df488adceb59)


### Далее будут рассмотрены разные части ТЗ:

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/9736629a-802e-4ce4-a047-870d5b53431d)


### Для регистрации надо ввести следующие данные:Никнейм,Пароль,Дата рождения

Например:

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/5f7caa25-22b6-4121-82b1-573950ce8718)


Ответом в случае успешной регистрации является JWT-токен для пользователя.

В случае если происходят ошибки в данных или имя пользователя занято уже,были учтены тоже.

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/213506a9-e0fa-46ce-aab4-1d03e56e246c)


### Для авторизации надо ввести следующие данные:Никнейм,Пароль.

Например:

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/696bf176-024d-455e-af73-35de74b97b30)


Ответом в случае успешного логина является JWT-токен для пользователя.

В случае если происходят ошибки в данных были учтены тоже.

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/7baab4a5-6660-4f37-9a24-97f2776182be)

### Поиск был реализован в двух вариантах:поиск по строке,на которую начинается никнейм пользователей, или поиск по строке,который содержится в никнейме пользователей.

Для поиска(в любой вариации) надо ввести в url следующие данные:строка,по которой будет происходить поиск.Также в headers запроса(предполагается,что вы проверяете в PostMan) добавляется headear 

Authorization со значением Bearer + (токен,который  вы получили при регистрации или логине).

Поиск по строке,на которую начинается никнейм пользователей.Результатом является JSON-массив всех пользователей,у которых никнейм начинается на заданную строку.

Пример:

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/8b2a4904-c81e-40cd-81bf-1b13d4b7cc3d)


Поиск по строке,которая содержится в никнейме пользователей.Результатом является JSON-массив всех пользователей,у которых в никнейм содержится заданная строка.

Пример:

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/05a5dc69-62dc-4a6e-98db-27f1dac39009)


![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/82dbe1de-5582-42d6-86aa-b3fd407ac1b1)

### Для добавления в друзья надо ввести ID пользователя,которого хотите добавить.

Например:

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/b957ce1f-c959-43f3-a6df-491a7aadb249)


Ответом в случае успешного добавления является сообщение со значением "OK".

В случае,если вы друзья и вы хотите добавить опять,и когда вы добавляете ID несуществующего пользователя тоже учтены.

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/30dfba36-b44c-48e7-9326-6c187798d1a8)

### Для удаления из друзей надо ввести ID пользователя,которого хотите удалить.

Например:

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/47878d0d-7e30-43a6-8deb-e94288a989ed)


Ответом в случае успешного удаления является сообщение со значением "OK".

Все остальные случаи тоже учтены.

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/2b0b6d32-71e3-4332-9006-328c1e415e65)

### Для того,чтобы получить список своих друзей,надо перейти по соответсвующему энпоинту ничего не вводив.

Например:

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/2c7b9cad-935b-405c-96bc-0ae4d304d007)


Ответом в случае успешного запроса являестся JSON-массив друзей текущего пользователя.

Все остальные случаи тоже учтены.

### Технологический стэк:

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/e5b1b31c-2c2e-45e5-956f-0aff265219fd)


### Unit-Тестирование было реализовано только для контроллера.

### Unit-тесты:

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/bff0a7f9-cd7b-4491-a74e-74deb71f4afa)


Проходят все,написанные Unit-тесты.

### Также был реализован часть Integration-тестирования для контроллера.

### Integration-тесты:

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/1543f845-b129-4a5e-abf7-21a8bc3b945f)


Проходят все Integration-тесты,однако:

    1)Для регистрации надо менять каждый раз имя,ибо в БД данные сохраняются.
    
    2)Для логина надо 1 раз поменять имя пользователя на рандомный существующий из БД.



