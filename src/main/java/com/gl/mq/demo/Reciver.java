package com.gl.mq.demo;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by gl on 2016/11/14.
 */
public class Reciver {

    public static void main(String[] args) {
        //创建工厂
        ConnectionFactory connectionFactory;
        //创建connection
        Connection connection = null;
        //创建session
        Session session;
        //创建目的地
        Destination destination;
        //消费者
        MessageConsumer consumer;
        //得到工厂
        connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD , "tcp://localhost:61616");

        try {
            //创建链接
            connection = connectionFactory.createConnection();
            //启动
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //获取服务器上的消息
            destination = session.createQueue("my_demo_que");
            consumer = session.createConsumer(destination);
            while(true){
                TextMessage message = (TextMessage) consumer.receive(100000);
                if(null != message){
                    System.out.println(message.getText());
                }else{
                    break;
                }
            }
        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            try{
                if(null != connection){
                    connection.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
