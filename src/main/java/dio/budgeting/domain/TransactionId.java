package dio.budgeting.domain;

import java.util.UUID;

/**
 * Identificador tipado de uma transação.
 *
 * <p>Encapsular o UUID evita misturar identificadores de tipos diferentes e deixa
 * explícito que o valor pertence a uma transação.</p>
 *
 * @param uuid valor único persistido no banco
 */
public record TransactionId(UUID uuid) {
    /**
     * Gera um identificador único para uma nova transação.
     */
    public TransactionId() {
        this(UUID.randomUUID());
    }
}
