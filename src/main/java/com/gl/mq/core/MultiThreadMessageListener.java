package com.gl.mq.core;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.gl.base.common.PropertyConfigure;
import com.gl.mq.handler.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 消息消费者中使用的多线程消息监听服务 采用的拒绝策略为CallerRunsPolicy，保证消息不丢失
 *
 * @author QIANG
 * @data 2016年6月23日下午12:23:46
 */
public class MultiThreadMessageListener extends ThreadMessageListener {

	public static final Logger LOG = LoggerFactory.getLogger(MultiThreadMessageListener.class);

	private final static ThreadPoolExecutor executor;

	static {

		String pooSize = PropertyConfigure.getContextProperty("ecej.mq.consumer.multi.psize", "500");
		String queueSize = PropertyConfigure.getContextProperty("ecej.mq.consumer.multi.qsize", "5000");

		int psize = Integer.valueOf(pooSize);
		int qsize = Integer.valueOf(queueSize);

		executor = new ThreadPoolExecutor(psize, psize, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(qsize), new ThreadPoolExecutor.CallerRunsPolicy());
		LOG.info("创建异步消费线程池,核心线程数为:[{}],最大线程数为:[{}],默认队列为LinkedBlockingQueue,其队列深度为:[{}],拒绝策略为:CallerRunsPolicy", psize,
				psize, qsize);
	}

	public MultiThreadMessageListener(MessageHandler messageHandler) {
		this(messageHandler, executor);
	}

	public MultiThreadMessageListener(MessageHandler messageHandler, ThreadPoolExecutor executorService) {
		super(messageHandler, executorService);
	}

}
