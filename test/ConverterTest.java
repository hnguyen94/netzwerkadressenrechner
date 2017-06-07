import logic.Converter;
import logic.IPAddress;
import logic.IPv4.IPv4Address;
import logic.Type;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ConverterTest {
    

    @Test
    public void convertToBinaryTest(){
        new Converter();
        assertEquals("10001110", Converter.convertDecimalToBinary(142,new StringBuilder()));
        assertEquals("00110110", Converter.convertDecimalToBinary(54,new StringBuilder()));
        assertEquals("10000000", Converter.convertDecimalToBinary(128,new StringBuilder()));
        assertEquals("00000000", Converter.convertDecimalToBinary(0,new StringBuilder()));
    }

    @Test
    public void convertToDecimalTest(){
        new Converter();
        assertEquals("105",Converter.convertToDecimal("01101001", Type.BINARY ));
        assertEquals("231",Converter.convertToDecimal("11100111", Type.BINARY ));
        assertEquals("0",Converter.convertToDecimal("00000000", Type.BINARY ));
        assertEquals("16",Converter.convertToDecimal("00010000", Type.BINARY ));

        assertEquals("11704", Converter.convertToDecimal("2DB8", Type.HEX));
        assertEquals("3567", Converter.convertToDecimal("0DEF", Type.HEX));
    }

    @Test
    public void convertToHexTest(){
        new Converter();
        assertEquals("13DB", Converter.convertDecimalToHex(5083,new StringBuilder()));
        assertEquals("0082", Converter.convertDecimalToHex(130,new StringBuilder()));

    }

    @Test
    public void convertIPv4ToString(){
        new Converter();
        String[] firstIp = {"128","178","30","2"};
        String[] secondIp = {"128","148","32","4"};
        assertEquals("128.178.30.2", Converter.convertIPv4ToString(firstIp));
        assertEquals("128.148.32.4", Converter.convertIPv4ToString(secondIp));
    }

    @Test
    public void convertTest(){
        new Converter();
        String[] firstIp = {"10000000","10110010","00011110","00000010"};//128.178.30.2
        String[] firstIpRes = {"128","178","30","2"};
        IPAddress firstIpAddress = new IPv4Address(firstIp, Type.BINARY);
        IPAddress firstIpAddressRes = new IPv4Address(firstIpRes, Type.DECIMAL);
        assertEquals(firstIpAddressRes.getIpAddressBlocks(),Converter.convert(firstIpAddress,Type.DECIMAL).getIpAddressBlocks());
        assertEquals(firstIpAddress.getIpAddressBlocks(),Converter.convert(firstIpAddressRes,Type.BINARY).getIpAddressBlocks());

    }

    @Test
    public void prefixToMaskTest() {
        int prefix0 = 0;
        String mask0 = "0.0.0.0";
        int prefix1 = 32;
        String mask1 = "255.255.255.255";
        int prefix2 = 24;
        String mask2 = "255.255.255.0";
        int prefix3 = 16;
        String mask3 = "255.255.0.0";
        assertEquals(mask0, Converter.prefixToMask(prefix0));
        assertEquals(mask0, Converter.prefixToMask(prefix0));
        assertEquals(mask0, Converter.prefixToMask(prefix0));
        assertEquals(mask0, Converter.prefixToMask(prefix0));
    }

    @Test
    public void isCollidingTest() {
        assertEquals(true, Converter.isColliding("10.0.0.0/24", "10.0.0.0/24"));
        assertEquals(false, Converter.isColliding("129.2.4.0/24", "10.0.0.0/24"));
        assertEquals(true, Converter.isColliding("192.168.0.0/16", "192.168.35.0/24"));
        assertEquals(true, Converter.isColliding("0.0.0.0/0", "192.168.254.0/24"));
        assertEquals(false, Converter.isColliding("172.50.5.0/16", "172.55.0.6/16"));
        assertEquals(false, Converter.isColliding("133.35.1.0/16", "134.35.1.0/16"));
    }
}
