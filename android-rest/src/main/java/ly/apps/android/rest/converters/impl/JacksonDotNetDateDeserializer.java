package ly.apps.android.rest.converters.impl;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by raulraja on 12/6/13.
 */
public class JacksonDotNetDateDeserializer extends JsonDeserializer<Date> {


    /** Pattern for parsing date time values sent by a .NET web service. */
    private static Pattern pattern = Pattern.compile("^/Date\\([0-9\\+-]*\\)/$");

    private static DecimalFormat formatter = new DecimalFormat("#00.###");

    private static final long HOUR_IN_MILLISECOND = 60L * 60 * 1000;

    private static final String minusSign = "-";

    private static final String plusSign = "+";


    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String value = jsonParser.getText();
        // example .NET web service return value: /Date(1302847200000+0200)/
        // milliseconds since midnight 1970 UTC + time zone offset of the server to UTC
        // GMT == UTC == UT it's all the same...afaik
        if (!pattern.matcher(value).matches()) {
            return null;
        }
        // get UTC millisecond value
        long utcMillisecond = Long.parseLong(value.substring(value.indexOf("(") + 1, value.indexOf(")") - 5));

        // new Date(long) takes milliseconds since 1970 in UTC
        return new Date(utcMillisecond);
    }


}
