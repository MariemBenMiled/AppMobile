package com.example.myapplication;
public class Absence {

    private String id;
    private String className;
    private String date;
    private String room;
    private String time;
    private String teacherId;

    private String agentId;

    public Absence() {
    }

    public Absence(String id,String time, String teacherId, String room, String date, String className, String agentId) {
        this.id=id;
        this.time = time;
        this.teacherId = teacherId;
        this.room = room;
        this.date = date;
        this.className = className;
        this.agentId=agentId;
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
}