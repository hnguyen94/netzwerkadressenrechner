public class NetworkAddressValidator {

    private static final String NETWORK_ADDRESS =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\/" +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    public static boolean validate(String network_address) {

        return network_address.matches(NETWORK_ADDRESS);

    }
}
