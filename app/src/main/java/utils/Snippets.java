package utils;

import java.util.Random;

/**
 * Created by Pavan on 1/16/16.
 */
public class Snippets {

    public static String userObjectId;

    public static final String getUniqueObjectId() {

        // get the universal unique identifier
        //userObjectId = UUID.randomUUID().toString();

        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder( 9 );
        for( int i = 0; i < 9; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );

        userObjectId = "u" + sb.toString();

        return userObjectId;
    }

    public static final String getUniqueSessionId() {

        // get the universal unique identifier
        //userObjectId = UUID.randomUUID().toString();

        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder( 9 );
        for( int i = 0; i < 9; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );

        userObjectId = "s" + sb.toString();

        return userObjectId;
    }

    public static final String getUniqueWallId() {

        // get the universal unique identifier
        //userObjectId = UUID.randomUUID().toString();

        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder( 9 );
        for( int i = 0; i < 9; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );

        userObjectId = "w" + sb.toString();

        return userObjectId;
    }

    public static final String getCommentId() {

        // get the universal unique identifier
        //userObjectId = UUID.randomUUID().toString();

        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder( 9 );
        for( int i = 0; i < 9; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );

        userObjectId = "c" + sb.toString();

        return userObjectId;
    }

    public static final String getUniqueReactionId() {

        // get the universal unique identifier
        //userObjectId = UUID.randomUUID().toString();

        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder( 9 );
        for( int i = 0; i < 9; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );

        userObjectId = "r" + sb.toString();

        return userObjectId;
    }

    public static final String getUniqueMessageId() {

        // get the universal unique identifier
        //userObjectId = UUID.randomUUID().toString();

        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder( 9 );
        for( int i = 0; i < 9; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );

        userObjectId = "m" + sb.toString();

        return userObjectId;
    }

    public static final String getUniqueFeedbackId() {

        // get the universal unique identifier
        //userObjectId = UUID.randomUUID().toString();

        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder( 9 );
        for( int i = 0; i < 9; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );

        userObjectId = "f" + sb.toString();

        return userObjectId;
    }

    public static final String getUniqueGlobalInfoId() {

        // get the universal unique identifier
        //userObjectId = UUID.randomUUID().toString();

        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder( 9 );
        for( int i = 0; i < 9; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );

        userObjectId = "i" + sb.toString();

        return userObjectId;
    }

    public static final String getUniquePrivilegeId() {

        // get the universal unique identifier
        //userObjectId = UUID.randomUUID().toString();

        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder( 9 );
        for( int i = 0; i < 9; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );

        userObjectId = "p" + sb.toString();

        return userObjectId;
    }

    public static final String getUniqueImageName() {

        // get the universal unique identifier
        //userObjectId = UUID.randomUUID().toString();

        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder( 29 );
        for( int i = 0; i < 30; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );

        userObjectId = "i" + sb.toString();

        return userObjectId+".jpg";
    }


    // this method will convert normal string to string which includes hex characters
    public static String escapeURIPathParam(String input) {
        StringBuilder resultStr = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if (isUnsafe(ch)) {
                resultStr.append('%');
                resultStr.append(toHex(ch / 16));
                resultStr.append(toHex(ch % 16));
            } else{
                resultStr.append(ch);
            }
        }
        return resultStr.toString();
    }

    private static char toHex(int ch) {
        return (char) (ch < 10 ? '0' + ch : 'A' + ch - 10);
    }

    private static boolean isUnsafe(char ch) {
        if (ch > 128 || ch < 0)
            return true;
        return " %$&+,/:;=?@<>#%".indexOf(ch) >= 0;
    }
}
