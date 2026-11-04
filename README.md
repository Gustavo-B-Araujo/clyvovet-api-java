# ClyvoVet

API + aplicação web para centralização do histórico de saúde de pets e geração de alertas preventivos.
Projeto desenvolvido como solução para o **Challenge FIAP 2026** – turma Java Advanced.

Vídeo de demonstração: https://youtu.be/XIvTQeDpgEs

---

## Sumário

- [Sobre o projeto](#sobre-o-projeto)
- [Tecnologias utilizadas](#tecnologias-utilizadas)
- [Como executar localmente](#como-executar-localmente)
- [Usando a aplicação web](#usando-a-aplicação-web)
- [Testando a API](#testando-a-api)
- [Integração multidisciplinar](#integração-multidisciplinar)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Equipe](#equipe)

---

## Sobre o projeto

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

## Tecnologias utilizadas

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

## Como executar localmente

A aplicação já está publicada (veja a próxima seção). Para rodar na sua máquina:

**Pré-requisitos:** JDK 21, acesso a um Oracle Database e Git (o Gradle Wrapper já está incluído).

1. Clone o repositório:

```bash
git clone https://github.com/Gustavo-B-Araujo/clyvovet-api-java
cd clyvovet-api-java
```

2. Crie o arquivo `src/main/resources/application-local.properties` (já está no `.gitignore`) com as suas credenciais:

```properties
spring.datasource.url=jdbc:oracle:thin:@HOST:1521:SID
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
jwt.secret=UMA_CHAVE_SECRETA_LONGA
```

3. Execute:

```bash
./gradlew bootRun        # Linux / Mac
gradlew.bat bootRun      # Windows
```

A aplicação sobe em `http://localhost:8080`. O schema é criado pelo Hibernate e, na primeira execução, o `DataSeeder` insere os dados de demonstração e as contas de login abaixo.

Para rodar os testes: `./gradlew test`.

---

## Usando a aplicação web

Acesse: https://app-clyvo-api.azurewebsites.net/

Use uma das contas de demonstração criadas pelo `DataSeeder`:

| Perfil       | E-mail                        | Senha     |
|--------------|-------------------------------|-----------|
| Tutor        | tutor@clyvovet.com.br         | tutor123  |
| Veterinário  | veterinario@clyvovet.com.br   | vet123    |

### Fluxo do tutor

1. Login com a conta de tutor → `/app/tutor/pets` lista os pets do tutor autenticado.
2. Ao abrir um pet, o tutor vê consultas, vacinas e alertas de saúde daquele pet.
3. Em **"Agendar consulta"**, o tutor escolhe veterinário, data/hora e tipo de consulta.
   O sistema valida que o pet é dele e que o veterinário está livre naquele horário.
4. Em **"Alertas"**, o tutor visualiza e marca como lidos os alertas gerados para seus pets.

### Fluxo do veterinário

1. Login com a conta de veterinário → `/app/veterinario/agenda` lista as consultas pendentes
   dele (agendadas, confirmadas ou em andamento).
2. Ao clicar em **"Atender"**, o veterinário registra diagnóstico e tratamento e pode marcar:
   - **Aplicar vacina** → informa nome, fabricante e em quantos dias é a próxima dose. A
     consulta é concluída, a vacina é registrada e um alerta `VACINA_PROXIMA` é criado
     automaticamente para o pet.
   - **Agendar retorno** → informa em quantos dias. Um alerta `RETORNO_PENDENTE` é criado
     automaticamente para o pet.

---

## Testando a API

### 1. Usando o Swagger UI

Acesse: https://app-clyvo-api.azurewebsites.net/swagger-ui/index.html

Lá você pode:

- Visualizar todos os endpoints
- Ver parâmetros e exemplos
- Executar requisições diretamente no navegador

---

#### Ordem recomendada para testes

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

#### Observações

Para endpoints `GET`, utilize filtros como:

```http
GET /pets?especie=CACHORRO
```

Para `POST`, `PUT` e `DELETE`, garanta que os IDs relacionados existam no banco.

---

### 2. Exemplos de endpoints

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

## Integração multidisciplinar

A API em Java é o núcleo do ClyvoVet e se integra aos demais componentes do projeto:

| Disciplina | Como foi aplicada na solução |
|---|---|
| **Java Advanced** | API REST + aplicação web (Spring Boot, JPA, Security/JWT, Thymeleaf), publicada online. |
| **DevOps / Cloud** | Código no Azure Repos, pipeline CI/CD (testes, build do JAR e deploy no Azure App Service) e banco Oracle em container (ACI) no grupo de recursos `ch-clyvo`. |
| **Mobile** | O aplicativo mobile (Android, `com.clyvo.app`) está registrado no projeto Firebase `clyvovet-ch`. |

Evidências (prints): [`docs/evidencias/`](docs/evidencias/)

---

## Estrutura do projeto

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

## Equipe

| Nome | RM |
|---|---|
| Phietro Solon Oliveira | RM563842 |
| Matheus Almeida Ribeiro | RM562980 |
| Gustavo Barrios de Araujo | RM563358 |
