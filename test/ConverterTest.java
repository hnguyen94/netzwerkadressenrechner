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


}
