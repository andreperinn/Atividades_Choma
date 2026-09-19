package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AnaliseRiscoTest {

    private final AnaliseRisco risco = new AnaliseRisco();

    @Test
    void lancaExcecaoQuandoTotalNegativo() {
        Cliente cliente = new Cliente(false, false, 0);

        assertThrows(IllegalArgumentException.class, () -> risco.avaliar(cliente, -1, false));
    }

    @Test
    void bloqueadoRetornaRecusadoIndependenteDoTotal() {
        Cliente cliente = new Cliente(false, true, 0);

        assertEquals("RECUSADO", risco.avaliar(cliente, 0, false));
    }

    @Test
    void semComprasAnterioresEComTotalAcimaDoLimiteRetornaRevisao() {
        Cliente cliente = new Cliente(false, false, 0);

        assertEquals("REVISAO", risco.avaliar(cliente, 100_001, false));
    }

    @Test
    void semComprasAnterioresEComTotalNoLimiteRetornaAprovado() {
        Cliente cliente = new Cliente(false, false, 0);

        assertEquals("APROVADO", risco.avaliar(cliente, 100_000, false));
    }

    @Test
    void semComprasAnterioresEExpressoRetornaRevisaoMesmoComTotalBaixo() {
        Cliente cliente = new Cliente(false, false, 0);

        assertEquals("REVISAO", risco.avaliar(cliente, 0, true));
    }

    @Test
    void semComprasAnterioresComTotalBaixoENaoExpressoRetornaAprovado() {
        Cliente cliente = new Cliente(false, false, 0);

        assertEquals("APROVADO", risco.avaliar(cliente, 50_000, false));
    }

    @Test
    void comComprasAnterioresEComTotalAcimaDoLimiteENaoVipRetornaRevisao() {
        Cliente cliente = new Cliente(false, false, 1);

        assertEquals("REVISAO", risco.avaliar(cliente, 500_001, false));
    }

    @Test
    void comComprasAnterioresEComTotalAcimaDoLimiteEVipRetornaAprovado() {
        Cliente cliente = new Cliente(true, false, 1);

        assertEquals("APROVADO", risco.avaliar(cliente, 500_001, false));
    }

    @Test
    void comComprasAnterioresEComTotalNoLimiteRetornaAprovado() {
        Cliente cliente = new Cliente(false, false, 1);

        assertEquals("APROVADO", risco.avaliar(cliente, 500_000, false));
    }
}
