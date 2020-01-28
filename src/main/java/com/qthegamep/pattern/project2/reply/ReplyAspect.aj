package com.qthegamep.pattern.project2.reply;

import com.qthegamep.pattern.project2.exception.ServiceException;
import com.qthegamep.pattern.project2.exception.runtime.RetryRuntimeException;
import com.qthegamep.pattern.project2.model.container.Error;
import org.aspectj.lang.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public aspect ReplyAspect {

    private static final Logger LOG = LoggerFactory.getLogger(ReplyAspect.class);

    private static final boolean enableReplyAspect = Boolean.parseBoolean(System.getProperty("aop.enable.reply.aspect"));

    pointcut replyAnnotation(Reply reply): @annotation(reply) && execution(* *(..)) && if(enableReplyAspect);

    public ReplyAspect() {
        LOG.warn("Enable reply aspect: {}", enableReplyAspect);
    }

    Object around(Reply reply): replyAnnotation(reply) {
        int replyTimes = reply.times();
        Signature signature = thisJoinPoint.getSignature();
        LOG.debug("Reply Times: {} Signature: {}", replyTimes, signature);
        for (int i = 0; i < replyTimes; i++) {
            try {
                return proceed(reply);
            } catch (Throwable e) {
                LOG.error("Error while execute: {} With error: {} Try to reply iteration: {}", signature, e, i + 1);
                if (i == (replyTimes - 1)) {
                    if (e instanceof ServiceException) {
                        throw new RetryRuntimeException(e, ((ServiceException) e).getError());
                    } else {
                        throw new RetryRuntimeException(Error.RETRY_ERROR);
                    }
                }
            }
        }
        return null;
    }
}
