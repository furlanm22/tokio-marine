# Tokio Marine

Projeto desenvolvido para a Tokio Marine.

## Descrição

Este repositório contém o código fonte do projeto Tokio Marine, uma aplicação web desenvolvida com Spring Boot e Vue.js.

## Tecnologias

### Backend
- Java 11
- Spring Boot 2.7.18
- Spring Data JPA
- H2 Database
- Lombok
- Maven

### Frontend (a ser implementado)
- Vue.js ou Angular

## Estrutura do Projeto

```
src/main/java/com/tokiomarine/tokiomarine/
├── config/         # Configurações da aplicação
├── controller/     # Controladores REST
├── dto/           # Objetos de transferência de dados
├── exception/     # Tratamento de exceções
├── model/         # Entidades JPA
├── repository/    # Repositórios JPA
└── service/       # Lógica de negócio
```

## Como executar

### Pré-requisitos
- Java 11
- Maven
- Node.js (para o frontend, quando implementado)

### Backend
1. Clone o repositório
2. Navegue até a pasta do projeto
3. Execute o comando: `mvn spring-boot:run`
4. A aplicação estará disponível em: `http://localhost:8080`
5. Console H2 disponível em: `http://localhost:8080/h2-console`

### Frontend (a ser implementado)
- Instruções serão adicionadas quando o frontend for implementado

## Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Faça commit das suas alterações (`git commit -m 'Adiciona nova feature'`)
4. Faça push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request 