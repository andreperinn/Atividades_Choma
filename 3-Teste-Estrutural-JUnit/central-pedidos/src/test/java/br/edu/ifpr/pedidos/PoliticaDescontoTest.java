package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PoliticaDescontoTest {

    private final PoliticaDesconto politica = new PoliticaDesconto();

    @Test
    void lancaExcecaoQuandoSubtotalNegativo() {
        Cliente cliente = new Cliente(false, false, 0);

        assertThrows(IllegalArgumentException.class, () -> politica.calcular(cliente, -1, null));
    }

    @Test
    void vipSemCupomRecebeDezPorCentoSemLimite() {
        Cliente cliente = new Cliente(true, false, 0);

        long desconto = politica.calcular(cliente, 100_000, null);

        assertEquals(10_000L, desconto);
    }

    @Test
    void comumComSubtotalAbaixoDoLimiteNaoTemDesconto() {
        Cliente cliente = new Cliente(false, false, 0);

        long desconto = politica.calcular(cliente, 49_999, null);

        assertEquals(0L, desconto);
    }

    @Test
    void comumComSubtotalNoLimiteRecebeCincoPorCento() {
        Cliente cliente = new Cliente(false, false, 0);

        long desconto = politica.calcular(cliente, 50_000, null);

        assertEquals(2_500L, desconto);
    }

    @Test
    void cupomEmBrancoMantemDescontoBaseSemAplicarTeto() {
        Cliente cliente = new Cliente(true, false, 0);

        long desconto = politica.calcular(cliente, 10_000, "   ");

        assertEquals(1_000L, desconto);
    }

    @Test
    void cupomBemVindoNormalizadoComEspacosEMinusculasSomaVinteReais() {
        Cliente cliente = new Cliente(false, false, 0);

        long desconto = politica.calcular(cliente, 10_000, " bemvindo ");

        assertEquals(2_000L, desconto);
    }

    @Test
    void cupomBemVindoNaoElegivelPorTerComprasAnteriores() {
        Cliente cliente = new Cliente(false, false, 1);

        long desconto = politica.calcular(cliente, 10_000, "BEMVINDO");

        assertEquals(0L, desconto);
    }

    @Test
    void cupomBemVindoNaoElegivelPorSubtotalAbaixoDoLimite() {
        Cliente cliente = new Cliente(false, false, 0);

        long desconto = politica.calcular(cliente, 9_999, "BEMVINDO");

        assertEquals(0L, desconto);
    }

    @Test
    void cupomExtra10ElegivelSomaDezPorCento() {
        Cliente cliente = new Cliente(false, false, 5);

        long desconto = politica.calcular(cliente, 20_000, "EXTRA10");

        assertEquals(2_000L, desconto);
    }

    @Test
    void cupomExtra10NaoElegivelPorSubtotalAbaixoDoLimite() {
        Cliente cliente = new Cliente(false, false, 5);

        long desconto = politica.calcular(cliente, 19_999, "EXTRA10");

        assertEquals(0L, desconto);
    }

    @Test
    void cupomDesconhecidoLancaExcecao() {
        Cliente cliente = new Cliente(false, false, 0);

        assertThrows(IllegalArgumentException.class, () -> politica.calcular(cliente, 10_000, "NATAL"));
    }

    @Test
    void descontoCombinadoEhLimitadoAVintePorCentoDoSubtotal() {
        Cliente cliente = new Cliente(true, false, 0);

        long desconto = politica.calcular(cliente, 10_000, "BEMVINDO");

        assertEquals(2_000L, desconto);
    }

    @Test
    void truncaDescontoPercentualParaBaixo() {
        Cliente cliente = new Cliente(true, false, 0);

        long desconto = politica.calcular(cliente, 333, null);

        assertEquals(33L, desconto);
    }
}
