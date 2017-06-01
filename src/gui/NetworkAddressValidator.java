package gui;

public class NetworkAddressValidator {

    private static final String NETWORK_ADDRESS =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\/" +
            "([1-9]|1[0-9]|2[0-9]|3[0-2])$";

    public static boolean  validate(String network_address) {

        return network_address.matches(NETWORK_ADDRESS);

    }


    public static boolean validateSubnetMask(String subnetMask) {

        StringBuilder binarySubnetMask = new StringBuilder();

        String[] characters = subnetMask.split("\\.");
        for (int i = 0; i < characters.length; i++) {
            int character = Integer.parseInt(characters[i]);

            if (!(0 <= character) || !(character <= 255)) {
                return false;
            }

            binarySubnetMask.append(Integer.toBinaryString(character));
        }

        String[] binarySubnetSplit = binarySubnetMask.toString().split("0", 2);
        return !(binarySubnetSplit[1].contains("1"));

    }

}
