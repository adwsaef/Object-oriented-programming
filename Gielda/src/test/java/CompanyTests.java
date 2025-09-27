import market.Company;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CompanyTests {
    @Test
    public void testCompany(){

        assertDoesNotThrow( () -> new Company("A", 1));
        assertDoesNotThrow( () -> new Company("BZCDE", 1));

        assertThrows(RuntimeException.class, () -> new Company("aAA", 1));
        assertThrows(RuntimeException.class, () -> new Company("", 1));
        assertThrows(RuntimeException.class, () -> new Company("BBBc", 1));
        assertThrows(RuntimeException.class, () -> new Company("BBBBBB", 1));
        assertThrows(RuntimeException.class, () -> new Company("AB", 0));
        assertThrows(RuntimeException.class, () -> new Company("ZZ", -1));
        assertThrows(RuntimeException.class, () -> new Company("?!?!", 1));
        assertThrows(RuntimeException.class, () -> new Company("9222", 1));
        assertThrows(RuntimeException.class, () -> new Company("AB AB", 1));
        assertThrows(RuntimeException.class, () -> new Company("AB|BA", 1));

    }
}
