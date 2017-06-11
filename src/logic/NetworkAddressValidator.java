package logic;

public class NetworkAddressValidator {

    private static final String NETWORK_ADDRESS =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\/" +
            "([0-9]|1[0-9]|2[0-9]|3[0-2])$";

    /**
     *
     * @param networkAddress parent network address
     * @return true if the network address is valid, fale if not
     */
    public static boolean  validate(String networkAddress) {
        return networkAddress.matches(NETWORK_ADDRESS);
    }


}
