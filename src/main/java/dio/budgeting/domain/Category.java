package dio.budgeting.domain;

/**
 * Categorias financeiras suportadas pelo projeto.
 *
 * <p>O enum define um conjunto fechado de valores válidos. Ele é usado tanto na
 * API quanto nas consultas ao banco, evitando categorias arbitrárias e
 * permitindo que o modelo de IA escolha uma das opções conhecidas.</p>
 */
public enum Category {
    GROCERIES,
    PHARMA,
    AUTO,
}
