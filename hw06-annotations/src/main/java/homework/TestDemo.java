package homework;

import homework.annotations.After;
import homework.annotations.Before;
import homework.annotations.Test;

public class TestDemo {

    @Before
    public void setUp() {
        System.out.println("Before...");
    }

    @Test
    public void failedTest() {
        throw new RuntimeException("Something went wrong");
    }

    @Test
    public void successTest() {
        System.out.println("This test was successfull");
    }

    @After
    public void tearDown() {
        System.out.println("After...");
    }

}
