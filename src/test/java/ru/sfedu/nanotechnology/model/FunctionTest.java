package ru.sfedu.nanotechnology.model;

import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.sfedu.nanotechnology.controller.RegexFunc;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


public class FunctionTest {

    private RegexFunc regexFunc = new RegexFunc();

    /**
     * \
     * I'm going to check RegexFunc class First
     */
    @Test
    public void testCheckXInSomeDegree() {
        HashMap<Double, Double> xinSomeDegr = regexFunc.checkXinSomeDegr("-4x^2+4.3x+5.5'");
        assertTrue(xinSomeDegr.containsKey(-4.0));
        assertTrue(xinSomeDegr.containsValue(2.0));
        assertThat(xinSomeDegr.get(-4.0), is(2.0));
    }

    @Test
    public void testCheckXInFirstDegree() {
        double xinFirstDegr = regexFunc.checkXinFirstDegr("4x^2+4.3x+5.5'");
        assertThat(xinFirstDegr, is(4.3));
    }

    @Test
    public void testCheckFreeCoef() {
        double freeCoef = regexFunc.checkFreeCoef("x^2+5.5'");
        Assert.assertTrue(freeCoef == 5.5);
    }

}


    /*
    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    */
    /* Some Calculation tests
        I don't know Why are they here
        I don't need them
    @Test
    public void testCalculate0() {
        System.out.println("calculate");
        double x = 0.0;
        Function instance = new Function();
        double expResult = 0.0;
        double result = instance.calculate(x);
        assertEquals(expResult, result, 0.0);
    }

    @Test
    public void testCalculate1() {
        System.out.println("calculate");
        double x = 1.0;
        Function instance = new Function();
        double expResult = 1.0;
        double result = instance.calculate(x);
        assertEquals(expResult, result, 0.0);
    }
*/
