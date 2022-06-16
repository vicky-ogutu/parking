package com.mhealthkenya.hn.sehemusecurity.models;

public class ActiveVisit {

    private int id;
    private String uuid;
    private String temp;
    private String first_name;
    private String last_name;
    private String vehicle_reg;
    private String msisdn;
    private String is_approved;
    private String is_active;
    private String is_started;
    private String is_ended;
    private String national_id;
    private String created_at;
    private String date;
    private String time;
    private String updated_at;
    private String person_visit;
    private String house_visit;
    private String residential;
    private int created_by;
    private String house;
    public boolean expanded = false;
    public boolean parent = false;


    public ActiveVisit(int id, String uuid, String temp, String first_name, String last_name, String vehicle_reg, String msisdn, String is_approved, String is_active,String is_started, String is_ended, String national_id, String created_at, String date,String time, String updated_at,String person_visit, String house_visit, String residential, int created_by, String house ) {
        this.id = id;
        this.uuid = uuid;
        this.temp = temp;
        this.first_name = first_name;
        this.last_name = last_name;
        this.vehicle_reg = vehicle_reg;
        this.msisdn = msisdn;
        this.is_approved = is_approved;
        this.is_active = is_active;
        this.is_started = is_started;
        this.is_ended = is_ended;
        this.national_id = national_id;
        this.created_at = created_at;
        this.date = date;
        this.time = time;
        this.updated_at = updated_at;
        this.person_visit = person_visit;
        this.house_visit = house_visit;
        this.residential = residential;
        this.created_by = created_by;
        this.house = house;
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

    public void getTemp(String temp){this.temp = temp;}
    public void setTemp(String temp){this.temp = temp;}

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

    public String getIs_approved() {
        return is_approved;
    }

    public void setIs_approved(String is_approved) {
        this.is_approved = is_approved;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getIs_started() {
        return is_started;
    }

    public void setIs_started(String is_started) {
        this.is_started = is_started;
    }

    public String getIs_ended() {
        return is_ended;
    }

    public void setIs_ended(String is_ended) {
        this.is_ended = is_ended;
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

    public String getHouse_visit() {
        return house_visit;
    }

    public void setHouse_visit(String house_visit) {
        this.house_visit = house_visit;
    }

    public String getResidential() {
        return residential;
    }

    public void setResidential(String residential) {
        this.residential = residential;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }




}
