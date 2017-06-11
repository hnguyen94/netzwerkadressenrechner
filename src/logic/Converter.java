package logic;

import com.sun.deploy.util.StringUtils;

import javax.swing.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.toIntExact;

public class Converter {


    public static String convertIpToBinary(String ip){
        String[] ipBlocks = ip.split("\\.");
        String ipAsBinary = Arrays.stream(ipBlocks)
                            .map(ipBlock -> convertIpBlockToBinary(new StringBuilder(),Integer.parseInt(ipBlock)).concat("."))
                            .collect(Collectors.joining());
        return ipAsBinary.substring(0,ipAsBinary.length() - 1 );
    }

    public static String convertIpBlockToBinary(StringBuilder convertedIpBlock, int num){
        if(num == 0 && convertedIpBlock.length() > 7){
            return convertedIpBlock.reverse().toString();
        }

        convertedIpBlock.append(String.valueOf(num % 2));
        return convertIpBlockToBinary(convertedIpBlock,num / 2);
    }

    public static String convertIpToHex(String ip){
        String[] ipBlocks = ip.split("\\.");
        String ipAsHex = Arrays.stream(ipBlocks)
                .map(ipBlock -> convertIpBlockToHex(new StringBuilder(),Integer.parseInt(ipBlock)).concat("."))
                .collect(Collectors.joining());
        return ipAsHex.substring(0,ipAsHex.length() - 1 );
    }

    public static String convertIpBlockToHex(StringBuilder convertedIpBlock, int num){
        if(num == 0 && convertedIpBlock.length() > 1){
            return convertedIpBlock.reverse().toString();
        }

        convertedIpBlock.append(digitToHex(num % 16));
        return convertIpBlockToHex(convertedIpBlock,num / 16);
    }

    public static String digitToHex(int digit){
        switch (digit){
            case 10:
                return "A";
            case 11:
                return "B";
            case 12:
                return "C";
            case 13:
                return "D";
            case 14:
                return "E";
            case 15:
                return "F";
            default:
                return String.valueOf(digit);
        }
    }


    /**
     *
     * @param completeIP parent IP Address String
     * @return IP Address as Long
     */
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

    /**
     *
     * @param completeIP parent Network
     * @return Array of IP Address Strings
     */
    public static String[] getAllIPsInNetwork(String completeIP) {
        String[] allIPs = new String[toIntExact(ipToLong(getBroadcastFromNetwork(completeIP) + "/20")  - ipToLong(completeIP)) + 1];

        int counter = 0;
        for (long l = ipToLong(completeIP); l <= ipToLong(getBroadcastFromNetwork(completeIP) + "/20"); l++) {
            allIPs[counter] =  longToIP(l);
            counter++;
        }

        return allIPs;
    }

    /**
     *
     * @param completeIP parent IP Address String
     * @return Broadcast IP as Long
     */
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

    /**
     *
     * @param IPAsInt parent IP address
     * @return IP as a String
     */
    public static String longToIP(long IPAsInt) {
        return ((IPAsInt >> 24) & 0xFF) + "." + ((IPAsInt >> 16) & 0xFF) + "." + ((IPAsInt >> 8) & 0xFF) + "." + (IPAsInt  & 0xFF);
    }

    /**
     *
     * @param oldNetworks parent Network
     * @param newNetwork parent Network
     * @return true if new network is not colliding with some from the old networks, false if it does
     */
    public static boolean checkIfPossibleNewNetwork(String[] oldNetworks, String newNetwork) {
        return Arrays.stream(oldNetworks).noneMatch(v -> isColliding(v, newNetwork));
    }

    /**
     *
     * @param networkA parent network
     * @param networkB parent network
     * @return true if the two networks are colliding, false if not
     */
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

    /**
     * Converts a Subnetmask String into a prefix as int
     * @param mask parent SubnetMask
     * @return prefix
     */
    public static int maskToPrefix(String mask) {
        String[] maskBlocks = mask.split("\\.");
        StringBuilder maskAsBinary = new StringBuilder();

        for (int i = 0; i < maskBlocks.length; i++){
            maskAsBinary.append(Integer.toBinaryString(Integer.valueOf(maskBlocks[i])));
        }

        String maskAsBinaryString = maskAsBinary.toString();
        return maskAsBinaryString.length() - maskAsBinaryString.replace("1", "").length();
    }

    /**
     *
     * @param amountOfHosts parent hosts
     * @return prefix
     */
    public static int getPrefixFromAmountOfHosts(int amountOfHosts) {
        return (int) (32 - Math.ceil(Math.log(amountOfHosts + 2) / Math.log(2)));
    }

    /**
     *
     * @param prefix parent prefix/subnetmask
     * @return amount of hosts
     */
    public static int getAmountOfIPsFromPrefix (int prefix) {
        return (int) Math.pow(2, 32 - prefix);
    }

    /**
     *
     * @param network parent network
     * @return prefix
     */
    public static int getPrefixFromCompleteNetwork(String network) {
        return Integer.valueOf(network.split("/")[1]);
    }

    /**
     *
     * @param network parent network
     * @return next free Network ID IP Address
     */
    public static String getNewFreeIPAfterNetwork(String network) {
        long networkAsLong = ipToLong(network);
        return longToIP(networkAsLong + getAmountOfIPsFromPrefix(getPrefixFromCompleteNetwork(network)));
    }

    /**
     *
     * @param model parent ListModel
     * @return sorted Array of network Strings
     */
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

    /**
     * Converts a Prefix into a SubnetMask
     * @param network parent Network
     * @return Subnetmask
     */
    public static String getSubnetMaskFromNetwork(String network) {
        int prefix = Integer.valueOf(network.split("/")[1]);
        return prefixToMask(prefix);
    }
}

