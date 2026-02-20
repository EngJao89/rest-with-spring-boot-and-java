# REST API com Spring Boot e Java

API RESTful construída do zero com Java 21, Spring Boot 3.5, MySQL, Flyway, HATEOAS e documentação OpenAPI/Swagger.

---

## Índice

- [Descrição do Projeto](#descrição-do-projeto)
- [Como Rodar o Projeto](#como-rodar-o-projeto)
- [Decisões Técnicas e Arquiteturais](#decisões-técnicas-e-arquiteturais)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Endpoints e Documentação](#endpoints-e-documentação)

---

## Descrição do Projeto

Esta aplicação é uma **API REST** que expõe recursos de **Pessoas (Person)** e **Livros (Book)** com as seguintes características:

- **CRUD completo** para Person e Book
- **Paginação e ordenação** nos listagens (Person)
- **Busca por nome** (Person) com suporte a paginação
- **HATEOAS**: links de navegação nos recursos (self, findAll, create, update, disable)
- **Versionamento de API**: DTOs em V1 e V2 (Person com e sem birthDay)
- **Content negotiation**: respostas em JSON, XML e YAML via header `Accept`
- **Documentação OpenAPI 3**: Swagger UI para explorar e testar a API
- **CORS** configurável para origens permitidas
- **Tratamento global de exceções** com respostas padronizadas
- **Migrações de banco** com Flyway
- **Testes de integração** com Testcontainers (MySQL) e REST Assured

### Recursos principais

| Recurso   | Base path           | Operações                                      |
|----------|---------------------|-------------------------------------------------|
| Person   | `/api/person/v1`    | GET (listar/paginado/buscar), GET /{id}, POST, PUT, PATCH /{id} (disable), DELETE /{id} |
| Book     | `/api/book/v1`      | GET, GET /{id}, POST, PUT, DELETE /{id}        |

---

## Como Rodar o Projeto

### Pré-requisitos

- **Java 21**
- **Maven 3.9+**
- **MySQL 8** (para rodar localmente) ou **Docker** (para rodar tudo em containers)

### 1. Rodar localmente (com MySQL na máquina)

1. Crie o banco e o usuário no MySQL (se ainda não existir):

```sql
CREATE DATABASE IF NOT EXISTS rest_with_spring_boot;
CREATE USER IF NOT EXISTS 'app_user'@'%' IDENTIFIED BY 'minha_senha';
GRANT ALL ON rest_with_spring_boot.* TO 'app_user'@'%';
FLUSH PRIVILEGES;
```

2. Opcional: configure variáveis de ambiente ou edite `src/main/resources/application.yml`:

| Variável         | Descrição        | Padrão              |
|------------------|------------------|----------------------|
| `MYSQL_HOST`     | Host do MySQL    | `localhost`          |
| `MYSQL_PORT`     | Porta            | `3306`               |
| `MYSQL_DATABASE` | Nome do banco    | `rest_with_spring_boot` |
| `MYSQL_USER`     | Usuário          | `app_user`           |
| `MYSQL_PASSWORD` | Senha            | `minha_senha`        |

3. Compile e execute:

```bash
./mvnw clean package
./mvnw spring-boot:run
```

A aplicação sobe em **http://localhost:8080**. O Flyway aplica as migrations na primeira subida.

### 2. Rodar com Docker (recomendado se não quiser instalar MySQL)

1. Na raiz do projeto:

```bash
docker-compose up -d --build
```

2. A API fica em **http://localhost:8080** e o MySQL em **localhost:3307** (porta externa 3307 para não conflitar com um MySQL local na 3306).

3. Parar e remover containers:

```bash
docker-compose down
# Remover também volumes (dados do banco):
docker-compose down -v
```

### 3. Executar testes

```bash
# Todos os testes (requer Docker para testes de integração com Testcontainers)
./mvnw test

# Apenas testes unitários (sem subir containers)
./mvnw test -Dtest=!*Integration*
```

---

## Decisões Técnicas e Arquiteturais

### Stack principal

- **Java 21** – LTS e recursos modernos da linguagem.
- **Spring Boot 3.5** – Framework para API, injeção de dependência, JPA, configuração.
- **Spring Data JPA** – Abstração de repositórios e paginação.
- **MySQL 8** – Banco relacional com suporte a timezone e boas práticas de segurança (usuário dedicado).
- **HikariCP** – Pool de conexões (default do Spring Boot), configurado com timeouts e tamanho de pool.
- **Flyway** – Migrações versionadas em SQL; `baseline-on-migrate` para projetos já existentes; migrations em `src/main/resources/db/migration`.

### API e contrato

- **REST** com JSON como formato principal; suporte a **XML e YAML** via content negotiation (`Accept`).
- **Versionamento por URL** (`/api/person/v1`, `/api/book/v1`) para evoluir o contrato sem quebrar clientes.
- **HATEOAS** (Spring HATEOAS) para expor links de navegação nos DTOs, facilitando descoberta e uso da API.
- **OpenAPI 3 (SpringDoc)** para documentação e Swagger UI; configuração centralizada em `OpenApiConfig`.

### Camadas da aplicação

- **Controller** – Expõe endpoints, valida parâmetros, delega para serviços e devolve DTOs/`ResponseEntity`.
- **Service** – Regras de negócio, orquestração, mapeamento Entity ↔ DTO e adição de links HATEOAS.
- **Repository** – Acesso a dados (JPA); queries customizadas quando necessário (ex.: `findPeopleByName`).
- **DTO** – Contratos da API (V1, V2); separados de entidades JPA.
- **Exception handling** – `@ControllerAdvice` com `CustomEntityResponseHandler` para exceções globais e respostas padronizadas (`ExceptionResponse`).

### Segurança e CORS

- CORS configurado por propriedades (`cors.originPatterns`) para permitir apenas origens definidas (ex.: frontends em localhost).
- Credenciais do banco via variáveis de ambiente, sem senhas fixas em código.

### Testes

- **JUnit 5** e **Mockito** para testes unitários.
- **Testcontainers** com MySQL para testes de integração que sobem um banco real.
- **REST Assured** para chamar a API nos testes de integração.
- Perfil `test` e `application-test.yml` com porta fixa (8888) e configuração do datasource para o container.

### Build e deploy

- **Maven** com Spring Boot Parent; empacotamento em JAR executável.
- **Docker** em build multi-stage: estágio Maven para compilar, estágio JRE para rodar; imagem final só com JRE 21 e usuário não-root.
- **docker-compose** orquestra app + MySQL, com healthcheck no MySQL e `depends_on` para a aplicação só subir após o banco estar saudável.

### Outras escolhas

- **Dozer** para mapeamento entre entidades e DTOs onde faz sentido.
- **ObjectMapper** customizado (filtros, serialização) em `ObjectMapperConfig`.
- **ddl-auto: update** em desenvolvimento; em produção recomenda-se usar apenas Flyway e `ddl-auto: validate` ou `none`.

---

## Estrutura do Projeto

```
src/main/java/engjao89/rest_with_spring_boot_and_java/
├── config/              # Configurações (OpenAPI, Web, ObjectMapper)
├── controllers/         # REST controllers e interfaces de documentação (docs)
├── data/dto/V1, V2/     # DTOs da API
├── exception/            # Exceções e handler global
├── mapper/               # Mapeamento Entity ↔ DTO (Dozer, custom)
├── model/                # Entidades JPA (Person, Book, etc.)
├── repository/           # Spring Data JPA repositories
├── service/              # Regras de negócio
├── serialization/        # Serializadores e conversores (YAML, etc.)
├── util/                 # Utilitários
└── Startup.java          # Classe principal

src/main/resources/
├── application.yml       # Configuração da aplicação
├── application-docker.yml  # Perfil Docker (datasource)
└── db/migration/         # Scripts Flyway (V1__, V2__, ...)
```

---

## Endpoints e Documentação

- **Base URL**: `http://localhost:8080`
- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### Exemplos rápidos (Person)

```bash
# Listar primeira página (12 itens, ordem ascendente por firstName)
curl -s http://localhost:8080/api/person/v1 | jq

# Buscar por nome
curl -s "http://localhost:8080/api/person/v1/findPeopleByName/Ayrton?page=0&size=5" | jq

# Criar pessoa (JSON)
curl -s -X POST http://localhost:8080/api/person/v1 \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Nome","lastName":"Sobrenome","address":"Endereço","gender":"Male","enabled":true}' | jq

# Resposta em XML
curl -s -H "Accept: application/xml" http://localhost:8080/api/person/v1/1
```

---

## Licença

Este projeto é de uso educacional/demonstrativo.
