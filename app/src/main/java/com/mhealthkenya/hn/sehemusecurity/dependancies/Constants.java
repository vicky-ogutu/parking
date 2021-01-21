package com.mhealthkenya.hn.sehemusecurity.dependancies;

public class Constants {

    /*ENDPOINT*/
    public static String ENDPOINT = "http://sequ-app.mhealthkenya.co.ke/";


    //AUTH
    public static String LOGIN = "api/security/login/";
    public static String MY_PROFILE = "api/security/profile";
    public static String CURRENT_USER = "auth/users/me/";
    public static String UPDATE_USER = "auth/users/me/";
    public static String RESET_PASSWORD = "auth/users/reset_password/";
    public static String LOGOUT = "auth/token/logout/";


    //VISITS
    public static String ORGANIZATION =  "api/organization/get/";
    public static String PERSON_VISITING =  "api/persons/org/";
    public static String WALKIN_VISIT =  "api/visits/walkin/";
    public static String VERIFY_VISIT =  "api/visits/verify/";
    public static String ACTIVE_VISIT =  "api/security/active/visits/";


    /*MODELS*/
    public static String AUTH_TOKEN = "";


}
