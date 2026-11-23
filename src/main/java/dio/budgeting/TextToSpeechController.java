package dio.budgeting;

import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Converte texto em um arquivo de áudio MP3 com o Speech API do Spring AI.
 *
 * <p>{@code @RestController} marca a classe como serviço web e
 * {@code @RequestMapping("/api")} define o prefixo dos endpoints.</p>
 */
@RestController
@RequestMapping("/api")
public class TextToSpeechController {
    private final TextToSpeechModel textToSpeechModel;

    public TextToSpeechController(TextToSpeechModel textToSpeechModel) {
        this.textToSpeechModel = textToSpeechModel;
    }

    /**
     * Gera o áudio correspondente ao texto recebido no corpo da requisição.
     *
     * <p>{@code @PostMapping} recebe dados por HTTP. O {@code produces} informa que
     * a resposta possui o tipo {@code audio/mp3}. {@code @RequestBody} transforma o
     * JSON da requisição no record {@link SynthesizeRequest}.</p>
     *
     * @param request objeto contendo o texto a ser convertido
     * @return resposta HTTP com o conteúdo MP3 anexado como arquivo
     */
    @PostMapping(value = "/sinthesize", produces = "audio/mp3")
    public ResponseEntity<Resource> sinthesize(@RequestBody SynthesizeRequest request) {
        byte[] audio = textToSpeechModel.call(request.text());
        var resource = new ByteArrayResource(audio);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("audio.mp3")
                                .build()
                                .toString())
                .body(resource);
    }

    /**
     * Dados JSON necessários para solicitar uma síntese de voz.
     *
     * @param text texto que será convertido em áudio
     */
    public record SynthesizeRequest(String text) {

    }

}
