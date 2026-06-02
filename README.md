# Gerador de Nota Fiscal - Aplicação para Cálculo de Tributos e Fluxos de Pós-Venda

Este projeto é uma aplicação Spring Boot 3 desenvolvida em Java 21 voltada para o cálculo de tributos de notas fiscais e a orquestração resiliente e assíncrona de fluxos secundários pós-venda (Estoque, Registro, Entrega e Financeiro).

## 🛠️ Tecnologias Utilizadas

- **Java 21** (Uso de Virtual Threads habilitado implicitamente por recursos de concorrência)
- **Spring Boot 3.3.0**
- **Spring AOP** (Aspect-Oriented Programming para interceptação de falhas)
- **Resilience4j 2.2.0** (Mecanismo de Retry, Backoff Exponencial e Jitter)
- **Lombok**
- **Maven**

---

## 🚀 Arquitetura de Resiliência e Falhas

Para garantir o padrão de resiliência exigido em sistemas de missão crítica e alta disponibilidade, a aplicação implementa o padrão **Retry com Backoff Exponencial, Jitter e Fallback Individual (DLQ)**.

### Características do Design:
1. **Isolamento de Falhas:** O `@Retry` e o `@Async` foram desacoplados e alocados na entrada pública de cada `Service` de integração. Se o sistema financeiro falhar, o estoque e a entrega continuam funcionando independentemente.
2. **Backoff Exponencial & Jitter:** Evita o efeito de manada (thundering herd) em APIs parceiras instáveis, aplicando tempos de espera progressivos combinados a uma variação aleatória.
3. **Contingência Tardia (Dead Letter Queue):** Caso as 3 retentativas falhem, o método de `fallback` captura a exceção de forma controlada e simula a postagem do payload em filas SQS específicas de contingência para reprocessamento assíncrono futuro.

---

## 🏃 Como Executar a Aplicação Localmente

### Pré-requisitos
- JDK 21 instalado e configurado nas variáveis de ambiente (`JAVA_HOME`).
- Maven instalado (ou utilize o wrapper `./mvnw`).

### Passo 1: Compilar e Instalar as Dependências
Abra o terminal na raiz do projeto e execute:
```bash
mvn clean install

./mvnw spring-boot:run