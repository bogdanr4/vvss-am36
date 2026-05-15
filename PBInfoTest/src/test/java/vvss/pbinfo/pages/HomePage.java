package vvss.pbinfo.pages;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

@DefaultUrl("https://www.pbinfo.ro")
public class HomePage extends PageObject {
    @FindBy(xpath = "/html/body/div[1]/nav/div/div/ul[2]/li[1]/a")
    private WebElementFacade loginModalTrigger;

    @FindBy(id = "user_login")
    private WebElementFacade usernameInput;

    @FindBy(id = "parola_login")
    private WebElementFacade passwordInput;

    @FindBy(xpath = "/html/body/div[1]/div[1]/div/div/form/div[3]/div/div[1]/button[2]")
    private WebElementFacade loginButton;

    @FindBy(xpath = "/html/body/div[1]/div[1]/div/div/form/div[2]/div[3]/div")
    private WebElementFacade loginErrorMessage;

    @FindBy(xpath = "/html/body/div[1]/nav/div/div/ul[2]/li[5]/a")
    private WebElementFacade logoutButton;

    @FindBy(id = "search_box")
    private WebElementFacade searchInput;

    @FindBy(css = "button.fc-cta-do-not-consent")
    private WebElementFacade refuseCookiesButton;

    public void openLoginModal() {
        loginModalTrigger.waitUntilClickable().click();
    }

    public void enterUsername(String username) {
        usernameInput.waitUntilVisible().type(username);
    }

    public void enterPassword(String password) {
        passwordInput.waitUntilVisible().type(password);
    }

    public void clickLogin() {
        loginButton.click();
    }

    public boolean isErrorMessageDisplayed() {
        return loginErrorMessage.isVisible();
    }

    public boolean isLogoutButtonVisible() {
        return logoutButton.waitUntilVisible().isVisible();
    }

    public void clickLogout() {
        logoutButton.click();
    }

    public void searchForProblem(String name) {
        searchInput.typeAndEnter(name);
    }

    public void refuseCookies() {
        try {
            if (refuseCookiesButton.withTimeoutOf(Duration.ofSeconds(5)).isVisible()) {
                refuseCookiesButton.waitUntilClickable().click();
            }
        } catch (Exception e) {
            System.out.println("Nu a aparut cererea de cookies.");
        }
    }
}
