package dio.budgeting.application;

import dio.budgeting.application.output.TransactionOutput;
import dio.budgeting.domain.Category;
import dio.budgeting.domain.TransactionRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Caso de uso que consulta transações financeiras por categoria.
 *
 * <p>{@code @Service} indica que a classe contém uma regra de aplicação gerenciada
 * pelo Spring. {@code @Tool} expõe o método ao modelo de linguagem para chamadas
 * automáticas; o modelo lê o nome e a descrição para decidir quando usar a ação.
 * {@code @ToolParam} explica o significado do parâmetro recebido pela IA.</p>
 */
@Service
public class ListTransactionsByCategoryUseCase {
    private final TransactionRepository transactionRepository;

    public ListTransactionsByCategoryUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Localiza todas as transações da categoria solicitada.
     *
     * @param category categoria usada como filtro
     * @return lista convertida para objetos voltados à saída da aplicação
     */
    @Tool(name = "list-transactions-by-category", description = "Lista transações financeiras por categoria")
    public List<TransactionOutput> execute(@ToolParam(description = "Categoria de uma transação") Category category) {
        return transactionRepository.findAllByCategory(category).stream().map(TransactionOutput::from).toList();
    }
}
