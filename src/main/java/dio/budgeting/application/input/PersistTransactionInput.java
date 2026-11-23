package dio.budgeting.application.input;

import dio.budgeting.domain.Category;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * Dados de entrada usados para criar uma transação.
 *
 * <p>Um record é uma classe Java imutável que gera automaticamente construtor,
 * getters como métodos de componente e os métodos {@code toString}, {@code equals}
 * e {@code hashCode}. {@code @ToolParam} fornece descrições ao modelo de IA.</p>
 *
 * @param description texto que identifica o gasto
 * @param amount valor do gasto em centavos
 * @param category categoria financeira selecionada
 */
public record PersistTransactionInput(@ToolParam(description = "Descrição do gasto") String description,
                                      @ToolParam(description = "Valor do gasto (em centavos)") long amount,
                                      @ToolParam(description = "Categoria de uma transação") Category category) {
}
