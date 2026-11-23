package dio.budgeting.domain;

import java.util.List;

/**
 * Porta de persistência usada pela camada de domínio e pelos casos de uso.
 *
 * <p>É uma interface, não uma implementação: o domínio declara quais operações
 * precisa e a infraestrutura fornece a implementação com JPA/MySQL. Isso reduz
 * o acoplamento entre regras de negócio e banco de dados.</p>
 */
public interface TransactionRepository {
    /**
     * Persiste uma transação e devolve a versão resultante.
     *
     * @param transaction transação a salvar
     * @return transação persistida
     */
    Transaction save(Transaction transaction);

    /**
     * Busca transações associadas a uma categoria.
     *
     * @param category categoria utilizada como filtro
     * @return transações encontradas
     */
    List<Transaction> findAllByCategory(Category category);
}
