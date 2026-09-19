package br.edu.ifpr.pedidos;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PedidoServiceTest {
    @Test
    void deveFecharPedidoDeClienteComumComFreteDoParanaEPagamentoAprovado() {
        // 1. Preparar: cliente comum, uma compra anterior e item disponível de R$ 100,00.
        Cliente cliente = new Cliente(false, false, 1);
        ItemPedido item = new ItemPedido("LIVRO-JAVA", 10_000, 1, 5, 1_000, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, null);

        // Simula o pagamento e registra as cobranças, sem banco ou serviço externo.
        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return true;
        });

        // 2. Executar: percorrer um caminho completo do fechamento.
        ResultadoPedido resultado = service.fechar(pedido, cliente);

        // 3. Verificar: sem desconto; frete de R$ 12,00; total de R$ 112,00.
        assertAll(
            () -> assertEquals("PAGO", resultado.status()),
            () -> assertEquals(10_000L, resultado.subtotalCentavos()),
            () -> assertEquals(0L, resultado.descontoCentavos()),
            () -> assertEquals(1_200L, resultado.freteCentavos()),
            () -> assertEquals(11_200L, resultado.totalCentavos()),
            // A lista comprova uma única cobrança, com o valor correto.
            () -> assertEquals(List.of(11_200L), cobrancas)
        );
    }

    @Test
    void deveRetornarBloqueadoAntesDeAvaliarItensOuCupom() {
        Cliente cliente = new Cliente(false, true, 0);
        ItemPedido item = new ItemPedido("SKU1", 10_000, 1, 5, 1_000, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, "CUPOM-INEXISTENTE");

        int[] chamadas = {0};
        PedidoService service = new PedidoService(total -> {
            chamadas[0]++;
            return true;
        });

        ResultadoPedido resultado = service.fechar(pedido, cliente);

        assertAll(
            () -> assertEquals("BLOQUEADO", resultado.status()),
            () -> assertEquals(0L, resultado.subtotalCentavos()),
            () -> assertEquals(0L, resultado.descontoCentavos()),
            () -> assertEquals(0L, resultado.freteCentavos()),
            () -> assertEquals(0L, resultado.totalCentavos()),
            () -> assertEquals(0, chamadas[0])
        );
    }

    @Test
    void deveLancarExcecaoQuandoSubtotalAtivoForZero() {
        Cliente cliente = new Cliente(false, false, 0);
        ItemPedido itemInativo = new ItemPedido("SKU1", 10_000, 0, 5, 1_000, false);
        Pedido pedido = new Pedido(List.of(itemInativo), "PR", false, null);
        PedidoService service = new PedidoService(total -> true);

        assertThrows(IllegalArgumentException.class, () -> service.fechar(pedido, cliente));
    }

    @Test
    void deveRetornarSemEstoqueAntesDeValidarCupom() {
        Cliente cliente = new Cliente(false, false, 0);
        ItemPedido itemSemEstoque = new ItemPedido("SKU1", 10_000, 5, 1, 1_000, false);
        Pedido pedido = new Pedido(List.of(itemSemEstoque), "PR", false, "CUPOM-INEXISTENTE");

        int[] chamadas = {0};
        PedidoService service = new PedidoService(total -> {
            chamadas[0]++;
            return true;
        });

        ResultadoPedido resultado = service.fechar(pedido, cliente);

        assertAll(
            () -> assertEquals("SEM_ESTOQUE", resultado.status()),
            () -> assertEquals(0L, resultado.subtotalCentavos()),
            () -> assertEquals(0L, resultado.totalCentavos()),
            () -> assertEquals(0, chamadas[0])
        );
    }

    @Test
    void deveRetornarRevisaoComValoresCalculadosESemCobranca() {
        Cliente cliente = new Cliente(false, false, 0);
        ItemPedido item = new ItemPedido("SKU1", 200_000, 1, 1, 100, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, null);

        int[] chamadas = {0};
        PedidoService service = new PedidoService(total -> {
            chamadas[0]++;
            return true;
        });

        ResultadoPedido resultado = service.fechar(pedido, cliente);

        assertAll(
            () -> assertEquals("REVISAO", resultado.status()),
            () -> assertEquals(200_000L, resultado.subtotalCentavos()),
            () -> assertEquals(10_000L, resultado.descontoCentavos()),
            () -> assertEquals(0L, resultado.freteCentavos()),
            () -> assertEquals(190_000L, resultado.totalCentavos()),
            () -> assertEquals(0, chamadas[0])
        );
    }

    @Test
    void deveRetornarPagoComDescontoNoTetoEFreteZeradoPelaIsencao() {
        Cliente cliente = new Cliente(true, false, 2);
        ItemPedido item = new ItemPedido("SKU1", 100_000, 1, 1, 6_000, false);
        Pedido pedido = new Pedido(List.of(item), "SP", false, "EXTRA10");

        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return true;
        });

        ResultadoPedido resultado = service.fechar(pedido, cliente);

        assertAll(
            () -> assertEquals("PAGO", resultado.status()),
            () -> assertEquals(100_000L, resultado.subtotalCentavos()),
            () -> assertEquals(20_000L, resultado.descontoCentavos()),
            () -> assertEquals(0L, resultado.freteCentavos()),
            () -> assertEquals(80_000L, resultado.totalCentavos()),
            () -> assertEquals(List.of(80_000L), cobrancas)
        );
    }

    @Test
    void deveRetornarPagamentoRecusadoQuandoProcessadorNegaImediatamente() {
        Cliente cliente = new Cliente(false, false, 0);
        ItemPedido item = new ItemPedido("SKU1", 10_000, 1, 1, 100, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, null);

        int[] chamadas = {0};
        PedidoService service = new PedidoService(total -> {
            chamadas[0]++;
            return false;
        });

        ResultadoPedido resultado = service.fechar(pedido, cliente);

        assertAll(
            () -> assertEquals("PAGAMENTO_RECUSADO", resultado.status()),
            () -> assertEquals(11_200L, resultado.totalCentavos()),
            () -> assertEquals(1, chamadas[0])
        );
    }

    @Test
    void deveRetentarPagamentoAteTresVezesEDepoisRecusarQuandoProcessadorIndisponivel() {
        Cliente cliente = new Cliente(false, false, 0);
        ItemPedido item = new ItemPedido("SKU1", 10_000, 1, 1, 100, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, null);

        int[] chamadas = {0};
        PedidoService service = new PedidoService(total -> {
            chamadas[0]++;
            throw new IllegalStateException("indisponivel");
        });

        ResultadoPedido resultado = service.fechar(pedido, cliente);

        assertAll(
            () -> assertEquals("PAGAMENTO_RECUSADO", resultado.status()),
            () -> assertEquals(3, chamadas[0])
        );
    }

    @Test
    void deveLancarNullPointerQuandoPedidoNulo() {
        Cliente cliente = new Cliente(false, false, 0);
        PedidoService service = new PedidoService(total -> true);

        assertThrows(NullPointerException.class, () -> service.fechar(null, cliente));
    }

    @Test
    void deveLancarNullPointerQuandoClienteNulo() {
        ItemPedido item = new ItemPedido("SKU1", 10_000, 1, 1, 100, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, null);
        PedidoService service = new PedidoService(total -> true);

        assertThrows(NullPointerException.class, () -> service.fechar(pedido, null));
    }
}
