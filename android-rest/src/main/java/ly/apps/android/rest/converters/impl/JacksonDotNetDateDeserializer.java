package ly.apps.android.rest.converters.impl;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Pattern;

public class JacksonDotNetDateDeserializer extends JsonDeserializer<Date> {

    /** Pattern for parsing date time values sent by a .NET web service. */
    private static Pattern pattern = Pattern.compile("^/Date\\([0-9]*\\)/$");

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getText();
        if (!pattern.matcher(value).matches()) {
            return null;
        }
        // get UTC millisecond value
        long utcMillisecond = Long.parseLong(value.substring(value.indexOf("(") + 1, value.indexOf(")")));

        // new Date(long) takes milliseconds since 1970 in UTC
        return new Date(utcMillisecond);
    }

}
