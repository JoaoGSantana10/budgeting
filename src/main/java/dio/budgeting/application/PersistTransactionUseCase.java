package dio.budgeting.application;

import dio.budgeting.application.input.PersistTransactionInput;
import dio.budgeting.application.output.TransactionOutput;
import dio.budgeting.domain.Category;
import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável por registrar uma nova transação financeira.
 *
 * <p>{@code @Service} marca a classe como componente da camada de aplicação.
 * O método de execução é também uma {@code @Tool}, permitindo que um modelo do
 * Spring AI o invoque a partir de uma entrada estruturada.</p>
 */
@Service
public class PersistTransactionUseCase {
    private final TransactionRepository transactionRepository;

    public PersistTransactionUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Cria uma entidade de domínio a partir da entrada e a envia ao repositório.
     *
     * @param input dados da transação informados pela IA ou pela API HTTP
     * @return representação de saída da transação salva
     */
    @Tool(name = "persist_transaction", description = "Persiste uma nova transação financeira")
    public TransactionOutput execute(PersistTransactionInput input) {
        var transaction = transactionRepository.save(
                new Transaction(input.description(), input.amount(), input.category()));

        return TransactionOutput.from(transaction);
    }
}
