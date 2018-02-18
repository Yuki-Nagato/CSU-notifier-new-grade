# 中南大学新成绩通知

## 部署

### 直接下载

[下载jar包](https://github.com/Yuki-Nagato/CSU-notifier-new-grade/releases/download/main/CSU-notifier-new-grade-jar.zip)

解压后修改`config/config.yaml`，然后运行

```
java -jar csu-new-grade.jar
```

### 源码编译

Clone并编译、打包jar

```
git clone https://github.com/Yuki-Nagato/CSU-notifier-new-grade.git
cd CSU-notifier-new-grade/
mkdir out

# Linux
javac -cp ".:lib/javax.mail.jar:lib/json-20171018.jar:lib/yamlbeans-1.13.jar" -d out -sourcepath ./src/ -encoding utf8 src/com/yuki_nagato/csunotifier/Main.java
# Windows
javac -cp ".;lib/javax.mail.jar;lib/json-20171018.jar;lib/yamlbeans-1.13.jar" -d out -sourcepath ./src/ -encoding utf8 src/com/yuki_nagato/csunotifier/Main.java

cp lib/* out/
cd out/
jar -xf javax.mail.jar
jar -xf json-20171018.jar
jar -xf yamlbeans-1.13.jar
rm *.jar
cd ../
mkdir bin
jar cfe bin/csu-new-grade.jar com.yuki_nagato.csunotifier.Main -C out/ .
```

建立配置文件

```
cd bin/
mkdir config
cp ../config/config.example.yaml config/config.yaml
```

运行

确保在bin目录下，执行

```
java -jar csu-new-grade.jar
```

## 开发

### 修改邮件内容

修改`src/com/yuki_nagato/csunotifier/MailSender.java`中的`generateContent`方法。
