package com.wzes.huddle;

import com.wzes.huddle.service.Identity;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

        boolean tongjiUser = Identity.Login("1552730", "427053");
        assertEquals(true, tongjiUser);
    }
}