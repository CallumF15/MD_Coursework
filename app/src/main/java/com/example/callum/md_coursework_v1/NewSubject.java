package com.example.callum.md_coursework_v1;

/**
 * Created by Callum on 02/12/2015.
 */
public class NewSubject {

    //Variables
    private int subject_id;
    private String subject_name;

    //Getters & Setters

    //returns the value of subject_id
    public int getSubject_id() {
        return subject_id;
    }
    //sets the value of subject_id
    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    //returns the value of subject_name
    public String getSubject_name() {
        return subject_name;
    }
    //sets the value of subject_name
    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    //Constructor
    public NewSubject(int subject_id, String subject_name) {
        this.subject_id = subject_id;
        this.subject_name = subject_name;
    }

    //Empty Constructor
    public NewSubject() {

    }
}
