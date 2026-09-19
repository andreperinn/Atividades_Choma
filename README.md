# Teste de Software 2026 - Avaliação prática Prova 01

Atividades da avaliação prática, cada uma na sua pasta.

| Pasta | Atividade |
| --- | --- |
| [1-Artefatos-Planos-de-Teste-e-Casos-de-Teste](1-Artefatos-Planos-de-Teste-e-Casos-de-Teste) | Plano de teste e casos de teste (Reserva de Salas) |
| [2-Teste-Funcional-Playwright](2-Teste-Funcional-Playwright) | Teste funcional com Playwright |
| [3-Teste-Estrutural-JUnit](3-Teste-Estrutural-JUnit) | Teste estrutural com JUnit e JaCoCo (`boletim-simples` e `central-pedidos`) |

## Como rodar

- Playwright: dentro de `2-Teste-Funcional-Playwright`, `npm install` e depois `npx playwright test`.
- JUnit: dentro de `boletim-simples` ou `central-pedidos`, `mvn clean test`. O relatório do JaCoCo fica em `target/site/jacoco/index.html`.
