# Comparator API - Desafio Backend Mercado Livre

## Visão Geral

API RESTful desenvolvida para fornecer detalhes de produtos para uso em um recurso de comparação de itens.  
A aplicação foi construída em Java 21 com Spring Boot, seguindo boas práticas de design, tratamento de erros, documentação e testes.

### Escolha da Stack Backend: 

Optei por usar Java 21 com Spring Boot devido à minha familiaridade consolidada com a linguagem e o framework, o que me permite desenvolver com mais rapidez e segurança, além de aproveitar os recursos modernos e melhorias de desempenho trazidos pela versão recente do Java. Para aumentar ainda mais a eficiência, integrei ferramentas de IA generativa (ChatGPT) ao fluxo de desenvolvimento, facilitando a prototipagem, revisão de código, esclarecimento de dúvidas técnicas e organização da documentação. Essa combinação garantiu um backend robusto, moderno e alinhado às melhores práticas atuais, entregando qualidade com agilidade.

---

## Design da API

### Modelo de Produto (`Product`)

- `id` (Long): Identificador único do produto.
- `name` (String): Nome do produto.
- `imageUrl` (String): URL da imagem do produto.
- `description` (String): Descrição detalhada.
- `price` (BigDecimal): Preço do produto.
- `classification` (String): Categoria do produto (ex: Eletronics, Furniture).
- `specifications` (Map<String, String>): Parâmetros flexíveis como cor, tamanho, etc.

### Endpoints principais

| Método | Endpoint            | Descrição                                 | Parâmetros                                                                                   | Retorno                     |
|--------|---------------------|-------------------------------------------|----------------------------------------------------------------------------------------------|-----------------------------|
| GET    | `/products`         | Lista produtos paginados e filtrados      | `page`, `size` (paginacao), filtros opcionais por atributos (`name`, `classification`, `price`, `description`) de `Product` via query params | Página com produtos filtrados |
| GET    | `/products/{id}`    | Busca produto por ID                       | `id` (path variable)                                                                         | Produto ou 404 se não encontrado |

### Filtragem

- Filtros opcionais via query string usando atributos existentes no produto (`name`, `classification`, `price`, `description`).
- Exemplo:  
```

GET /products?page=0\&size=10\&classification=Electronics\&price=777.99

````
- Filtros aplicados apenas se os atributos estejam presentes; caso contrário, lista todos os produtos paginados.

### Paginação

- Utiliza `Pageable` do Spring Data para facilitar paginação e ordenação.
- Parâmetros padrão: `page` (início em 0) e `size`.

---

## Tratamento de Erros

- Erros específicos, como produto não encontrado, retornam respostas JSON com código HTTP adequado e mensagens claras.
- Validação básica de parâmetros e tipos via handlers globais com respostas padronizadas.
- Log estruturado em JSON para facilitar análise.

---

## Cache

- Implementado cache com Spring Cache usando Caffeine para otimizar consultas filtradas e paginadas.
- Cache é aplicado em nível de serviço para minimizar leituras repetidas do arquivo JSON.

---

## Configuração e Execução

### Requisitos

- Java 21
- Maven 3.8+
- Docker & Docker Compose

### Rodando localmente

```bash
mvn clean package
java -jar target/comparator-0.0.1-SNAPSHOT.jar
````

### Rodando com Docker

1. Certifique-se que Docker e Docker Compose estão instalados.
2. Na raiz do projeto, execute:

```bash
docker-compose up --build
```

* O comando irá:

    * Construir o JAR fat com Maven
    * Construir a imagem Docker
    * Subir o container na porta 8080

3. Acesse `http://localhost:8080/swagger-ui.html` para documentação interativa da API.

---

## Decisões Arquitetônicas

* **Persistência local em JSON**: Para simplificar e seguir o requisito do desafio, o repositório carrega produtos de um arquivo JSON interno, evitando bancos reais.
* **Inicialização dos dados**: A lista de produtos foi carregada a partir de um arquivo JSON localizado na pasta de recursos, utilizando um bloco estático de inicialização na classe responsável pelo repositório. Essa decisão visa facilitar o carregamento inicial e garantir que o arquivo seja lido apenas uma vez durante a vida da aplicação, evitando leituras repetidas e custos desnecessários de I/O em cada consulta.
* **Design modular e coeso**: Serviços, controladores, utilitários e handlers são separados para facilitar manutenção e testes.
* **Filtragem dinâmica com reflexão**: Para permitir filtros genéricos por qualquer campo do produto, a filtragem usa reflexão para acessar campos dinamicamente.
* **Paginação via Spring Data Pageable**: Facilita integração futura com banco real ou outros repositórios.
* **Cache Caffeine**: Introduzido para demonstrar preocupação com performance, mesmo em uma base estática.
* **Cache aplicado na consulta filtrada e paginada**: Mesmo se tratando de uma aplicação que consulta dados em memória a partir de um JSON estático, foi implementado um cache usando Spring Cache com Caffeine para demonstrar preocupação com performance e boas práticas. Pelos testes realizados, as consultas repetidas com os mesmos filtros tiveram uma redução significativa no tempo de resposta, caindo de aproximadamente 15ms para entre 6 e 8ms — ou seja, uma redução de quase 50%. Isso mostra que mesmo em sistemas simples, a aplicação criteriosa de cache pode trazer ganhos reais, além de reduzir a carga computacional ao evitar processamento repetitivo.
* **Logging JSON estruturado**: Para facilitar integração com sistemas de monitoramento e observabilidade.
* **Documentação OpenAPI (Swagger)**: Para facilitar o uso e teste da API por outros desenvolvedores.

---

## Observações Finais

* Projeto preparado para facilitar extensão futura (ex: integração com banco, mais filtros, autenticação).
* Priorizei clareza, coesão e tratamento de erros robusto para garantir qualidade e boa experiência de uso da API.
* Código totalmente comentado para facilitar entendimento.

---

## Contato

Marcos Paulo – [LinkedIn](https://www.linkedin.com/in/marcos-paulo20/)

Email: [marcosp.silva280@gmail.com](mailto:marcosp.silva280@gmail.com)