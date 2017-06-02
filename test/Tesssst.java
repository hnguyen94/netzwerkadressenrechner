import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class Tesssst {
    @Test
    public void teeesssstt(){
        String network = "192.168.20.0/24";
        String[] ipAndSuffix = network.split("\\/");

        String[] ip = ipAndSuffix[0].split("\\.");

    }
}
