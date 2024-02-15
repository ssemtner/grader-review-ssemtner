import static org.junit.Assert.*;

import org.junit.*;
import org.junit.rules.Timeout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class IsMoon implements StringChecker {
    public boolean checkString(String s) {
        return s.equalsIgnoreCase("moon");
    }
}

class CheckContains implements StringChecker {
    private String contains;

    CheckContains(String contains) {
        this.contains = contains;
    }

    @Override
    public boolean checkString(String s) {
        return s.contains(contains);
    }
}

public class TestListExamples {
    @Rule
    public Timeout timeout =
            Timeout.seconds(1); // 1 second max per method tested

    @Test
    public void testMergeRightEnd() {
        List<String> left = Arrays.asList("a", "b", "c");
        List<String> right = Arrays.asList("a", "d");
        List<String> merged = ListExamples.merge(left, right);
        List<String> expected = Arrays.asList("a", "a", "b", "c", "d");
        assertEquals(expected, merged);
    }

    @Test
    public void testFilter() {
        ArrayList<String> input1 = new ArrayList<>();
        input1.add("a");
        input1.add("b");
        input1.add("ab");
        input1.add("bc");
        input1.add("abc");

        assertArrayEquals(
                new String[] {"a", "ab", "abc"},
                ListExamples.filter(input1, new CheckContains("a")).toArray());

        ArrayList<String> input2 = new ArrayList<>();
        input2.add("no");
        input2.add("yes");
        input2.add("help");
        input2.add("ahhhh");
        input2.add("hello");

        assertArrayEquals(
                new String[] {"help", "hello"},
                ListExamples.filter(input2, new CheckContains("he")).toArray());
    }

    @Test
    public void testFilterEmpty() {
        ArrayList<String> input = new ArrayList<>();
        input.add("a");
        input.add("b");
        input.add("ab");
        input.add("bc");
        input.add("abc");

        assertArrayEquals(
                new String[] {},
                ListExamples.filter(input, new IsMoon()).toArray());
    }

    @Test
    public void testMergeLeftEnd() {
        ArrayList<String> input1 = new ArrayList<>();
        input1.add("apple");
        input1.add("orange");
        input1.add("strawberry");

        ArrayList<String> input2 = new ArrayList<>();
        input2.add("attack");
        input2.add("battle");
        input2.add("locate");
        input2.add("yonder");

        assertArrayEquals(
                new String[] {
                    "apple",
                    "attack",
                    "battle",
                    "locate",
                    "orange",
                    "strawberry",
                    "yonder"
                },
                ListExamples.merge(input1, input2).toArray());
    }

    @Test
    public void testMergeObjects() {
      ArrayList<String> input1 = new ArrayList<>();
      var item1 = new String("a");
      var item2 = new String("b");
      input1.add(item1);
      input1.add(item2);

      ArrayList<String> input2 = new ArrayList<>();
      var item3 = new String("c");
      var item4 = new String("d");
      input2.add(item3);
      input2.add(item4);

      var output = ListExamples.merge(input1, input2);
      assertSame(item1, output.get(0));
      assertSame(item2, output.get(1));
      assertSame(item3, output.get(2));
      assertSame(item4, output.get(3));
    }
}
