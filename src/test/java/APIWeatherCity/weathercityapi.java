package APIWeatherCity;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.path.json.exception.JsonPathException;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/****
*
* @author Pradeep
*
*/

public class WeathercityApi implements  APIFields{
	
		protected final static String APIKEY = "7fe67bf08c80ded756e598d6f8fedaea";
		private static final String BASE_STATION_URI = "https://api.openweathermap.org/data/2.5/weather?";
		private static RequestSpecification request = null;
		
		 public WeathercityApi() {
		        request = given().contentType(ContentType.JSON);
		    }
		 
		 /*'#####################################################################################################################################   
			'Function Name   : weatherreportapi    
			'Purpose         : This function used to get response from API
			'Input           : apikey, json
			'Returns         : Response    
			'####################################################################################################################################### */
		 
		    public static ResponseOptions<Response> weatherreportapi(String apiKey, HashMap<String, Object> json ) {
		        ResponseOptions<Response> responseOptionReturn = null;
		        try {
		            responseOptionReturn = request.post(BASE_STATION_URI + "q=" + json.get("q") + "&units=" + json.get("units") +"&appid=" + apiKey);
		        }catch (Exception ex) {
		            System.out.println("EXCEPTION!: " + ex.getMessage());
		        }
		        return responseOptionReturn;
		    }
		    
		    /*'#####################################################################################################################################   
			'Function Name   : getValueByPath    
			'Purpose         : This function used to get value of a field using jsonpath from response
			'Input           : response, jsonpath
			'Returns         : String    
			'####################################################################################################################################### */
		    
			 public String getValueByPath(ResponseOptions<Response> resp, String jsonpath) {
			        String returnString = "";
			        try {
			        	returnString = resp.getBody().path(jsonpath).toString();
			        } catch (JsonPathException jsonEx) {
			            System.out.println("EXCEPTION: JSON PATH NOT FOUND");
			            returnString = "NA";
			        }
			        return returnString;
			    }
		 
			 
			 /*'#####################################################################################################################################   
				'Function Name   : getValueByKey    
				'Purpose         : This function used to get value of a field using key from response
				'Input           : response, key
				'Returns         : String    
				'####################################################################################################################################### */
			 
				 public String getValueByKey(ResponseOptions<Response> resp, String key) {
				        String returnString = "";
				        try {
				            returnString = resp.getBody().jsonPath().getString(key).toString();
				        } catch (JsonPathException jsonEx) {
				            System.out.println("EXCEPTION: JSON PATH NOT FOUND");
				            returnString = "NA";
				        }
				        return returnString;
				    }
		 
				 /*'#####################################################################################################################################   
					'Function Name   : getStatusCode    
					'Purpose         : This function used to get status code from response
					'Input           : response
					'Returns         : String   
					'####################################################################################################################################### */
				    
				 	public String getStatusCode(ResponseOptions<Response> resp) {
				    	return String.valueOf(((RestAssuredResponseImpl) resp).then().extract().statusCode());
				    }
				    
				    /*'#####################################################################################################################################   
					'Function Name   : createRequestJsonFromList    
					'Purpose         : This function used to create Json request
					'Input           : List
					'Returns         : HashMap    
					'####################################################################################################################################### */
				    
				    protected static HashMap<String, Object> createRequestJsonFromList (List<String> list) {
				        HashMap<String, Object> requestJson = new HashMap<>();
				        requestJson.put(fields.q.toString(), list.get(0));
				        requestJson.put(fields.UNITS.toString(), list.get(1));
				        return requestJson;
				    }
}
