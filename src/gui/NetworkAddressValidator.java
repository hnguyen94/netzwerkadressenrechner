package gui;

public class NetworkAddressValidator {

    private static final String NETWORK_ADDRESS =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\/" +
                    "([0-9]|1[0-9]|2[0-9]|3[0-2])$";

    /**
     *
     * @param network_address parent network address
     * @return true if the network address is valid, fale if not
     */
    public static boolean  validate(String network_address) {
        return network_address.matches(NETWORK_ADDRESS);
    }

    /**
     *
     * @param subnetMask parent subnetmask
     * @return true if the subnet mask if valid, false if not
     */
    public static boolean validateSubnetMask(String subnetMask) {
        StringBuilder binarySubnetMask = new StringBuilder();

        String[] characters = subnetMask.split("\\.");
        for (int i = 0; i < characters.length; i++) {
            int character = Integer.parseInt(characters[i]);
            if (!(0 <= character) || !(character <= 255)) {
                return false;
            }

            String blockAsBinaryString = Integer.toBinaryString(character);
            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < 8 - blockAsBinaryString.length(); j++) {
                stringBuilder.append("0");
            }
            stringBuilder.append(blockAsBinaryString);

            binarySubnetMask.append(stringBuilder.toString());
        }

        String[] binarySubnetSplit = binarySubnetMask.toString().split("0", 2);
        try {
            return !(binarySubnetSplit[1].contains("1"));
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

}
