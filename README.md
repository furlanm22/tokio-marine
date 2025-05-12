# Projeto Tokio Marine

## Objetivo do Projeto

O projeto Tokio Marine é uma aplicação web para agendamento de transferências financeiras. Ele permite que os usuários agendem transferências entre contas, calculando taxas e validando dados de entrada.

## Tecnologias Usadas

### Frontend
- Vue 3
- Vite

### Backend
- Java 11
- Spring Boot 2.7.18
- Spring Data JPA
- H2 Database
- Lombok
- Maven

## Estrutura da Aplicação

### Frontend
```
frontend/
├── src/
│   ├── components/    # Componentes Vue
│   ├── services/      # Serviços de API
│   └── assets/        # Arquivos de estilo e imagens
├── public/            # Arquivos públicos
└── index.html         # Arquivo HTML principal
```

### Backend
```
backend/src/main/java/com/tokiomarine/tokiomarine/
├── config/         # Configurações da aplicação
├── controller/     # Controladores REST
├── dto/            # Objetos de transferência de dados
├── exception/      # Tratamento de exceções
├── model/          # Entidades JPA
├── repository/     # Repositórios JPA
└── service/        # Lógica de negócio
```

## Instruções para Rodar

### Pré-requisitos
- Node.js e npm (para o frontend)
- Java 11 e Maven (para o backend)

### Rodando o Frontend
1. Navegue até a pasta do frontend: `cd frontend`
2. Instale as dependências: `npm install`
3. Inicie o servidor de desenvolvimento: `npm run dev`
4. A aplicação estará disponível em: `http://localhost:5173`

### Rodando o Backend
1. Navegue até a pasta do backend: `cd backend`
2. Execute o comando: `mvn spring-boot:run`
3. A aplicação estará disponível em: `http://localhost:8081`
4. Console H2 disponível em: `http://localhost:8081/h2-console`

### Configurações do Backend
- Banco de dados H2 em memória
- Porta padrão: 8081
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
