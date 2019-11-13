package com.vpryimak.cli.rpn.calculator.service;

import lombok.NonNull;

/**
 * Reverse Polish Notation Calculator
 */
public interface RpnCalculator {

    /**
     * This method expects combined expressions with more than
     * one number ot operation as an input parameter.
     *
     * @param in NonNull input string.
     * @return Result of operation as Double.
     */
    Double processElements(@NonNull String in);

    /**
     * This method expects one number or operation as an input parameter.
     *
     * @param in NonNull input parameter.
     * @return Result of operation as Double.
     */
    Double processElement(@NonNull String in);

    /**
     * It clears stack of numbers.
     */
    void reset();

    /**
     * It clears stack of numbers and sets new stack limit.
     */
    void setNewLimitOfStackAndReset(@NonNull Integer limit);

    /**
     * Returns last result.
     */
    Double getLast();
}
