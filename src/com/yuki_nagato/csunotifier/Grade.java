package com.yuki_nagato.csunotifier;

import org.json.JSONObject;

import java.util.Map;

public class Grade {
    public final String course,orientationTerm,getTerm,processScore,examScore,mark,credit;
    public Grade(String course,String orientationTerm, String getTerm, String processScore, String examScore, String mark, String credit) {
        this.course = course;
        this.orientationTerm = orientationTerm;
        this.getTerm = getTerm;
        this.processScore = processScore;
        this.examScore = examScore;
        this.mark = mark;
        this.credit = credit;
    }
    public Grade(Map<String, String> map) {
        course = map.get("course");
        orientationTerm = map.get("orientationTerm");
        getTerm = map.get("getTerm");
        processScore = map.get("processScore");
        examScore = map.get("examScore");
        mark = map.get("mark");
        credit = map.get("credit");
    }

    public JSONObject toJson() {
        JSONObject rst = new JSONObject();
        rst.put("course", course);
        rst.put("orientationTerm", orientationTerm);
        rst.put("getTerm", getTerm);
        rst.put("processScore", processScore);
        rst.put("examScore", examScore);
        rst.put("mark", mark);
        rst.put("credit", credit);
        return rst;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass()!=Grade.class) return false;
        Grade b = (Grade)obj;
        return course.equals(b.course) &&
                orientationTerm.equals(b.orientationTerm) &&
                getTerm.equals(b.getTerm) &&
                processScore.equals(b.processScore) &&
                examScore.equals(b.examScore) &&
                mark.equals(b.mark) &&
                credit.equals(b.credit);
    }

    @Override
    public int hashCode() {
        return course.hashCode();
    }
}
