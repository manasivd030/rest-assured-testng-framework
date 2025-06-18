import com.github.javafaker.Faker;
import org.testng.annotations.DataProvider;
import util.TestDataHelper;

import java.time.format.DateTimeFormatter;
import java.util.stream.IntStream;

//Use case of this class would be to keep the data providers so that these data providers can be reused within all the other test classes
public class BaseTest {

    protected final Faker faker = TestDataHelper.getFaker();

    @DataProvider(name = "bookingDataWithStream", parallel = true)
    public Object[][] bookingDataProviderWithStream()
    {
        //
        var faker = TestDataHelper.getFaker();
        var name = faker.name();
        var dateFormatter = DateTimeFormatter.ISO_DATE;
        return IntStream.range(0,2)
                        .mapToObj(i->
                        {
                            //This is random and dynamic number
                            var numberofPlusDays = TestDataHelper.getRandomInt(2);
                            return  new Object[]{name.firstName(), name.lastName(),faker.bool().bool(),faker.food().dish(),TestDataHelper.getRandomInt(3),
                                    TestDataHelper.getFutureDate(numberofPlusDays,dateFormatter),
                                    TestDataHelper.getFutureDate(numberofPlusDays+4,dateFormatter)
                            };
                        }).toArray(Object[][]::new);


    }

}
