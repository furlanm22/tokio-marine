# Backend - Tokio Marine

Backend da aplicação Tokio Marine desenvolvido com Spring Boot.

## Tecnologias

- Java 11
- Spring Boot 2.7.18
- Spring Data JPA
- H2 Database
- Lombok
- Maven

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

### Passos
1. Navegue até a pasta do projeto: `cd backend`
2. Execute o comando: `mvn spring-boot:run`
3. A aplicação estará disponível em: `http://localhost:8080`
4. Console H2 disponível em: `http://localhost:8080/h2-console`

### Configurações
- Banco de dados H2 em memória
- Porta padrão: 8080
- Console H2 habilitado em: /h2-console
- Credenciais H2:
  - URL: jdbc:h2:mem:tokiodb
  - Username: sa
  - Password: password

## Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Faça commit das suas alterações (`git commit -m 'Adiciona nova feature'`)
4. Faça push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request 