//Some assertion to be added
// Expecting that the create booking API response will give us the exact payload
import apis.CreateBookingApi;
import apis.DeleteBookingApi;
import apis.GetBookingApi;
import apis.UpdateBookingApi;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import util.ApiRequestHelper;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class CRUDTests extends BaseTest {

    @Test(description = "CRUD operation on Restful Booker API resource", dataProvider = "bookingDataWithStream")
    public void crudTest(String firstName, String lastName, Boolean depositPaid,
                         String additionalNeeds, Integer totalPrice, String checkInDate, String checkOutDate) {
        var updateBookingApi = new UpdateBookingApi();

        var createBookingPayload = ApiRequestHelper.getCreateBookingRequest(firstName, lastName, Math.toIntExact(totalPrice),
                depositPaid, additionalNeeds, checkInDate, checkOutDate);

        var createBookingApi = new CreateBookingApi();
        var deleteBookingApi = new DeleteBookingApi();
        var getBookingApi = new GetBookingApi();


        var createBookingApiResponse = createBookingApi.createNewBooking(createBookingPayload)
                                                       .then().assertThat().statusCode(200)
                                                       .and().body("bookingid", is(not(equalTo(0))));

        var bookingid = createBookingApiResponse.extract().jsonPath().getInt("bookingid");

        //Retrieve this created booking using booking ID
        var getBookingByIdApiResponse = getBookingApi.getBookingById(bookingid);
        validateRetrieveBookingDataFromGetApi(firstName, lastName, depositPaid, additionalNeeds, totalPrice, checkInDate, checkOutDate, getBookingByIdApiResponse);


        //Update the booking using PUT Api
        var updatedLastName = this.faker.name().lastName();
        var updatedTotalPrice = Math.toIntExact(this.faker.number().randomNumber(3, true));
        var updatedDepositPaid = this.faker.bool().bool();

        createBookingPayload.replace("lastname", updatedLastName);
        createBookingPayload.replace("totalprice", updatedTotalPrice);
        createBookingPayload.replace("depositpaid", updatedDepositPaid);

        String username = System.getenv("RESTBOOKER_USERNAME");
        String password = System.getenv("RESTBOOKER_PASSWORD");
        System.out.println(username + password);

        var updateBookingApiResponse = updateBookingApi.updateBooking(createBookingPayload, bookingid, username, password)
                                                       .then().assertThat().statusCode(200)
                .and().body("lastname", equalTo(updatedLastName))
                .and().body("totalprice", equalTo(updatedTotalPrice))
                .and().body("depositpaid", equalTo(updatedDepositPaid));


        var deleteBookingApiResponse = deleteBookingApi.deleteBookingById(bookingid, username, password)
                                                       .then().assertThat().statusCode(201);

        getBookingApi.getBookingById(bookingid).then().assertThat().statusCode(404);

    }

    private void validateRetrieveBookingDataFromGetApi(String firstName, String lastName, Boolean depositPaid, String additionalNeeds, Integer totalPrice, String checkInDate, String checkOutDate, Response getBookingByIdApiResponse) {
        getBookingByIdApiResponse
                .then().assertThat().statusCode(200)
                .and().body("firstname", is(equalTo(firstName)))
                .and().body("lastname", is(equalTo(lastName)))
                .and().body("depositpaid", is(equalTo(depositPaid)))
                .and().body("additionalneeds", is(equalTo(additionalNeeds)))
                .and().body("totalprice", is(equalTo(totalPrice)))
                .and().rootPath("bookingdates")
                .and().body("checkin", is(equalTo(checkInDate)))
                .and().body("checkout", is(equalTo(checkOutDate)))
                .and().detachRootPath("bookingdates");
    }

}
