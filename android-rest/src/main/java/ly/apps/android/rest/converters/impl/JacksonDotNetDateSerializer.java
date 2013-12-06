package ly.apps.android.rest.converters.impl;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * Created by raulraja on 12/6/13.
 */
public class JacksonDotNetDateSerializer extends JsonSerializer<Date> {


    /** Pattern for parsing date time values sent by a .NET web service. */
    private static Pattern pattern = Pattern.compile("^/Date\\([0-9\\+-]*\\)/$");

    private static DecimalFormat formatter = new DecimalFormat("#00.###");

    private static final long HOUR_IN_MILLISECOND = 60L * 60 * 1000;

    private static final String minusSign = "-";

    private static final String plusSign = "+";

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        int zoneOffsetMillisecond = TimeZone.getDefault().getOffset(date.getTime());
        String sign = plusSign;
        if (zoneOffsetMillisecond < 0) { // negative offset
            sign = minusSign;
            zoneOffsetMillisecond *= -1;
        }
        int minute = (int) (zoneOffsetMillisecond % HOUR_IN_MILLISECOND);
        int hour = (zoneOffsetMillisecond / 1000 / 60 / 60);
        jsonGenerator.writeString("/Date(" + date.getTime() + sign + formatter.format(hour) + formatter.format(minute) + ")/");
    }
}
