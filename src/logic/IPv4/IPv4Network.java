package logic.IPv4;

import logic.Type;

import java.util.ArrayList;

public class IPv4Network extends IPv4Net{
    private ArrayList<IPv4Subnet> ipv4Subnets = new ArrayList<>();
    public final String TO_MUCH_HOSTS_MSG = "Das Subnetz ist zu groß";
    public final String SUBNET_BIGGER_NETWORK_MSG = "Das Subnetz ist größer als das Netzwerk";


    public IPv4Network(){

    }

    public IPv4Network(int suffix, IPv4Address networkIPAddress){
        super.setSuffix(suffix);
        super.setNetworkIpAddress(networkIPAddress);
        super.setSubnetmask(createSubnetmaskBy(suffix));
        super.setMaxAmountHosts(getSubnetmask());
    }

    public void fillSubnetsListWith(IPv4Subnet iPv4Subnet, int maxAmountHosts) {
        if(ipv4Subnets.isEmpty()){
            ipv4Subnets.add(iPv4Subnet);
        }else {
            int sum = 0;
            for(IPv4Subnet iPv4SubnetValue : ipv4Subnets){
                sum += iPv4SubnetValue.getHostIpAddresses().length + 2;
            }
            if(sum + iPv4Subnet.getHostIpAddresses().length + 2 <= maxAmountHosts + 2){
                ipv4Subnets.add(iPv4Subnet);
            }else{
                throw new IndexOutOfBoundsException(TO_MUCH_HOSTS_MSG);
            }
        }
    }

    public IPv4Subnet createIPv4Subnet(IPv4Address networkAddress, int amountHosts){
        int suffix = createSuffixBy(amountHosts);
        IPv4Subnet iPv4Subnet = new IPv4Subnet(suffix,networkAddress);
        int maxAmountHostsOfSubnet = iPv4Subnet.getMaxAmountHosts();
        if(maxAmountHostsOfSubnet > getMaxAmountHosts()){
            throw new IndexOutOfBoundsException(SUBNET_BIGGER_NETWORK_MSG);
        }
        iPv4Subnet.setHostIpAddresses(iPv4Subnet.createIPv4HostAddresses(maxAmountHostsOfSubnet,networkAddress.getIpAddressBlocks()));
        iPv4Subnet.setBroadcastAddress(new IPv4Address(iPv4Subnet.createBroadcastIPAddress(iPv4Subnet.getHostIpAddresses()), Type.DECIMAL));

        return iPv4Subnet;
    }


    public int createSuffixBy(int amountOfHosts){
        int host = amountOfHosts + 2;
        int suffix = 32;

        Double logOfHost = Math.ceil(Math.log(host) / Math.log(2));
        long logOfHostLong = logOfHost.longValue();
        suffix -= logOfHostLong;

        return suffix;
    }


    public ArrayList<IPv4Subnet> getIpv4Subnets() {
        return ipv4Subnets;
    }

    public void setIpv4Subnets(ArrayList<IPv4Subnet> ipv4Subnets) {
        this.ipv4Subnets = ipv4Subnets;
    }
}
