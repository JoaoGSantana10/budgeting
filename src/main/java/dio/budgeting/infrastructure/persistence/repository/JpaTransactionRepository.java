package dio.budgeting.infrastructure.persistence.repository;

import dio.budgeting.domain.Category;
import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionRepository;
import dio.budgeting.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Implementação JPA da porta de domínio {@link TransactionRepository}.
 *
 * <p>{@code @Repository} registra a classe como componente Spring especializado
 * em acesso a dados. Esta classe converte entidades JPA em entidades de domínio
 * e vice-versa, mantendo o domínio independente do Spring Data.</p>
 */
@Repository
public class JpaTransactionRepository implements TransactionRepository {
    private final TransactionEntityRepository transactionEntityRepository;

    public JpaTransactionRepository(TransactionEntityRepository transactionEntityRepository) {
        this.transactionEntityRepository = transactionEntityRepository;
    }

    /**
     * Salva a transação de domínio em JPA e converte o resultado.
     *
     * @param transaction transação a persistir
     * @return transação convertida para o modelo de domínio
     */
    @Override
    public Transaction save(Transaction transaction) {
        var entity = TransactionEntity.from(transaction);
        return transactionEntityRepository.save(entity).toDomain();
    }

    @Override
    public List<Transaction> findAllByCategory(Category category) {
        return transactionEntityRepository.findAllByCategory(category)
                .stream()
                .map(TransactionEntity::toDomain)
                .toList();
    }
}
