package com.yuki_nagato.csunotifier;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class MailSender {
    private final InternetAddress from;
    private final String server, username, password, port, senderName;

    public MailSender(String address, String name, String server, String username, String password, String port) throws UnsupportedEncodingException {
        this.from = new InternetAddress(address, name, "utf-8");
        this.server = server;
        this.username = username;
        this.password = password;
        this.port = port;
        this.senderName = name;
    }
    public void send(Student student, Diff diff) throws MessagingException, UnsupportedEncodingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", server);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(from);
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(student.email,student.getName(),"utf-8"));
        message.setSubject("中南大学新成绩通知","utf-8");
        message.setContent(generateContent(student, diff), "text/html;charset=utf-8");
        message.saveChanges();
        Transport.send(message);
    }

    String generateContent(Student student, Diff diff) {
        StringBuilder rst = new StringBuilder();
        rst.append("<p>").append(student.getName()).append("同学：</p>");
        rst.append("<p>您有成绩变化：</p>");
        if(!diff.additions.isEmpty()) {
            rst.append("<p>新增的成绩为</p>");
            rst.append("<table border=\"1\"><tr><th>课程</th><th>初修学期</th><th>获得学期</th><th>过程成绩</th><th>期末成绩</th><th>成绩</th><th>学分</th></tr>");
            for(Grade grade : diff.additions) {
                rst.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", grade.course, grade.orientationTerm, grade.getTerm, grade.processScore, grade.examScore, grade.mark, grade.credit));
            }
            rst.append("</table>");
        }
        if(!diff.deletions.isEmpty()) {
            rst.append("<p>消失的成绩为</p>");
            rst.append("<table border=\"1\"><tr><th>课程</th><th>初修学期</th><th>获得学期</th><th>过程成绩</th><th>期末成绩</th><th>成绩</th><th>学分</th></tr>");
            for(Grade grade : diff.deletions) {
                rst.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", grade.course, grade.orientationTerm, grade.getTerm, grade.processScore, grade.examScore, grade.mark, grade.credit));
            }
            rst.append("</table>");
        }
        rst.append("<p>Yours sincerely,<br/>").append(senderName).append("</p>");
        return rst.toString();
    }
}
