package com.vpryimak.cli.rpn.calculator.service;

/**
 * Abstraction of RPN Service.
 * @param <T> return type for particular vendor.
 */
public interface RpnOperationSupport<T> {

    /**
     * Calculating through the Reverse Polish Notation approach.
     * @param in String request for calculation.
     * @return Calculation result.
     */
    T calculate(String in);
}
