package dio.budgeting.infrastructure.persistence.entity;

import dio.budgeting.domain.Category;
import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Entidade JPA correspondente à tabela que armazena transações.
 *
 * <p>{@code @Entity} informa ao JPA que esta classe representa dados persistentes.
 * {@code @Id} identifica a chave primária. {@code @Enumerated(EnumType.STRING)}
 * grava a categoria como texto, em vez de número interno. {@code @Data} e os
 * construtores são fornecidos pelo Lombok para reduzir código repetitivo.</p>
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {
    @Id
    private UUID id;
    private String description;
    private long amount;

    @Enumerated(EnumType.STRING)
    private Category category;

    public static TransactionEntity from(Transaction transaction) {
       return new TransactionEntity(
                transaction.getId().uuid(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getCategory()
       );
    }

    /**
     * Converte esta entidade JPA em uma entidade de domínio.
     *
     * @return transação no modelo usado pelas regras de negócio
     */
    public Transaction toDomain() {
        return new Transaction(
                new TransactionId(this.id),
                this.description,
                this.amount,
                this.category
        );
    }
}
    /**
     * Converte uma entidade de domínio em uma entidade JPA.
     *
     * @param transaction objeto de domínio
     * @return representação persistível
     */
