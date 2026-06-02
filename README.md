# Gerador de Nota Fiscal - Projeto "ME BOOSTA"

Este projeto é uma solução robusta e escalável para o processamento de notas fiscais, corrigindo problemas de performance, inconsistência de dados e falta de observabilidade do legado.

## 🚀 Soluções Implementadas

- **Refatoração de Arquitetura**: Transformação de componentes *Singleton* em *Stateless*, eliminando o acúmulo de estado entre execuções.
- **Otimização de Performance**: Estratégia de *batching* e concorrência para lidar com o gargalo de integração simulada em pedidos com alto volume de itens.
- **Resiliência**: Implementação de *Retry Pattern* via `Resilience4j` para garantir robustez em integrações instáveis.
- **Observabilidade**:
    - Logs estruturados via *Logback*.
    - Métricas de latência e saúde via *Micrometer/Actuator* (Prometheus ready).
- **Modernização**: Atualização para **Java 21** e **Spring Boot 3.x**.
- **DevOps**: Dockerização com *Multi-stage build* pronta para deploy em nuvem (EKS/ECS).

## 🛠 Tecnologias
* Java 21
* Spring Boot 3.x
* Maven
* Resilience4j (Retry)
* Micrometer & Actuator (Prometheus)
* Docker

## 🚀 Como Executar

### Pré-requisitos
* Java 21 instalado.
* Docker (opcional, para ambiente conteinerizado).

### Execução local
1. Clone o repositório.
2. Compile o projeto: `mvn clean package`
3. Execute: `java -jar target/gerador-nota-fiscal-1.0.jar`

### Execução com Docker
1. Construa a imagem: `docker build -t gerador-nota-fiscal:1.0 .`
2. Rode o container: `docker run -p 8080:8080 gerador-nota-fiscal:1.0`

## 📊 Monitoramento (Actuator)
Após iniciar, você pode acompanhar a saúde e métricas em:
- **Health:** `http://localhost:8080/actuator/health`
- **Métricas:** `http://localhost:8080/actuator/prometheus`

## 🧪 Testes
Para executar a suíte de testes:
```bash
mvn test