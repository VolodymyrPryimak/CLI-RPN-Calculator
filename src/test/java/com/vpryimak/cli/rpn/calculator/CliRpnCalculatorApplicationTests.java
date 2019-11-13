package com.vpryimak.cli.rpn.calculator;

import com.vpryimak.cli.rpn.calculator.service.RpnCalculator;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Input;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
public class CliRpnCalculatorApplicationTests {

    @Autowired
    private Shell shell;

    @Autowired
    RpnCalculator rpnCalculator;

    /**
     * > 5
     * 5
     * > 8
     * 8
     * > +
     * 13
     */
    @Test
    public void firstScenarioTest() {
        Assert.assertEquals(Double.valueOf(5), rpnCalculator.processElement("5"));
        Assert.assertEquals(Double.valueOf(8), rpnCalculator.processElement("8"));
        Assert.assertEquals(Double.valueOf(13), rpnCalculator.processElement("+"));
    }

    /**
     * > 5 8 +
     * 13.0
     * > 13 -
     * 0.0
     */
    @Test
    public void secondScenarioTest() {
        Assert.assertEquals(Double.valueOf(13.0), rpnCalculator.processElements("5 8 +"));
        Assert.assertEquals(Double.valueOf(0.0), rpnCalculator.processElements("13 -"));
    }

    /**
     * > -3
     * -3.0
     * > -2
     * -2.0
     * > *
     * 6.0
     * > 5
     * 5.0
     * > +
     * 11.0
     */
    @Test
    public void thirdScenarioTest() {
        Assert.assertEquals(Double.valueOf(-3.0), rpnCalculator.processElement("-3"));
        Assert.assertEquals(Double.valueOf(-2.0), rpnCalculator.processElement("-2"));
        Assert.assertEquals(Double.valueOf(6.0), rpnCalculator.processElement("*"));
        Assert.assertEquals(Double.valueOf(5.0), rpnCalculator.processElement("5"));
        Assert.assertEquals(Double.valueOf(11.0), rpnCalculator.processElement("+"));
    }

    /**
     * > 5
     * 5
     * > 9
     * 9
     * > 1
     * 1
     * > -
     * 8
     * > /
     * 0.625
     */
    @Test
    public void fourthTestScenario() {
        Assert.assertEquals(Double.valueOf(5), rpnCalculator.processElement("5"));
        Assert.assertEquals(Double.valueOf(9), rpnCalculator.processElement("9"));
        Assert.assertEquals(Double.valueOf(1), rpnCalculator.processElement("1"));
        Assert.assertEquals(Double.valueOf(8), rpnCalculator.processElement("-"));
        Assert.assertEquals(Double.valueOf(0.625), rpnCalculator.processElement("/"));
    }

    @Test
    public void firstScenarioShellTest() {
        Assert.assertEquals(shell.evaluate(() -> "calc 5"), "5.00");
        Assert.assertEquals(shell.evaluate(() -> "calc 8"), "8.00");
        Assert.assertEquals(shell.evaluate(() -> "calc +"), "13.00");
    }

    /**
     * > 5 8 +
     * 13.0
     * > 13 -
     * 0.0
     */
    @Test
    public void secondScenarioShellTest() {
        Input input1 = new Input() {
            @Override
            public String rawText() {
                return null;
            }

            @Override
            public List<String> words(){
                return Arrays.asList("calc","5 8 +");
            }
        };
        Input input2 = new Input() {
            @Override
            public String rawText() {
                return null;
            }

            @Override
            public List<String> words(){
                return Arrays.asList("calc","13 -");
            }
        };
        Assert.assertEquals(shell.evaluate(input1), "13.00");
        Assert.assertEquals(shell.evaluate(input2), "0.00");
    }

    /**
     * > -3
     * -3.0
     * > -2
     * -2.0
     * > *
     * 6.0
     * > 5
     * 5.0
     * > +
     * 11.0
     */
    @Test
    public void thirdScenarioShellTest() {
        Assert.assertEquals(shell.evaluate(() -> "calc -3"), "-3.00");
        Assert.assertEquals(shell.evaluate(() -> "calc -2"), "-2.00");
        Assert.assertEquals(shell.evaluate(() -> "calc *"), "6.00");
        Assert.assertEquals(shell.evaluate(() -> "calc 5"), "5.00");
        Assert.assertEquals(shell.evaluate(() -> "calc +"), "11.00");
    }

    /**
     * > 5
     * 5
     * > 9
     * 9
     * > 1
     * 1
     * > -
     * 8
     * > /
     * 0.625
     */
    @Test
    public void fourthScenarioShellTest() {
        Assert.assertEquals(shell.evaluate(() -> "calc 5"), "5.00");
        Assert.assertEquals(shell.evaluate(() -> "calc 9"), "9.00");
        Assert.assertEquals(shell.evaluate(() -> "calc 1"), "1.00");
        Assert.assertEquals(shell.evaluate(() -> "calc -"), "8.00");
        Assert.assertEquals(shell.evaluate(() -> "calc /"), "0.62");
    }

    @Test
    public void fourthScenarioShellTestWithExtraSpacesAndComma() {
        Assert.assertEquals(shell.evaluate(() -> "calc 5.0"), "5.00");
        Assert.assertEquals(shell.evaluate(() -> "calc 9,00     "), "9.00");
        Assert.assertEquals(shell.evaluate(() -> "calc     1 "), "1.00");
        Assert.assertEquals(shell.evaluate(() -> "calc -    "), "8.00");
        Assert.assertEquals(shell.evaluate(() -> "calc      /"), "0.62");
    }

    @Test
    public void nullTest() {
        Assert.assertEquals("0.00 (Cannot perform last operation because of: 'For input string: \"null\"')",
                shell.evaluate(() -> "calc null"));
        Assert.assertTrue(
                shell.evaluate(() -> "calc ").toString().contains("Parameter '--in string' should be specified"));
    }
}
