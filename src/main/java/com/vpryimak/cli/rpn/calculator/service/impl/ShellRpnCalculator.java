package com.vpryimak.cli.rpn.calculator.service.impl;

import com.vpryimak.cli.rpn.calculator.service.RpnCalculator;
import com.vpryimak.cli.rpn.calculator.service.RpnOperationSupport;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * Spring Shell allows one to easily create such a runnable application,
 * where the user will enter textual commands that will get executed until the
 * program terminates.
 */
@Slf4j
@ShellComponent
public class ShellRpnCalculator implements RpnOperationSupport<String> {

    /**
     * Prototype bean of RpnCalculator.
     */
    private final RpnCalculator rpnCalculator;

    /**
     * NumberFormat for converting double to desired string result.
     */
    private final NumberFormat formatter;

    /**
     * @param rpnCalculator Prototype bean of RpnCalculator component.
     * @param decimalFormat Format for {@link ShellRpnCalculator#formatter}
     */
    @Autowired
    public ShellRpnCalculator(RpnCalculator rpnCalculator,
            @Value("${rpn.shell.decimalFormat}") String decimalFormat) {
        this.rpnCalculator = rpnCalculator;
        formatter = new DecimalFormat(decimalFormat);
    }

    /**
     * Call 'calc' shell command for computing RPN result.
     * @param in String request for calculation.
     * @return Formatted string result.
     */
    @ShellMethod(key = "calc", value = "Reverse Polish Notation Calculator")
    public String calc(
            @ShellOption(help = "Enter number or operation. Supported operations: '+/*-'.") String in) {
        try {
            return calculate(in);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.debug(e.getMessage(), e);
            return formatter.format(rpnCalculator.getLast())
                    + " (Cannot perform last operation because of: '" + e.getMessage() + "')";
        }
    }

    /**
     * Do some initial preparation for preventing common mistakes.
     * @param in Input request string.
     * @return Normalized string.
     */
    private String normaliseInput(String in) {
        in = in.trim();
        in = in.replaceAll(",", ".");
        in = in.replaceAll(" {2}", " ");
        return in;
    }

    /**
     * Call 'reset' shell command for resetting stack of numbers.
     * @return Text message - result of operation.
     */
    @ShellMethod("Reset stack of numbers")
    public String reset() {
        log.info("Resetting stack of numbers...");
        rpnCalculator.reset();
        return "Operation succeeded!";
    }

    /**
     * Sets the new limit of stack.
     * @param limit New limit.
     * @return Text message - result of operation.
     */
    @ShellMethod("Set limits of stack")
    public String setLimitOfStack(Integer limit) {
        log.info("Setting new stack limit... ");
        rpnCalculator.setNewLimitOfStackAndReset(limit);
        return "Operation succeeded!";
    }

    @Override
    public String calculate(String in) {
        in = normaliseInput(in);
        return formatter.format(rpnCalculator.processElements(in));
    }
}
