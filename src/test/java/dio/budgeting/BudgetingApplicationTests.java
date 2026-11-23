package dio.budgeting;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Teste básico que verifica se o contexto Spring consegue ser criado.
 *
 * <p>{@code @SpringBootTest} carrega a configuração da aplicação. A ausência de
 * exceções em {@code contextLoads} indica que componentes e beans foram iniciados.</p>
 */
@SpringBootTest
class BudgetingApplicationTests {

    @Test
    void contextLoads() {
    }

}
