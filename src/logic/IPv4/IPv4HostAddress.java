package logic.IPv4;

public class IPv4HostAddress {
    private IPv4Address ipv4Address;
    private String name;

    public IPv4HostAddress(IPv4Address ipv4Address){
        this.ipv4Address = ipv4Address;
    }

    public IPv4HostAddress(IPv4Address ipv4Address, String name){
        this.ipv4Address = ipv4Address;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IPv4Address getIpv4Address() {
        return ipv4Address;
    }

    @Override
    public String toString(){
        return getIpv4Address().convertIpv4ToString() + "|" + getName();
    }
}
