package dio.budgeting.infrastructure.persistence.http;

import dio.budgeting.application.ListTransactionsByCategoryUseCase;
import dio.budgeting.application.PersistTransactionUseCase;
import dio.budgeting.domain.Category;
import dio.budgeting.infrastructure.persistence.http.request.TransactionRequest;
import dio.budgeting.infrastructure.persistence.http.response.TransactionResponse;
import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Controlador HTTP principal para transações financeiras e seu fluxo com IA.
 *
 * <p>{@code @RestController} expõe JSON e recursos HTTP. {@code @RequestMapping}
 * define o prefixo comum {@code /transactions}. O controlador integra transcrição,
 * ChatClient, ferramentas de aplicação e síntese de voz em um único fluxo.</p>
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final PersistTransactionUseCase persistTransactionUseCase;
    private final ListTransactionsByCategoryUseCase listTransactionsByCategoryUseCase;

    private final TranscriptionModel transcriptionModel;
    private final ChatClient chatClient;
    private final TextToSpeechModel textToSpeechModel;

    public TransactionController(PersistTransactionUseCase persistTransactionUseCase,
                                 ListTransactionsByCategoryUseCase listTransactionsByCategoryUseCase,
                                 TranscriptionModel transcriptionModel,
                                 @Value("classpath:prompts/system-message.st") Resource systemPrompt,
                                 ChatClient.Builder chatClientBuilder,
                                 TextToSpeechModel textToSpeechModel) throws IOException {
        this.persistTransactionUseCase = persistTransactionUseCase;
        this.listTransactionsByCategoryUseCase = listTransactionsByCategoryUseCase;
        this.transcriptionModel = transcriptionModel;
        this.chatClient = chatClientBuilder
                .defaultSystem(systemPrompt.getContentAsString(Charset.defaultCharset()))
                .defaultTools(persistTransactionUseCase, listTransactionsByCategoryUseCase)
                .build();
        this.textToSpeechModel = textToSpeechModel;
    }

    /**
     * Cadastra uma transação recebida no corpo JSON.
     *
     * <p>{@code @RequestBody} converte o JSON em {@link TransactionRequest}.
     * {@code @ResponseStatus(HttpStatus.CREATED)} indica o código HTTP 201.</p>
     *
     * @param request dados da nova transação
     * @return transação criada no formato HTTP
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransactions(@RequestBody TransactionRequest request) {
        var transaction = persistTransactionUseCase.execute(request.toInput());
        return TransactionResponse.from(transaction);
    }

    /**
     * Lista transações de uma categoria escolhida no caminho da URL.
     *
     * <p>{@code @PathVariable} converte o texto do caminho em um valor do enum
     * {@link Category}; valores fora do enum não são aceitos automaticamente.</p>
     *
     * @param category categoria solicitada
     * @return lista de respostas HTTP
     */
    @GetMapping("/{category}")
    public List<TransactionResponse> readTransactions(@PathVariable Category category){
        return listTransactionsByCategoryUseCase.execute(category).stream()
                .map(TransactionResponse::from)
                .toList();
    }

    /**
     * Executa o fluxo completo de áudio: transcrição, decisão da IA e resposta sonora.
     *
     * <p>Primeiro o Spring AI transforma o arquivo em texto. Depois o ChatClient usa
     * o prompt do sistema e as ferramentas registradas para interpretar a intenção.
     * Por fim, o texto é convertido em MP3.</p>
     *
     * @param file áudio enviado em formato multipart
     * @return áudio MP3 com a resposta da IA
     */
    @PostMapping(value = "/ai", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "audio/mp3")
    ResponseEntity<Resource> transcribe(@RequestParam("file") MultipartFile file) {
        var userMessage =  transcriptionModel.transcribe(file.getResource());
        var result = chatClient.prompt().user(userMessage).call().content();

        byte[] audio = textToSpeechModel.call(result);
        var resource = new ByteArrayResource(audio);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("audio.mp3")
                                .build()
                                .toString())
                .body(resource);
    }

}
    /**
     * Inicializa o controlador e configura o ChatClient.
     *
     * <p>A mensagem {@code system-message.st} define a personalidade do assistente.
     * {@code @Value} injeta o arquivo como recurso. Os dois casos de uso informados
     * em {@code defaultTools} podem ser chamados pelo modelo via tool calling.</p>
     */
