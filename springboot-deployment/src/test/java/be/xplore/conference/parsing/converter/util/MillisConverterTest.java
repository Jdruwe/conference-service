package be.xplore.conference.parsing.converter.util;

import be.xplore.conference.converter.util.MillisConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class MillisConverterTest {

    @Test
    public void testMillisConversion() {
        //16 May 2019
        long millis = 1_558_004_311_889L;
        LocalDate convertedLocalDate = MillisConverter.toDate(millis);
        Assert.assertNotNull(convertedLocalDate);
        LocalDate correctLocalDate = LocalDate.of(2019, 5, 16);
        Assert.assertEquals(correctLocalDate, convertedLocalDate);
    }

}
