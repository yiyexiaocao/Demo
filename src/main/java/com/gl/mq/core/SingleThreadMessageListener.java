package com.gl.mq.core;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.gl.base.common.PropertyConfigure;
import com.gl.mq.handler.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 消息消费者中使用的多线程消息监听服务
 *
 * @author QIANG
 * @data 2016年6月23日下午12:23:46
 */
public class SingleThreadMessageListener extends ThreadMessageListener {

    public static final Logger              LOG               = LoggerFactory
        .getLogger(SingleThreadMessageListener.class);

    private final static int                DEFAULT_POOL_SIZE = 1;

    private final static ThreadPoolExecutor executor;

    static {

        String queueSize = PropertyConfigure.getContextProperty("ecej.mq.consumer.single.qsize",
            "5000");
        int qsize = Integer.valueOf(queueSize);

        executor = new ThreadPoolExecutor(DEFAULT_POOL_SIZE, DEFAULT_POOL_SIZE, 0L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(qsize),
            new ThreadPoolExecutor.CallerRunsPolicy());
        LOG.info(
            "创建异步消费线程池,核心线程数为:[{}],最大线程数为:[{}],默认队列为LinkedBlockingQueue,其队列深度为:[{}],拒绝策略为:CallerRunsPolicy",
            DEFAULT_POOL_SIZE, DEFAULT_POOL_SIZE, qsize);
    }

    public SingleThreadMessageListener(MessageHandler messageHandler) {
        this(messageHandler, executor);
    }

    public SingleThreadMessageListener(MessageHandler messageHandler,
            ThreadPoolExecutor executorService) {
        super(messageHandler, executorService);
    }

}
