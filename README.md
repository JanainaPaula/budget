# Budget App [WIP]

Aplicação de controle de orçamento pessoal.

### Tecnologias usadas

- Java 17
- Spring boot
- Maven
- Postgres
- Docker

### Arquitetura usada no projeto

Esse projeto foi projetado usando a arquitetura Hexagonal, visando facilitar e extensão e manutenção do projeto a 
longo prazo.

### Como subir o projeto localmente

Para subir o projeto, a primeira coisa a ser feita é rodar o arquivo ```docker-compose.yml```, que está no diretório 
raiz do projeto, para subir o container com o banco de dados. Segue comando:

``` bash
docker-compose up -d
````

Depois dentro da IDE rodar a classe main da aplicação, a ````BudgetApplication.java````.

### Testes

### Responsáveis

- [Janaina Paula](https://github.com/JanainaPaula)
