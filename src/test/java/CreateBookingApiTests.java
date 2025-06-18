import apis.CreateBookingApi;
import constants.ApiPath;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojo.request.Bookingdates;
import pojo.request.CreateBookingRequest;
import util.ApiRequestHelper;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class CreateBookingApiTests {

    private CreateBookingApi createBookingApi;

    @BeforeClass
    public void initApi()
    {
        this.createBookingApi = new CreateBookingApi();
    }

    @Test(description = "Create a new booking and validate HTTP status code")
    public void createAndValidateStatusCode()
    {
        var createBookingPayload = ApiRequestHelper.getCreateBookingRequest("Zach","Newman",799,
                false,"Nothing else","2024-02-02","2024-03-03");

        var createBookingApiResponse = this.createBookingApi.createNewBooking(createBookingPayload)
                .then().assertThat().statusCode(200)
                .and().body("bookingid",is(not(equalTo(0))));
    }

}
