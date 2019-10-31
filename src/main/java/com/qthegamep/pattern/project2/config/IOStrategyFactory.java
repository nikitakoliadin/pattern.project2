package com.qthegamep.pattern.project2.config;

import com.qthegamep.pattern.project2.model.IoStrategyType;
import org.glassfish.grizzly.IOStrategy;
import org.glassfish.grizzly.strategies.LeaderFollowerNIOStrategy;
import org.glassfish.grizzly.strategies.SameThreadIOStrategy;
import org.glassfish.grizzly.strategies.SimpleDynamicNIOStrategy;
import org.glassfish.grizzly.strategies.WorkerThreadIOStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOStrategyFactory {

    private static final Logger LOG = LoggerFactory.getLogger(IOStrategyFactory.class);

    public IOStrategy createIOStrategy(IoStrategyType ioStrategyType) {
        LOG.info("IO strategy: {}", ioStrategyType);
        switch (ioStrategyType) {
            case WORKER_IO_STRATEGY:
                return WorkerThreadIOStrategy.getInstance();
            case SAME_IO_STRATEGY:
                return SameThreadIOStrategy.getInstance();
            case DYNAMIC_IO_STRATEGY:
                return SimpleDynamicNIOStrategy.getInstance();
            case LEADER_FOLLOWER_IO_STRATEGY:
            default:
                return LeaderFollowerNIOStrategy.getInstance();
        }
    }
}
