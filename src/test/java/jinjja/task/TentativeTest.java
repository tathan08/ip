package jinjja.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for Tentative functionality.
 */
public class TentativeTest {
    private Tentative tentative;
    private LocalDateTime slot1From;
    private LocalDateTime slot1To;
    private LocalDateTime slot2From;
    private LocalDateTime slot2To;

    @BeforeEach
    public void setUp() {
        tentative = new Tentative("Team meeting");
        slot1From = LocalDateTime.of(2025, 9, 15, 10, 0);
        slot1To = LocalDateTime.of(2025, 9, 15, 11, 0);
        slot2From = LocalDateTime.of(2025, 9, 16, 14, 0);
        slot2To = LocalDateTime.of(2025, 9, 16, 15, 0);
    }

    @Test
    public void addTentativeSlot_validSlot_success() {
        tentative.addTentativeSlot(slot1From, slot1To);
        assertEquals(1, tentative.getSlotCount());
        assertFalse(tentative.isConfirmed());
    }

    @Test
    public void addTentativeSlot_multipleSlots_success() {
        tentative.addTentativeSlot(slot1From, slot1To);
        tentative.addTentativeSlot(slot2From, slot2To);
        assertEquals(2, tentative.getSlotCount());
        assertFalse(tentative.isConfirmed());
    }

    @Test
    public void confirmSlot_validSlot_success() {
        tentative.addTentativeSlot(slot1From, slot1To);
        tentative.addTentativeSlot(slot2From, slot2To);

        assertTrue(tentative.confirmSlot(1));
        assertTrue(tentative.isConfirmed());
        assertEquals(slot1From, tentative.getConfirmedSlot().getFrom());
        assertEquals(slot1To, tentative.getConfirmedSlot().getTo());
    }

    @Test
    public void confirmSlot_invalidSlot_failure() {
        tentative.addTentativeSlot(slot1From, slot1To);

        assertFalse(tentative.confirmSlot(2)); // Invalid slot number
        assertFalse(tentative.isConfirmed());
    }

    @Test
    public void removeTentativeSlot_validSlot_success() {
        tentative.addTentativeSlot(slot1From, slot1To);
        tentative.addTentativeSlot(slot2From, slot2To);

        assertTrue(tentative.removeTentativeSlot(1));
        assertEquals(1, tentative.getSlotCount());
    }

    @Test
    public void removeTentativeSlot_confirmedSlot_removesConfirmation() {
        tentative.addTentativeSlot(slot1From, slot1To);
        tentative.confirmSlot(1);
        assertTrue(tentative.isConfirmed());

        assertTrue(tentative.removeTentativeSlot(1));
        assertFalse(tentative.isConfirmed());
        assertEquals(0, tentative.getSlotCount());
    }

    @Test
    public void toString_withTentativeSlots_showsSlots() {
        tentative.addTentativeSlot(slot1From, slot1To);
        tentative.addTentativeSlot(slot2From, slot2To);

        String result = tentative.toString();
        assertTrue(result.contains("[TE]"));
        assertTrue(result.contains("Team meeting"));
        assertTrue(result.contains("TENTATIVE SLOTS:"));
        assertTrue(result.contains("1."));
        assertTrue(result.contains("2."));
    }

    @Test
    public void toString_withConfirmedSlot_showsConfirmation() {
        tentative.addTentativeSlot(slot1From, slot1To);
        tentative.confirmSlot(1);

        String result = tentative.toString();
        assertTrue(result.contains("CONFIRMED:"));
        assertTrue(result.contains("Sep 15 2025"));
    }
}
