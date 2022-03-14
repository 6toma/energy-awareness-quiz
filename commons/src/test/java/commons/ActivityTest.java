package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActivityTest {

    @Test
    void constructorTest1() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        assertNotNull(activity);
    }

    @Test
    void constructorTest2() {
        Activity activity = new Activity("image_a", "a", 10L, "a");
        assertNotNull(activity);
    }

    @Test
    void readabilityTest() {
        Activity activity = new Activity("image_a", "a", 1L, "a");
        assertEquals("Activity{id=null, image_path='image_a', title='a', consumption_in_wh=1, source='a'}", activity.toString());
    }

    @Test
    void equalsTest1() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        assertEquals(activity, activity);
    }

    @Test
    void equalsTest2() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        Activity activity2 = new Activity("1L", "image_a", "a", 1L, "a");
        assertEquals(activity, activity2);
    }

    @Test
    void equalsTest3() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        Activity activity2 = new Activity("1L", "image_a", "b", 1L, "a");
        assertNotEquals(activity, activity2);
    }

    @Test
    void getIdTest() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        assertEquals("1L", activity.getId());
    }

    @Test
    void setIdTest() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        activity.setId("2L");
        assertEquals("2L", activity.getId());
    }

    @Test
    void getImagePathTest() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        assertEquals("image_a", activity.getImage_path());
    }

    @Test
    void setImagePathTest() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        activity.setImage_path("image_b");
        assertEquals("image_b", activity.getImage_path());
    }

    @Test
    void getTitleTest() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        assertEquals("a", activity.getTitle());
    }

    @Test
    void setTitleTest() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        activity.setTitle("b");
        assertEquals("b", activity.getTitle());
    }

    @Test
    void getConsumption_in_whTest() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        assertEquals(1, activity.getConsumption_in_wh());
    }

    @Test
    void setConsumption_in_whTest() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        activity.setConsumption_in_wh(2L);
        assertEquals(2, activity.getConsumption_in_wh());
    }

    @Test
    void getSourceTest() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        assertEquals("a", activity.getSource());
    }

    @Test
    void setSourceTest() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        activity.setSource("c");
        assertEquals("c", activity.getSource());
    }

}