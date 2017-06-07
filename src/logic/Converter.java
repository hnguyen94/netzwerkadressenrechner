package logic;

import com.sun.deploy.util.ArrayUtil;
import logic.IPv4.IPv4Address;
import logic.IPv4.IPv4Network;
import logic.IPv4.IPv4Subnet;

import javax.swing.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.IntStream;

import static java.lang.Math.toIntExact;

public class Converter {

    private static final HashMap<String, Integer> hexMap = createHexMap();

    private static HashMap<String, Integer> createHexMap() {
        HashMap<String, Integer> createdHexMap = new HashMap<>();
        createdHexMap.put("0", 0);
        createdHexMap.put("1", 1);
        createdHexMap.put("2", 2);
        createdHexMap.put("3", 3);
        createdHexMap.put("4", 4);
        createdHexMap.put("5", 5);
        createdHexMap.put("6", 6);
        createdHexMap.put("7", 7);
        createdHexMap.put("8", 8);
        createdHexMap.put("9", 9);
        createdHexMap.put("A", 10);
        createdHexMap.put("B", 11);
        createdHexMap.put("C", 12);
        createdHexMap.put("D", 13);
        createdHexMap.put("E", 14);
        createdHexMap.put("F", 15);

        return createdHexMap;
    }

    public static IPAddress convert(IPAddress ipAddress, Type type) {
        int ipAddressLength = ipAddress.getIpAddressBlocks().length;
        String[] newIPAddress = new String[ipAddressLength];

        if(ipAddress.getType().equals(type)){
            return ipAddress;
        }

        switch (type) {
            case BINARY:
                IntStream.range(0, ipAddressLength).forEach(iteration -> {
                    String valueToConvert = ipAddress.getIpAddressBlocks()[iteration];

                    if (ipAddress.getType() != Type.DECIMAL) {
                        valueToConvert = convertToDecimal(valueToConvert, ipAddress.getType());
                    }

                    newIPAddress[iteration] = convertDecimalToBinary(Integer.parseInt(valueToConvert), new StringBuilder());

                });
                break;
            case HEX:
                IntStream.range(0, ipAddressLength).forEach(iteration -> {
                    String valueToConvert = ipAddress.getIpAddressBlocks()[iteration];

                    if (ipAddress.getType() != Type.DECIMAL) {
                        valueToConvert = convertToDecimal(valueToConvert, ipAddress.getType());
                    }

                    newIPAddress[iteration] = convertDecimalToHex(Integer.parseInt(valueToConvert), new StringBuilder());
                });
                break;
            case DECIMAL:
                IntStream.range(0, ipAddressLength).forEach(iteration -> {

                    String valueToConvert = ipAddress.getIpAddressBlocks()[iteration];
                    newIPAddress[iteration] = convertToDecimal(valueToConvert, ipAddress.getType());

                });
                break;
            default:
                return null;
        }
        IPv4Address newIpv4Address = new IPv4Address(newIPAddress, type);
        return newIpv4Address;
    }

    public static String convertToDecimal(String value, Type type) {
        StringBuilder decimalBuilder = new StringBuilder(value);
        String reverseValue = decimalBuilder.reverse().toString();

        if (type == Type.BINARY) {
            return convertBinaryToDecimal(reverseValue);
        } else if (type == Type.HEX) {
            return convertHexToDecimal(reverseValue);
        }

        return null;
    }

    public static String convertDecimalToBinary(int value, StringBuilder binaryBuilder) {
        int restValue = value / 2;
        StringBuilder updatedBinaryBuilder = binaryBuilder.insert(0, String.valueOf(value % 2));
        if (restValue != 0) {
            return convertDecimalToBinary(restValue, updatedBinaryBuilder);
        }
        int restLength = 8 - updatedBinaryBuilder.length();
        if (restLength != 0) {
            IntStream.range(0, restLength).forEach(i -> updatedBinaryBuilder.insert(0, "0"));
        }
        return updatedBinaryBuilder.toString();
    }

