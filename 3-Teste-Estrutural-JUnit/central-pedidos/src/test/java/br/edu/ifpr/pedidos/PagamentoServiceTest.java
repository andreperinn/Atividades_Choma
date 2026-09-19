package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PagamentoServiceTest {

    @Test
    void lancaExcecaoQuandoProcessadorNulo() {
        assertThrows(NullPointerException.class, () -> new PagamentoService(null));
    }

    @Test
    void lancaExcecaoQuandoTotalZero() {
        PagamentoService service = new PagamentoService(total -> true);

        assertThrows(IllegalArgumentException.class, () -> service.pagar(0, 1));
    }

    @Test
    void lancaExcecaoQuandoTotalNegativo() {
        PagamentoService service = new PagamentoService(total -> true);

        assertThrows(IllegalArgumentException.class, () -> service.pagar(-1, 1));
    }

    @Test
    void lancaExcecaoQuandoMaxTentativasAbaixoDoLimite() {
        PagamentoService service = new PagamentoService(total -> true);

        assertThrows(IllegalArgumentException.class, () -> service.pagar(100, 0));
    }

    @Test
    void lancaExcecaoQuandoMaxTentativasAcimaDoLimite() {
        PagamentoService service = new PagamentoService(total -> true);

        assertThrows(IllegalArgumentException.class, () -> service.pagar(100, 4));
    }

    @Test
    void aprovaNaPrimeiraTentativa() {
        int[] chamadas = {0};
        PagamentoService service = new PagamentoService(total -> {
            chamadas[0]++;
            return true;
        });

        boolean aprovado = service.pagar(100, 3);

        assertTrue(aprovado);
        assertEquals(1, chamadas[0]);
    }

    @Test
    void recusaImediataSemRepetir() {
        int[] chamadas = {0};
        PagamentoService service = new PagamentoService(total -> {
            chamadas[0]++;
            return false;
        });

        boolean aprovado = service.pagar(100, 3);

        assertFalse(aprovado);
        assertEquals(1, chamadas[0]);
    }

    @Test
    void tentaNovamenteAposIndisponibilidadeEDepoisAprova() {
        int[] chamadas = {0};
        PagamentoService service = new PagamentoService(total -> {
            chamadas[0]++;
            if (chamadas[0] == 1) throw new IllegalStateException("indisponivel");
            return true;
        });

        boolean aprovado = service.pagar(100, 3);

        assertTrue(aprovado);
        assertEquals(2, chamadas[0]);
    }

    @Test
    void esgotaTentativasQuandoSempreIndisponivelERetornaFalso() {
        int[] chamadas = {0};
        PagamentoService service = new PagamentoService(total -> {
            chamadas[0]++;
            throw new IllegalStateException("indisponivel");
        });

        boolean aprovado = service.pagar(100, 3);

        assertFalse(aprovado);
        assertEquals(3, chamadas[0]);
    }

    @Test
    void esgotaComUmaUnicaTentativaQuandoLimiteEUm() {
        int[] chamadas = {0};
        PagamentoService service = new PagamentoService(total -> {
            chamadas[0]++;
            throw new IllegalStateException("indisponivel");
        });

        boolean aprovado = service.pagar(100, 1);

        assertFalse(aprovado);
        assertEquals(1, chamadas[0]);
    }

    @Test
    void propagaExcecaoDiferenteDeIndisponibilidadeSemRepetir() {
        int[] chamadas = {0};
        PagamentoService service = new PagamentoService(total -> {
            chamadas[0]++;
            throw new RuntimeException("falha grave");
        });

        assertThrows(RuntimeException.class, () -> service.pagar(100, 3));
        assertEquals(1, chamadas[0]);
    }
}
