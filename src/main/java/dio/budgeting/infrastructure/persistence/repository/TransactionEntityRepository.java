package dio.budgeting.infrastructure.persistence.repository;

import dio.budgeting.domain.Category;
import dio.budgeting.domain.Transaction;
import dio.budgeting.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;


/**
 * Repositório Spring Data para operações CRUD sobre {@link TransactionEntity}.
 *
 * <p>{@code CrudRepository} fornece operações básicas, como salvar e localizar.
 * O método derivado {@code findAllByCategory} é implementado automaticamente pelo
 * Spring Data a partir do nome e dos parâmetros declarados.</p>
 */
public interface TransactionEntityRepository extends CrudRepository<TransactionEntity, UUID> {
    /**
     * Busca todas as entidades da categoria informada.
     *
     * @param category categoria usada pelo Spring Data como filtro
     * @return entidades encontradas
     */
    List<TransactionEntity> findAllByCategory(Category category);
}
