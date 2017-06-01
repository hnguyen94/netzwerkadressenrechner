package logic;

import logic.IPv4.IPv4Address;

import java.util.HashMap;
import java.util.stream.IntStream;

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
}
