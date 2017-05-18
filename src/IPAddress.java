
public abstract class IPAddress {
    private String[] ipAddressBlocks;
    private Type type;

    public void convertIPAddressTo(Type type) {
        IPAddress newIpAddress = Converter.convert(this, type);
        this.setIpAddressBlocks(newIpAddress.getIpAddressBlocks());
        this.setType(newIpAddress.getType());
    }

    public String[] getIpAddressBlocks() {
        return ipAddressBlocks;
    }

    public void setIpAddressBlocks(String[] ipAddressBlocks) {
        this.ipAddressBlocks = ipAddressBlocks;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
