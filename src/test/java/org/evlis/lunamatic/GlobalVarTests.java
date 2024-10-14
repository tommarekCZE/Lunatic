package org.evlis.lunamatic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GlobalVarTests {

    private ServerMock server;

    @BeforeEach
    public void setUp()
    {
        server = MockBukkit.mock();
    }

    @AfterEach
    public void tearDown()
    {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Verify that all global flags initialize to FALSE:")
    public void allFlagsStartAsFalse() {
        assertEquals(false, GlobalVars.debug);
        assertEquals(false, GlobalVars.bloodMoonToday);
        assertEquals(false, GlobalVars.harvestMoonToday);
        assertEquals(false, GlobalVars.bloodMoonNow);
        assertEquals(false, GlobalVars.harvestMoonNow);
    }
}
