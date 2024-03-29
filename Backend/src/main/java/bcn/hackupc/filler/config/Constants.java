package bcn.hackupc.filler.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String ANONYMOUS_USER = "anonymoususer";  //2019-11-09T23:59:59
    public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private Constants() {
    }
}
