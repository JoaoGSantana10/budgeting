package dio.budgeting;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Demonstra uma chamada direta ao modelo de chat da OpenAI.
 *
 * <p>{@code @RestController} expõe endpoints HTTP e {@code @RequestMapping("/api")}
 * define o prefixo compartilhado por este controlador.</p>
 */
@RestController
@RequestMapping("/api")
public class ChatModelController {
    private final OpenAiChatModel openAiChatModel;

    public ChatModelController(OpenAiChatModel openAiChatModel) {
        this.openAiChatModel = openAiChatModel;
    }

    /**
     * Envia o prompt diretamente ao modelo configurado e devolve a resposta.
     *
     * @param prompt mensagem enviada pelo cliente
     * @return texto produzido pelo {@link OpenAiChatModel}
     */
    @GetMapping("/chat-model")
    String chat(String prompt) {
        return this.openAiChatModel.call(prompt);
    }
}
