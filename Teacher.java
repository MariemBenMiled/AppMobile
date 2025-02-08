package com.example.myapplication;

public class Teacher {

    private String name;

    private String email;
    private String teacherId;
    private String id;

    private String className;


    public Teacher() {

    }

    public Teacher(String id,String name, String email, String teacherId, String className) {
        this.id=id;
        this.name = name;
        this.email = email;
        this.teacherId = teacherId;
        this.className = className;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

