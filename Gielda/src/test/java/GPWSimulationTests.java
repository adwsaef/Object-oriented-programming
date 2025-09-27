import investors.Investor;
import simulation.GPWSimulation;
import org.junit.jupiter.api.Test;

import java.util.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class GPWSimulationTests {
    public static String[] castTable(String a, String b) {
        String[] tmp = new String[2];
        tmp[0] = a;
        tmp[1] = b;
        return tmp;
    }

    @Test
    public void testOpening() {

        assertDoesNotThrow(() -> new GPWSimulation(castTable("src/test/resources/CorrectInput1", "10")));
        assertDoesNotThrow(() -> new GPWSimulation(castTable("src/test/resources/CorrectInput1", "0")));
        assertDoesNotThrow(() -> new GPWSimulation(castTable("src/test/resources/CorrectInput2", "10")));
        assertDoesNotThrow(() -> new GPWSimulation(castTable("src/test/resources/CorrectInput3", "10")));
        assertDoesNotThrow(() -> new GPWSimulation(castTable("src/test/resources/CorrectInput4", "10")));
        assertDoesNotThrow(() -> new GPWSimulation(castTable("src/test/resources/CorrectInput5", "10")));
        assertDoesNotThrow(() -> new GPWSimulation(castTable("src/test/resources/CorrectInput6", "10")));

        assertThrows(RuntimeException.class, () -> new GPWSimulation(castTable("src/test/resources/CorrectInput1", "-1")));
        assertThrows(RuntimeException.class, () -> new GPWSimulation(castTable("src/test/resources/CorrectInput1", "1A")));
        assertThrows(RuntimeException.class, () -> new GPWSimulation(castTable("src/test/resources/CorrectInput1", "10 ")));
        assertThrows(RuntimeException.class, () -> new GPWSimulation(castTable("src/test/resources/CorrectInput1", "?? !")));

        assertThrows(RuntimeException.class, () -> new GPWSimulation(castTable("src/test/resources/inCorrectInput1", "10")));
        assertThrows(RuntimeException.class, () -> new GPWSimulation(castTable("src/test/resources/inCorrectInput2", "10")));
        assertThrows(RuntimeException.class, () -> new GPWSimulation(castTable("src/test/resources/inCorrectInput3", "10")));
        assertThrows(RuntimeException.class, () -> new GPWSimulation(castTable("src/test/resources/inCorrectInput4", "10")));
        assertThrows(RuntimeException.class, () -> new GPWSimulation(castTable("src/test/resources/inCorrectInput5", "10")));
        assertThrows(RuntimeException.class, () -> new GPWSimulation(castTable("src/test/resources/inCorrectInput6", "10")));
        assertThrows(RuntimeException.class, () -> new GPWSimulation(castTable("src/test/resources/inCorrectInput7", "10")));
        assertThrows(RuntimeException.class, () -> new GPWSimulation(castTable("src/test/resources/inCorrectInput8", "10")));
        assertThrows(RuntimeException.class, () -> new GPWSimulation(castTable("src/test/resources/inCorrectInput9", "10")));
        assertThrows(RuntimeException.class, () -> new GPWSimulation(castTable("src/test/resources/inCorrectInput10", "10")));
        assertThrows(RuntimeException.class, () -> new GPWSimulation(castTable("src/test/resources/inCorrectInput11", "10")));
        assertThrows(RuntimeException.class, () -> new GPWSimulation(castTable("src/test/resources/inCorrectInput12", "10")));
        assertThrows(RuntimeException.class, () -> new GPWSimulation(castTable("src/test/resources/inCorrectInput13", "10")));

    }

    public void processTest(String arg1, String arg2, int moneySum, Map<String, Integer> ActionsSum) throws IOException {
        GPWSimulation s = new GPWSimulation(castTable(arg1, arg2));
        Investor[] res = s.performSimulation();
        int money = 0;
        Map<String, Integer> actions = new HashMap<>();
        for (Investor re : res) {
            money += re.getMoney();
            Map<String, Integer> all = re.getAllActions();
            for (Map.Entry<String, Integer> entry : all.entrySet()) {
                actions.put(entry.getKey(), actions.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }
        }

        actions.entrySet().removeIf(entry -> entry.getValue().equals(0));
        ActionsSum.entrySet().removeIf(entry -> entry.getValue().equals(0));

        assertEquals(actions, ActionsSum);
        assertEquals(money, moneySum);
    }

    @Test
    public void testMoneyActionsSum() {
        {
            Map<String, Integer> actions = new HashMap<>();
            actions.put("APL", 5 * 6);
            actions.put("MSFT", 15 * 6);
            actions.put("GOOGL", 3 * 6);
            int money = 100000 * 6;
            assertDoesNotThrow(() -> processTest("src/test/resources/CorrectInput1", "10", money, actions));
        }
        {
            Map<String, Integer> actions = new HashMap<>();
            actions.put("APL", 5 * 6);
            actions.put("MSFT", 15 * 6);
            actions.put("GOOGL", 3 * 6);
            int money = 100000 * 6;
            assertDoesNotThrow(() -> processTest("src/test/resources/CorrectInput2", "10", money, actions));
        }
        {
            Map<String, Integer> actions = new HashMap<>();
            actions.put("APL", 5 * 2);
            actions.put("MSFT", 15 * 2);
            actions.put("GOOGL", 3 * 2);
            int money = 100000 * 2;
            assertDoesNotThrow(() -> processTest("src/test/resources/CorrectInput3", "10", money, actions));
        }
        {
            Map<String, Integer> actions = new HashMap<>();
            actions.put("APL", 10 * 6);
            actions.put("MSFT", 15 * 6);
            actions.put("GOOGL", 3 * 6);
            int money = 0 * 6;
            assertDoesNotThrow(() -> processTest("src/test/resources/CorrectInput4", "10", money, actions));
        }
        {
            Map<String, Integer> actions = new HashMap<>();
            actions.put("APL", 0 * 6);
            actions.put("MSFT", 15 * 6);
            actions.put("GOOGL", 3 * 6);
            int money = 100000 * 6;
            assertDoesNotThrow(() -> processTest("src/test/resources/CorrectInput5", "10", money, actions));
        }
        {
            Map<String, Integer> actions = new HashMap<>();
            actions.put("APL", 0 * 6);
            actions.put("MSFT", 0 * 6);
            actions.put("GOOGL", 0 * 6);
            int money = 100000 * 6;
            assertDoesNotThrow(() -> processTest("src/test/resources/CorrectInput6", "10", money, actions));
        }
    }
}