package ly.apps.android.rest.utils;


public class StringUtils {

    private StringUtils(){
        //No instances
    }

    public static String join(Object[] iterable, String separator) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < iterable.length; i++) {
            builder.append(iterable[i]);
            if (i != iterable.length - 1) {
                builder.append(separator);
            }
        }
        return builder.toString();
    }

}
