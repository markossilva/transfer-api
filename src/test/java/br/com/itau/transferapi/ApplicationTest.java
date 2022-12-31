package br.com.itau.transferapi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
public class ApplicationTest {

    @Test
    void applicationContextLoads() {
        Application.main(new String[]{});
        assertTrue(true);
    }
}
