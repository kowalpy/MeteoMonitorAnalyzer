import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

class MeteoMonitorEngine {
	HashMap<String, String> variableSet;
	WebDriver driver;
	String[] dividedPage;
	String[] pageParts;
	 
	public MeteoMonitorEngine(HashMap<String, String> hm){
		variableSet = hm;
		String newUrl = variableSet.get("url") + variableSet.get("args_0");
		variableSet.put("url", newUrl);
	}
	
	public void run(){
		divideWebSrc(getWebPage());
		parseWebParts();
	}
	
	private String getWebPage(){
		driver = new FirefoxDriver();
		driver.get(variableSet.get("url"));
		try{
			Thread.sleep(5000);
		}
		catch(InterruptedException ex) {
			System.out.println(ex);
		    Thread.currentThread().interrupt();
		}
		finally{
			
		}

		WebElement webelement = driver.findElement(By.xpath(variableSet.get("appHostxPath")));
		webelement.click();
		String meteoPageSrc = driver.getPageSource();
		return meteoPageSrc;
	}
	
	private void divideWebSrc(String webSrc){
		dividedPage = webSrc.split("<tr data-bind=\"foreach: \\$data\">");
		pageParts = dividedPage[0].split("\n");
	}
	
	private void parseWebParts(){
		boolean lastHour = false;
		String station = "";
		String stationNumber = "";
		String fallLastHour;
		String fallLastHourTime;
		PrintWriter writer = null;

		for(String x : pageParts){
			if(x.contains("<title>")){
				String stationPattern = "<title>(\\w+) \\(";
				String stationNumberPattern = "\\((\\d+)\\)";		
				Pattern r1 = Pattern.compile(stationPattern);
				Pattern r2 = Pattern.compile(stationNumberPattern);
			    Matcher m1 = r1.matcher(x);
			    if (m1.find( )) {
			    	try{
			    		station = m1.group(1);
			        }
			        catch(Exception e){
			        	System.out.println(e);
			        }
			         
			    } else {
			    	System.out.println("NO MATCH");
			    }
				Matcher m2 = r2.matcher(x);
				if (m2.find( )) {
					try{
						stationNumber = m2.group(1);
				    }
				    catch(Exception e){
				    	System.out.println(e);
				    }
				} else {
					System.out.println("NO MATCH");
				}
			}
			else if(x.contains("Opad za ostatni¹ godzinê:")){
				lastHour = true;
			} else if(lastHour == true){
				lastHour = false;
				Pattern r1 = Pattern.compile(">(.+)mm");
				Pattern r2 = Pattern.compile(" \\((.+)\\)");
			    Matcher m1 = r1.matcher(x);
			    if (m1.find( )) {
			    	try{
			    		fallLastHour = m1.group(1);
			        }
			        catch(Exception e){
			        	System.out.println(e);
			        }
			   }
			   Matcher m2 = r2.matcher(x);
			   if (m2.find( )) {
				   try{
					   fallLastHourTime = m2.group(1);
				   }
				   catch(Exception e){
				       System.out.println(e);
				   }
			   }
			}
		}
				
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		try{
			writer = new PrintWriter(variableSet.get("dirPath") + timeStamp + "_" + station + "_" + stationNumber + ".csv", "UTF-8");
			writer.println("Stacja;" + station);
			writer.println("Numer stacji;" + stationNumber);
			writer.println("");
		}
		catch(Exception e){
			System.out.println(e);
		}
		String someCsvLine = "";
		for(String y : dividedPage){
			String[] z = y.split("\n");
			for(String line : z){
				if(line.contains("class=\"default-state-cell text-left\">") || line.contains("class=\"default-state-cell text-right\">")){ 
					if(line.contains("class=\"default-state-cell text-left\"></td>")){
						someCsvLine += " ;";
					}
					else{
						Pattern r8 = Pattern.compile(">(.+)</td>");
					    Matcher m8 = r8.matcher(line);
			            if (m8.find()) {
			            	try{
							    someCsvLine += m8.group(1) + ";";
							}
							catch(Exception e){
							  	System.out.println(e);
							}
						}
						m8 = null;
					}
				}
			}
			writer.println(someCsvLine);
			someCsvLine = "";			
		}
    	writer.close();	
		driver.quit();
	}
}
