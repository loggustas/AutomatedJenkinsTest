import org.junit.*;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;


public class TestClass {

    private ChromeDriver driver;
    public static String email;
    public static String password;

    @BeforeClass
    public static void CreateUser() throws InterruptedException {
        ChromeDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://demowebshop.tricentis.com/");
        Thread.sleep(500);

        driver.findElement(By.xpath("//a[@class='ico-login']")).click();
        Thread.sleep(500);

        driver.findElement(By.xpath("//input[@class='button-1 register-button']")).click();
        Thread.sleep(500);

        email = GenerateRandomEmail();
        password = GenerateRandomPassword();

        driver.findElement(By.id("gender-male")).click();
        driver.findElement(By.id("FirstName")).sendKeys("Timonsas");
        driver.findElement(By.id("LastName")).sendKeys("Battalions");
        driver.findElement(By.id("Email")).sendKeys(email);
        driver.findElement(By.id("Password")).sendKeys(password);
        driver.findElement(By.id("ConfirmPassword")).sendKeys(password);
        Thread.sleep(500);

        driver.findElement(By.xpath("//input[@class='button-1 register-next-step-button']")).click();

        Thread.sleep(500);
        driver.findElement(By.xpath("//input[@class='button-1 register-continue-button']")).click();

        Thread.sleep(500);
        driver.quit();
    }

    @Before
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @After
    public void cleanUp() {
        if (driver != null) {
            driver.quit();
        }
    }

    public void TestProcedure(String filePath) throws InterruptedException {
        driver.get("https://demowebshop.tricentis.com/");
        Thread.sleep(500);

        driver.findElement(By.xpath("//a[@class='ico-login']")).click();
        Thread.sleep(500);

        driver.findElement(By.id("Email")).sendKeys(email);
        driver.findElement(By.id("Password")).sendKeys(password);
        Thread.sleep(500);

        driver.findElement(By.xpath("//input[@class='button-1 login-button']")).click();
        Thread.sleep(500);

        driver.findElement(By.xpath("//a[normalize-space()='Digital downloads']")).click();
        Thread.sleep(500);

        List<String> itemsAddToCart = GetItemsToAdd(filePath);

        for (int i = 0; i < itemsAddToCart.size(); i++)
        {
            driver.findElement(By.xpath(String.format("//div[contains(@class, 'product-item') and .//a[contains(text(), '%s')]]//input[@value='Add to cart']", itemsAddToCart.get(i)))).click();
            Thread.sleep(1000);
        }

        driver.findElement(By.xpath("//a[@class='ico-cart']")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("//input[@id='termsofservice']")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("//button[@id='checkout']")).click();
        Thread.sleep(1000);

        List<WebElement> selectElements = driver.findElements(By.xpath("//select[@id='billing-address-select']"));
        if (selectElements.isEmpty()) {
            // if not exists create an address
            String City = "Atlanta";
            String Address = "390 Clement Street";
            String Zip = "30329";
            String PhoneNumber = "404-417-9759";

            WebElement dropdown = driver.findElement(By.id("BillingNewAddress_CountryId"));
            Select select = new Select(dropdown);
            select.selectByVisibleText("United States");

            Thread.sleep(1000);
            driver.findElement(By.xpath("//input[@id='BillingNewAddress_City']")).sendKeys(City);
            driver.findElement(By.xpath("//input[@id='BillingNewAddress_Address1']")).sendKeys(Address);
            driver.findElement(By.xpath("//input[@id='BillingNewAddress_ZipPostalCode']")).sendKeys(Zip);
            driver.findElement(By.xpath("//input[@id='BillingNewAddress_PhoneNumber']")).sendKeys(PhoneNumber);
            Thread.sleep(2000);
        }

        driver.findElement(By.xpath("//input[@class='button-1 new-address-next-step-button']")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("//input[@class='button-1 payment-method-next-step-button']")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("//input[@class='button-1 payment-info-next-step-button']")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("//input[@class='button-1 confirm-order-next-step-button']")).click();
        Thread.sleep(1000);

        Assert.assertEquals("Your order has been successfully processed!", driver.findElement(By.xpath("//strong[text()='Your order has been successfully processed!']")).getText());
    }

    @Test
    public void Test1() throws InterruptedException {

        TestProcedure("C:\\Users\\Gustas\\Desktop\\PS_Y1\\6SEMESTER\\ProgramuSistemuTestavimas\\LAB1_TASK4\\data1.txt");
    }

    @Test
    public void Test2() throws InterruptedException {
        TestProcedure("C:\\Users\\Gustas\\Desktop\\PS_Y1\\6SEMESTER\\ProgramuSistemuTestavimas\\LAB1_TASK4\\data2.txt");
    }

    private List<String> GetItemsToAdd(String filePath) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line); // Add each line to the list
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        return lines;
    }

    public static String GenerateRandomPassword() {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()-_=+";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        int length = random.nextInt(10) + 10; // random length

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    public static String GenerateRandomUsername()
    {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int length = random.nextInt(10) + 5; // random length

        for (int i = 0; i < length; i++) {
            char c = characters.charAt(random.nextInt(characters.length()));
            sb.append(c);
        }

        return sb.toString();
    }

    public static String GenerateRandomDomain() {
        String[] domains = {"gmail.com", "yahoo.com", "hotmail.com", "example.com", "test.com"};
        Random random = new Random();
        int index = random.nextInt(domains.length);
        return domains[index];
    }

    // Generate a completely random email
    public static String GenerateRandomEmail() {
        String username = GenerateRandomUsername();
        String domain = GenerateRandomDomain();
        return username + "@" + domain;
    }


}
