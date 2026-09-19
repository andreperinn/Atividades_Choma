package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemPedidoTest {

    @Test
    void lancaExcecaoQuandoSkuNulo() {
        assertThrows(IllegalArgumentException.class,
            () -> new ItemPedido(null, 1_000, 1, 5, 100, false));
    }

    @Test
    void lancaExcecaoQuandoSkuEmBranco() {
        assertThrows(IllegalArgumentException.class,
            () -> new ItemPedido("   ", 1_000, 1, 5, 100, false));
    }

    @Test
    void lancaExcecaoQuandoPrecoZero() {
        assertThrows(IllegalArgumentException.class,
            () -> new ItemPedido("SKU1", 0, 1, 5, 100, false));
    }

    @Test
    void lancaExcecaoQuandoPrecoAcimaDoLimite() {
        assertThrows(IllegalArgumentException.class,
            () -> new ItemPedido("SKU1", 1_000_001, 1, 5, 100, false));
    }

    @Test
    void aceitaPrecoNoLimiteMinimo() {
        ItemPedido item = new ItemPedido("SKU1", 1, 1, 5, 100, false);

        assertEquals(1, item.precoCentavos());
    }

    @Test
    void aceitaPrecoNoLimiteMaximo() {
        ItemPedido item = new ItemPedido("SKU1", 1_000_000, 1, 5, 100, false);

        assertEquals(1_000_000, item.precoCentavos());
    }

    @Test
    void lancaExcecaoQuandoQuantidadeNegativa() {
        assertThrows(IllegalArgumentException.class,
            () -> new ItemPedido("SKU1", 1_000, -1, 5, 100, false));
    }

    @Test
    void lancaExcecaoQuandoQuantidadeAcimaDoLimite() {
        assertThrows(IllegalArgumentException.class,
            () -> new ItemPedido("SKU1", 1_000, 101, 200, 100, false));
    }

    @Test
    void aceitaQuantidadeZero() {
        ItemPedido item = new ItemPedido("SKU1", 1_000, 0, 5, 100, false);

        assertEquals(0, item.quantidade());
    }

    @Test
    void aceitaQuantidadeNoLimiteMaximo() {
        ItemPedido item = new ItemPedido("SKU1", 1_000, 100, 200, 100, false);

        assertEquals(100, item.quantidade());
    }

    @Test
    void lancaExcecaoQuandoEstoqueNegativo() {
        assertThrows(IllegalArgumentException.class,
            () -> new ItemPedido("SKU1", 1_000, 1, -1, 100, false));
    }

    @Test
    void lancaExcecaoQuandoPesoZero() {
        assertThrows(IllegalArgumentException.class,
            () -> new ItemPedido("SKU1", 1_000, 1, 5, 0, false));
    }

    @Test
    void lancaExcecaoQuandoPesoAcimaDoLimite() {
        assertThrows(IllegalArgumentException.class,
            () -> new ItemPedido("SKU1", 1_000, 1, 5, 100_001, false));
    }

    @Test
    void aceitaPesoNoLimiteMinimo() {
        ItemPedido item = new ItemPedido("SKU1", 1_000, 1, 5, 1, false);

        assertEquals(1, item.pesoGramas());
    }

    @Test
    void aceitaPesoNoLimiteMaximo() {
        ItemPedido item = new ItemPedido("SKU1", 1_000, 1, 5, 100_000, false);

        assertEquals(100_000, item.pesoGramas());
    }

    @Test
    void calculaTotalCentavosMultiplicandoPrecoPelaQuantidade() {
        ItemPedido item = new ItemPedido("SKU1", 1_500, 3, 5, 100, false);

        assertEquals(4_500L, item.totalCentavos());
    }

    @Test
    void calculaTotalCentavosZeroQuandoQuantidadeZero() {
        ItemPedido item = new ItemPedido("SKU1", 1_500, 0, 5, 100, false);

        assertEquals(0L, item.totalCentavos());
    }

    @Test
    void estaDisponivelQuandoQuantidadeIgualAoEstoque() {
        ItemPedido item = new ItemPedido("SKU1", 1_000, 5, 5, 100, false);

        assertTrue(item.disponivel());
    }

    @Test
    void estaDisponivelQuandoQuantidadeMenorQueEstoque() {
        ItemPedido item = new ItemPedido("SKU1", 1_000, 3, 5, 100, false);

        assertTrue(item.disponivel());
    }

    @Test
    void estaIndisponivelQuandoQuantidadeMaiorQueEstoque() {
        ItemPedido item = new ItemPedido("SKU1", 1_000, 6, 5, 100, false);

        assertFalse(item.disponivel());
    }
}
