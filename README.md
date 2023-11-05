# SocialNetworkRESTAPI
Техническое задание:
![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/a215dfdd-4379-4179-b67e-ba8fc655bcfa)
Для реализации данного ТЗ было использовано следующее:

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
Для успешного запуска проекта необходимо:

    1)Создать БД по имени SocialNet по пути jdbc:postgresql://localhost:5432/SocialNet,логин и пароль от БД в данном случае = postgres.
    
    2)Выполнить SQL-код для создания таблиц БД,который можно найти по пути src/main/java/ru/Vlad/Spring/SocialNet/SocialNetwork/utils/SQL/Tables-Creating.txt.
    
    3)Для запуска проекта можно в командной строке ввести mvn spring-boot:run или просто нажать на кнопку запуска в IntellijIDEA/Eclipse:)
    
Вывод при запуске через терминал:

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/cbbde492-c38d-4f82-a3dc-2bc88e63f3d7)

Параметры проекта в application.properties:

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/a9e6bb41-9cb4-4de5-b686-3abcc3d12301)

Далее будут рассмотрены разные части ТЗ:

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/86da1d6d-705c-401d-b7ab-95f81976f2da)

Для регистрации надо ввести следующие данные:Никнейм,Пароль,Дата рождения

Например:![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/36d26e90-a350-4b63-aad5-15c9896c9853)

Ответом в случае успешной регистрации является JWT-токен для пользователя.

В случае если происходят ошибки в данных или имя пользователя занято уже,были учтены тоже.

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/932fb4b5-90f7-4fe5-85f5-9b18912bc9bc)

Для авторизации надо ввести следующие данные:Никнейм,Пароль.

Например:![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/be9db66a-5ca8-453c-befb-a59d14a3c589)

Ответом в случае успешного логина является JWT-токен для пользователя.

В случае если происходят ошибки в данных были учтены тоже.

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/5341fd3f-4fe3-4c6c-93f1-d0cae494bd2c)

Поиск был реализован в двух вариантах:поиск по строке,на которую начинается никнейм пользователей, или поиск по строке,который содержится в никнейме пользователей.

Для поиска(в любой вариации) надо ввести в url следующие данные:строка,по которой будет происходить поиск.Также в headers запроса(предполагается,что вы проверяете в PostMan) добавляется headear 

Authorization со значением Bearer + (токен,который  вы получили при регистрации или логине).

Поиск по строке,на которую начинается никнейм пользователей.Результатом является JSON-массив всех пользователей,у которых никнейм начинается на заданную строку.Пример:

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/68df1eea-8e7a-4ae7-9f46-49a6a531bc50)

Поиск по строке,которая содержится в никнейме пользователей.Результатом является JSON-массив всех пользователей,у которых в никнейм содержится заданная строка.Пример:

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/e126d166-fd94-4fb3-b47e-f84b705790dc)

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/6e22c5a3-dcb5-45ba-a38c-41fd83e6dd35)

Для добавления в друзья надо ввести ID пользователя,которого хотите добавить.

Например:![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/4f85302a-131e-4a20-8db7-59b9e1d68201)

Ответом в случае успешного добавления является сообщение со значением "OK".

В случае,если вы друзья и вы хотите добавить опять,и когда вы добавляете ID несуществующего пользователя тоже учтены.

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/a21e25aa-43c3-4e13-9460-62dbfb3bb366)

Для удаления из друзей надо ввести ID пользователя,которого хотите удалить.

Например:![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/0eab5500-f99a-4ebf-b883-675226e703d3)

Ответом в случае успешного удаления является сообщение со значением "OK".

Все остальные случаи тоже учтены.

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/151fe743-7162-4524-bed1-e4f30c13f744)

Для того,чтобы получить список своих друзей,надо перейти по соответсвующему энпоинту ничего не вводив.

Например:![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/779eb47a-4f69-4e47-ab1c-b3ce308c61b4)

Ответом в случае успешного запроса являестся JSON-массив друзей текущего пользователя.

Все остальные случаи тоже учтены.

Технологический стэк:
![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/e68d085e-8bc9-4811-b56b-da780ec7a5d9)

Unit-Тестирование было реализовано только для контроллера.

Unit-тесты:

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/9b48b8e3-420f-45c8-b36b-61be02e65a44)

Проходят все,написанные Unit-тесты.

Также был реализован часть Integration-тестирования для контроллера.

Integration-тесты:

![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/d4da2e77-2481-4f7b-88f4-7a8690860504)

Проходят все Integration-тесты,однако:

    1)Для регистрации надо менять каждый раз имя,ибо в БД данные сохраняются.
    
    2)Для логина надо 1 раз поменять имя пользователя на рандомный существующий из БД.



