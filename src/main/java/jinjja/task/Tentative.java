package jinjja.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a tentatively scheduled event that can have multiple possible time slots. Only one slot can be confirmed
 * at a time.
 */
public class Tentative extends Task {
    private static final DateTimeFormatter DATETIME_OUTPUT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");
    private static final DateTimeFormatter DATETIME_FILE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private List<TimeSlot> tentativeSlots;
    private TimeSlot confirmedSlot;

    /**
     * Represents a time slot with start and end times.
     */
    public static class TimeSlot {
        private LocalDateTime from;
        private LocalDateTime to;

        /**
         * Constructs a new TimeSlot with start and end times.
         *
         * @param from The start time of the slot
         * @param to The end time of the slot
         */
        public TimeSlot(LocalDateTime from, LocalDateTime to) {
            assert from != null : "Start time cannot be null";
            assert to != null : "End time cannot be null";
            assert !from.isAfter(to) : "Start time should not be after end time";
            this.from = from;
            this.to = to;
        }

        public LocalDateTime getFrom() {
            return from;
        }

        public LocalDateTime getTo() {
            return to;
        }

        public String toDisplayString() {
            return "from: " + from.format(DATETIME_OUTPUT) + " to: " + to.format(DATETIME_OUTPUT);
        }

        public String toFileString() {
            return from.format(DATETIME_FILE) + "|" + to.format(DATETIME_FILE);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            TimeSlot timeSlot = (TimeSlot) obj;
            return from.equals(timeSlot.from) && to.equals(timeSlot.to);
        }
    }

    /**
     * Constructs a new Tentative with the given description.
     *
     * @param description The description of the tentative event
     */
    public Tentative(String description) {
        super(description);
        this.tentativeSlots = new ArrayList<>();
        this.confirmedSlot = null;
    }

    /**
     * Adds a tentative time slot to this event.
     *
     * @param from The start time of the slot
     * @param to The end time of the slot
     */
    public void addTentativeSlot(LocalDateTime from, LocalDateTime to) {
        assert from != null : "Start time cannot be null";
        assert to != null : "End time cannot be null";
        assert !from.isAfter(to) : "Start time should not be after end time";

        TimeSlot slot = new TimeSlot(from, to);
        if (!tentativeSlots.contains(slot)) {
            tentativeSlots.add(slot);
        }
    }

    /**
     * Confirms one of the tentative slots by its index (1-based).
     *
     * @param slotIndex The 1-based index of the slot to confirm
     * @return true if confirmation was successful, false if index is invalid
     */
    public boolean confirmSlot(int slotIndex) {
        if (slotIndex < 1 || slotIndex > tentativeSlots.size()) {
            return false;
        }

        confirmedSlot = tentativeSlots.get(slotIndex - 1);
        return true;
    }

    /**
     * Removes a tentative slot by its index (1-based).
     *
     * @param slotIndex The 1-based index of the slot to remove
     * @return true if removal was successful, false if index is invalid
     */
    public boolean removeTentativeSlot(int slotIndex) {
        if (slotIndex < 1 || slotIndex > tentativeSlots.size()) {
            return false;
        }

        TimeSlot removedSlot = tentativeSlots.remove(slotIndex - 1);
        if (confirmedSlot != null && confirmedSlot.equals(removedSlot)) {
            confirmedSlot = null;
        }
        return true;
    }

    /**
     * Returns the list of tentative slots.
     *
     * @return A copy of the tentative slots list
     */
    public List<TimeSlot> getTentativeSlots() {
        return new ArrayList<>(tentativeSlots);
    }

    /**
     * Returns the confirmed slot.
     *
     * @return The confirmed slot, or null if no slot is confirmed
     */
    public TimeSlot getConfirmedSlot() {
        return confirmedSlot;
    }

    /**
     * Checks if this event has a confirmed slot.
     *
     * @return true if a slot is confirmed, false otherwise
     */
    public boolean isConfirmed() {
        return confirmedSlot != null;
    }

    /**
     * Returns the number of tentative slots.
     *
     * @return The number of tentative slots
     */
    public int getSlotCount() {
        return tentativeSlots.size();
    }

    @Override
    public String toFileFormat() {
        StringBuilder sb = new StringBuilder();
        sb.append("TE | ").append(super.toFileFormat());

        // Add confirmed slot (empty if none)
        sb.append(" | ");
        if (confirmedSlot != null) {
            sb.append(confirmedSlot.toFileString());
        }

        // Add tentative slots
        sb.append(" | ").append(tentativeSlots.size());
        for (TimeSlot slot : tentativeSlots) {
            sb.append(" | ").append(slot.toFileString());
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[TE]").append(super.toString());

        if (isConfirmed()) {
            sb.append(" (CONFIRMED: ").append(confirmedSlot.toDisplayString()).append(")");
        }

        if (!tentativeSlots.isEmpty()) {
            sb.append(" [TENTATIVE SLOTS: ");
            for (int i = 0; i < tentativeSlots.size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append((i + 1)).append(". ").append(tentativeSlots.get(i).toDisplayString());
            }
            sb.append("]");
        }

        return sb.toString();
    }
}
