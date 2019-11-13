package com.vpryimak.cli.rpn.calculator.service.impl;

import com.vpryimak.cli.rpn.calculator.exception.RpnCalculatorException;
import com.vpryimak.cli.rpn.calculator.service.RpnCalculator;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Implementation of Reverse Polish Notation Calculator
 *
 * Reverse Polish notation (RPN) is a method for representing expressions
 * in which the operator symbol is placed after the arguments being operated on.
 *
 * Component will be used as a separate component instance for services when it is going to be used.
 */
@Slf4j
@Component
@Scope("prototype")
public class RpnCalculatorImpl implements RpnCalculator {

    /**
     * Error message when we reached limit.
     */
    private final String LIMIT_ERROR = "Stack of numbers is overloaded. Your stack size is: %s.";

    /**
     * It was added to control capacity of stack
     */
    private Integer limitOfStack;

    /**
     * Represents a last-in-first-out(LIFO) stack of numbers.
     */
    private Deque<Double> numbers;

    /**
     * Setting default limit of stack.
     * @param limitOfStack Default limit from properties.
     */
    RpnCalculatorImpl(@Value("${rpn.defaultStackSize}") Integer limitOfStack){
        this.limitOfStack = limitOfStack;
        numbers = new ArrayDeque<>(limitOfStack);
    }

    /**
     * This method expects combined expressions with more than
     * one number ot operation as an input parameter.
     *
     * @param in NonNull input string.
     * @return Result of operation as Double.
     * @throws NumberFormatException In case we cannot parse number it throws this exception.
     */
    @Override
    public Double processElements(@NonNull String in){
        Stream.of(in.split(" ")).forEach(this::processElement);
        return numbers.getFirst();
    }

    /**
     * This method expects one number or operation as an input parameter.
     *
     * @param in NonNull input parameter.
     * @return Result of operation as Double.
     * @throws NumberFormatException In case we cannot parse number it throws this exception.
     */
    @Override
    public Double processElement(@NonNull String in) throws NumberFormatException {
        log.debug("Processing the following input {}", in);
        switch (in) {
            case "+": {
                log.debug("Determined '+' operation.");
                return calc(Double::sum);
            }
            case "-": {
                log.debug("Determined '-' operation.");
                return calc((n1, n2) -> n2 - n1);
            }
            case "/": {
                log.debug("Determined '/' operation.");
                return calc((n1, n2) -> n2 / n1);
            }
            case "*": {
                log.debug("Determined '*' operation.");
                return calc((n1, n2) -> n2 * n1);
            }
            default:
                log.debug("Trying to parse and add number '{}' to stack.", in);
                Double parseResult = Double.parseDouble(in);
                checkStackSize(limitOfStack, String.format(LIMIT_ERROR, limitOfStack));
                numbers.push(parseResult);
                return parseResult;
        }
    }

    /**
     * It clears stack of numbers.
     */
    @Override
    public void reset() {
        numbers.clear();
        log.info("Reset operation was successfully performed.");
    }

    /**
     * It clears stack of numbers and sets new stack limit.
     */
    @Override
    public void setNewLimitOfStackAndReset(@NonNull Integer limit) {
        numbers = new ArrayDeque<>(limit);
        log.info("New limit was set successfully.");
    }

    /**
     * Returns last result.
     */
    @Override
    public Double getLast() {
        return numbers.size() > 0 ? numbers.getLast() : 0.0;
    }

    /**
     * Performing calculation with operation.
     *
     * @param operation desired operation.
     * @return Result of calculation as Double.
     */
    private Double calc(BiFunction<Double, Double, Double> operation) {
        checkStackSize(0, "There are no numbers in stack!");
        if (numbers.size() == 1) {
            return numbers.getFirst();
        }
        checkStackSize(limitOfStack, String.format(LIMIT_ERROR, limitOfStack));
        numbers.push(operation.apply(numbers.pop(), numbers.pop()));
        return numbers.getFirst();
    }

    /**
     * Checking stack size in order to determine whether
     * stack is empty or we are reached the limit.
     * @param size Size that we are going to check.
     * @param errorMessage Error message in case we have issue.
     */
    private void checkStackSize(Integer size, String errorMessage) {
        if (numbers.size() == size) {
            throw new RpnCalculatorException(errorMessage);
        }
    }
}
