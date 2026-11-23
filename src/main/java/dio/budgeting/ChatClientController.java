package dio.budgeting;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Demonstra o uso de alto nível da API ChatClient do Spring AI.
 *
 * <p>{@code @RestController} transforma esta classe em um controlador HTTP:
 * cada resposta é escrita diretamente no corpo da resposta. {@code @RequestMapping}
 * define o prefixo comum {@code /api}.</p>
 */
@RestController
@RequestMapping("/api")
public class ChatClientController {
    private final ChatClient chatClient;

    public ChatClientController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * Envia um texto ao modelo de chat e devolve apenas o conteúdo gerado.
     *
     * <p>{@code @GetMapping} expõe o método em {@code GET /api/chat}.
     * A variável {@code prompt} é recebida como parâmetro de consulta. O encadeamento
     * {@code prompt().user(...).call()} monta a mensagem do usuário e executa a chamada.</p>
     *
     * @param prompt texto enviado ao modelo
     * @return resposta textual produzida pelo modelo
     */
    @GetMapping("/chat")
    String chat(String prompt) {
        return this.chatClient.prompt().user(prompt).call().content();
    }
}
