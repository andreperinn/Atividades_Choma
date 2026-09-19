package br.edu.ifpr.pedidos;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    @Test
    void lancaExcecaoQuandoListaDeItensNula() {
        assertThrows(IllegalArgumentException.class, () -> new Pedido(null, "PR", false, null));
    }

    @Test
    void lancaExcecaoQuandoListaTemMaisDeCemItens() {
        List<ItemPedido> itens = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            itens.add(new ItemPedido("SKU" + i, 100, 0, 0, 10, false));
        }

        assertThrows(IllegalArgumentException.class, () -> new Pedido(itens, "PR", false, null));
    }

    @Test
    void aceitaListaComExatamenteCemItens() {
        List<ItemPedido> itens = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            itens.add(new ItemPedido("SKU" + i, 100, 0, 0, 10, false));
        }

        Pedido pedido = new Pedido(itens, "PR", false, null);

        assertEquals(100, pedido.itens().size());
    }

    @Test
    void lancaNullPointerQuandoItemDaListaENulo() {
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido("SKU1", 100, 1, 5, 10, false));
        itens.add(null);

        assertThrows(NullPointerException.class, () -> new Pedido(itens, "PR", false, null));
    }

    @Test
    void lancaExcecaoQuandoUfNula() {
        List<ItemPedido> itens = List.of(new ItemPedido("SKU1", 100, 1, 5, 10, false));

        assertThrows(IllegalArgumentException.class, () -> new Pedido(itens, null, false, null));
    }

    @Test
    void lancaExcecaoQuandoUfMinuscula() {
        List<ItemPedido> itens = List.of(new ItemPedido("SKU1", 100, 1, 5, 10, false));

        assertThrows(IllegalArgumentException.class, () -> new Pedido(itens, "pr", false, null));
    }

    @Test
    void lancaExcecaoQuandoUfComTamanhoDiferenteDeDois() {
        List<ItemPedido> itens = List.of(new ItemPedido("SKU1", 100, 1, 5, 10, false));

        assertThrows(IllegalArgumentException.class, () -> new Pedido(itens, "PAR", false, null));
    }

    @Test
    void lancaExcecaoQuandoUfComDigito() {
        List<ItemPedido> itens = List.of(new ItemPedido("SKU1", 100, 1, 5, 10, false));

        assertThrows(IllegalArgumentException.class, () -> new Pedido(itens, "P1", false, null));
    }

    @Test
    void aceitaCupomNulo() {
        List<ItemPedido> itens = List.of(new ItemPedido("SKU1", 100, 1, 5, 10, false));

        Pedido pedido = new Pedido(itens, "PR", false, null);

        assertNull(pedido.cupom());
    }

    @Test
    void aceitaCupomEmBranco() {
        List<ItemPedido> itens = List.of(new ItemPedido("SKU1", 100, 1, 5, 10, false));

        Pedido pedido = new Pedido(itens, "PR", false, "   ");

        assertEquals("   ", pedido.cupom());
    }

    @Test
    void copiaListaDefensivamente() {
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido("SKU1", 100, 1, 5, 10, false));

        Pedido pedido = new Pedido(itens, "PR", false, null);
        itens.add(new ItemPedido("SKU2", 200, 1, 5, 10, false));

        assertEquals(1, pedido.itens().size());
    }

    @Test
    void subtotalCentavosSomaApenasItensAtivos() {
        List<ItemPedido> itens = List.of(
            new ItemPedido("SKU1", 1_000, 2, 5, 10, false),
            new ItemPedido("SKU2", 500, 0, 5, 10, false),
            new ItemPedido("SKU3", 300, 3, 5, 10, false)
        );
        Pedido pedido = new Pedido(itens, "PR", false, null);

        assertEquals(2_900L, pedido.subtotalCentavos());
    }

    @Test
    void subtotalCentavosEhZeroQuandoListaVazia() {
        Pedido pedido = new Pedido(List.of(), "PR", false, null);

        assertEquals(0L, pedido.subtotalCentavos());
    }

    @Test
    void pesoGramasSomaPesoDeTodosOsItensAtivosEIgnoraInativos() {
        List<ItemPedido> itens = List.of(
            new ItemPedido("SKU1", 1_000, 2, 5, 300, false),
            new ItemPedido("SKU2", 500, 0, 5, 900, false)
        );
        Pedido pedido = new Pedido(itens, "PR", false, null);

        assertEquals(600, pedido.pesoGramas());
    }

    @Test
    void temFragilRetornaVerdadeiroComItemFragilAtivo() {
        List<ItemPedido> itens = List.of(new ItemPedido("SKU1", 1_000, 1, 5, 10, true));
        Pedido pedido = new Pedido(itens, "PR", false, null);

        assertTrue(pedido.temFragil());
    }

    @Test
    void temFragilRetornaFalsoComItemFragilInativo() {
        List<ItemPedido> itens = List.of(new ItemPedido("SKU1", 1_000, 0, 5, 10, true));
        Pedido pedido = new Pedido(itens, "PR", false, null);

        assertFalse(pedido.temFragil());
    }

    @Test
    void temFragilRetornaFalsoSemItemFragil() {
        List<ItemPedido> itens = List.of(new ItemPedido("SKU1", 1_000, 1, 5, 10, false));
        Pedido pedido = new Pedido(itens, "PR", false, null);

        assertFalse(pedido.temFragil());
    }

    @Test
    void estoqueSuficienteRetornaVerdadeiroQuandoTodosDisponiveis() {
        List<ItemPedido> itens = List.of(
            new ItemPedido("SKU1", 1_000, 2, 5, 10, false),
            new ItemPedido("SKU2", 500, 1, 1, 10, false)
        );
        Pedido pedido = new Pedido(itens, "PR", false, null);

        assertTrue(pedido.estoqueSuficiente());
    }

    @Test
    void estoqueSuficienteRetornaFalsoQuandoAlgumItemFalta() {
        List<ItemPedido> itens = List.of(
            new ItemPedido("SKU1", 1_000, 6, 5, 10, false),
            new ItemPedido("SKU2", 500, 1, 5, 10, false)
        );
        Pedido pedido = new Pedido(itens, "PR", false, null);

        assertFalse(pedido.estoqueSuficiente());
    }

    @Test
    void estoqueSuficienteRetornaVerdadeiroParaListaVazia() {
        Pedido pedido = new Pedido(List.of(), "PR", false, null);

        assertTrue(pedido.estoqueSuficiente());
    }
}
