# 🐾 ClyvoVet

API + aplicação web para centralização do histórico de saúde de pets e geração de alertas preventivos.
Projeto desenvolvido como solução para o **Challenge FIAP 2026** – turma Java Advanced.

Vídeo de demonstração: https://youtu.be/22aJcCVcHc4

---

## 📋 Sumário

- [🆕 Novidades da Sprint 3](#-novidades-da-sprint-3)
- [📖 Sobre o projeto](#-sobre-o-projeto)
- [🛠️ Tecnologias utilizadas](#️-tecnologias-utilizadas)
- [⚙️ Pré-requisitos](#️-pré-requisitos)
- [🗄️ Configuração do banco de dados](#️-configuração-do-banco-de-dados)
- [🚀 Como executar o projeto](#-como-executar-o-projeto)
- [🌐 Usando a aplicação web](#-usando-a-aplicação-web)
- [🧪 Testando a API](#-testando-a-api)
- [📚 Documentação da API (Swagger)](#-documentação-da-api-swagger)
- [🧱 Estrutura do projeto](#-estrutura-do-projeto)
- [👥 Equipe](#-equipe)

---

## 🆕 Novidades da Sprint 3

1. **Camada de visualização (frontend)** — aplicação web server-side renderizada com
   **Thymeleaf**, com login e páginas para tutores e veterinários (`src/main/resources/templates`,
   controllers em `br.com.fiap.clyvovet.web`).
2. **Massa de dados de demonstração** — um `DataSeeder` (`CommandLineRunner`) cria, na
   primeira subida da aplicação, 1 clínica, 1 veterinário, 2 tutores, 2 pets e os 2 usuários
   de demonstração usados no login. O schema continua sendo criado/atualizado automaticamente
   pelo Hibernate (`spring.jpa.hibernate.ddl-auto=update`).
3. **Spring Security** — dois perfis de usuário (`TUTOR` e `VETERINARIO`), com login por
   formulário, senhas com hash BCrypt e proteção de rotas por perfil (`/app/tutor/**` só para
   tutores, `/app/veterinario/**` só para veterinários). A API REST existente continua aberta
   (Swagger/Postman), apenas a camada web nova é protegida.
4. **Dois fluxos completos** (além dos CRUDs já existentes):
   - **Agendamento de consulta pelo tutor** (`AgendamentoConsultaService`): o tutor escolhe um
     pet seu, um veterinário e um horário; o sistema valida que o pet pertence ao tutor logado
     e que o veterinário está livre naquele horário antes de confirmar o agendamento.
   - **Atendimento da consulta pelo veterinário** (`AtendimentoConsultaService`): o veterinário
     conclui a consulta registrando diagnóstico e tratamento e, opcionalmente, registra a
     aplicação de uma vacina e/ou a necessidade de retorno — em ambos os casos o sistema
     **gera automaticamente um Alerta de Saúde** (próxima dose ou retorno pendente) para o pet.

---

## 📖 Sobre o projeto

O **ClyvoVet** resolve o problema da fragmentação na jornada de saúde do pet.  
Tutores, clínicas e veterinários podem registrar e consultar:

- Tutores e seus pets
- Clínicas e veterinários
- Consultas, vacinas, medicamentos e exames
- Alertas de saúde (vacinas vencidas, retornos pendentes, etc.)

A API segue os princípios RESTful, utiliza:

- Paginação
- Cache
- Validação de dados
- Tratamento global de exceções

---

## 🛠️ Tecnologias utilizadas

- **Java 21**
- **Spring Boot 3.4.5**
- **Spring Data JPA** + **Hibernate**
- **Oracle Database** (19c/21c) – JDBC via ojdbc11
- **Spring Security** (login por formulário, BCrypt, autorização por perfil)
- **Thymeleaf** + **thymeleaf-extras-springsecurity6** (camada de visualização)
- **Spring Cache** (simple)
- **Spring Validation** (Bean Validation)
- **SpringDoc OpenAPI** (Swagger UI)
- **Lombok**
- **Gradle** (build tool)
- **Postman / Insomnia** (testes de API)

---

## ⚙️ Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

- **JDK 21** (ou superior compatível)
- **Oracle Database** (local ou remoto)
- **Gradle** (opcional – o projeto inclui o Gradle Wrapper)
- **Git**
- **Postman** (opcional)

---

## 🗄️ Configuração do banco de dados

1. Certifique-se de que o Oracle Database está ativo.
2. Use um usuário/schema com privilégios para criar tabelas, sequences e constraints — o
   schema é criado e atualizado automaticamente pelo Hibernate (`ddl-auto=update`), a partir
   das entidades JPA.
3. Altere o arquivo `src/main/resources/application.properties` com as suas credenciais:

```properties
spring.datasource.url=jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

> **Nota:** Na primeira subida da aplicação, além de criar/atualizar o schema, o Hibernate
> encontra o banco sem a tabela `TB_USUARIO` populada e o `DataSeeder` (`CommandLineRunner`,
> em `br.com.fiap.clyvovet.config`) insere automaticamente uma massa mínima de dados: 1
> clínica, 1 veterinário, 2 tutores, 2 pets e os 2 usuários de demonstração usados no login.
> Nas subidas seguintes, como já existem usuários cadastrados, o `DataSeeder` não insere
> nada novamente.

---

# 🚀 Como executar o projeto

## 1. Clone o repositório

```bash
git clone https://github.com/Gustavo-B-Araujo/clyvovet-api-java
cd clyvovet-api-java
```

## 2. Execute a aplicação via Gradle Wrapper

### Linux / Mac

```bash
./gradlew bootRun
```

### Windows

```bash
gradlew.bat bootRun
```

Ou via IntelliJ IDEA: abra o projeto e execute a classe `ClyvovetApplication`.

A aplicação iniciará na porta `8080`.

Você verá no console:

```text
Started ClyvovetApplication ...
```

---

# 🌐 Usando a aplicação web

Com a aplicação rodando, acesse:

```text
http://localhost:8080/login
```

Use uma das contas de demonstração criadas pelo `DataSeeder`:

| Perfil       | E-mail                        | Senha     |
|--------------|-------------------------------|-----------|
| Tutor        | tutor@clyvovet.com.br         | tutor123  |
| Veterinário  | veterinario@clyvovet.com.br   | vet123    |

## Fluxo do tutor

1. Login com a conta de tutor → `/app/tutor/pets` lista os pets do tutor autenticado.
2. Ao abrir um pet, o tutor vê consultas, vacinas e alertas de saúde daquele pet.
3. Em **"Agendar consulta"**, o tutor escolhe veterinário, data/hora e tipo de consulta.
   O sistema valida que o pet é dele e que o veterinário está livre naquele horário.
4. Em **"Alertas"**, o tutor visualiza e marca como lidos os alertas gerados para seus pets.

## Fluxo do veterinário

1. Login com a conta de veterinário → `/app/veterinario/agenda` lista as consultas pendentes
   dele (agendadas, confirmadas ou em andamento).
2. Ao clicar em **"Atender"**, o veterinário registra diagnóstico e tratamento e pode marcar:
   - **Aplicar vacina** → informa nome, fabricante e em quantos dias é a próxima dose. A
     consulta é concluída, a vacina é registrada e um alerta `VACINA_PROXIMA` é criado
     automaticamente para o pet.
   - **Agendar retorno** → informa em quantos dias. Um alerta `RETORNO_PENDENTE` é criado
     automaticamente para o pet.

---

# 🧪 Testando a API

## ✅ 1. Usando o Swagger UI (recomendado)

Acesse:

```text
http://localhost:8080/swagger-ui.html
```

Lá você pode:

- Visualizar todos os endpoints
- Ver parâmetros e exemplos
- Executar requisições diretamente no navegador

---

### Ordem recomendada para testes

Execute as requisições nesta ordem:

1. Tutores
2. Pets
3. Clínicas
4. Veterinários
5. Consultas
6. Vacinas
7. Medicamentos
8. Exames
9. Alertas

---

### Observações

Para endpoints `GET`, utilize filtros como:

```http
GET /pets?especie=CACHORRO
```

Para `POST`, `PUT` e `DELETE`, garanta que os IDs relacionados existam no banco.

---

## ✅ 3. Exemplos de endpoints

```http
POST   /tutores
GET    /tutores
GET    /tutores/{id}
PUT    /tutores/{id}
DELETE /tutores/{id}

GET    /pets?tutorId=1

GET    /vacinas/vencidas

PATCH  /alertas/{id}/lido
```

---

## ✅ 4. Populando o banco

O banco será criado vazio.

Você pode:

- Executar scripts SQL da pasta `documents/` (caso existam)

### Recomendação mínima de dados

- 2 tutores
- 2 pets
- 1 clínica
- 1 veterinário
- 1 consulta

---

# 📚 Documentação da API (Swagger)

Com a aplicação em execução, acesse:

```text
http://localhost:8080/swagger-ui.html
```

A interface Swagger permite:

- Navegar pelos endpoints
- Ver exemplos de request/response
- Testar a API diretamente

---

# 🧱 Estrutura do projeto

```text
src/main/java/br/com/fiap/clyvovet/
├── config/            # DataSeeder (massa de dados de demonstração)
├── controller/        # 9 controllers REST (API existente, inalterada)
├── dto/
│   ├── request/       # DTOs de entrada da API REST
│   ├── response/      # DTOs de saída da API REST
│   └── web/           # Forms da camada web (Thymeleaf)
├── enums/             # Enums do sistema (inclui Role, novo na Sprint 3)
├── exception/         # Exceptions e handlers (REST e web)
├── mapper/            # Conversores DTO ↔ Entity
├── model/             # Entidades JPA (inclui Usuario, novo na Sprint 3)
├── repository/        # Interfaces Spring Data JPA
├── security/          # Spring Security: UserDetailsService, UserDetails e config
├── service/           # Regras de negócio, cache e os 2 fluxos completos
└── web/               # Controllers MVC da aplicação web (tutor e veterinário)

src/main/resources/
├── static/css/        # Estilos da aplicação web
└── templates/         # Views Thymeleaf (login, tutor/, veterinario/, erro/)
```

---

# 👥 Equipe

| Nome | RM |
|---|---|
| Phietro Solon Oliveira | RM563842 |
| Matheus Almeida Ribeiro | RM562980 |
| Gustavo Barrios de Araujo | RM563358 |
