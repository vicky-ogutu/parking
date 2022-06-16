package com.mhealthkenya.hn.sehemusecurity.models;

public class VerifiedVisitor {

    private int id;
    private String uuid;
    private String temp;
    private String first_name;
    private String vehicle_reg;
    private String msisdn;
    private String date;
    private String time;
    private String is_active;
    private String national_id;
    private String created_at;
    private String updated_at;
    private String person_visit;
    private String created_by;
    private String updated_by;



    public VerifiedVisitor(int id, String temp, String uuid, String first_name, String vehicle_reg, String msisdn, String date, String time, String is_active, String national_id, String created_at, String updated_at, String person_visit,String created_by, String updated_by) {

        this.id = id;
        this.uuid = uuid;
        this.temp = temp;
        this.first_name = first_name;
        this.vehicle_reg = vehicle_reg;
        this.msisdn = msisdn;
        this.date = date;
        this.time = time;
        this.is_active = is_active;
        this.national_id = national_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.person_visit = person_visit;
        this.created_by = created_by;
        this.updated_by = updated_by;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }


    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getVehicle_reg() {
        return vehicle_reg;
    }

    public void setVehicle_reg(String vehicle_reg) {
        this.vehicle_reg = vehicle_reg;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getNational_id() {
        return national_id;
    }

    public void setNational_id(String national_id) {
        this.national_id = national_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getPerson_visit() {
        return person_visit;
    }

    public void setPerson_visit(String person_visit) {
        this.person_visit = person_visit;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

}
