package com.vpryimak.cli.rpn.calculator.service;

import static java.lang.Double.sum;

import com.vpryimak.cli.rpn.calculator.exception.RpnCalculatorException;
import java.util.ArrayDeque;
import java.util.Deque;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Scope("prototype")
public class RpnCalculatorImpl implements RpnCalculator {

    private Integer limitOfStack = 1000;
    private Deque<Double> stack = new ArrayDeque<>(limitOfStack);

    @Override
    public Double process(String in) throws NumberFormatException {
        log.debug("Processing the following input {}", in);
        switch (in) {
            case "+": {
                log.debug("Determined '+' operation.");
                operationSupportCheck();
                if (stack.size() == 1) {
                    return stack.getLast();
                }
                Double result = sum(stack.pop(), stack.pop());
                stack.push(result);
                return result;
            }
            case "-": {
                log.debug("Determined '-' operation.");
                operationSupportCheck();
                if (stack.size() == 1) {
                    return stack.getLast();
                }
                Double first = stack.pop();
                Double second = stack.pop();
                Double result = second - first;
                stack.push(result);
                return result;
            }
            case "/": {
                log.debug("Determined '/' operation.");
                operationSupportCheck();
                if (stack.size() == 1) {
                    return stack.getLast();
                }
                Double first = stack.pop();
                Double second = stack.pop();
                Double result = second / first;
                stack.push(result);
                return result;
            }
            case "*": {
                log.debug("Determined '*' operation.");
                operationSupportCheck();
                if (stack.size() == 1) {
                    return stack.getLast();
                }
                Double first = stack.pop();
                Double second = stack.pop();
                Double result = second * first;
                stack.push(result);
                return result;
            }
            default:
                log.debug("Trying to parse and add number '{}' to stack.", in);
                Double parseResult = Double.parseDouble(in);
                stack.push(parseResult);
                return parseResult;
        }
    }

    @Override
    public void reset() {
        stack.clear();
        log.info("Reset operation was successfully performed.");
    }

    @Override
    public void setNewLimitOfStackAndReset(Integer limit) {
        stack = new ArrayDeque<>(limit);
        log.info("New limit was set successfully.");
    }

    @Override
    public Double getLast() {
        return stack.size() > 0 ? stack.getLast() : 0.0;
    }

    private void operationSupportCheck() {
        if (stack.size() == 0) {
            throw new RpnCalculatorException("There are no numbers in stack!");
        }
    }
}
