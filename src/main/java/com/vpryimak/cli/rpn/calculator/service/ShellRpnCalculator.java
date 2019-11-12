package com.vpryimak.cli.rpn.calculator.service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@Slf4j
@ShellComponent
public class ShellRpnCalculator {

    private final RpnCalculator rpnCalculator;
    private final NumberFormat formatter = new DecimalFormat("#0.00");

    @Autowired
    public ShellRpnCalculator(RpnCalculator rpnCalculator) {
        this.rpnCalculator = rpnCalculator;
    }

    @ShellMethod("Reverse Polish Notation Calculator")
    public String calc(
            @ShellOption(help = "Enter number or operation. Supported operations: '+/*-'.") String in) {

        in = normaliseInput(in);
        String result = "";
        try {
            for (String input : in.split(" ")) {
                if (!input.isEmpty()) {
                    result = formatter.format(rpnCalculator.process(input));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            log.debug(e.getMessage(), e);
            return formatter.format(rpnCalculator.getLast())
                    + " (Cannot perform last operation because of " + e.getMessage() + ")";
        }
        return result;
    }

    private String normaliseInput(String in) {
        in = in.trim();
        in = in.replaceAll(",", ".");
        in = in.replaceAll(" {2}", " ");
        return in;
    }

    @ShellMethod("Reset stack of numbers")
    public String reset() {
        log.info("Resetting stack of numbers...");
        rpnCalculator.reset();
        return "Operation succeeded!";
    }

    @ShellMethod("Reset")
    public String setLimitOfStack(Integer limit) {
        log.info("Setting new stack limit... ");
        rpnCalculator.setNewLimitOfStackAndReset(limit);
        return "Operation succeeded!";
    }
}
