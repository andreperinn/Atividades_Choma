package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void aceitaComprasAnterioresZero() {
        Cliente cliente = new Cliente(false, false, 0);

        assertEquals(0, cliente.comprasAnteriores());
    }

    @Test
    void lancaExcecaoQuandoComprasAnterioresNegativo() {
        assertThrows(IllegalArgumentException.class, () -> new Cliente(false, false, -1));
    }

    @Test
    void exposVipBloqueadoEComprasAnterioresCorretamente() {
        Cliente cliente = new Cliente(true, true, 5);

        assertAll(
            () -> assertTrue(cliente.vip()),
            () -> assertTrue(cliente.bloqueado()),
            () -> assertEquals(5, cliente.comprasAnteriores())
        );
    }
}
