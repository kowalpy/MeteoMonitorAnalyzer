/*
 * Meteo Monitor Analyzer 1.0
 * 
 * This is a simple Selenium Web Driver based application for getting
 * weather station data gathered by IMGW institute. Data is saved to 
 * CSV format. 
 * 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.util.HashMap;

	public class MeteoMonitorAnalyzer {
		static HashMap<String,String> meteoVariables = new HashMap<String,String>(); 
		public MeteoMonitorAnalyzer() {
			
		}

		public static void main(String[] args) {
			meteoVariables.put("url", "http://monitor.pogodynka.pl/#station/meteo/");
		    meteoVariables.put("dirPath", " ");
		    meteoVariables.put("appHostxPath", "//*[@id=\"applicationHost\"]/div/div[2]/div/div[1]/div/div[2]/div[8]/div/div[2]/table/tbody[2]/tr[7]/td[1]");
		    meteoVariables.put("args_0", args[0]);
		    meteoVariables.put("dirPath", args[1]);    
			MeteoMonitorEngine mme = new MeteoMonitorEngine(meteoVariables);
			mme.run();
		}
	}


