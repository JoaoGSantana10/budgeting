package dio.budgeting;

import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Converte arquivos de áudio enviados por HTTP em texto transcrito.
 *
 * <p>{@code @RestController} expõe o endpoint como recurso web.
 * {@code @PostMapping} aceita uma requisição multipart e
 * {@code @RequestParam("file")} identifica o arquivo enviado pelo cliente.</p>
 */
@RestController
@RequestMapping("/api")
public class TranscriptionController {
    private final TranscriptionModel transcriptionModel;

    public TranscriptionController(TranscriptionModel transcriptionModel) {
        this.transcriptionModel = transcriptionModel;
    }

    /**
     * Recebe um áudio, encaminha-o ao modelo de transcrição e devolve o texto.
     *
     * @param file arquivo enviado pelo campo multipart chamado {@code file}
     * @return texto reconhecido pelo modelo
     */
    @PostMapping(value = "/transcribe", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String transcribe(@RequestParam("file")MultipartFile file) {
        var resource = file.getResource();
        return transcriptionModel.transcribe(resource);
    }
}
