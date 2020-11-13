package com.mhealthkenya.hn.sehemusecurity.models;

public class PersonDetails {

    private String first_name;
    private String last_name;
    private String email;
    private String msisdn;



    public PersonDetails(String first_name, String last_name, String email, String msisdn) {

        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.msisdn = msisdn;

    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

}