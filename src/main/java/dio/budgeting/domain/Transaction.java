package dio.budgeting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Entidade de domínio que representa uma transação financeira.
 *
 * <p>O código usa {@code @Getter} do Lombok para gerar os métodos de leitura e
 * {@code @AllArgsConstructor} para gerar um construtor com todos os campos.
 * O construtor específico cria o identificador automaticamente com UUID.</p>
 */
@Getter
@AllArgsConstructor
public class Transaction {
    private TransactionId id;
    private String description;
    private long amount;
    private Category category;

    public Transaction(String description, long amount, Category category) {
        this.id = new TransactionId();
        this.description = description;
        this.amount = amount;
        this.category = category;
    }
}
    /**
     * Cria uma nova transação ainda não persistida e gera seu identificador.
     *
     * @param description descrição do gasto
     * @param amount valor em centavos
     * @param category categoria do gasto
     */
