import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class TestKondratov {
    WebDriver driver;
    WebDriverWait wait;


    @Before
    public void preparation() {
        System.setProperty("webdriver.chrome.driver", "webdriver/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 10, 1000);
        String baseUrl = "http://www.rgs.ru";
        String page = "https://www.rgs.ru/products/juristic_person/health/dms/index.wbp";
        driver.get(baseUrl);


    }


    @Test
    public void runTest() throws InterruptedException {
        System.out.println("start");

        String btnMenu = "//a[contains(text(), 'Меню') and @class='hidden-xs']";

        clickButton(btnMenu);

        clickButton(a_contains_text("Компаниям"));

        clickButton("//div[contains(@class, 'list')]" + "//*[contains(text(), '" + "Здоровье" + "')]");

        clickButton(a_contains_text("Добровольное медицинское страхование"));

        WebElement contentHeader = driver.findElement(By.xpath("//h1"));

        checkElementisVisible(contentHeader, "Добровольное медицинское страхование");

        clickButton("//div[@class='rgs-main-context-bar']//*[contains(text(), 'Отправить заявку')]");

        checkElementisVisible(a_contains_text("Заявка"), "Заявка на добровольное медицинское страхование");


        inputLabelForm("Фамилия", "Кодавр");
        inputLabelForm("Имя", "Саул");
        inputLabelForm("Отчество", "Вичевски");

        WebElement citiesList = driver.findElement(By.xpath("//*[@name= 'Region']"));
        clickButton(citiesList);
        String city = "Москва";
        clickButton("//*[@name= 'Region']" + "/option[text()='" + city + "']");

        inputLabelForm("Телефон", "9197710062");

        inputLabelForm("Эл. почта", "ыфвараыв");

        inputLabelForm("Предпочитаемая дата контакта", "12.05.2021");

        String textAreaCommentaries = "//label[text()='Комментарии']/../textarea";

        WebElement textAreaComment = driver.findElement(By.xpath(textAreaCommentaries));

        inputLabelForm(textAreaComment, "комментарии наши, ё маё");

        WebElement iAgre = driver.findElement(By.xpath("//label[contains(text(), 'Я согласен')]"));
        iAgre.click();

        String allertEmail = "//span[@class='validation-error-text']";
        //Введите адрес электронной почты
        WebElement allertemail = driver.findElement(By.xpath(allertEmail));
        Assert.assertEquals("Введите адрес электронной почты", allertemail.getText());


    }

    @After
    public void afterRun() {
        driver.quit();
    }

    private void inputLabelForm(WebElement element, String keys) {
        element.click();
        element.sendKeys(keys);
    }

    private void inputLabelForm(String value, String keys) {
        WebElement element = driver.findElement(By.xpath("//label[text()='" + value + "']/../input"));
        element.click();
        element.sendKeys(keys);
    }


    private void elementClickable(String locate) {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locate)));
    }

    private void elementClickable(WebElement element) {
       // System.out.println(element.getText());
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    private void clickButton(String locate) {
        WebElement button = driver.findElement(By.xpath(locate));
        elementClickable(button);
        try {
            button.click();
        } catch (Exception e) {
            elementClickable(button);
            button.click();
        }
    }

    private void clickButton(WebElement button) {

        elementClickable(button);
        try {
            button.click();
        } catch (Exception e) {
            elementClickable(button);
            button.click();
        }
    }

    private WebElement a_contains_text(String text) {

        return driver.findElement(By.xpath("//*[contains(text(), '" + text + "')]"));

    }

    private void checkElementisVisible(WebElement element, String expected) {
        wait.until(ExpectedConditions.visibilityOf(element));
        System.out.println(element.getText() + "assert");
        Assert.assertEquals("Совпадают", expected, element.getText());

    }

}
