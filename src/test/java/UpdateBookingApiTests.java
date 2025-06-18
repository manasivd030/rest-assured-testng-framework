import apis.CreateBookingApi;
import apis.DeleteBookingApi;
import apis.UpdateBookingApi;
import com.github.javafaker.Faker;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import util.ApiRequestHelper;
import util.TestDataHelper;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.*;

public class UpdateBookingApiTests extends BaseTest{


//DataProvider with stream is mentioned in BaseTest class

 /*   @DataProvider(name = "bookingDataWithForLoop")
    public Object[][] bookingDataProviderWithLoop()
    {
        //
        var faker = TestDataHelper.getFaker();
        var name = faker.name();
        var dateFormatter = DateTimeFormatter.ISO_DATE;
        List<Object[]> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Object[] objects = new Object[]{name.firstName(), name.lastName(), faker.bool().bool(), faker.food().dish(), faker.number().randomNumber(3, true),
                    TestDataHelper.getFutureDate(10, dateFormatter),
                    TestDataHelper.getFutureDate(14, dateFormatter)};
            list.add(objects);
        }
        return list.toArray(new Object[0][]);


    }
This particular block is used for to show the Dataprovider with forloop
*/

    @Test(description = "Update a newly created booking and validate HTTP status code", dataProvider = "bookingDataWithStream")
    public void updateAndValidateStatusCode(String firstName, String lastName, Boolean depositPaid,
                                            String additionalNeeds, Integer totalPrice, String checkInDate, String checkOutDate) {

        //This instance will be each time created whenever this test will run,
        //that way we would be 100% sure that this is a new instance and is not being shared among threads
        var updateBookingApi = new UpdateBookingApi();

        var createBookingPayload = ApiRequestHelper.getCreateBookingRequest(firstName, lastName, Math.toIntExact(totalPrice),
                depositPaid, additionalNeeds, checkInDate, checkOutDate);

        var createBookingApi = new CreateBookingApi();
        var deleteBookingApi = new DeleteBookingApi();
        var createBookingApiResponse = createBookingApi.createNewBooking(createBookingPayload)
                                                       .then().assertThat().statusCode(200)
                                                       .and().body("bookingid", is(not(equalTo(0))));

        var bookingid = createBookingApiResponse.extract().jsonPath().getInt("bookingid");

        createBookingPayload.replace("lastname", this.faker.name().lastName());
        createBookingPayload.replace("totalprice", this.faker.number().randomNumber(3, true));
        createBookingPayload.replace("depositpaid", this.faker.bool().bool());

        String username = System.getenv("RESTBOOKER_USERNAME");
        String password = System.getenv("RESTBOOKER_PASSWORD");
        System.out.println(username + password);
        var updateBookingApiResponse = updateBookingApi.updateBooking(createBookingPayload, bookingid, username, password)
                                                            .then().assertThat().statusCode(200)
                                                            .and().body("bookingid", is(not(equalTo(0))));


        var deleteBookingApiResponse = deleteBookingApi.deleteBookingById(bookingid,username,password)
                                                    .then().assertThat().statusCode(201);

    }

    public static void main(String[] args)
    {
        var faker = new Faker();

        var firstName = faker.name().firstName();
        var lastName = faker.name().lastName();

        var depositPaid = faker.bool().bool();
        var additionalNeeds = faker.food().dish();

        var dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        var futureDate = TestDataHelper.getFutureDate(20,DateTimeFormatter.ISO_DATE);
        System.out.println(firstName.concat(" ")
                                    .concat(lastName)
                                    .concat("and I need")
                                    .concat(additionalNeeds)
                .concat("and I have paid deposit: " + depositPaid));

        System.out.println("I will checkin at ".concat(futureDate));
    }

}
