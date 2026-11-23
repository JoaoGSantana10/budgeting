package dio.budgeting.application.output;

import dio.budgeting.domain.Category;
import dio.budgeting.domain.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Representação imutável de uma transação para uso pela aplicação.
 *
 * <p>Separa dados de domínio da forma entregue por casos de uso e evita expor
 * diretamente objetos internos da entidade ou da infraestrutura.</p>
 *
 * @param id identificador da transação em formato textual
 * @param description descrição do gasto
 * @param category nome da categoria
 * @param value valor da transação convertido para apresentação
 */
public record TransactionOutput(String id, String description, String category, double value) {
    /**
     * Converte uma transação de domínio em seu DTO de saída.
     *
     * @param transaction objeto de domínio
     * @return representação pronta para JSON ou uso pelos casos de uso
     */
    public static TransactionOutput from(Transaction transaction) {
        return new TransactionOutput(
                transaction.getId().uuid().toString(),
                transaction.getDescription(),
                transaction.getCategory().name(),
                BigDecimal.valueOf(transaction.getAmount()).setScale(2, RoundingMode.HALF_UP).doubleValue());
    }
}
