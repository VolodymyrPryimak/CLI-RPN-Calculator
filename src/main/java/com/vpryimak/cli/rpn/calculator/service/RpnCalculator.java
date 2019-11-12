package com.vpryimak.cli.rpn.calculator.service;

public interface RpnCalculator {

    Double process(String in);

    void reset();

    void setNewLimitOfStackAndReset(Integer limit);

    Double getLast();
}
