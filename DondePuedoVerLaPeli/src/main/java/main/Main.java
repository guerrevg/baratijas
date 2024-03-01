package main;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10L));

        driver.get("https://www.imdb.com/");

        WebElement searchBox = driver.findElement(By.id("suggestion-search"));
        searchBox.sendKeys("The Thing" + Keys.ENTER);

        List<WebElement> resultList = driver.findElements(By.cssSelector("section[data-testid='find-results-section-title'] " +
                "ul[role='presentation'] li div[class='ipc-metadata-list-summary-item__tc']"));

        if (resultList.size() == 0) {
            System.out.println("Sin resultados");
            return;
        }

        List<String> urlsList = new ArrayList<>();
        for (WebElement e : resultList) {
            String url = e.findElement(By.tagName("a")).getAttribute("href");
            urlsList.add(url);
        }

        for (String url : urlsList) {
            driver.get(url);

            String title = driver.findElement(By.cssSelector("span[data-testid='hero__primary-text']")).getText();
            String info = driver.findElement(By.xpath(
                    "//*[@id=\"__next\"]/main/div/section[1]/section/div[3]/section/section/div[2]/div[1]/ul")).getText();

            info = info.replace("\n",", ");
            System.out.println(title + " (" + info + ")");

            List<WebElement> streamingServices = driver.findElements(By.cssSelector("img[alt^='Watch on']"));
            for (WebElement streamingService : streamingServices) {
                System.out.println("- " +
                        streamingService.getAttribute("alt").replace("Watch on ", ""));
            }

            System.out.println("*".repeat(10));
            System.out.println();
        }

        driver.quit();
    }
}
