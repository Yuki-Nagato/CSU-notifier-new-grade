package com.yuki_nagato.csunotifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScorePage {
    private final String content;

    public ScorePage(String username, String password) throws IOException, AuthenticationException {
        URL url = new URL("http://csujwc.its.csu.edu.cn/jsxsd/xk/LoginToXk");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setInstanceFollowRedirects(false);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        String encoded = Base64.getEncoder().encodeToString(username.getBytes("utf-8")) +
                "%%%" +
                Base64.getEncoder().encodeToString(password.getBytes("utf-8"));
        connection.getOutputStream().write(("encoded="+ URLEncoder.encode(encoded,"utf-8")).getBytes("utf-8"));
        if(connection.getResponseCode()!=302) {
            throw new AuthenticationException();
        }
        List<String> cookies = connection.getHeaderFields().get("Set-Cookie");

        // get page
        url = new URL("http://csujwc.its.csu.edu.cn/jsxsd/kscj/yscjcx_list");
        connection = (HttpURLConnection)url.openConnection();
        for(String cookie : cookies) {
            connection.addRequestProperty("Cookie", cookie.substring(0,cookie.indexOf(';')));
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        StringBuilder body = new StringBuilder();
        String line;
        while((line = br.readLine())!=null) {
            body.append(line);
            body.append('\n');
        }

        //exit
        url = new URL("http://csujwc.its.csu.edu.cn/jsxsd/xk/LoginToXk?method=exit");
        connection = (HttpURLConnection)url.openConnection();
        connection.setInstanceFollowRedirects(false);
        for(String cookie : cookies) {
            connection.addRequestProperty("Cookie", cookie.substring(0, cookie.indexOf(';')));
        }
        connection.getResponseCode();

        content = body.toString();
    }
    public ArrayList<Grade> getGrades() {
        String pt = "<tr>.*?<td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>.*?<td align=\"left\">(.*?)</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>.*?,700,500\\)\">(.*?)</a>.*?<td>(.*?)</td>";
        Pattern pattern = Pattern.compile(pt, Pattern.DOTALL);
        Matcher m = pattern.matcher(content);
        ArrayList<Grade> rst = new ArrayList<>();
        while(m.find()) {
            rst.add(new Grade(m.group(3),m.group(1),m.group(2),m.group(4),m.group(5),m.group(6),m.group(7)));
        }
        return rst;
    }
    public String getName() {
        String pt = "&nbsp;&nbsp;(.*)\\(";
        Pattern pattern = Pattern.compile(pt);
        Matcher m = pattern.matcher(content);
        m.find();
        return m.group(1);
    }
}

class AuthenticationException extends Exception {
    AuthenticationException() {
        super("用户名或密码错误");
    }
}