package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActivityTest {

    @Test
    void constructorTest1() {
        Activity activity = new Activity(1L, "a", 1, "a");
        assertNotNull(activity);
    }

    @Test
    void constructorTest2() {
        Activity activity = new Activity("a", 10, "a");
        assertNotNull(activity);
    }

    @Test
    void readabilityTest() {
        Activity activity = new Activity(1L, "a", 1, "a");
        assertEquals("Activity{id=1, title='a', consumption_in_wh=1, source='a'}", activity.toString());
    }

    @Test
    void equalsTest1() {
        Activity activity = new Activity(1L, "a", 1, "a");
        assertTrue(activity.equals(activity));
    }

    @Test
    void equalsTest2() {
        Activity activity = new Activity(1L, "a", 1, "a");
        Activity activity2 = new Activity(1L, "a", 1, "a");
        assertTrue(activity.equals(activity2));
    }

    @Test
    void equalsTest3() {
        Activity activity = new Activity(1L, "a", 1, "a");
        Activity activity2 = new Activity(1L, "b", 1, "a");
        assertFalse(activity.equals(activity2));
    }

    @Test
    void getIdTest() {
        Activity activity = new Activity(1L, "a", 1, "a");
        assertEquals(1L, activity.getId());
    }

    @Test
    void setIdTest() {
        Activity activity = new Activity(1L, "a", 1, "a");
        activity.setId(2L);
        assertEquals(2L, activity.getId());
    }

    @Test
    void getTitleTest() {
        Activity activity = new Activity(1L, "a", 1, "a");
        assertEquals("a", activity.getTitle());
    }

    @Test
    void setTitleTest() {
        Activity activity = new Activity(1L, "a", 1, "a");
        activity.setTitle("b");
        assertEquals("b", activity.getTitle());
    }

    @Test
    void getConsumption_in_whTest() {
        Activity activity = new Activity(1L, "a", 1, "a");
        assertEquals(1, activity.getConsumption_in_wh());
    }

    @Test
    void setConsumption_in_whTest() {
        Activity activity = new Activity(1L, "a", 1, "a");
        activity.setConsumption_in_wh(2);
        assertEquals(2, activity.getConsumption_in_wh());
    }

    @Test
    void getSourceTest() {
        Activity activity = new Activity(1L, "a", 1, "a");
        assertEquals("a", activity.getSource());
    }

    @Test
    void setSourceTest() {
        Activity activity = new Activity(1L, "a", 1, "a");
        activity.setSource("c");
        assertEquals("c", activity.getSource());
    }


}