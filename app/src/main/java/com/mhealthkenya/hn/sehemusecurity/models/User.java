package com.mhealthkenya.hn.sehemusecurity.models;

public class User {
    private int id;
    private String f_name;
    private String l_name;
    private String msisdn;
    private String email;
    private String gender;
    private int access_level;


    public User(int id, String f_name,String l_name,String msisdn,String email,String gender,int access_level) {

        this.id = id;
        this.f_name = f_name;
        this.l_name = l_name;
        this.msisdn = msisdn;
        this.email = email;
        this.gender = gender;
        this.access_level = access_level;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAccess_level() {
        return access_level;
    }

    public void setAccess_level(int access_level) {
        this.access_level = access_level;
    }


}

