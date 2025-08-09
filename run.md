
# Instruções para Executar o Projeto

Este documento descreve os passos para executar a API de comparação de produtos localmente ou via Docker.

---

## Pré-requisitos

- **Java 21** instalado e configurado (para execução local)
- **Maven** instalado (para build local)
- **Docker** e **Docker Compose** instalados (para execução via container)

---

## Execução Local (sem Docker)

1. **Clone o repositório**

```bash
git clone https://github.com/marcospaulo7/meli-hackerrank
cd comparator
```

2. **Build do projeto**

```bash
mvn clean package
```

3. **Executar a aplicação**

```bash
java -jar target/comparator-0.0.1-SNAPSHOT.jar
```

4. A API estará disponível em: `http://localhost:8080`

---

## Execução via Docker

O projeto possui um Dockerfile configurado para gerar a imagem do container e um arquivo `docker-compose.yml` para facilitar a execução.

### Passos:

1. **Construir a imagem e subir o container**

Na raiz do projeto (onde está o Dockerfile e docker-compose.yml), execute:

```bash
docker-compose up --build
```

2. A aplicação ficará acessível em: `http://localhost:8080`

3. Para parar o container, use:

```bash
docker-compose down
```

---

## Testes

Após a aplicação estar rodando, você pode testar os endpoints via:

- **Navegador ou Postman**:  
  Exemplo:  
  `GET http://localhost:8080/products?page=0&size=10`

- **Swagger UI** disponível em:  
  `http://localhost:8080/swagger-ui.html`

- **Documentação OpenAPI JSON**:  
  `http://localhost:8080/api-docs`

---

## Observações

- O projeto utiliza cache para melhorar a performance das consultas.
- O carregamento inicial dos produtos é feito via bloco estático para evitar múltiplas leituras do arquivo JSON.
- Logs são configurados para saída em formato JSON para facilitar integração com ferramentas de observabilidade.

---

Caso tenha qualquer dúvida ou problema, entre em contato.