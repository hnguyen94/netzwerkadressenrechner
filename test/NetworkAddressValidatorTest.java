import gui.NetworkAddressValidator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;



public class NetworkAddressValidatorTest {

    @Test
    public void CorrectNetworkAddressTest() {
        assertEquals(NetworkAddressValidator.validate("111.111.111.0/1"), true);
    }

    @Test
    public void FalseWhenIpIsNotADigit() {
        assertEquals(NetworkAddressValidator.validate("111.111.111.a/1"), false);
    }

    @Test
    public void FalseWhenPrefixIsNotADigit() {
        assertEquals(NetworkAddressValidator.validate("111.111.111.1/a"), false);
    }

    @Test
    public void FalseWhenPrefixIsOver32() {
        assertEquals(NetworkAddressValidator.validate("1.1.1.1/33"), false);
    }

    @Test
    public void FalseWhenStringIsEmpty() {
        assertEquals(NetworkAddressValidator.validate(""), false);
    }

    @Test
    public void FalseWhenBlockHasFourDigit() {
        assertEquals(NetworkAddressValidator.validate("1111.1111.1111.1111/1"), false);
    }

    @Test
    public void FalseWhenNumberIsOver255() {
        assertEquals(NetworkAddressValidator.validate("256.111.111.111/1"), false);
    }
}


