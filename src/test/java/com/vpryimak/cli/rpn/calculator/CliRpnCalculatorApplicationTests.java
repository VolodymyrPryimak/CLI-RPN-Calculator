package com.vpryimak.cli.rpn.calculator;

import com.vpryimak.cli.rpn.calculator.service.RpnCalculator;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CliRpnCalculatorApplicationTests {

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
    void firstTestScenario() {
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
    void secondTestScenario() {
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
    void thirdTestScenario() {
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
    void fourthTestScenario() {
        Assert.assertEquals(Double.valueOf(5), rpnCalculator.process("5"));
        Assert.assertEquals(Double.valueOf(9), rpnCalculator.process("9"));
        Assert.assertEquals(Double.valueOf(1), rpnCalculator.process("1"));
        Assert.assertEquals(Double.valueOf(8), rpnCalculator.process("-"));
        Assert.assertEquals(Double.valueOf(0.625), rpnCalculator.process("/"));
    }
}
