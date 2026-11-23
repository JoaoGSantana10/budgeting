# Documentação detalhada

Este guia explica as responsabilidades das classes, os principais conceitos do
Spring usados no projeto e o fluxo de dados da aplicação.

## Índice

- [Arquitetura](#arquitetura)
- [Pacote raiz](#pacote-raiz)
- [Camada application](#camada-application)
- [Camada domain](#camada-domain)
- [Camada infrastructure](#camada-infrastructure)
- [Testes](#testes)
- [Anotações principais](#anotações-principais)

## Arquitetura

A organização segue uma separação por responsabilidades:

1. **domain** contém o modelo de negócio e a interface de persistência.
2. **application** coordena casos de uso e adapta dados de entrada/saída.
3. **infrastructure** liga o sistema HTTP, JPA, banco e APIs externas.

Essa organização evita que a regra de negócio dependa diretamente de MySQL,
Spring MVC ou Spring AI.

## Pacote raiz

### `BudgetingApplication`

Ponto de entrada da aplicação. `@SpringBootApplication` ativa componentes,
configurações automáticas e recursos web. O método `main` executa
`SpringApplication.run`.

O método `chatChatClient` cria um `ChatClient` e o registra como bean. Assim,
outros componentes podem receber o cliente por injeção de dependência.

### `ChatClientController`

Controlador didático que usa `ChatClient`, a API fluente do Spring AI:

```java
chatClient.prompt().user(prompt).call().content()
```

- `prompt()` inicia uma requisição;
- `user(prompt)` define a mensagem do usuário;
- `call()` executa a chamada;
- `content()` recupera o texto produzido.

### `ChatModelController`

Mostra uma chamada direta ao `OpenAiChatModel`. Diferentemente do ChatClient,
esta opção usa a abstração específica do modelo OpenAI.

### `TranscriptionController`

Recebe um arquivo multipart no endpoint `POST /api/transcribe`. O recurso enviado
é passado ao `TranscriptionModel`, que usa Whisper pela configuração OpenAI.

`@RequestParam("file")` liga o campo multipart chamado `file` ao parâmetro Java.

### `TextToSpeechController`

Recebe texto em JSON e usa `TextToSpeechModel` para produzir áudio MP3. A resposta
usa `ResponseEntity<Resource>` e define um nome de download.

`SynthesizeRequest` é um record que representa o JSON de entrada.

## Camada application

### `PersistTransactionUseCase`

Coordena o cadastro de uma transação. Recebe `PersistTransactionInput`, cria uma
entidade `Transaction`, usa o repositório de domínio e converte o resultado em
`TransactionOutput`.

O método é uma tool com nome `persist_transaction`. O modelo pode chamá-lo por
tool calling quando entende que o usuário deseja registrar um gasto.

### `ListTransactionsByCategoryUseCase`

Busca transações por categoria e converte cada item em `TransactionOutput`.

O método é uma tool chamada `list-transactions-by-category`. `@ToolParam`
explica ao modelo o significado do parâmetro `Category`.

### `PersistTransactionInput`

Record imutável usado pelos casos de uso. Contém:

- `description`: descrição do gasto;
- `amount`: valor em centavos;
- `category`: uma das categorias do enum.

Cada campo possui `@ToolParam`, permitindo que o modelo entenda como preencher
os dados ao executar a tool.

### `TransactionOutput`

Objeto imutável usado como saída da aplicação. Ao contrário da entidade JPA,
ele não expõe detalhes internos da infraestrutura.

O método `from` converte `Transaction` em `TransactionOutput`.

## Camada domain

### `Category`

Enum de categorias suportadas:

- `GROCERIES`;
- `PHARMA`;
- `AUTO`.

Por ser fechado, impede valores desconhecidos e facilita a validação automática
em parâmetros HTTP e tool calling.

### `Transaction`

Entidade de domínio que representa um gasto. Contém identificador, descrição,
valor em centavos e categoria.

Anotações Lombok:

- `@Getter` cria automaticamente os métodos `getId`, `getDescription`, `getAmount`
  e `getCategory`;
- `@AllArgsConstructor` cria o construtor com todos os campos.

O construtor alternativo cria o identificador com `TransactionId` antes da
persistência.

### `TransactionId`

Record que encapsula o `UUID` da transação. A vantagem do record é impedir que
o código se esqueça de gerar um identificador válido em uma nova transação.

### `TransactionRepository`

Interface que define o contrato de persistência sem mencionar JPA ou bancos
concretos. Isso permite trocar a implementação sem alterar os casos de uso.

## Camada infrastructure

### `TransactionController`

Controlador REST principal em `/transactions`.

`POST /transactions` usa `TransactionRequest` para criar uma transação.
`GET /transactions/{category}` consulta por categoria.
`POST /transactions/ai` executa o fluxo composto por áudio.

No método de áudio:

1. `TranscriptionModel` converte o arquivo em texto;
2. `ChatClient` interpreta o texto;
3. tools registradas podem persistir ou consultar dados;
4. `TextToSpeechModel` converte a resposta em MP3.

No construtor, `@Value("classpath:prompts/system-message.st")` carrega o prompt
de sistema. `defaultTools` registra os casos de uso disponíveis ao modelo.

### `TransactionRequest`

Record recebido no JSON de cadastro. O método `toInput` transforma o formato HTTP
no formato esperado pelo caso de uso.

### `TransactionResponse`

Record retornado pela API. O método `from` cria a resposta a partir de
`TransactionOutput`.

### `TransactionEntity`

Entidade JPA ligada à tabela de transações.

Anotações:

- `@Entity`: informa ao JPA que é uma entidade persistente;
- `@Id`: define a chave primária;
- `@Enumerated(EnumType.STRING)`: salva o enum como texto no banco;
- `@Data`: gera getters, setters, `equals`, `hashCode` e `toString`;
- `@NoArgsConstructor`: cria o construtor vazio exigido pelo JPA;
- `@AllArgsConstructor`: cria o construtor usado nas conversões.

Os métodos `from` e `toDomain` fazem a tradução entre infraestrutura e domínio.

### `JpaTransactionRepository`

Implementação da interface `TransactionRepository`. É anotada com `@Repository`,
componente Spring para acesso a dados.

Ela recebe um `TransactionEntityRepository`, converte para JPA e transforma o
resultado novamente em entidades de domínio.

### `TransactionEntityRepository`

Interface do Spring Data. `CrudRepository<TransactionEntity, UUID>` fornece
operações básicas de CRUD.

O método `findAllByCategory(Category category)` é derivado automaticamente pela
nomeação. O Spring gera a consulta SQL pela combinação do nome do método e do
parâmetro.

## Testes

### `BudgetingApplicationTests`

Verifica se o contexto Spring inicia sem erros.

### `OpenAiChatClientIT`

Testa uma operação matemática simples via ChatClient.

### `OpenAiChatModelIT`

Constrói explicitamente um `OpenAiChatModel` e verifica se uma chamada retorna texto.

### `OpenAiTranscriptionModelIT`

Testa vários arquivos de áudio em português e verifica palavras esperadas.
`@ParameterizedTest` e `@CsvSource` fornecem os casos de teste.

### `OpenAiSpeechModelModelIT`

Gera áudio a partir de texto e confirma que o resultado tem tamanho maior que
1024 bytes.

### `ToolCallingIT`

Registra ferramentas matemáticas no ChatClient e verifica se o modelo escolhe e
encadeia as tools para responder ao prompt.

## Anotações principais

### Spring Boot e componentes

- `@SpringBootApplication`: inicia o projeto Spring Boot.
- `@Bean`: registra um objeto criado manualmente no contêiner do Spring.
- `@Service`: marca um caso de uso gerenciado pelo Spring.
- `@Repository`: marca um componente de acesso a dados.
- `@RestController`: expõe uma classe como controlador REST.

### Spring MVC

- `@RequestMapping`: define prefixo ou caminho base.
- `@GetMapping`: trata requisições GET.
- `@PostMapping`: trata requisições POST.
- `@RequestBody`: converte o JSON do corpo em objeto Java.
- `@RequestParam`: lê parâmetro de consulta ou multipart.
- `@PathVariable`: lê valor diretamente da URL.
- `@ResponseStatus`: define o status HTTP padrão.
- `@Value`: injeta configuração ou recurso em uma propriedade.

### Spring AI

- `@Tool`: permite que o modelo invoque um método Java.
- `@ToolParam`: descreve o significado de um parâmetro da tool.
- `ChatClient`: API fluente para prompts e chamadas de modelo.
- `OpenAiChatModel`: conexão direta com o modelo de chat.
- `TranscriptionModel`: abstração para converter áudio em texto.
- `TextToSpeechModel`: abstração para converter texto em áudio.

### JPA

- `@Entity`: declara uma entidade persistente.
- `@Id`: marca a chave primária.
- `@Enumerated(EnumType.STRING)`: persiste um enum pelo nome textual.

### Lombok

- `@Getter`: gera getters.
- `@Data`: gera getters, setters e métodos utilitários.
- `@AllArgsConstructor`: gera construtor com todos os campos.
- `@NoArgsConstructor`: gera construtor sem argumentos.

## Valores e unidades

O projeto representa o valor monetário como `long` em centavos. Essa escolha
evita problemas de precisão que podem ocorrer em operações financeiras feitas
com ponto flutuante.
