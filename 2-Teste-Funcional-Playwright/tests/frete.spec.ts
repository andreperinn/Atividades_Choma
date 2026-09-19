import { test, expect } from "@playwright/test";

test("frete de R$15 para CEP iniciado em 8 e valor abaixo de 200", async ({
  page,
}) => {
  await page.goto("/frete");
  await page.getByLabel("CEP").fill("88888888");
  await page.getByLabel("Valor do pedido").fill("50,00");
  await page.getByRole("button", { name: "Calcular frete" }).click();
  await expect(page.getByText("Frete: R$ 15,00")).toBeVisible();
});

test("frete de R$25 para CEP iniciado em 0 e valor abaixo de 200", async ({
  page,
}) => {
  await page.goto("/frete");
  await page.getByLabel("CEP").fill("01310100");
  await page.getByLabel("Valor do pedido").fill("50,00");
  await page.getByRole("button", { name: "Calcular frete" }).click();
  await expect(page.getByText("Frete: R$ 25,00")).toBeVisible();
});

test("frete grátis para valor a partir de 200", async ({ page }) => {
  await page.goto("/frete");
  await page.getByLabel("CEP").fill("01310100");
  await page.getByLabel("Valor do pedido").fill("350,00");
  await page.getByRole("button", { name: "Calcular frete" }).click();
  await expect(page.getByRole("status")).toHaveText("Frete grátis");
});

test("frete grátis no valor exato de 200,00", async ({ page }) => {
  await page.goto("/frete");
  await page.getByLabel("CEP").fill("01310100");
  await page.getByLabel("Valor do pedido").fill("200,00");
  await page.getByRole("button", { name: "Calcular frete" }).click();
  await expect(page.getByRole("status")).toHaveText("Frete grátis");
});

test("CEP Inavlido", async ({ page }) => {
  await page.goto("/frete");
  await page.getByLabel("CEP").fill("100");
  await page.getByLabel("Valor do pedido").fill("80,00");
  await page.getByRole("button", { name: "Calcular frete" }).click();
  await expect(page.getByRole("alert")).toHaveText("Dados inválidos");
});

test("Um centavo abaixo do limite", async ({ page }) => {
  await page.goto("/frete");
  await page.getByLabel("CEP").fill("12345678");
  await page.getByLabel("Valor do pedido").fill("199,99");
  await page.getByRole("button", { name: "Calcular frete" }).click();
  await expect(page.getByRole("status")).toHaveText("Frete: R$ 25,00");
});

test("Valor Inaválido (0)", async ({ page }) => {
  await page.goto("/frete");
  await page.getByLabel("CEP").fill("12345678");
  await page.getByLabel("Valor do pedido").fill("0");
  await page.getByRole("button", { name: "Calcular frete" }).click();
  await expect(page.getByRole("alert")).toHaveText("Dados inválidos");
});
