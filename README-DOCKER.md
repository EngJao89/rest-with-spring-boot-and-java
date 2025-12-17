# Docker Setup

Este documento explica como executar a aplicação usando Docker.

## Pré-requisitos

- Docker instalado
- Docker Compose instalado

## Como executar

### 1. Construir e iniciar os containers

```bash
docker-compose up -d --build
```

Este comando irá:
- Construir a imagem da aplicação Spring Boot
- Iniciar o container MySQL
- Iniciar o container da aplicação
- Aguardar o MySQL estar saudável antes de iniciar a aplicação

### 2. Verificar os logs

```bash
# Ver logs da aplicação
docker-compose logs -f app

# Ver logs do MySQL
docker-compose logs -f mysql

# Ver logs de todos os serviços
docker-compose logs -f
```

### 3. Acessar a aplicação

- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/v3/api-docs
- **MySQL**: localhost:3307 (porta externa, interna do container é 3306)

### 4. Parar os containers

```bash
docker-compose down
```

Para remover também os volumes (dados do banco):

```bash
docker-compose down -v
```

## Comandos úteis

### Reconstruir apenas a aplicação

```bash
docker-compose build app
docker-compose up -d app
```

### Acessar o container da aplicação

```bash
docker exec -it rest-api-app bash
```

### Acessar o MySQL

**Dentro do container:**
```bash
docker exec -it rest-api-mysql mysql -u app_user -pminha_senha rest_with_spring_boot
```

**De fora do container (usando cliente MySQL local):**
```bash
mysql -h localhost -P 3307 -u app_user -pminha_senha rest_with_spring_boot
```

### Verificar status dos containers

```bash
docker-compose ps
```

## Variáveis de ambiente

As variáveis de ambiente podem ser configuradas no arquivo `docker-compose.yml`:

- `MYSQL_ROOT_PASSWORD`: Senha do root do MySQL
- `MYSQL_DATABASE`: Nome do banco de dados
- `MYSQL_USER`: Usuário do banco de dados
- `MYSQL_PASSWORD`: Senha do usuário do banco de dados

## Estrutura

- **Dockerfile**: Define como construir a imagem da aplicação
- **docker-compose.yml**: Orquestra os serviços (app + MySQL)
- **.dockerignore**: Arquivos ignorados no build
- **application-docker.yml**: Configurações específicas para Docker

## Troubleshooting

### A aplicação não inicia

1. Verifique se o MySQL está saudável:
   ```bash
   docker-compose ps
   ```

2. Verifique os logs:
   ```bash
   docker-compose logs app
   ```

3. Verifique se a porta 8080 está livre:
   ```bash
   lsof -i :8080
   ```

### Erro de conexão com o banco

1. Verifique se o MySQL está rodando:
   ```bash
   docker-compose ps mysql
   ```

2. Verifique as variáveis de ambiente no `docker-compose.yml`

3. Teste a conexão manualmente:
   ```bash
   docker exec -it rest-api-mysql mysql -u app_user -pminha_senha rest_with_spring_boot
   ```
