package dio.budgeting.infrastructure.persistence.http.response;

import dio.budgeting.application.output.TransactionOutput;

import java.math.BigDecimal;

/**
 * Resposta HTTP usada para representar uma transação.
 *
 * @param id identificador da transação
 * @param category nome da categoria
 * @param description descrição do gasto
 * @param amount valor no formato utilizado pela API
 */
public record TransactionResponse(String id, String category, String description, double amount) {
    /**
     * Converte o objeto de saída da aplicação em uma resposta HTTP.
     *
     * @param output resultado produzido pelo caso de uso
     * @return resposta serializável em JSON
     */
    public static TransactionResponse from(TransactionOutput output) {
        return new TransactionResponse(
                output.id(),
                output.category(),
                output.description(),
                output.value()
        );
    }
}
