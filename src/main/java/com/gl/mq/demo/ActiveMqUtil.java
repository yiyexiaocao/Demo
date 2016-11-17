package com.gl.mq.demo;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by gl on 2016/11/14.
 */
public class ActiveMqUtil {

    public  static void senderMessage(String message){

        //链接工厂
        ConnectionFactory connectionFactory ;
        //创建连接
        Connection connection = null;
        //创建一个session
        Session session;
        //创建目的地
        Destination destination;
        //消息提供者
        MessageProducer messageProducer;

        //构件ConnectionFactory
        connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,ActiveMQConnection.DEFAULT_PASSWORD,"tcp://localhost:61616");
        try {
            //得到连接对象
            connection = connectionFactory.createConnection();
            //启动连接
            connection.start();
            //创建session
            session = connection.createSession(true,Session.AUTO_ACKNOWLEDGE);
            //获取服务商信息
            destination = session.createQueue("my_demo_que");
            //得到消息的发送者
            messageProducer = session.createProducer(destination);
            messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
            for (int i=0;i<20;i++){
                senderMessage(session,messageProducer,message);
            }
            session.commit();
            session.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
            try {
                if(null != connection){
                connection.close();
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    public static void senderMessage(Session session,MessageProducer messageProducer,String msg) throws JMSException {
        TextMessage message = session.createTextMessage(msg);
        //发送消息到队列
        messageProducer.send(message);

    }

    public static void main(String[] args) {
        ActiveMqUtil.senderMessage("发送者提供的消息");
    }

}
