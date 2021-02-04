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
        //переходим по ссылке
        String baseUrl = "http://www.rgs.ru";

        driver.get(baseUrl);


    }


    @Test
    public void runTest() throws InterruptedException {

        //Нажимаем кнопку "меню" вверху справа
        String btnMenu = "//a[contains(text(), 'Меню') and @data-toggle='dropdown']";
        clickButton(btnMenu);

        //в появившейся вкладке нажимаем "Компаниям" переходим на страницу "Корпоративное страхование"
        clickButton(getElementContainsText("Компаниям"));

        //В списке слева нажимаем "Здоровье" переходим на страницу "ДМС для сотрудников"
        clickButton("//div[contains(@class, 'list')]//*[contains(text(), 'Здоровье')]");

        //В списке слева нажимаем "Добровольное медицинское страхование" переходим на страницу "Добровольное медицинское страхование"
        clickButton(getElementContainsText("Добровольное медицинское страхование"));

        //Проверяем, что в заголовке указано "Добровольное медицинское страхование"
        WebElement contentHeader = driver.findElement(By.xpath("//h1"));
        checkElementisVisible(contentHeader, "Добровольное медицинское страхование");

        //Нажимаем кнопку внизу отправить заявку
        clickButton("//div[@class='rgs-main-context-bar']//*[contains(text(), 'Отправить заявку')]");

        //Проверяем, что открылось окно и в заголовке "Заявка на добровольное медицинское страхование"
        checkElementisVisible(getElementContainsText("Заявка"), "Заявка на добровольное медицинское страхование");

        //заполняем поля
        inputLabelForm("Фамилия", "Кодавр");
        inputLabelForm("Имя", "Саул");
        inputLabelForm("Отчество", "Вичевски");

        //выбераем город
        WebElement citiesList = driver.findElement(By.xpath("//*[@name= 'Region']"));
        clickButton(citiesList);
        String city = "Москва";
        clickButton("//*[@name= 'Region']/option[text()='" + city + "']");

        //заполняем поля
        inputLabelForm("Телефон", "9197710062");
        inputLabelForm("Эл. почта", "ыфвараыв"); //вводим не корректную почту
        inputLabelForm("Предпочитаемая дата контакта", "12.05.2021");

        //переключаемся на ввод коммента, чтобы свернулся календарь
        String textAreaCommentaries = "//label[text()='Комментарии']/../textarea";
        WebElement textAreaComment = driver.findElement(By.xpath(textAreaCommentaries));
        inputLabelForm(textAreaComment, "комментарии наши, ё маё");

        //ставим галку
        WebElement iAgre = driver.findElement(By.xpath("//label[contains(text(), 'Я согласен')]"));
        iAgre.click();

        //проверяем, что загорелся аллерт о некоректной почте
        String allertEmail = "//span[@class='validation-error-text']";
        WebElement allertemail = driver.findElement(By.xpath(allertEmail));
        Assert.assertEquals("Введите адрес электронной почты", allertemail.getText());


    }

    @After
    //закрываем браузер
    public void afterRun() {
        driver.quit();
    }

    //метод для заполнения формы с предварительным кликом
    private void inputLabelForm(WebElement element, String keys) {
        element.click();
        element.sendKeys(keys);
    }

    //метод для заполнения формы с предварительным кликом
    private void inputLabelForm(String value, String keys) {
        WebElement element = driver.findElement(By.xpath("//label[text()='" + value + "']/../input"));
        element.click();
        element.sendKeys(keys);
    }

    //проверка, что элемент кликабельный
    private void elementClickable(String locate) {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locate)));
    }

    //проверка, что элемент кликабельный
    private void elementClickable(WebElement element) {
        // System.out.println(element.getText());
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    //клик с проверкой на кликабельность
    private void clickButton(String locate) {
        WebElement button = driver.findElement(By.xpath(locate));
        elementClickable(button);
        button.click();
    }

    //клик с проверкой на кликабельность
    private void clickButton(WebElement button) {
        elementClickable(button);
        button.click();
    }

    //возвращает вебэлемент по содержанию текста
    private WebElement getElementContainsText(String text) {
        return driver.findElement(By.xpath("//*[contains(text(), '" + text + "')]"));
    }

    //проверка на содержание текста в элементе
    private void checkElementisVisible(WebElement element, String expected) {
        wait.until(ExpectedConditions.visibilityOf(element));
        System.out.println(element.getText() + "assert");
        Assert.assertEquals("Значения не совпадают", expected, element.getText());
    }

}
