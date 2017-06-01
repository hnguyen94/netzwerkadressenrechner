package logic.IPv4;

import logic.Converter;
import logic.IPAddress;
import logic.Type;

public class IPv4Address extends IPAddress {

    private final int maxValue = 255;
    private final int minValue = 0;

    public IPv4Address(String[] ipAddressBlocks, Type type) {
        super.setIpAddressBlocks(ipAddressBlocks);
        super.setType(type);
    }

    public String convertIpv4ToString(){
       return Converter.convertIPv4ToString(super.getIpAddressBlocks());
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }
}
