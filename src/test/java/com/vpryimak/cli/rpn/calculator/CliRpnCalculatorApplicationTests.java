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
        Assert.assertEquals(Double.valueOf(5), rpnCalculator.process("5"));
        Assert.assertEquals(Double.valueOf(8), rpnCalculator.process("8"));
        Assert.assertEquals(Double.valueOf(13), rpnCalculator.process("+"));
    }

    /**
     * > 5 8 +
     * 13.0
     * > 13 -
     * 0.0
     */
    @Test
    public void secondScenarioTest() {
        Assert.assertEquals(Double.valueOf(13.0), rpnCalculator.process("5 8 +"));
        Assert.assertEquals(Double.valueOf(0.0), rpnCalculator.process("13 -"));
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
        Assert.assertEquals(Double.valueOf(-3.0), rpnCalculator.process("-3"));
        Assert.assertEquals(Double.valueOf(-2.0), rpnCalculator.process("-2"));
        Assert.assertEquals(Double.valueOf(6.0), rpnCalculator.process("*"));
        Assert.assertEquals(Double.valueOf(5.0), rpnCalculator.process("5"));
        Assert.assertEquals(Double.valueOf(11.0), rpnCalculator.process("+"));
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
        Assert.assertEquals(Double.valueOf(5), rpnCalculator.process("5"));
        Assert.assertEquals(Double.valueOf(9), rpnCalculator.process("9"));
        Assert.assertEquals(Double.valueOf(1), rpnCalculator.process("1"));
        Assert.assertEquals(Double.valueOf(8), rpnCalculator.process("-"));
        Assert.assertEquals(Double.valueOf(0.625), rpnCalculator.process("/"));
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
}
