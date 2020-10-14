import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();
    // Your tests go here.
    @Test
    public void testEqualChars() {
        assertTrue(offByOne.equalChars('a','b'));
        assertTrue(offByOne.equalChars('E','D'));
        assertTrue(offByOne.equalChars('1','2'));
        assertTrue(offByOne.equalChars('&','%'));
        assertTrue(offByOne.equalChars('r','q'));
        assertFalse(offByOne.equalChars('t','b'));
        assertFalse(offByOne.equalChars('a','a'));
        assertFalse(offByOne.equalChars('a','e'));
    }

}
