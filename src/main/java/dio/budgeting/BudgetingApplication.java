package dio.budgeting;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Classe principal que inicia a aplicação Spring Boot.
 *
 * <p>{@code @SpringBootApplication} combina três configurações: busca componentes
 * para injeção de dependência, ativa configurações automáticas do Spring Boot e
 * habilita a configuração de aplicações web.</p>
 *
 * <p>Esta classe também cria um {@link ChatClient}, um cliente de alto nível do
 * Spring AI que usa o {@link ChatClient.Builder} configurado pelo framework.</p>
 */
@SpringBootApplication
public class BudgetingApplication {

    /**
     * Cria o cliente padrão usado pelas conversas com modelos de IA.
     *
     * <p>{@code @Bean} informa ao Spring que o objeto retornado deve ser gerenciado
     * pelo contêiner e ficar disponível para classes que o solicitarem por injeção
     * de dependência.</p>
     *
     * @param builder construtor fornecido pelo Spring AI
     * @return cliente de chat pronto para ser utilizado
     */
    @Bean
    ChatClient chatChatClient(ChatClient.Builder builder) {
        return builder.build();
    }

    /**
     * Ponto de entrada da aplicação.
     *
     * @param args argumentos informados na linha de comando
     */
    public static void main(String[] args) {
        SpringApplication.run(BudgetingApplication.class, args);
    }

}