    public static String convertBinaryToDecimal(String binaries) {
        int sum = 0;
        int binaryValue;
        for (int i = 0; i < binaries.length(); i++) {
            binaryValue = Character.getNumericValue(binaries.charAt(i));
            sum += (binaryValue * Math.pow(2, i));
        }

        return String.valueOf(sum);
    }

    public static String convertHexToDecimal(String hexCode) {
        int sum = 0;
        for (int i = 0; i < hexCode.length(); i++) {
            sum += (hexMap.get(String.valueOf(hexCode.charAt(i))) * Math.pow(16, i));
        }
        return String.valueOf(sum);
    }

    public static String convertDecimalToHex(int value, StringBuilder hexBuilder) {
        int restValue = value / 16;
        String hexValue = hexMap.entrySet().stream().filter(entry -> entry.getValue() == value % 16).findFirst().get().getKey();
        StringBuilder updatedHexBuilder = hexBuilder.insert(0, String.valueOf(hexValue));

        if (restValue != 0) {
            return convertDecimalToHex(restValue, updatedHexBuilder);
        }

        int restLength = 4 - updatedHexBuilder.length();
        if (restLength != 0) {
            IntStream.range(0, restLength).forEach(i -> updatedHexBuilder.insert(0, "0"));
        }

        return hexBuilder.toString();
    }

    public static String convertIPv4ToString(String[] ipAddressBlocks){
        String ipAddress = "";
        for(int i = 0; i < ipAddressBlocks.length; i++){
            if(i == 0){
                ipAddress += ipAddressBlocks[i];
            }else {
                ipAddress += "." + ipAddressBlocks[i];
            }
        }

        return ipAddress;
    }


    public static IPv4Subnet convertStringToIpv4Subnet(String network){

        String[] ipAndSuffix = network.split("/");

        String[] ipAddress = ipAndSuffix[0].split("\\.");
        System.out.println(network);
        int suffix = Integer.parseInt(ipAndSuffix[1]);

        return new IPv4Subnet(suffix,new IPv4Address(ipAddress,Type.DECIMAL));
    }

    public static IPv4Network convertStringToIpv4Network(String network){

        String[] ipAndSuffix = network.split("\\/");

        String[] ipAddress = ipAndSuffix[0].split("\\.");

        int suffix = Integer.parseInt(ipAndSuffix[1]);

        return new IPv4Network(suffix,new IPv4Address(ipAddress,Type.DECIMAL));
    }


    public static long ipToLong(String completeIP){
        long iPBlock1, iPBlock2, iPBlock3, iPBlock4, iPasLong;

        String ipSplit[] = completeIP.split("/");

        String ip = ipSplit[0];

        String[] ipParts = ip.split("\\.");
        iPBlock1 = Long.parseLong(ipParts[0]);
        iPBlock2 = Long.parseLong(ipParts[1]);
        iPBlock3 = Long.parseLong(ipParts[2]);
        iPBlock4 = Long.parseLong(ipParts[3]);

        iPasLong = (iPBlock1*16777216L)+(iPBlock2*65536L)+(iPBlock3*256L)+(iPBlock4);

        return iPasLong;
    }

    public static String[] getAllIPsInNetwork(String completeIP) {
        String[] allIPs = new String[toIntExact(ipToLong(getBroadcastFromNetwork(completeIP) + "/20")  - ipToLong(completeIP)) + 1];

        int counter = 0;
        for (long l = ipToLong(completeIP); l <= ipToLong(getBroadcastFromNetwork(completeIP) + "/20"); l++) {
            allIPs[counter] =  longToIP(l);
            counter++;
        }

        return allIPs;
    }

