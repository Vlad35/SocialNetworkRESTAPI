# SocialNetworkRESTAPI
Техническое задание:
![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/a215dfdd-4379-4179-b67e-ba8fc655bcfa)
Для реализации данного ТЗ было использовано следующее:
1)Spring Boot Starter Data JPA
2)Spring Boot Starter Security
3)Spring Boot Starter Thymeleaf
4)Spring Boot Starter Web
5)PostgreSQL (для работы с базой данных PostgreSQL)
6)Lombok (дополнительная библиотека для упрощения разработки)
7)Spring Boot Starter Test (зависимость для тестирования)
8)Spring Security Test (зависимость для тестирования Spring Security)
10)Spring Boot Starter Validation (зависимость для валидации данных)
10)ModelMapper (для маппинга объектов)
11)JavaFaker (для генерации фейковых данных)
12)Hibernate Validator (для валидации данных с использованием Hibernate)
13)Java JWT (для работы с JSON Web Tokens)

Для успешного запуска проекта необходимо:
    1)Создать БД по имени SocialNet по пути jdbc:postgresql://localhost:5432/SocialNet,логин и пароль от БД в данном случае = postgres.
    2)Выполнить SQL-код для создания таблиц БД,который можно найти по пути src/main/java/ru/Vlad/Spring/SocialNet/SocialNetwork/utils/SQL/Tables-Creating.txt.
    3)Для запуска проекта можно в командной строке ввести mvn spring-boot:run или просто нажать на кнопку запуска в IntellijIDEA/Eclipse:)
Вывод при запуске через терминал:
![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/cbbde492-c38d-4f82-a3dc-2bc88e63f3d7)
Параметры проекта в application.properties:
![image](https://github.com/Vlad35/SocialNetworkRESTAPI/assets/90512038/a9e6bb41-9cb4-4de5-b686-3abcc3d12301)

