package logic;

import javax.swing.*;
import java.util.Arrays;
import java.util.Comparator;
import static java.lang.Math.toIntExact;

public class Converter {

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
}

