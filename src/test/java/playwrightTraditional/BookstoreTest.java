package playwrightTraditional;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookstoreTest {

    @Test
    void bookstoreFlow() {

        try (Playwright playwright = Playwright.create()) {

            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(false));

            BrowserContext context = browser.newContext(
                    new Browser.NewContextOptions()
                            .setRecordVideoDir(Paths.get("videos/"))
                            .setRecordVideoSize(1280,720)
            );

            Page page = context.newPage();

            page.navigate("https://depaul.bncollege.com/");

            page.getByPlaceholder("Enter your search details (").click();
            page.getByPlaceholder("Enter your search details (").fill("earbuds");
            page.getByPlaceholder("Enter your search details (").press("Enter");

            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("brand")).click();
            page.locator("#facet-brand")
                    .getByRole(AriaRole.LIST)
                    .locator("label")
                    .filter(new Locator.FilterOptions().setHasText("brand JBL"))
                    .getByRole(AriaRole.IMG)
                    .click();

            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Color")).click();
            page.locator("label")
                    .filter(new Locator.FilterOptions().setHasText("Color Black"))
                    .getByRole(AriaRole.IMG)
                    .click();

            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Price")).click();
            page.locator("#facet-price").getByRole(AriaRole.IMG).click();

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions()
                    .setName("JBL Quantum True Wireless")).click();

            assertThat(page.getByText("JBL Quantum").first()).isVisible();
            assertThat(page.locator(".sku").first()).containsText("668");
            assertThat(page.getByText("$164.98").first()).isVisible();
            assertThat(page.getByLabel("Add to cart")).isVisible();

            page.getByLabel("Add to cart").click();

            Locator cartIcon = page.getByRole(AriaRole.LINK,new Page.GetByRoleOptions().setName("Cart 1 items"));

            cartIcon.waitFor();
            assertThat(cartIcon).isVisible();
            cartIcon.click();

            assertThat(page.getByText("Your Shopping Cart").first()).isAttached();
            assertThat(page.getByText("JBL Quantum").first()).isVisible();
            assertThat(page.getByText("(1 Item)").first()).isAttached();

            assertThat(page.locator(".bned-cart-total-item-value.subtotal"))
                    .containsText("$164.98");

            page.getByText("FAST In-Store PickupDePaul").click();

            assertThat(page.getByText("$164.98").first()).isVisible();
            assertThat(page.locator("p#bned-handling-fees-tooltip")).containsText("$3.00");
            assertThat(page.getByText("TBD").first()).isVisible();
            assertThat(page.getByText("$167.98").first()).isVisible();

            page.getByLabel("Enter Promo Code").click();
            page.getByLabel("Enter Promo Code").fill("TEST");
            page.getByLabel("Apply Promo Code").click();

            Locator promoError = page.locator(".promo-code-error");

            if (promoError.count() > 0) {
                assertThat(promoError.first()).containsText("invalid");
            } else {
                System.out.println("Promo code error not found — continuing");
            }
            assertThat(page.getByText("$167.98").first()).isVisible();

            page.getByLabel("Proceed To Checkout").click();

            assertThat(page.getByText("Create Account").first()).isVisible();

            page.getByRole(AriaRole.LINK,
                    new Page.GetByRoleOptions().setName("Proceed As Guest")).click();

            Locator contactInfo = page.getByText("Contact Information").first();
            assertThat(contactInfo).isAttached();

            page.getByPlaceholder("Please enter your first name").fill("test");
            page.getByPlaceholder("Please enter your last name").fill("test");
            page.getByPlaceholder("Please enter a valid email").fill("test@test.test");
            page.getByPlaceholder("Please enter a valid phone").fill("7777777777777");

            assertThat(page.getByText("$164.98").first()).isAttached();
            assertThat(page.getByText("$3.00").first()).isAttached();
            assertThat(page.getByText("TBD").first()).isAttached();
            assertThat(page.getByText("$167.98").first()).isAttached();

            page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Continue")).click();

            assertThat(page.getByText("test").first()).isVisible();
            assertThat(page.getByText("test@test.test").first()).isVisible();
            assertThat(page.getByText("7777777777777").first()).isVisible();

            assertThat(page.getByText("I'll pick them up").first()).isVisible();

            assertThat(page.getByText("$164.98").first()).isAttached();
            assertThat(page.getByText("$3.00").first()).isAttached();
            assertThat(page.getByText("TBD").first()).isAttached();
            assertThat(page.getByText("$167.98").first()).isAttached();

            page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Continue")).click();

            assertThat(page.getByText("$164.98").first()).isAttached();
            assertThat(page.getByText("$3.00").first()).isAttached();
            assertThat(page.getByText("TBD").first()).isAttached();
            assertThat(page.getByText("$167.98").first()).isAttached();

            page.getByRole(AriaRole.LINK,new Page.GetByRoleOptions().setName("Back to cart")).click();

            page.getByLabel("Remove product JBL Quantum").click();

            assertThat(page.getByText("Your cart is empty").first()).isVisible();

            browser.close();
        }
    }
}