    public static String getBroadcastFromNetwork(String completeIP) {
        long IPasLong = ipToLong(completeIP);
        int prefix = Integer.valueOf(completeIP.split("/")[1]);

        StringBuilder wildcardBuilder = new StringBuilder();
        for (int i = 0; i < 32 - prefix; i++) {
            wildcardBuilder.append("1");
        }

        String wildcardString = wildcardBuilder.toString();
        long wildcardAsLong = Long.parseLong(wildcardString, 2);

        long broadcastAsLong = IPasLong + wildcardAsLong;


        return longToIP(broadcastAsLong);
    }

    public static String longToIP(long IPAsInt) {
        return ((IPAsInt >> 24) & 0xFF) + "." + ((IPAsInt >> 16) & 0xFF) + "." + ((IPAsInt >> 8) & 0xFF) + "." + (IPAsInt  & 0xFF);
    }

    public static int calculateAmountReservedIPs(int prefix){
        int reversePrefix = 32-prefix;
        int reservedIPs = 1;

        for (int i = 1; i <= reversePrefix; i++){
            reservedIPs *= 2;
        }
        return reservedIPs;
    }

    public static boolean checkIfPossibleNewNetwork(String[] oldNetworks, String newNetwork) {

        /*
        String[] allNewIPs = getAllIPsInNetwork(newNetwork);

        for (int i = 0; i < oldNetworks.length; i++) {
            String[] allOldIPs = getAllIPsInNetwork(oldNetworks[i]);

            for (int j = 0; j < allNewIPs.length; j++) {
                if (Arrays.asList(allOldIPs).contains(allNewIPs[j])) {
                    return false;
                }
            }
        }

        return true;
        */

        return Arrays.stream(oldNetworks).noneMatch(v -> isColliding(v, newNetwork));
    }

    public static boolean isColliding(String networkA, String networkB) {
        long networkAId = ipToLong(networkA);
        long networkAMask = ipToLong(prefixToMask(getPrefixFromCompleteNetwork(networkA)));
        long networkBId = ipToLong(networkB);
        long networkBMask = ipToLong(prefixToMask(getPrefixFromCompleteNetwork(networkB)));
        return isInSubnet(networkAId, networkAMask, networkBId)
                || isInSubnet(networkBId, networkBMask, networkAId);
    }

    /**
     *
     * @param networkId parent network ID
     * @param networkMask parent network mask
     * @param hostId host address
     * @return If the host address ist inside network ID/network mask combination
     */
    private static boolean isInSubnet(long networkId, long networkMask, long hostId) {
        return (hostId & networkMask) == (networkId & networkMask);
    }

    /**
     * creates an netmask from prefix
     * @param prefix network prefix
     * @return The netmask
     */
    public static String prefixToMask(int prefix) {
        if(prefix < 0 || prefix > 32) throw new IllegalArgumentException("Illegal prefix '" + prefix + "'");
        long mask = prefix == 0 ? 0 : -1 << (32 - prefix);
        return longToIP(mask);
    }



    public static int getPrefixFromAmountOfHosts(int amountOfHosts) {

        return (int) (32 - Math.ceil(Math.log(amountOfHosts + 2) / Math.log(2)));

    }

    public static int getAmountOfIPsFromPrefix (int prefix) {
        return (int) Math.pow(2, 32 - prefix);

    }

    public static String getSmallestNewSubnetInNetwork() {
        return "Test";
    }

    public static int getPrefixFromCompleteNetwork(String network) {
        return Integer.valueOf(network.split("/")[1]);
    }

    public static String getNewFreeIPAfterNetwork(String network) {
        long networkAsLong = ipToLong(network);
        return longToIP(networkAsLong + getAmountOfIPsFromPrefix(getPrefixFromCompleteNetwork(network)));
    }

    public static String[] sortNetworksInModel(DefaultListModel model) {
        String[] networks = new String[model.size()];


        for (int i = 0; i < model.size(); i++) {
            networks[i] = model.get(i).toString();
        }
        Arrays.sort(networks, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Long.compare(ipToLong(o1), ipToLong(o2));
            }
        });

        return networks;

    }

}

