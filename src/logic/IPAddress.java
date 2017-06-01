package logic;

public abstract class IPAddress {
    private String[] ipAddressBlocks;
    private Type type;

    public IPAddress convertIPAddressTo(Type type) {
        IPAddress newIpAddress = Converter.convert(this, type);
        newIpAddress.setIpAddressBlocks(newIpAddress.getIpAddressBlocks());
        newIpAddress.setType(newIpAddress.getType());

        return newIpAddress;
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
