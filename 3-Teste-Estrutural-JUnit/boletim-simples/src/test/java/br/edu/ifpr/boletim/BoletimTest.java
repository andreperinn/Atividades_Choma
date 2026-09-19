package br.edu.ifpr.boletim;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BoletimTest {

    @Test
    void deveAprovarAlunoComMediaOito() {
        // Preparar: criar o objeto que será testado.
        Boletim boletim = new Boletim();

        // Executar: chamar um único método com uma entrada conhecida.
        String resultado = boletim.verificarSituacao(8);

        // Verificar: comparar o resultado esperado com o resultado obtido.
        assertEquals("APROVADO", resultado);
    }

    @Test
    void deveRecuperarNotaAlunoComMediaQuatro() {
        Boletim boletim = new Boletim();

        String resultado = boletim.verificarSituacao(4);

        assertEquals("RECUPERACAO", resultado);
    }

    @Test
    void deveReprovarAlunoComMediaDois() {
        Boletim boletim = new Boletim();

        String resultado = boletim.verificarSituacao(2);

        assertEquals("REPROVADO", resultado);
    }

    @Test
    void deveCalcularMediaIgualCinco() {
        Boletim boletim = new Boletim();

        double resultado = boletim.calcularMedia(5,5);

        assertEquals(5,resultado);
    }

    @Test
    void deveCalcularMediaComCasasDecimais() {
        Boletim boletim = new Boletim();

        double resultado = boletim.calcularMedia(7, 8);

        assertEquals(7.5, resultado, 0.0001);
    }

    @Test
    void deveCalcularMediaComNotasExtremas() {
        Boletim boletim = new Boletim();

        double resultado = boletim.calcularMedia(0, 10);

        assertEquals(5.0, resultado, 0.0001);
    }

    @Test
    void deveCalcularMediaComAmbasNotasZero() {
        Boletim boletim = new Boletim();

        double resultado = boletim.calcularMedia(0, 0);

        assertEquals(0.0, resultado, 0.0001);
    }

    @Test
    void deveCalcularMediaComAmbasNotasMaximas() {
        Boletim boletim = new Boletim();

        double resultado = boletim.calcularMedia(10, 10);

        assertEquals(10.0, resultado, 0.0001);
    }

    @Test
    void deveAprovarAlunoComMediaExatamenteSete() {
        Boletim boletim = new Boletim();

        String resultado = boletim.verificarSituacao(7);

        assertEquals("APROVADO", resultado);
    }

    @Test
    void deveRecuperarAlunoComMediaLogoAbaixoDeSete() {
        Boletim boletim = new Boletim();

        String resultado = boletim.verificarSituacao(6.99);

        assertEquals("RECUPERACAO", resultado);
    }

    @Test
    void deveReprovarAlunoComMediaLogoAbaixoDeQuatro() {
        Boletim boletim = new Boletim();

        String resultado = boletim.verificarSituacao(3.99);

        assertEquals("REPROVADO", resultado);
    }

    @Test
    void deveAprovarAlunoComMediaMaxima() {
        Boletim boletim = new Boletim();

        String resultado = boletim.verificarSituacao(10);

        assertEquals("APROVADO", resultado);
    }

    @Test
    void deveReprovarAlunoComMediaMinima() {
        Boletim boletim = new Boletim();

        String resultado = boletim.verificarSituacao(0);

        assertEquals("REPROVADO", resultado);
    }

    @Test
    void deveContarZeroAprovadosQuandoArrayVazio() {
        Boletim boletim = new Boletim();

        int resultado = boletim.contarAprovados(new double[] {});

        assertEquals(0, resultado);
    }

    @Test
    void deveContarUmAprovadoComUmElementoAprovado() {
        Boletim boletim = new Boletim();

        int resultado = boletim.contarAprovados(new double[] {8});

        assertEquals(1, resultado);
    }

    @Test
    void deveContarZeroAprovadosComUmElementoNaoAprovado() {
        Boletim boletim = new Boletim();

        int resultado = boletim.contarAprovados(new double[] {5});

        assertEquals(0, resultado);
    }

    @Test
    void deveContarTodosAprovadosQuandoTodasMediasAltas() {
        Boletim boletim = new Boletim();

        int resultado = boletim.contarAprovados(new double[] {7, 8, 10});

        assertEquals(3, resultado);
    }

    @Test
    void deveContarZeroAprovadosQuandoNenhumaMediaAlcancaSete() {
        Boletim boletim = new Boletim();

        int resultado = boletim.contarAprovados(new double[] {2, 3, 6.9});

        assertEquals(0, resultado);
    }

    @Test
    void deveContarAprovadosComMediasMistas() {
        Boletim boletim = new Boletim();

        int resultado = boletim.contarAprovados(new double[] {8, 5, 7, 3});

        assertEquals(2, resultado);
    }

    @Test
    void deveContarAprovadoComMediaExatamenteNoLimite() {
        Boletim boletim = new Boletim();

        int resultado = boletim.contarAprovados(new double[] {7});

        assertEquals(1, resultado);
    }
}
