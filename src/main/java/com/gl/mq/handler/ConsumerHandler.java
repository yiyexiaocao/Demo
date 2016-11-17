package com.gl.mq.handler;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import com.gl.base.common.PropertyConfigure;
import com.gl.mq.core.MultiThreadMessageListener;
import com.gl.mq.core.SingleThreadMessageListener;
import com.gl.mq.utils.SendType;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ConsumerHandler {

    public static final Logger LOG = LoggerFactory.getLogger(ConsumerHandler.class);

    private static String      brokerUrl;

    private static String      userName;

    private static String      password;

    private Connection         connection;

    private Session            session;

    private Destination        destination;
    // 队列名
    private String             queue;

    private MessageHandler messageHandler;

    public ConsumerHandler(MessageHandler messageHandler, String queue) {
        this.messageHandler = messageHandler;
        this.queue = queue;
    }

    static {
        brokerUrl = PropertyConfigure.getContextProperty("gl.mq.url");
        // userName =
        // PropertyConfigure.getContextProperty("ecej.mq.factor.username");
        // password =
        // PropertyConfigure.getContextProperty("ecej.mq.factor.password");
        LOG.info("初始化brokerUrl,brokerUrl, password");
    }

    /**
     * 启动长连(单线程执行)
     *
     * @param SendType
     *
     */
    public void startServer(SendType sendType) {

        try {
            MessageConsumer consumer = createConsumer(sendType);
            consumer.setMessageListener(new SingleThreadMessageListener(messageHandler));
        } catch (JMSException e) {
            LOG.error("connection broker error ,msg:{}", e.getMessage());
        }
    }

    /**
     * 启动长连
     *
     * @param SendType
     *
     */
    public void startMultiThreadServer(SendType sendType) {

        try {
            MessageConsumer consumer = createConsumer(sendType);
            consumer.setMessageListener(new MultiThreadMessageListener(messageHandler));
        } catch (JMSException e) {
            LOG.error("connection broker error ,msg:{}", e.getMessage());
        }
    }

    /**
     * 创建基础连接
     *
     * @param sendType
     * @return
     * @throws JMSException
     */
    private MessageConsumer createConsumer(SendType sendType) throws JMSException {

        // ActiveMQ的连接工厂
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(userName, password,
            brokerUrl);
        connection = connectionFactory.createConnection();
        connection.start();
        // 会话采用非事务级别，消息到达机制使用自动通知机制
        session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
        if (SendType.QUEUE == sendType) {
            destination = session.createQueue(queue);
        } else {
            destination = session.createTopic(queue);

        }
        MessageConsumer consumer = session.createConsumer(destination);
        consumer.setMessageListener(new MultiThreadMessageListener(messageHandler));
        return consumer;
    }

    /**
     * 关闭连接
     */
    public void shutdown() {

        try {
            if (session != null) {
                session.close();
                session = null;
            }
            LOG.info("close session");
            if (connection != null) {
                connection.close();
                connection = null;
            }
            LOG.info("close connection");
        } catch (Exception e) {
            LOG.error("close consumer failed. Exception:{}", e.getMessage());
        }
    }

}
