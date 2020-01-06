package com.qthegamep.pattern.project2.service;

import com.qthegamep.pattern.project2.model.container.IoStrategy;
import org.glassfish.grizzly.IOStrategy;

@FunctionalInterface
public interface IOStrategyFactoryService {

    IOStrategy createIOStrategy(IoStrategy ioStrategy);
}
