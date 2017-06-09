import logic.Converter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ConverterTest {

    @Test
    public void convertToBinaryTest(){
        assertEquals("11000010.10101000.00000010.00000001", Converter.convertIpToBinary("194.168.2.1"));
        assertEquals("10000000.10100100.00000000.00000111", Converter.convertIpToBinary("128.164.0.7"));
    }

    @Test
    public void convertIPBlockToBinary(){
        assertEquals("10000000", Converter.convertIpBlockToBinary(new StringBuilder(),128));
        assertEquals("10100100", Converter.convertIpBlockToBinary(new StringBuilder(),164));
        assertEquals("00000000", Converter.convertIpBlockToBinary(new StringBuilder(),0));
        assertEquals("00000111", Converter.convertIpBlockToBinary(new StringBuilder(),7));
        assertEquals("00111000", Converter.convertIpBlockToBinary(new StringBuilder(),56));

    }

    @Test
    public void convertToHexTest(){
        assertEquals("C2.A8.02.01", Converter.convertIpToHex("194.168.2.1"));
        assertEquals("87.A5.00.38", Converter.convertIpToHex("135.165.0.56"));
    }

    @Test
    public void convertIpBlockToHex(){
        assertEquals("87", Converter.convertIpBlockToHex(new StringBuilder(),135));
        assertEquals("A5", Converter.convertIpBlockToHex(new StringBuilder(),165));
        assertEquals("00", Converter.convertIpBlockToHex(new StringBuilder(),0));
        assertEquals("07", Converter.convertIpBlockToHex(new StringBuilder(),7));
        assertEquals("38", Converter.convertIpBlockToHex(new StringBuilder(),56));
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
        assertEquals(mask1, Converter.prefixToMask(prefix1));
        assertEquals(mask2, Converter.prefixToMask(prefix2));
        assertEquals(mask3, Converter.prefixToMask(prefix3));
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
