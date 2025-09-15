# Desafio Backend - Requisitos

## 1. Validações

Você deve ajustar as entidades (model e sql) de acordo com as regras abaixo: 

- `Product.name` é obrigatório, não pode ser vazio e deve ter no máximo 100 caracteres.
- `Product.description` é opcional e pode ter no máximo 255 caracteres.
- `Product.price` é obrigatório deve ser > 0.
- `Product.status` é obrigatório.
- `Product.category` é obrigatório.
- `Category.name` deve ter no máximo 100 caracteres.
- `Category.description` é opcional e pode ter no máximo 255 caracteres.

## 2. Otimização de Performance
- Analisar consultas para identificar possíveis gargalos.
- Utilizar índices e restrições de unicidade quando necessário.
- Implementar paginação nos endpoints para garantir a escala conforme o volume de dados crescer.
- Utilizar cache com `Redis` para o endpoint `/auth/context`, garantindo que a invalidação seja feita em caso de alteração dos dados.

## 3. Logging
- Registrar logs em arquivos utilizando um formato estruturado (ex.: JSON).
- Implementar níveis de log: DEBUG, INFO, WARNING, ERROR, CRITICAL.
- Utilizar logging assíncrono.
- Definir estratégias de retenção e compressão dos logs.

## 4. Refatoração
- Atualizar a entidade `Product`:
  - Alterar o atributo `code` para o tipo inteiro.
- Versionamento da API:
  - Manter o endpoint atual (v1) em `/api/products` com os códigos iniciados por `PROD-`.
  - Criar uma nova versão (v2) em `/api/v2/products` onde `code` é inteiro.

## 5. Integração com Swagger
- Documentar todos os endpoints com:
  - Descrições detalhadas.
  - Exemplos de JSON para requisições e respostas.
  - Listagem de códigos HTTP e mensagens de erro.

## 6. Autenticação e Gerenciamento de Usuários
- Criar a tabela `users` com as colunas:
  - `id` (chave primária com incremento automático)
  - `name` (obrigatório)
  - `email` (obrigatório, único e com formato válido)
  - `password` (obrigatório)
  - `role` (obrigatório e com valores permitidos: `admin` ou `user`)
- Inserir um usuário admin inicial:
  - Email: `contato@simplesdental.com`
  - Password: `KMbT%5wT*R!46i@@YHqx`
- Endpoints:
  - `POST /auth/login` - Realiza login.
  - `POST /auth/register` - Registra novos usuários (se permitido).
  - `GET /auth/context` - Retorna `id`, `email` e `role` do usuário autenticado.
  - `PUT /users/password` - Atualiza a senha do usuário autenticado.

## 7. Permissões e Controle de Acesso
- Usuários com `role` admin podem criar, alterar, consultar e excluir produtos, categorias e outros usuários.
- Usuários com `role` user podem:
  - Consultar produtos e categorias.
  - Atualizar apenas sua própria senha.
  - Não acessar ou alterar dados de outros usuários.

## 8. Testes
- Desenvolver testes unitários para os módulos de autenticação, autorização e operações CRUD.

---

# Perguntas

1. **Se tivesse a oportunidade de criar o projeto do zero ou refatorar o projeto atual, qual arquitetura você utilizaria e por quê?**
2. **Qual é a melhor estratégia para garantir a escalabilidade do código mantendo o projeto organizado?**  
3. **Quais estratégias poderiam ser utilizadas para implementar multitenancy no projeto?**
4. **Como garantir a resiliência e alta disponibilidade da API durante picos de tráfego e falhas de componentes?**
5. **Quais práticas de segurança essenciais você implementaria para prevenir vulnerabilidades como injeção de SQL e XSS?**
5. **Qual a abordagem mais eficaz para estruturar o tratamento de exceções de negócio, garantindo um fluxo contínuo desde sua ocorrência até o retorno da API?**
5. **Considerando uma aplicação composta por múltiplos serviços, quais componentes você considera essenciais para assegurar sua robustez e eficiência?**
6. **Como você estruturaria uma pipeline de CI/CD para automação de testes e deploy, assegurando entregas contínuas e confiáveis?**

# Respostas

## 1. Arquitetura
Se eu tivesse a oportunidade de criar o projeto do zero ou refatorar o atual, optaria por uma **arquitetura modular**, começando com um **monolito modular** se o projeto for pequeno ou médio, e evoluindo para **microserviços** caso o sistema cresça.  
Isso garante organização, separação de responsabilidades e facilidade de manutenção, além de permitir escalabilidade horizontal conforme a necessidade.

## 2. Escalabilidade e organização
Para garantir escalabilidade mantendo o código organizado, eu aplicaria:
- Camadas bem definidas (Controller, Service, Repository, Domain);
- Uso de **DTOs** para comunicação entre camadas;
- **Cache** para otimizar acesso a dados frequentes;
- Processamento assíncrono e filas para operações de longa duração;
- Padrões de projeto (Strategy, Factory, Dependency Injection) para reduzir acoplamento.

## 3. Multitenancy
As estratégias mais comuns para multitenancy incluem:
- **Banco por tenant**, para isolamento total;
- **Schema por tenant**, mantendo banco único, mas com schemas separados;
- **Shared schema com coluna tenant_id**, mais simples, mas exige cuidado em queries e validações.

## 4. Resiliência e alta disponibilidade
Para manter a API disponível mesmo durante picos ou falhas:
- **Load balancer** para distribuir o tráfego;
- **Circuit breaker e retry** (Resilience4j, Hystrix) para isolar falhas;
- **Replicação de banco de dados e clusters**;
- **Auto-scaling** em servidores e serviços críticos.

## 5. Segurança
Práticas essenciais para prevenir vulnerabilidades:
- **Prepared statements / ORM** para evitar SQL Injection;
- **Validação e sanitização de inputs** para XSS;
- **Autenticação e autorização robustas** (JWT, OAuth2);
- **Criptografia de dados sensíveis** e HTTPS/TLS;
- **Rate limiting** e monitoramento de endpoints críticos.

## 6. Tratamento de exceções de negócio
- Criar **exceções customizadas** para regras de negócio;
- Usar um **Global Exception Handler** (`@ControllerAdvice`) para padronizar respostas;
- Retornar **DTOs padronizados** com status, mensagem e timestamp;
- Garantir que o fluxo continue de forma previsível mesmo após erros.

## 7. Componentes essenciais para múltiplos serviços
- **API Gateway** para roteamento e autenticação;
- **Service Registry** (Eureka, Consul) para descoberta de serviços;
- **Mensageria** (Kafka, RabbitMQ) para comunicação assíncrona;
- **Monitoramento centralizado** (Prometheus, Grafana);
- **Circuit breaker e retry** para tolerância a falhas.

## 8. CI/CD
Uma pipeline confiável incluiria:
- **Build automático** em cada commit ou pull request;
- **Testes automatizados** (unitários, integração e E2E);
- **Análise estática de código** (SonarQube, linters);
- **Deploy automatizado** com rollback em caso de falha;
- Uso de **Docker e Kubernetes** para consistência de ambiente e escalabilidade.

