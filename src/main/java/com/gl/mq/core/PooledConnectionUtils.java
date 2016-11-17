package com.gl.mq.core;

import javax.jms.Session;

import com.gl.base.common.PropertyConfigure;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnection;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PooledConnectionUtils {

	public static final Logger LOG = LoggerFactory.getLogger(PooledConnectionUtils.class);
	private static PooledConnectionFactory poolFactory;

	static {
		String url = PropertyConfigure.getContextProperty("ecej.mq.url");
		// String userName =
		// SourceUtils.getSimpleMessage("ecej.mq.factor.username");
		// String password =
		// SourceUtils.getSimpleMessage("ecej.mq.factor.password");
		String maxConnections = PropertyConfigure.getContextProperty("ecej.mq.pool.maxConnections", "100");
		String activeSessionPerConnection = PropertyConfigure
				.getContextProperty("ecej.mq.pool.activeSessionPerConnection", "200");
		String expirationCheckMillis = PropertyConfigure.getContextProperty("ecej.mq.pool.expirationCheckMillis",
				"10000");
		if (StringUtils.isEmpty(url)) {
			LOG.warn("Wraning: mq.factor.url is empty!!");
		}
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
		poolFactory = new PooledConnectionFactory(factory);
		// 池中借出的对象的最大数目
		poolFactory.setMaxConnections(Integer.valueOf(maxConnections));
		poolFactory.setMaximumActiveSessionPerConnection(Integer.valueOf(activeSessionPerConnection));
		// 后台对象清理时，休眠时间超过了10000毫秒的对象为过期
		poolFactory.setTimeBetweenExpirationCheckMillis(Integer.valueOf(expirationCheckMillis));
		LOG.info(" PooledConnectionFactory create success,maxConnections:[{}],activeSessionPerConnection:[{}]",
				maxConnections, activeSessionPerConnection);

	}

	/**
	 * 1.对象池管理connection和session,包括创建和关闭等
	 *
	 * @return * @throws JMSException
	 */
	public static Session createSession() {

		PooledConnection pooledConnection = null;
		try {
			pooledConnection = (PooledConnection) poolFactory.createConnection();
			// false 参数表示 为非事务型消息，后面的参数表示消息的确认类型（见4.消息发出去后的确认模式）
			return pooledConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (Exception e) {
			LOG.error("create session error !{}", e.getMessage());
			LOG.info("当前线程池状态 ,最大线程池数:[{}],当前线程池连接数:[{}],当前线程session数:[{}],总session数量:[{}]",
					poolFactory.getMaxConnections(), poolFactory.getNumConnections(),
					pooledConnection.getNumActiveSessions(), pooledConnection.getNumSessions());
		}
		return null;
	}

	/**
	 * 获得链接池工厂
	 */
	public static PooledConnectionFactory getPooledConnectionFactory() {

		return poolFactory;
	}

}
