package dio.budgeting.infrastructure.persistence.http.request;

import dio.budgeting.application.input.PersistTransactionInput;
import dio.budgeting.domain.Category;

/**
 * Corpo JSON aceito para cadastrar uma transação diretamente pela API.
 *
 * @param description descrição do gasto
 * @param category categoria da transação
 * @param amount valor em centavos
 */
public record TransactionRequest(String description, Category category, long amount) {
    /**
     * Converte o formato HTTP no formato de entrada dos casos de uso.
     *
     * @return entrada preparada para persistir a transação
     */
    public PersistTransactionInput toInput() {
        return new PersistTransactionInput(description, amount, category);
    }
}
