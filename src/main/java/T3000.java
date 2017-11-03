
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.opencsv.CSVReader;

public class T3000 {

    public static void main(String[] args){

        System.out.println("... starting T3000");

        try {
            String fileName = "C:\\Users\\cliff\\Desktop\\T3000\\testcases.csv";

            CSVReader csvinput = new CSVReader(new FileReader(fileName));
            List csvinputdata = csvinput.readAll();
            csvinput.close();
            String[] steps = new String[8];

            MyWebDriver driver = new MyWebDriver("chrome");

            for(Object ob : csvinputdata) {
                String[] row=(String[]) ob;
                int i = 0;
                if(row[1].equals("1")){
                    for (String s : row) {
                        System.out.println(s);
                        steps[i]=s;
                        i++;
                    }
                    updateWeb(steps,driver.doAction(row[3], row[4], row[5], row[6], row[7]));
                }
            }

            driver.myClose();
        }
        catch(Exception ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("... ending T3000");
    }


    static public void updateWeb(String[] steps, Boolean result){
        try{
            FileReader fr = null;
            FileWriter fw;
            BufferedWriter bw;
            String html = "";
            BufferedReader br = null;

            try {
                //br = new BufferedReader(new FileReader(FILENAME));
                fr = new FileReader("C:\\inetpub\\tellurium\\help.html");
                br = new BufferedReader(fr);

                String sCurrentLine;
                while ((sCurrentLine = br.readLine()) != null) {
                    System.out.println(sCurrentLine);
                    html=html+sCurrentLine;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                try {
                    if (br != null)
                        br.close();
                    if (fr != null)
                        fr.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            fw = new FileWriter("C:\\inetpub\\tellurium\\help.html");

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));

            String ntext = "<tr><td>" + dateFormat.format(date) + "</td>";
            for(int j=0;j<steps.length;j++){
                ntext = ntext + "<td>" + steps[j] + "</td>";
            }

            if(result){
                ntext = ntext + "<td><img src='/images/Green-Tick.png' alt='Success' height='50' width='50'/></td></tr>";
            }
            else{
                ntext = ntext + "<td><img src='/images/Red-Cross.png' alt='Fail' height='50' width='50'/></td></tr>";
            }

            ntext = ntext + "<tr><td>..</td><td>..</td><td>..</td><td>..</td><td>..</td><td>..</td><td>..</td><td>..</td><td>..</td><td>..</td></tr>";

            String rtext = "<tr><td>..</td><td>..</td><td>..</td><td>..</td><td>..</td><td>..</td><td>..</td><td>..</td><td>..</td><td>..</td></tr>";

            String html2 = html.replace(rtext,ntext);

            bw = new BufferedWriter(fw);
            bw.write(html2);

            bw.newLine();
            bw.close();
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }

    }

}


class MyWebDriver {

    org.openqa.selenium.WebDriver driver;

    public MyWebDriver(String browser)
    {
        try {
            if (browser.equals("chrome")) {
                System.setProperty("webdriver.chrome.driver", "C:\\Users\\cliff\\Desktop\\T3000\\chromedriver.exe");
                driver = new ChromeDriver();
            }

        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }

    }

    public WebElement findMyElement(String type, String Identifier){

        WebElement e = null;

        if(type.equals("xpath")){
            e = driver.findElement(By.xpath(Identifier));
        }

        return e;
    }

    public void myClose()
    {
        driver.quit();
    }

    public boolean myAssert(String type, String Identifier, String Value, String type2){

        boolean output = false;
        try {
            WebElement e = findMyElement(type, Identifier);

            if(Value.equals("IsDisplayed")){
                if(e.isDisplayed()){
                    output = true;
                }
            }
            else{
                if(type2.equals("TextIs")){
                    if(e.getText().equals(Value)){
                        System.out.println("Test case asserted successfully");
                        output = true;
                    }
                    else
                    {
                        System.out.println("Test case failed");
                    }
                }
            }
        }
        catch(org.openqa.selenium.NoSuchElementException ex){
            System.out.println("Assertion fails - element does not exist");
        }
        return output;
    }

    public Boolean doAction(String action, String type, String Identifier, String Value, String type2){

        Boolean output = false;

        
        try {

            if(action.equals("navigate")){
                driver.get(Value);
            }

            if(action.equals("sendkeys")){
                WebElement e = findMyElement(type, Identifier);

                if(Value.contains("{{RETURN}}"))
                {
                    e.sendKeys(Keys.RETURN);
                }
                else {
                    e.sendKeys(Value);
                }
            }

            if(action.equals("click")){
                WebElement e = findMyElement(type, Identifier);
                e.click();
            }

            if(action.equals("assert")){
                if(!myAssert(type, Identifier, Value, type2)){
                    return output;
                }
            }

            Thread.sleep(2000);

            output = true;
            return output;
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return output;
        }
    }
}