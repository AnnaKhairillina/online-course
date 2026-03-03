# Online Course MVP

MVP платформа на `Spring Boot + Thymeleaf + MySQL` с ролями:
- `STUDENT` - регистрация, список купленных курсов и прогресс.
- `TEACHER` - добавление уроков.
- `ADMIN` - просмотр, редактирование и удаление пользователей.
- `MANAGER` - анализ доходов (таблица по данным).

## Запуск

1. Поднять MySQL:
```bash
docker compose up -d
```

2. Запустить приложение:
```bash
mvn spring-boot:run
```

Если порт `8080` занят:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

3. Открыть:
- `http://localhost:8080/login` (или `8081`)

## Тестовые пользователи

Пароль у всех: `password123`

- Админ: `admin@local`
- Учитель: `teacher@local`
- Менеджер: `manager@local`
- Студент: `student@local`

Новые студенты регистрируются через кнопку `Регистрация студента` на странице входа.
