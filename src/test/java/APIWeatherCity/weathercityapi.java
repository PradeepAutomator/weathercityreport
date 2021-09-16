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


public class WeathercityApi implements  APIFields{
	
		protected final static String APIKEY = "7fe67bf08c80ded756e598d6f8fedaea";
		private static final String BASE_STATION_URI = "https://api.openweathermap.org/data/2.5/weather?";
		private static RequestSpecification request = null;
		
		 public WeathercityApi() {
		        request = given().contentType(ContentType.JSON);
		    }
		 
		    public static ResponseOptions<Response> weatherreportapi(String apiKey, HashMap<String, Object> json ) {
		        ResponseOptions<Response> responseOptionReturn = null;
		        try {
		            responseOptionReturn = request.post(BASE_STATION_URI + "q=" + json.get("q") + "&units=" + json.get("units") +"&appid=" + apiKey);
		        }catch (Exception ex) {
		            System.out.println("EXCEPTION!: " + ex.getMessage());
		        }
		        // Extract Response into an Object "ResponseOptions<Response> response "
		        return responseOptionReturn;
		    }
		    
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
		 
		 	// Get the Status Code
		    public String getStatusCode(ResponseOptions<Response> resp) {
		    	return String.valueOf(((RestAssuredResponseImpl) resp).then().extract().statusCode());
		    }
		    
		    public String getresp(Response resp) {
		    	String returnString = "";
		        try {
		            returnString = resp.getBody().toString();
		        } catch (JsonPathException jsonEx) {
		            System.out.println("EXCEPTION: JSON PATH NOT FOUND");
		            returnString = "NA";
		        }
		        return returnString;
		    }
		    
		    protected static HashMap<String, Object> createRequestJsonFromList (List<String> list) {
		        HashMap<String, Object> requestJson = new HashMap<>();
		        requestJson.put(fields.q.toString(), list.get(0));
		        requestJson.put(fields.UNITS.toString(), list.get(1));
		        return requestJson;
		    }
}
