import io.restassured.RestAssured;
import com.jayway.jsonpath.JsonPath;

public class JSONPathExamples {
    public static void main(String[] args)
    {
        
        var responseString = RestAssured.given().baseUri("https://bookcart.azurewebsites.net/api")
                .and().basePath("/book/")
                .when().get().then().assertThat().statusCode(200)
                .extract().asString();

        //Filter a book with  title containing Ruin
        //If .* is removed from the expression than then it will consider Potter at the start
        var filteredBookWithTitle = JsonPath.read(responseString, "$[?(@.title =~ /.*Ruin.*/i)]");
        System.out.println("Book containing title: " +filteredBookWithTitle);

        //Filter books for Category 'Fiction' and price less than 500
        var filteredBookForCategory = JsonPath.read(responseString, "$[?(@.price < 500 && @.category == 'Fiction')]");
        System.out.println("Book with price < 500 and category 'Fiction': " +filteredBookForCategory);

        //Opposite or negative testcase to check "Books with title not containing 'Harry' and price not equals or less than 214
        var booksWithTitleNotHarryAndPriceNegation = JsonPath.read(responseString,"$[?(!(@.price < 214 || @.title =~ /.*Harry.*/))]");
        System.out.println("Book not containing title as Harry and Price less than 214: " +booksWithTitleNotHarryAndPriceNegation);

        //Nested Json
        var nestedJson = """
                {
                  "firstName": "John",
                  "lastName": "doe",
                  "age": 26,
                  "address": {
                    "streetAddress": "naist street",
                    "city": "Nara",
                    "postalCode": "630-0192"
                  },
                  "phoneNumbers": [
                    {
                      "type": "iPhone",
                      "number": "0123-4567-8888"
                    },
                    {
                      "type": "home",
                      "number": "0123-4567-8910"
                    }
                  ]
                }
                """;

        //Specific String related data
        var numberForTypeIphone = JsonPath.read(nestedJson,"$..phoneNumbers[?(@.type=='iPhone')].number");
        System.out.println("Object for iphone number: " +numberForTypeIphone);

        //List of data as per given element from rootpath
        var flattenPhoneNumbers = JsonPath.read(nestedJson, "$..phoneNumbers[*][*]");
        System.out.println("Object for flattened phone numbers: " +flattenPhoneNumbers);

    }
}
