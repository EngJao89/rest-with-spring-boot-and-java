# Configuração do MySQL

## Problema: Access denied for user 'root'@'localhost'

Este erro ocorre quando as credenciais do MySQL não estão corretas ou o usuário não tem permissões.

## Soluções:

### Opção 1: Configurar credenciais via variáveis de ambiente

O `application.yml` agora suporta variáveis de ambiente. Configure antes de executar:

```bash
export MYSQL_USER=root
export MYSQL_PASSWORD=minha_senha
export MYSQL_DATABASE=rest_with_spring_boot
./mvnw spring-boot:run
```

### Opção 2: Editar application.yml diretamente

Edite o arquivo `src/main/resources/application.yml` e ajuste:

```yaml
spring:
  datasource:
    username: seu_usuario
    password: sua_senha
```

### Opção 3: Criar novo usuário no MySQL

1. Acesse o MySQL:
```bash
sudo mysql -u root
```

2. Crie o banco e usuário:
```sql
CREATE DATABASE rest_with_spring_boot;
CREATE USER 'app_user'@'localhost' IDENTIFIED BY 'senha_segura';
GRANT ALL PRIVILEGES ON rest_with_spring_boot.* TO 'app_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

3. Atualize o `application.yml`:
```yaml
spring:
  datasource:
    username: app_user
    password: senha_segura
```

### Opção 4: Resetar senha do root (se necessário)

Se você esqueceu a senha do root:

```bash
sudo mysql
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'nova_senha';
FLUSH PRIVILEGES;
EXIT;
```

### Opção 5: Verificar se MySQL está rodando

```bash
sudo systemctl status mysql
sudo systemctl start mysql  # se não estiver rodando
```

## Teste de conexão

Teste se a conexão funciona:

```bash
mysql -u root -p
# ou
mysql -u app_user -p
```

