package com.yuki_nagato.csunotifier;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student {
    static File recordFile;
    static HashMap<String, ArrayList<Grade>> lasts;
    final String id, password, email;
    private final ScorePage sp;
    public Student(String id, String password, String email) throws IOException, AuthenticationException {
        this.id = id;
        this.password = password;
        this.email = email;
        sp = new ScorePage(id, password);
    }
    private ArrayList<Grade> getLastGrades() {
        return lasts.get(id);
    }
    private ArrayList<Grade> getNewGrades() {
        return sp.getGrades();
    }
    public String getName() {
        return sp.getName();
    }
    private void save(ArrayList<Grade> grades) {
        lasts.put(id, grades);
        JSONObject out = new JSONObject();
        for(Map.Entry<String, ArrayList<Grade>> student : lasts.entrySet()) {
            JSONArray gradesArr = new JSONArray();
            for(Grade grade : student.getValue()) {
                gradesArr.put(grade.toJson());
            }
            out.put(student.getKey(), gradesArr);
        }
        try {
            // fucking encoding
            //FileWriter fw = new FileWriter(recordFile);
            OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(recordFile),"utf-8");
            fw.write(out.toString());
            fw.close();
        }
        catch (IOException e) {
            System.err.println("写入文件 "+recordFile.getPath()+" 失败");
            e.printStackTrace();
        }
    }
    public Diff check() {
        ArrayList<Grade> last = getLastGrades(), now = getNewGrades();
        if(last==null) {
            save(now);
            return new Diff(new ArrayList<>(), new ArrayList<>());
        }
        Diff rst = new Diff();
        rst.additions = (ArrayList<Grade>) now.clone();
        rst.additions.removeAll(last);
        rst.deletions = (ArrayList<Grade>) last.clone();
        rst.deletions.removeAll(now);
        if(!rst.additions.isEmpty() || !rst.deletions.isEmpty()) {
            save(now);
        }
        return rst;
    }
    public static void load() throws IOException {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(recordFile),"utf-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine())!=null) {
                sb.append(line);
            }
            Map<String, Object> allTheStudents = new JSONObject(sb.toString()).toMap();
            lasts = new HashMap<>();
            for(Map.Entry<String, Object> student : allTheStudents.entrySet()) {
                ArrayList<Grade> grades = new ArrayList<>();
                for(Object grade : (List)(student.getValue())) {
                    grades.add(new Grade((Map<String, String>) grade));
                }
                lasts.put(student.getKey(), grades);
            }
        }
        catch (FileNotFoundException e) {
            //FileWriter fw = new FileWriter(recordFile);
            OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(recordFile),"utf-8");
            fw.write(new JSONObject().toString());
            fw.close();
            lasts = new HashMap<>();
        }
    }
}

class Diff {
    ArrayList<Grade> additions;
    ArrayList<Grade> deletions;
    Diff(){}
    Diff(ArrayList<Grade> additions, ArrayList<Grade> deletions) {
        this.additions=additions;
        this.deletions=deletions;
    }
}
