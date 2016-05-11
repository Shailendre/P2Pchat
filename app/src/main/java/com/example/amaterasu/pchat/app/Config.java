package com.example.amaterasu.pchat.app;

import java.util.HashMap;

/**
 * Created by Ravi on 08/07/15.
 */
public class Config {
    // server URL configuration
    public static final String URL_REQUEST_SMS = "http://athena.nitc.ac.in/nikhil_b120105cs/android_sms/msg91/request_sms.php";
    public static final String URL_VERIFY_OTP = "http://athena.nitc.ac.in/nikhil_b120105cs/android_sms/msg91/verify_otp.php";
    public static final String URL_REQUEST_SERVER_DATA = "http://athena.nitc.ac.in/nikhil_b120105cs/get_json.php";
    public static final String URL_UPDATE_IP = "http://athena.nitc.ac.in/nikhil_b120105cs/update_ip.php";
    public static final String URL_GETSTATUS_IP = "http://athena.nitc.ac.in/nikhil_b120105cs/getstatus.php";

    //
    public static final String INCOG_INITIATE_REQUEST = "INCOG_INITIATE_REQUEST";
    public static final String INCOG_REJECT_REQUEST = "INCOG_REJECT_REQUEST";
    public static final String INCOG_ACCEPT_REQUEST = "INCOG_ACCEPT_REQUEST";
    //

    public static final String SET_NEW_GROUP = "SET_NEW_GROUP";
    public static final String GROUP_MSG = "GROUP_MSG";
    public static final String FORCE_ABORT_INCOG = "FORCE_ABORT_INCOG";

    //
    public static HashMap<String,Boolean> IncogModeUsers;


    // SMS provider identification
    // It should match with your SMS gateway origin
    // You can use  MSGIND, TESTER and ALERTS as sender ID
    // If you want custom sender Id, approve MSG91 to get one
    public static final String SMS_ORIGIN = "ANHIVE";

    // special character to prefix the otp. Make sure this character appears only once in the sms
    public static final String OTP_DELIMITER = ":";
}
