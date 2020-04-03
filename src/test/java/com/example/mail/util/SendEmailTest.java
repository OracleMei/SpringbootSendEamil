package com.example.mail.util;

import com.example.mail.MailApplication;
import com.example.mail.entity.User;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

/**
 * @description:
 * @author: meiyc
 * @createDate: 2020/4/3 21:45
 * @version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SendEmailTest {
    @Autowired
    JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String myemail;
    
    @Value("${spring.mail.tousername}")
    private String tousername;

    @Test
    void contextLoads() {
    }

    /**
     * 发送简单邮件
     */
    @Test
    public void sendSimpleMail() {
        SimpleMailMessage message = new SimpleMailMessage();//构建一个邮件对象
        message.setSubject("发送简单邮件");//设置邮件主题
        message.setFrom(myemail);//设置邮件发送者
        message.setTo(tousername);//设置邮件接收者，可以有多个接收者
        //message.setCc("37xxxxx37@qq.com");//设置邮件抄送人，可以有多个抄送人
        //message.setBcc("14xxxxx098@qq.com");//设置隐秘抄送人，可以有多个
        message.setSentDate(new Date());//设置邮件发送日期
        message.setText("这是测试邮件的正文");//设置邮件的正文
        javaMailSender.send(message);//发送邮件
    }

    /**
     *
     * 发送带附件的邮件
     */
    @Test
    public void sendAttachFileMail() throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
        helper.setSubject("发送带附件的邮件");
        helper.setFrom(myemail);
        helper.setTo(tousername);
        /*helper.setCc("37xxxxx37@qq.com");
        helper.setBcc("14xxxxx098@qq.com");*/
        helper.setSentDate(new Date());
        helper.setText("这是测试邮件的正文");
        helper.addAttachment("javaboy.jpg",new File("D:\\phote\\mei.jpg"));
        javaMailSender.send(mimeMessage);
    }

    /**
     * 发送带图片资源的邮件
     */
    @Test
    public void sendImgResMail() throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("这是一封测试邮件");
        helper.setFrom(myemail);
        helper.setTo(tousername);
        /*helper.setCc("37xxxxx37@qq.com");
        helper.setBcc("14xxxxx098@qq.com");*/
        helper.setSentDate(new Date());
        helper.setText("<p>hello 大家好，这是一封测试邮件，这封邮件包含两种图片，分别如下</p><p>第一张图片：</p><img src='cid:p01'/><p>第二张图片：</p><img src='cid:p02'/>",true);
        helper.addInline("p01",new FileSystemResource(new File("D:\\phote\\mei.jpg")));
        helper.addInline("p02",new FileSystemResource(new File("D:\\phote\\yu.jpg")));
        javaMailSender.send(mimeMessage);
    }

    /**
     * 使用 Freemarker 作邮件模板
     */
    @Test
    public void sendFreemarkerMail() throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("这是一封测试邮件");
        helper.setFrom(myemail);
        helper.setTo(tousername);
        /*helper.setCc("37xxxxx37@qq.com");
        helper.setBcc("14xxxxx098@qq.com");*/
        helper.setSentDate(new Date());
        //构建 Freemarker 的基本配置
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);
        // 配置模板位置
        ClassLoader loader = MailApplication.class.getClassLoader();
        configuration.setClassLoaderForTemplateLoading(loader, "templates");
        //加载模板
        Template template = configuration.getTemplate("mail.ftl");
        User user = new User();
        user.setUsername("javaboy");
        user.setNum(1);
        user.setSalary((double) 99999);
        StringWriter out = new StringWriter();
        //模板渲染，渲染的结果将被保存到 out 中 ，将out 中的 html 字符串发送即可
        template.process(user, out);
        helper.setText(out.toString(),true);
        javaMailSender.send(mimeMessage);
    }

    /**
     * 使用 Thymeleaf 作邮件模板
     */
    @Autowired
    TemplateEngine templateEngine;
    @Test
    public void sendThymeleafMail() throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("使用 Thymeleaf 作邮件模板");
        helper.setFrom(myemail);
        helper.setTo(tousername);
        /*helper.setCc("37xxxxx37@qq.com");
        helper.setBcc("14xxxxx098@qq.com");*/
        helper.setSentDate(new Date());
        Context context = new Context();
        context.setVariable("username", "mei");
        context.setVariable("num","000001");
        context.setVariable("salary", "888888");
        String process = templateEngine.process("mail.html", context);
        helper.setText(process,true);
        javaMailSender.send(mimeMessage);
    }
}
