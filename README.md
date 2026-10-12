# Baozi Store - API REST

API REST simples para controle de clientes, produtos e pedidos da Baozi Store (loja de pao chines).

**Tecnologias:** Java 17, Spring Boot 3.3, Spring Data JPA, H2 (padrao) ou MySQL, Postman.

## Como executar
Requisitos: JDK 17+ e Maven (ou abrir o projeto no IntelliJ / Eclipse / VS Code e rodar `BaoziStoreApplication`).

```bash
mvn spring-boot:run
```
A API sobe em `http://localhost:8080`. Console do banco H2: `http://localhost:8080/h2-console`
(JDBC URL `jdbc:h2:file:./data/baozidb`, usuario `sa`, senha vazia).

Para usar MySQL, edite `src/main/resources/application.properties` (instrucoes no proprio arquivo).

## Estrutura (MVC)
```
br.com.baozistore
 |- model        -> entidades JPA (Cliente, Produto, Pedido)
 |- repository   -> interfaces Spring Data JPA
 |- controller   -> endpoints REST
 |- dto          -> PedidoRequest (clienteId, produtoId, quantidade)
 |- exception    -> tratamento de erros (409 em violacao de integridade)
```

## Endpoints (para cada entidade: /clientes, /produtos, /pedidos)
| Metodo | URL | Descricao |
|---|---|---|
| POST | /{entidade} | Criar |
| GET | /{entidade} | Listar todos |
| GET | /{entidade}/{id} | Consultar por ID |
| PUT | /{entidade}/{id} | Atualizar (opcional) |
| DELETE | /{entidade}/{id} | Apagar |

## Exemplos de JSON
```json
POST /clientes  { "nome": "Lucas5172218", "clienteDesde": "2026-10-12" }
POST /produtos  { "nome": "Baozi de Carne", "preco": 8.50, "estoque": true }
POST /pedidos   { "clienteId": 1, "produtoId": 1, "quantidade": 5 }
```
