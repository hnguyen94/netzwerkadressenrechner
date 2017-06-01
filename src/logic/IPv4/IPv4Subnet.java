package logic.IPv4;

import logic.IPv4.IPv4Address;
import logic.Type;


public class IPv4Subnet extends IPv4Net{

    private IPv4Address broadcastAddress;
    private IPv4HostAddress[] hostIpAddresses;

    public IPv4Subnet(){

    }

    public IPv4Subnet(int suffix, IPv4Address networkIpAddress) {

        super.setSuffix(suffix);
        super.setNetworkIpAddress(networkIpAddress);
        super.setSubnetmask(createSubnetmaskBy(suffix));
        super.setMaxAmountHosts(getSubnetmask());

    }

    public String[] createBroadcastIPAddress(IPv4HostAddress[] iPv4HostAddresses){
        String[] broadcastIpBlock = iPv4HostAddresses[iPv4HostAddresses.length - 1].getIpv4Address().getIpAddressBlocks();
        broadcastIpBlock[3] = String.valueOf(Integer.parseInt(broadcastIpBlock[3]) + 1);

        return broadcastIpBlock;
    }


    public IPv4HostAddress[] createIPv4HostAddresses(int amountHosts, String[] ipAddressBlocks) {

        IPv4HostAddress[] iPv4HostAddresses = new IPv4HostAddress[amountHosts];

        for (int i = 0; i < amountHosts; i++){
            int blockNumber = getBlockNumber(ipAddressBlocks, 3);
            if(blockNumber < 3){
                for (int j = 3; blockNumber < j; j--){
                    ipAddressBlocks[j] = "0";
                }
            }
            ipAddressBlocks[blockNumber] = String.valueOf(Integer.parseInt(ipAddressBlocks[blockNumber]) + 1);
            IPv4Address iPv4Address = new IPv4Address(ipAddressBlocks.clone(),Type.DECIMAL);
            iPv4HostAddresses[i] = new IPv4HostAddress(iPv4Address);

        }

        return iPv4HostAddresses;
    }


    public int getBlockNumber(String[] networkIPBlock, int blockNumber){
        if(Integer.parseInt(networkIPBlock[blockNumber]) < 255){
            return blockNumber;
        }
        int OneLessBlockNumber = blockNumber - 1;

        return getBlockNumber(networkIPBlock, OneLessBlockNumber);
    }

    public IPv4HostAddress[] getHostIpAddresses() {
        return hostIpAddresses;
    }

    public void setHostIpAddresses(IPv4HostAddress[] hostIpAddresses) {
        this.hostIpAddresses = hostIpAddresses;
    }

    public IPv4Address getBroadcastAddress() {
        return broadcastAddress;
    }

    public void setBroadcastAddress(IPv4Address broadcastAddress) {
        this.broadcastAddress = broadcastAddress;
    }
}
