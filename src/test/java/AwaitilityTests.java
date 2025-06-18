import apis.GetBookingApi;
import org.awaitility.Awaitility;
import org.testng.annotations.Test;

import java.time.Duration;
//This is very flexible tool that you should have in test automation framework, because certain APIs take time to update the resource and gives you updated response
// Thread sleep is very bad practice
public class AwaitilityTests {

    @Test
    public void waitUntilAsserted()
    {
        var getBookingApi = new GetBookingApi();

        //here we are purposely failing the testcase
        Awaitility.await()
                .and().with().alias("My custom message")
                .and().with().timeout(Duration.ofSeconds(100)) //Maximum for five seconds this assertion would be retried
                .then().untilAsserted(()-> {
                getBookingApi.getBookingById(20)
                        .then().assertThat().statusCode(200);

                 //    getBookingApi.getBookingById(20)
                  //                .then().assertThat().statusCode(400);

                /*
                As per the outcome of the untilAsserted lambda expression certain API calls are retried until the condition was matched
                Here the code inside until asserted is being retried until five seconds are reached
                Call can be made using methode reference


    });



    @Test
    public void waitUntil()
    {
        var getBookingApi = new GetBookingApi();


        Awaitility.await()
                  .and().with().alias("My custom message")
                  .and().with().timeout(Duration.ofSeconds(10)) //Maximum for five seconds this assertion would be retried
                  .then().until(()-> {
                    var statusCode =  getBookingApi.getBookingById(20).statusCode();

                    return statusCode == 400;

                 //     return true; //This is the outcome we want to achieve after 10 seconds

                /*
                This retry block where we don't need assertion but simple wait block for retrying to match specific condition
                If manual assertion needed then you need an exit criteria to write it down, this is not an assetion we are manual checking the condition
                 */

                  });

    }

    @Test
    public void waitUntilAndIgnoreAllExceptions()
    {
        var getBookingApi = new GetBookingApi();


        Awaitility.await()
                  .and().with().alias("My custom message")
                  .and().with().timeout(Duration.ofSeconds(10)) //Maximum for five seconds this assertion would be retried
                .and().ignoreExceptions()
                  .then().until(()-> {
                      getBookingApi.getBookingById(20)
                              .then().assertThat().statusCode(200);
                      return true;



                /*
                All the exceptions which are being thrown during this condition is tried
                 */

                  });

    }

    @Test
    public void waitUntilAndIgnoreSpecificException()
    {
        var getBookingApi = new GetBookingApi();


        Awaitility.await()
                  .and().with().alias("My custom message")
                  .and().with().timeout(Duration.ofSeconds(10)) //Maximum for five seconds this assertion would be retried
                  .and().ignoreExceptionsInstanceOf(AssertionError.class)
                  .then().until(()-> {
                      getBookingApi.getBookingById(20)
                                   .then().assertThat().statusCode(200);
                      return true;



                /*
                All the exceptions which are being thrown during this condition is tried
                 */

                  });

    }


    @Test
    public void definePollingDelay()
    {
        var getBookingApi = new GetBookingApi();

//Note: Polling should always be less than the timeout else it will not show anything and error will be "java.lang.IllegalArgumentException: Timeout (10 seconds) must be greater than the poll delay (10 seconds)."
        Awaitility.await()
                  .and().with().alias("My custom message")
                  .and().with().timeout(Duration.ofSeconds(15)) //Maximum for five seconds this assertion would be retried
                  .and().ignoreExceptionsInstanceOf(AssertionError.class)
                .and().pollDelay(Duration.ofMillis(500)) //Here frequent tried will be done but only after 300 milliseconds
                  .then().until(()-> {
                      getBookingApi.getBookingById(20)
                                   .then().assertThat().statusCode(200);
                      return true;
                /*
                All the exceptions which are being thrown during this condition is tried
                 */

                  });

    }



}
