import { test, expect } from "@playwright/test";

test("Login valido até a tela de contas", async ({ page }) => {
  await page.goto("/login");
  await page.getByLabel("E-mail").fill("ana@exemplo.com");
  await page.getByLabel("Senha").fill("SenhaSegura123!");
  await page.getByRole("button", { name: "Entrar" }).click();
  await expect(page).toHaveURL(/\/conta$/);
  await expect(page.getByTestId("usuario")).toHaveText("Usuário: Ana");
});

test("Login comsenha invalida", async ({ page }) => {
  await page.goto("/login");
  await page.getByLabel("E-mail").fill("ana@exemplo.com");
  await page.getByLabel("Senha").fill("SenhaErrada123");
  await page.getByRole("button", { name: "Entrar" }).click();
  await expect(page.getByRole("alert")).toHaveText("E-mail ou senha inválidos");
  await expect(page).toHaveURL(/\/login$/);
});
