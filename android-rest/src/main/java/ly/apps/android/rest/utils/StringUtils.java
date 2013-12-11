package ly.apps.android.rest.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtils {

    public static final String EMPTY = "";

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

    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
