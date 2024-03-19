# notification

### 개발환경
- Java 17
- Spring Boot 3.2.3
- Spring Batch
- Spring Data JPA
- Spring AMQP
- Gradle
- H2

### CLASS DIAGRAM
![class_diagram](./notification_class.jpg)

### API

[알림 예약]
- POST http://localhost:8090/api/notifications
  - -H ContentType: application/json

[예약 알림 발송]
- POST http://localhost:8090/api/notifications/reserved-sending
    - -H ContentType: application/json
