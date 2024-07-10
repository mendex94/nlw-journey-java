# NLW Jorney - Plann.er

## 🚀 Tecnologias

Esse projeto foi desenvolvido com as seguintes tecnologias:
- [Spring Web](https://spring.io/guides/gs/serving-web-content/)
- [Flyway](https://flywaydb.org/)
- [DevTools](https://docs.spring.io/spring-boot/docs/current/reference/html/using-spring-boot.html#using-boot-devtools)
- [Lombok](https://projectlombok.org/)
- [JPA](https://spring.io/guides/gs/accessing-data-jpa/)
- [H2 Database](https://www.h2database.com/html/main.html)

## 💻 Projeto

O Plann.er tem como objetivo ajudar o usuário a organizar viagens à trabalho ou lazer. O usuário pode criar uma viagem com nome, data de início e fim. Dentro da viagem o usuário pode planejar sua viagem adicionando atividades para realizar em cada dia.

## 🚀 Como executar

- Clone o repositório
- Abra o projeto no IntelliJ
- Execute a classe PlannerApplication

A aplicação estará disponível em `http://localhost:3000`.

## 📝 Requisitos Funcionais

- [x] O usuário pode criar uma viagem
- [x] O usuário pode adicionar atividades à viagem
- [x] O usuário pode visualizar as atividades da viagem
- [x] O usuário pode convites amigos para a viagem

## 📝 Bonus

- [ ] Criar documentação da API com Swagger
- [ ] Adicionar validação de campos de data
  - [ ] Data de início da viagem não pode ser menor que a data atual
  - [ ] Data de fim da viagem não pode ser menor que a data de início
- [ ] Extração do core das trips para dentro de um Service