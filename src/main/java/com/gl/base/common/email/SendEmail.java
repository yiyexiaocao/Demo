package com.gl.base.common.email;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.gl.base.common.PropertyConfigure;
import com.gl.base.common.ResultMessage;
import com.gl.base.common.enums.MessageEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;


public class SendEmail {

	private static Logger logger = LoggerFactory.getLogger(SendEmail.class);

	private static String smtpHost;// 邮箱服务器
	private static String serverPort;// 邮箱服务器端口
	private static String addresser;// 发件人
	private static String password;// 发件人密码

	static {
		try {
			smtpHost = PropertyConfigure.getContextProperty("mail.smtp.host");
			addresser = PropertyConfigure.getContextProperty("addresser");
			password = PropertyConfigure.getContextProperty("password");
			serverPort = PropertyConfigure.getContextProperty("serverPort");
			// encoding = PropertyConfigure.getContextProperty("encoding");
			// mailSession = getMailSession();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException("读取配置文件参数错误。");
		}
	}

	/**
	 *
	 * @param to 接收人
	 * @param cs 抄送人
	 * @param ms 密送人
	 * @param subject 主题
	 * @param content 内容
	 * @param fileList
	 * @return
	 */
	public ResultMessage<String> send(String to, String cs, String ms, String subject, String content,
									  String fileList[]) {
		ResultMessage<String> resultMessage = new ResultMessage<String>();
		try {
			Properties p = new Properties(); // Properties p =
			// System.getProperties();
			p.put("mail.smtp.auth", "true");
			p.put("mail.transport.protocol", "smtp");
			p.put("mail.smtp.host", smtpHost);
			p.put("mail.smtp.port", serverPort);
			// 建立会话
			Session session = Session.getInstance(p);
			Message msg = new MimeMessage(session); // 建立信息
			BodyPart messageBodyPart = new MimeBodyPart();
			Multipart multipart = new MimeMultipart();
			msg.setFrom(new InternetAddress(addresser)); // 发件人

			// 发送,
			if (!StringUtils.isEmpty(to)) {
				InternetAddress[] iaToList = InternetAddress.parse(to);
				msg.setRecipients(Message.RecipientType.TO, iaToList); // 收件人
			}

			// 抄送
			if (!StringUtils.isEmpty(cs)) {
				InternetAddress[] iaToListcs = InternetAddress.parse(cs);
				msg.setRecipients(Message.RecipientType.CC, iaToListcs); // 抄送人
			}

			// 密送
			if (!StringUtils.isEmpty(ms)) {
				InternetAddress[] iaToListms = InternetAddress.parse(ms);
				msg.setRecipients(Message.RecipientType.BCC, iaToListms); // 密送人
			}
			msg.setSentDate(new Date()); // 发送日期
			msg.setSubject(subject); // 主题
			msg.setText(content); // 内容
			// 显示以html格式的文本内容
			messageBodyPart.setContent(content, "text/html;charset=gbk");
			multipart.addBodyPart(messageBodyPart);

			// 2.保存多个附件
			if (fileList != null) {
				addTach(fileList, multipart);
			}

			msg.setContent(multipart);
			// 邮件服务器进行验证
			Transport tran = session.getTransport("smtp");
			tran.connect(smtpHost, addresser, password);
			tran.sendMessage(msg, msg.getAllRecipients()); // 发送

			resultMessage.setCode(200);
			resultMessage.setMessage("OK");
			logger.info(subject + " 邮件发送成功.to:" + to + " ,抄送：" + cs + " ,密送：" + ms);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resultMessage.setCode(MessageEnum.EMAIL_SEND_FAILURE.getValue());
			resultMessage.setMessage(MessageEnum.EMAIL_SEND_FAILURE.getDesc());
		}

		return resultMessage;
	}

	// 添加多个附件
	public void addTach(String fileList[], Multipart multipart)
			throws MessagingException, UnsupportedEncodingException {
		for (int index = 0; index < fileList.length; index++) {
			MimeBodyPart mailArchieve = new MimeBodyPart();
			FileDataSource fds = new FileDataSource(fileList[index]);
			mailArchieve.setDataHandler(new DataHandler(fds));
			mailArchieve.setFileName(MimeUtility.encodeText(fds.getName(), "GBK", "B"));
			multipart.addBodyPart(mailArchieve);
		}
	}
}
