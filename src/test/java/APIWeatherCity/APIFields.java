package APIWeatherCity;


/****
*
* @author Pradeep
*
*/


public interface APIFields {
	
	enum fields { q("q"),
        UNITS("units");
private String associatedText;

fields(String associatedText) { this.associatedText = associatedText; }

@Override
public String toString() {
  return this.associatedText;
}

};
}
