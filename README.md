# Sistema de Gestão de Produtos e Pedidos

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![H2](https://img.shields.io/badge/H2-0078D4?style=for-the-badge&logo=h2&logoColor=white)

## Descrição
Este projeto é um sistema de gestão de produtos e pedidos desenvolvido em Java 17+. Ele permite que os usuários gerenciem produtos e pedidos de forma eficiente, incluindo funcionalidades para criar, atualizar e recuperar pedidos e produtos.

## Funcionalidades do Projeto

### Gerenciamento de Produtos

- 🆕 **Criar Produto:** Permite a criação de novos produtos no sistema.
- 🔄 **Atualizar Produto:** Permite a atualização das informações de um produto existente.
- 🔍 **Recuperar Produto por Nome:** Permite a busca de um produto pelo seu nome.
- 🔍 **Recuperar Produto por SKU:** Permite a busca de um produto pelo seu SKU.
- 🗑️ **Deletar Produto:** Permite a exclusão de um produto do sistema.
- 🔄 **Atualizar Status do Produto:** Permite a atualização do status (ativo/inativo) de um produto.

### Gerenciamento de Pedidos

- 🆕 **Criar Pedido:** Permite a criação de novos pedidos no sistema.
- 🔍 **Recuperar Pedido por ID:** Permite a busca de um pedido pelo seu ID.
- 🔍 **Recuperar Pedidos por SKU do Produto:** Permite a busca de pedidos que contenham um produto específico pelo seu SKU.

## Arquitetura
O projeto segue a arquitetura de camadas, dividida nas seguintes camadas:

- 🕹️ **Controller:** Responsável por lidar com as requisições HTTP e direcioná-las para os serviços apropriados.
- 📦 **Domain:** Contém as entidades e objetos de domínio do sistema.
- ⚠️ **Exception:** Contém as classes de exceção personalizadas para tratamento de erros.
- 📥 **Input:** Contém os objetos de entrada (DTOs) usados para receber dados das requisições.
- 📤 **Output:** Contém os objetos de saída (DTOs) usados para enviar dados nas respostas.
- 🗄️ **Repository:** Contém as interfaces de repositório para acesso aos dados.
- 🛠️ **Service:** Contém a lógica de negócios e manipulação dos dados.
- 🔧 **Utils:** Contém classes utilitárias usadas em várias partes do sistema.

## Diagrama ER do Banco de Dados
O diagrama Mermaid representa um modelo de banco de dados relacional com três tabelas: `ORDER`, `ORDER_ITEM` e `PRODUCT`.

![img.png](img.png)

- A tabela `ORDER` contém informações sobre pedidos, incluindo o ID do pedido, valor total e data da compra.
- A tabela `ORDER_ITEM` contém detalhes sobre itens individuais dentro de um pedido, incluindo o ID do item, ID do pedido (chave estrangeira), ID do produto (chave estrangeira), quantidade e subtotal.
- A tabela `PRODUCT` contém informações sobre produtos, incluindo o ID do produto, nome, SKU, preço, estoque, status, data de criação e data de modificação.

As relações entre essas tabelas são as seguintes:
- Um `ORDER` pode conter múltiplas entradas de `ORDER_ITEM`, indicando que um pedido pode ter vários itens.
- Cada `ORDER_ITEM` referencia um único `PRODUCT`, indicando que cada item em um pedido corresponde a um produto específico.

## Começando

## Dependências Necessarias

A seguir estão as principais dependências necessárias para o sistema:

- **Spring Boot Starter Data JPA:** Fornece integração com JPA para persistência de dados.
```xml
<dependency>
     <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```
- **Spring Boot Starter Validation:** Fornece suporte de validação.
```xml 
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
  ```
- **Spring Boot Starter Web:** Fornece suporte para a construção de aplicações web, incluindo serviços RESTful. 
```xml 
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
  ```
- **H2 Database:** Um banco de dados na memória para desenvolvimento e teste.
```xml 
<dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <scope>runtime</scope>
</dependency>
  ```
- **Lombok:** Uma biblioteca que ajuda a reduzir o código clichê.
```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.34</version>
</dependency>
  ```
- **Spring Boot Starter Test:** Fornece suporte de teste.
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
  ```

### Instalação

Passos para configurar o projeto localmente:

1. Clone o repositório:
    ```sh
    git clone https://github.com/Filipescordeiro2/project-prodify.git
    ```
2. Navegue até o diretório do projeto:
    ```sh
    cd project-prodify
    ```
3. Construa o projeto usando Maven:
    ```sh
    mvn clean install
    ```

### Executando a Aplicação
Instruções para executar a aplicação:
1. Inicie a aplicação Spring Boot:
    ```sh
    mvn spring-boot:run
    ```

## API Endpoints

### OrderController

#### Criar Pedido
**Endpoint:** `POST /orders`

**Request JSON:**
```json
{
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

### ProductController

#### Criar Produto
**Endpoint:** `POST /products`

**Request JSON:**
```json
{
  "name": "Product Name",
  "price": 50.00,
  "stock": 100
}
```

**Response JSON:**
```json
{
  "message": "Product created successfully",
  "name": "Product Name",
  "price": 50.00,
  "stock": 100,
  "creationDate": "2025-02-02T19:32:11.2839509",
  "modificationDate": "2025-02-02T19:32:11.2839509",
  "status": true,
  "sku": "41c8d18d"
}
```

### Orders

#### Criar Pedido

**Request:**

`http://localhost:8080/orders`

**Request Body:**
```json
{
  "items": [
    {
      "productId": "1",
      "quantity": 1
    }
  ]
}
```

**Response:**
```json
{
  "message": "Order created successfully",
  "id": 1,
  "items": [
    {
      "sku": "dae1b6fa",
      "quantity": 1,
      "productName": "Product Name",
      "subtotal": 50.00
    }
  ],
  "total": 50.00,
  "purchaseDate": "2025-02-02T19:51:25.0441231"
}
```

#### Criar Pedido com Múltiplos Produtos

**Request Body:**
```json
{
  "items": [
    {
      "productId": "3",
      "quantity": 2
    },
    {
      "productId": "4",
      "quantity": 5
    }
  ]
}
```

**Response:**
```json
{
  "message": "Order created successfully",
  "id": 2,
  "items": [
    {
      "sku": "dae1b6fa",
      "quantity": 2,
      "productName": "Product Name",
      "subtotal": 100.00
    },
    {
      "sku": "c8f15232",
      "quantity": 5,
      "productName": "Product Name 2",
      "subtotal": 250.00
    }
  ],
  "total": 350.00,
  "purchaseDate": "2025-02-02T19:52:22.9709112"
}
```

#### Buscar Pedido por SKU

**Request:**

`http://localhost:8080/orders/SKU/{sku}`

**Exemplo:**

`http://localhost:8080/orders/SKU/dae1b6fa`

**Response:**
```json
{
  "content": [
    {
      "message": "Orders found successfully",
      "id": 1,
      "items": [
        {
          "sku": "dae1b6fa",
          "quantity": 1,
          "productName": "Product Name",
          "subtotal": 50.00
        }
      ],
      "total": 50.00,
      "purchaseDate": "2025-02-02T19:51:25.044123"
    }
  ],
  "totalElements": 1
}
```
## Creator

**Filipe Santana Cordeiro**  
**Backend Developer**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/filipesantanacordeiro/)
[![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/Filipescordeiro2)


