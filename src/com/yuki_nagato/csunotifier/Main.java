package com.yuki_nagato.csunotifier;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        final Config cfg = new Config(new File("config/config.yaml"));
        Student.recordFile = new File(cfg.recordFile);
        Student.load();
        MailSender sender = new MailSender(cfg.email.get("address"),cfg.email.get("sender-name"),cfg.email.get("server"),cfg.email.get("username"),cfg.email.get("password"),cfg.email.get("port"));
        while(true) {
            for(Map<String, String> student : cfg.students) {
                String id = student.get("id"), password = student.get("password"), email = student.get("email");
                try {
                    System.out.println("开始查询"+id);
                    Student stu = new Student(id,password,email);
                    Diff diff = stu.check();
                    if(diff.additions.isEmpty() && diff.deletions.isEmpty()) {
                        System.out.println("没有变化");
                    }
                    else {
                        System.out.printf("新增%d个成绩，减少%d个成绩\n", diff.additions.size(), diff.deletions.size());
                        sender.send(stu,diff);
                        System.out.println("邮件发送成功");
                    }
                }
                catch (MessagingException e) {
                    System.err.println("邮件发送失败");
                    e.printStackTrace();
                }
                catch (AuthenticationException e) {
                    System.err.println("用户名或密码错误");
                }
                catch (NullPointerException | IOException e) {
                    System.err.println("关于"+id+"的配置可能有误");
                    e.printStackTrace();
                }
                Thread.sleep(cfg.interval*1000/cfg.students.size());
            }
        }
    }
}
