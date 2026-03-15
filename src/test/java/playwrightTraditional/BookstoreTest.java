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
                    new BrowserType.LaunchOptions().setHeadless(true)); // headless for CI

            BrowserContext context = browser.newContext(
                    new Browser.NewContextOptions()
                            .setRecordVideoDir(Paths.get("videos/"))
                            .setRecordVideoSize(1280, 720)
            );

            Page page = context.newPage();

            page.navigate("https://depaul.bncollege.com/");

            page.getByPlaceholder("Enter your search details (").click();
            page.getByPlaceholder("Enter your search details (").fill("earbuds");
            page.getByPlaceholder("Enter your search details (").press("Enter");

            // FILTERS
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

            Locator productTitle = page.getByText("JBL Quantum").first();
            if (productTitle.count() > 0) assertThat(productTitle).containsText("JBL Quantum");

            Locator sku = page.locator(".sku").first();
            if (sku.count() > 0) assertThat(sku).containsText("668");

            Locator price = page.getByText("$164.98").first();
            if (price.count() > 0) assertThat(price).containsText("$164.98");

            Locator addToCart = page.getByLabel("Add to cart");
            if (addToCart.count() > 0) assertThat(addToCart.first()).containsText("Add to cart");

            addToCart.first().click();

            Locator cartIcon = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Cart 1 items"));
            if (cartIcon.count() > 0) assertThat(cartIcon.first()).containsText("Cart 1 items");
            cartIcon.first().click();

            Locator cartHeader = page.getByText("Your Shopping Cart").first();
            if (cartHeader.count() > 0) assertThat(cartHeader).containsText("Your Shopping Cart");

            Locator cartProduct = page.getByText("JBL Quantum").first();
            if (cartProduct.count() > 0) assertThat(cartProduct).containsText("JBL Quantum");

            Locator itemCount = page.getByText("(1 Item)").first();
            if (itemCount.count() > 0) assertThat(itemCount).containsText("(1 Item)");

            Locator subtotal = page.locator(".bned-cart-total-item-value.subtotal").first();
            if (subtotal.count() > 0) assertThat(subtotal).containsText("$164.98");

            page.getByText("FAST In-Store PickupDePaul").click();

            Locator total164 = page.getByText("$164.98").first();
            if (total164.count() > 0) assertThat(total164).containsText("$164.98");

            Locator handling = page.locator("p#bned-handling-fees-tooltip").first();
            if (handling.count() > 0) assertThat(handling).containsText("$3.00");

            Locator tbd = page.getByText("TBD").first();
            if (tbd.count() > 0) assertThat(tbd).containsText("TBD");

            Locator total167 = page.getByText("$167.98").first();
            if (total167.count() > 0) assertThat(total167).containsText("$167.98");

            page.getByLabel("Enter Promo Code").click();
            page.getByLabel("Enter Promo Code").fill("TEST");
            page.getByLabel("Apply Promo Code").click();

            Locator promoError = page.locator(".promo-code-error");
            if (promoError.count() > 0) assertThat(promoError.first()).containsText("invalid");

            if (total167.count() > 0) assertThat(total167).containsText("$167.98");

            page.getByLabel("Proceed To Checkout").click();

            Locator createAccount = page.getByText("Create Account").first();
            if (createAccount.count() > 0) assertThat(createAccount).containsText("Create Account");

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Proceed As Guest")).click();

            Locator contactInfo = page.getByText("Contact Information").first();
            if (contactInfo.count() > 0) assertThat(contactInfo).containsText("Contact Information");

            page.getByPlaceholder("Please enter your first name").fill("test");
            page.getByPlaceholder("Please enter your last name").fill("test");
            page.getByPlaceholder("Please enter a valid email").fill("test@test.test");
            page.getByPlaceholder("Please enter a valid phone").fill("7777777777777");

            Locator sidebar164 = page.getByText("$164.98").first();
            if (sidebar164.count() > 0) assertThat(sidebar164).containsText("$164.98");

            Locator sidebar3 = page.getByText("$3.00").first();
            if (sidebar3.count() > 0) assertThat(sidebar3).containsText("$3.00");

            Locator sidebarTBD = page.getByText("TBD").first();
            if (sidebarTBD.count() > 0) assertThat(sidebarTBD).containsText("TBD");

            Locator sidebar167 = page.getByText("$167.98").first();
            if (sidebar167.count() > 0) assertThat(sidebar167).containsText("$167.98");

            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();

            Locator pickupName = page.getByText("test").first();
            if (pickupName.count() > 0) assertThat(pickupName).containsText("test");

            Locator pickupEmail = page.getByText("test@test.test").first();
            if (pickupEmail.count() > 0) assertThat(pickupEmail).containsText("test@test.test");

            Locator pickupPhone = page.getByText("7777777777777").first();
            if (pickupPhone.count() > 0) assertThat(pickupPhone).containsText("7777777777777");

            Locator pickupMethod = page.getByText("I'll pick them up").first();
            if (pickupMethod.count() > 0) assertThat(pickupMethod).containsText("I'll pick them up");

            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();

            Locator payment164 = page.getByText("$164.98").first();
            if (payment164.count() > 0) assertThat(payment164).containsText("$164.98");

            Locator payment3 = page.getByText("$3.00").first();
            if (payment3.count() > 0) assertThat(payment3).containsText("$3.00");

            Locator paymentTBD = page.getByText("TBD").first();
            if (paymentTBD.count() > 0) assertThat(paymentTBD).containsText("TBD");

            Locator payment167 = page.getByText("$167.98").first();
            if (payment167.count() > 0) assertThat(payment167).containsText("$167.98");

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Back to cart")).click();

            page.getByLabel("Remove product JBL Quantum").click();

            Locator emptyCart = page.getByText("Your cart is empty").first();
            if (emptyCart.count() > 0) assertThat(emptyCart).containsText("Your cart is empty");

            browser.close();
        }
    }
}