import logic.Converter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ConverterTest {

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
