package br.edu.ifpr.pedidos;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculadoraFreteTest {

    private final CalculadoraFrete calculadora = new CalculadoraFrete();

    private Pedido pedidoComPeso(String uf, boolean expresso, int pesoGramas) {
        ItemPedido item = new ItemPedido("SKU1", 1_000, 1, 5, pesoGramas, false);
        return new Pedido(List.of(item), uf, expresso, null);
    }

    @Test
    void lancaExcecaoQuandoLiquidoNegativo() {
        Cliente cliente = new Cliente(false, false, 0);
        Pedido pedido = pedidoComPeso("PR", false, 500);

        assertThrows(IllegalArgumentException.class, () -> calculadora.calcular(pedido, cliente, -1));
    }

    @Test
    void baseParanaComPesoAbaixoDoLimite() {
        Cliente cliente = new Cliente(false, false, 0);
        Pedido pedido = pedidoComPeso("PR", false, 500);

        assertEquals(1_200L, calculadora.calcular(pedido, cliente, 1_000));
    }

    @Test
    void baseSaoPaulo() {
        Cliente cliente = new Cliente(false, false, 0);
        Pedido pedido = pedidoComPeso("SP", false, 500);

        assertEquals(2_000L, calculadora.calcular(pedido, cliente, 1_000));
    }

    @Test
    void baseRioDeJaneiro() {
        Cliente cliente = new Cliente(false, false, 0);
        Pedido pedido = pedidoComPeso("RJ", false, 500);

        assertEquals(2_000L, calculadora.calcular(pedido, cliente, 1_000));
    }

    @Test
    void baseOutraUfUsaTarifaPadrao() {
        Cliente cliente = new Cliente(false, false, 0);
        Pedido pedido = pedidoComPeso("MG", false, 500);

        assertEquals(3_000L, calculadora.calcular(pedido, cliente, 1_000));
    }

    @Test
    void pesoExatamenteNoLimiteNaoAdicionaExcedente() {
        Cliente cliente = new Cliente(false, false, 0);
        Pedido pedido = pedidoComPeso("PR", false, 2_000);

        assertEquals(1_200L, calculadora.calcular(pedido, cliente, 1_000));
    }

    @Test
    void pesoComFracaoAcimaDoLimiteCobraUmaFaixaInteira() {
        Cliente cliente = new Cliente(false, false, 0);
        Pedido pedido = pedidoComPeso("PR", false, 2_500);

        assertEquals(1_500L, calculadora.calcular(pedido, cliente, 1_000));
    }

    @Test
    void pesoComVariasFaixasSomaTodasAsIteracoes() {
        Cliente cliente = new Cliente(false, false, 0);
        Pedido pedido = pedidoComPeso("PR", false, 5_000);

        assertEquals(2_100L, calculadora.calcular(pedido, cliente, 1_000));
    }

    @Test
    void isencaoZeraFreteQuandoLiquidoAltoENaoExpresso() {
        Cliente cliente = new Cliente(false, false, 0);
        Pedido pedido = pedidoComPeso("PR", false, 5_000);

        assertEquals(0L, calculadora.calcular(pedido, cliente, 30_000));
    }

    @Test
    void isencaoNaoSeAplicaQuandoEntregaEhExpressa() {
        Cliente cliente = new Cliente(false, false, 0);
        Pedido pedido = pedidoComPeso("PR", true, 5_000);

        assertEquals(3_600L, calculadora.calcular(pedido, cliente, 30_000));
    }

    @Test
    void isencaoNaoSeAplicaQuandoLiquidoAbaixoDoLimite() {
        Cliente cliente = new Cliente(false, false, 0);
        Pedido pedido = pedidoComPeso("PR", false, 5_000);

        assertEquals(2_100L, calculadora.calcular(pedido, cliente, 29_999));
    }

    @Test
    void vipPagaMetadeDoFrete() {
        Cliente cliente = new Cliente(true, false, 0);
        Pedido pedido = pedidoComPeso("SP", false, 500);

        assertEquals(1_000L, calculadora.calcular(pedido, cliente, 1_000));
    }

    @Test
    void vipComFreteIsentoContinuaZerado() {
        Cliente cliente = new Cliente(true, false, 0);
        Pedido pedido = pedidoComPeso("PR", false, 5_000);

        assertEquals(0L, calculadora.calcular(pedido, cliente, 30_000));
    }

    @Test
    void expressoAdicionaValorFixo() {
        Cliente cliente = new Cliente(false, false, 0);
        Pedido pedido = pedidoComPeso("SP", true, 500);

        assertEquals(3_500L, calculadora.calcular(pedido, cliente, 1_000));
    }

    @Test
    void itemFragilAdicionaValorUmaUnicaVezMesmoComVariosItens() {
        Cliente cliente = new Cliente(false, false, 0);
        List<ItemPedido> itens = List.of(
            new ItemPedido("SKU1", 1_000, 1, 5, 100, true),
            new ItemPedido("SKU2", 1_000, 1, 5, 100, true)
        );
        Pedido pedido = new Pedido(itens, "SP", false, null);

        assertEquals(2_500L, calculadora.calcular(pedido, cliente, 1_000));
    }

    @Test
    void itemFragilInativoNaoAdicionaValor() {
        Cliente cliente = new Cliente(false, false, 0);
        Pedido pedido = new Pedido(
            List.of(new ItemPedido("SKU1", 1_000, 0, 5, 100, true)), "SP", false, null);

        assertEquals(2_000L, calculadora.calcular(pedido, cliente, 1_000));
    }

    @Test
    void adicionalDeItemFragilIncideMesmoComBaseZeradaPelaIsencao() {
        Cliente cliente = new Cliente(false, false, 0);
        List<ItemPedido> itens = List.of(new ItemPedido("SKU1", 1_000, 1, 5, 5_000, true));
        Pedido pedido = new Pedido(itens, "PR", false, null);

        assertEquals(500L, calculadora.calcular(pedido, cliente, 30_000));
    }

    @Test
    void adicionaisDeExpressoEFragilNaoSaoDivididosPelaMetadeDoVip() {
        Cliente cliente = new Cliente(true, false, 0);
        List<ItemPedido> itens = List.of(new ItemPedido("SKU1", 1_000, 1, 5, 500, true));
        Pedido pedido = new Pedido(itens, "SP", true, null);

        assertEquals(3_000L, calculadora.calcular(pedido, cliente, 1_000));
    }
}
