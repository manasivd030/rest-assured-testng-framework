/*
If you try to access get Booking API object, you will not be able to access the HTTP method, but only the public methods will be accessible.
Idea to keep those methods away from test class so that only API class can do operations which are related to HTTP, is to make them protected
If they are not protected, then all the methods are exposed to this class.

@Test(description = "Basic HTTP status check for get booking ids API", retryAnalyzer = RetryAnalyzer.class), This is the basic framework setup
The another way is we can use it with a listener by passing it in the testNG class, but before that passing it in the TestNG class, we will have to use the
annotation transformer listener so that
 */
import apis.GetBookingApi;
import listeners.RetryAnalyzer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class GetBookingApiTests
{

    @Parameters("testParam")
    @Test(description = "Basic HTTP status check for get booking ids API")
    public void validationStatusCodeForGetBookingIdApi(@Optional String testParam)
    {
       //The below line will not throw an error as it has been made as optional
        System.out.println("Test Param Value:" + testParam);

        //var is dynamic
        var getBookingIdsResponse = new GetBookingApi().getAllBookingIds()
                .then().assertThat().statusCode(200);
    }

    @Test(description = "Basic HTTP status check for get booking by ID API")
    public void validateStatusCodeForGetBookingByIdApi()
    {
        //var is dynamic
        var getBookingByIdApiResponse = new GetBookingApi().getBookingById(20)
                .then().assertThat().statusCode(200);
    }

}

