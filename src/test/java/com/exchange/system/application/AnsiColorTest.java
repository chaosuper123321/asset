package com.exchange.system.application;

import com.exchange.system.display.AnsiColor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class AnsiColorTest {
    @Test
    void testForegroundColorFormatting() {
        String text = "Hello, Akina!";
        assertEquals("\033[30;4mHello, Akina!\033[0m", AnsiColor.BLACK.format(text));
        assertEquals("\033[31;4mHello, Akina!\033[0m", AnsiColor.RED.format(text));
        assertEquals("\033[32;4mHello, Akina!\033[0m", AnsiColor.GREEN.format(text));
        assertEquals("\033[33;4mHello, Akina!\033[0m", AnsiColor.YELLOW.format(text));
        assertEquals("\033[34;4mHello, Akina!\033[0m", AnsiColor.BLUE.format(text));
        assertEquals("\033[35;4mHello, Akina!\033[0m", AnsiColor.MAGENTA.format(text));
        assertEquals("\033[36;4mHello, Akina!\033[0m", AnsiColor.CYAN.format(text));
        assertEquals("\033[37;4mHello, Akina!\033[0m", AnsiColor.WHITE.format(text));
    }
}
