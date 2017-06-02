package logic;

import com.sun.deploy.util.ArrayUtil;
import logic.IPv4.IPv4Address;
import logic.IPv4.IPv4Net;
import logic.IPv4.IPv4Network;
import logic.IPv4.IPv4Subnet;
import static java.lang.Math.toIntExact;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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


    public static long IPtoInt(String completeIP){
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
        String[] allIPs = new String[toIntExact(IPtoInt(getBroadcastFromNetwork(completeIP) + "/20")  - IPtoInt(completeIP)) + 1];

        int counter = 0;
        for (long l = IPtoInt(completeIP); l <= IPtoInt(getBroadcastFromNetwork(completeIP) + "/20"); l++) {
            allIPs[counter] =  intToIP(l);
            counter++;
        }

        return allIPs;
    }

    public static String getBroadcastFromNetwork(String completeIP) {
        long IPasLong = IPtoInt(completeIP);
        int prefix = Integer.valueOf(completeIP.split("/")[1]);

        StringBuilder wildcardBuilder = new StringBuilder();
        for (int i = 0; i < 32 - prefix; i++) {
            wildcardBuilder.append("1");
        }

        String wildcardString = wildcardBuilder.toString();
        long wildcardAsLong = Long.parseLong(wildcardString, 2);

        long broadcastAsLong = IPasLong + wildcardAsLong;


        return intToIP(broadcastAsLong);
    }

    public static String intToIP(long IPAsInt) {
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
        String[] allNewIPs = getAllIPsInNetwork(newNetwork);

        for (int i = 0; i <= oldNetworks.length; i++) {
            String[] allOldIPs = getAllIPsInNetwork(oldNetworks[i]);

            for (int j = 0; j < allOldIPs.length; j++) {
                if (allOldIPs[j].equals(allNewIPs[0]) || allOldIPs[j].equals(allNewIPs[allNewIPs.length])) {
                    return false;
                }
            }

            /*
            if (Arrays.asList(allOldIPs).contains(allNewIPs[0]) || Arrays.asList(allOldIPs).contains(allNewIPs[allNewIPs.length]) ) {
                return false;
            }
            */



        }

        return true;


    }


}

