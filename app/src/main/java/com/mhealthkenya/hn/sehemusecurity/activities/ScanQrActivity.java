package com.mhealthkenya.hn.sehemusecurity.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.fxn.stash.Stash;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.Result;
import com.mhealthkenya.hn.sehemusecurity.R;
import com.mhealthkenya.hn.sehemusecurity.activities.auth.LoginActivity;
import com.mhealthkenya.hn.sehemusecurity.dependancies.Constants;
import com.mhealthkenya.hn.sehemusecurity.dialogs.VerifiedVisitorDialog;
import com.mhealthkenya.hn.sehemusecurity.models.VerifiedVisitor;
import com.mhealthkenya.hn.sehemusecurity.models.auth;


import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler;

import static android.Manifest.permission_group.CAMERA;

import static com.mhealthkenya.hn.sehemusecurity.dependancies.AppController.TAG;

public class ScanQrActivity extends AppCompatActivity implements ResultHandler{

    private static final int REQUEST_CAMERA= 1;
    private ZXingScannerView scannerView;

    private ProgressDialog pDialog;

    private auth loggedInUser;

    private String scanResult = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView= new ZXingScannerView(this);
        Stash.init(this);
        setContentView(scannerView);

        loggedInUser = (auth) Stash.getObject(Constants.AUTH_TOKEN, auth.class);

        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Loading");
        pDialog.setMessage("Verifying code...");
        pDialog.setCancelable(false);
        pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pDialog.dismiss();//dismiss dialog
                scannerView.resumeCameraPreview(ScanQrActivity.this);
            }
        });

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            if (checkPermission())
            {
                Toast.makeText(ScanQrActivity.this, "Permission is granted", Toast.LENGTH_SHORT).show();
            }
            else
            {
                requestPermission();
            }
        }
    }



    private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(ScanQrActivity.this, CAMERA)== PackageManager.PERMISSION_GRANTED);
    }
    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA},REQUEST_CAMERA);
    }

    public void onRequestPermissionResult(int requestCode, String permission[],int grantResult[])
    {
        switch (requestCode)
        {
            case REQUEST_CAMERA :
                if (grantResult.length >0)
                {
                    boolean cameraAccepted = grantResult[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted)
                    {
                        Toast.makeText(ScanQrActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(ScanQrActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {
                            if (shouldShowRequestPermissionRationale(CAMERA))
                            {
                                displayAlertMessage("You need to allow access for both permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                requestPermissions(new String[]{CAMERA} , REQUEST_CAMERA);
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;

        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (scannerView == null)
            {
                scannerView = new ZXingScannerView(this);
                setContentView(scannerView);
            }
            scannerView.setResultHandler(this);
            scannerView.startCamera();
        }
        else
        {
            requestPermission();
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        scannerView.stopCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera on pause
        scannerView.stopCamera();
    }


    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener)
    {
        new AlertDialog.Builder(ScanQrActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @Override
    public void handleResult(Result result) {
        scanResult = result.getText();

        Log.e(TAG, scanResult);

        verifyQR(scanResult);

        pDialog.show();

    }

    private void verifyQR(String scanResult){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("visit_uuid", scanResult);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String auth_token = loggedInUser.getAuth_token();

        AndroidNetworking.post(Constants.ENDPOINT+Constants.VERIFY_VISIT)
                .addHeaders("Authorization","Token "+ auth_token)
                .addHeaders("Content-Type", "application.json")
                .addHeaders("Accept", "*/*")
                .addHeaders("Accept", "gzip, deflate, br")
                .addHeaders("Connection","keep-alive")
                .addJSONObjectBody(jsonObject) // posting json
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response

                        Log.e(TAG, response.toString());

                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog.cancel();
                        }

                        try {

                            boolean  status = response.has("success") && response.getBoolean("success");
                            String  message = response.has("message") ? response.getString("message") : "" ;
                            String  errors = response.has("errors") ? response.getString("errors") : "" ;
                            JSONObject verifiedObj = response.has("data") ? response.getJSONObject("data") : null ;

                            if (status){


                                int id = verifiedObj.has("id") ? verifiedObj.getInt("id") : 0;
                                String uuid = verifiedObj.has("uuid") ? verifiedObj.getString("uuid") : "";
                                String first_name = verifiedObj.has("first_name") ? verifiedObj.getString("first_name") : "";
                                String vehicle_reg = verifiedObj.has("vehicle_reg") ? verifiedObj.getString("vehicle_reg") : "";
                                String msisdn = verifiedObj.has("msisdn") ? verifiedObj.getString("msisdn") : "";
                                String date = verifiedObj.has("date") ? verifiedObj.getString("date") : "";
                                String time = verifiedObj.has("time") ? verifiedObj.getString("time") : "";
                                String is_active = verifiedObj.has("is_active") ? verifiedObj.getString("is_active") : "";
                                String national_id = verifiedObj.has("national_id") ? verifiedObj.getString("national_id") : "";
                                String created_at = verifiedObj.has("created_at") ? verifiedObj.getString("created_at") : "";
                                String updated_at = verifiedObj.has("updated_at") ? verifiedObj.getString("updated_at") : "";
                                String person_visit = verifiedObj.has("person_visit") ? verifiedObj.getString("person_visit") : "";
                                String created_by = verifiedObj.has("created_by") ? verifiedObj.getString("created_by") : "";
                                String updated_by = verifiedObj.has("updated_by") ? verifiedObj.getString("updated_by") : "";

                                VerifiedVisitor visitor = new VerifiedVisitor(id,uuid,first_name,vehicle_reg,msisdn,date,time,is_active,national_id,created_at,updated_at,person_visit,created_by,updated_by);

                                VerifiedVisitorDialog verifiedVisitorDialog = VerifiedVisitorDialog.newInstance(visitor, ScanQrActivity.this);
                                verifiedVisitorDialog.show(getSupportFragmentManager(), verifiedVisitorDialog.getTag());

//                                Toast.makeText(ScanQrActivity.this, "Visitor Announced!", Toast.LENGTH_LONG).show();

                            }
                            else{

                                Toast.makeText(ScanQrActivity.this, errors, Toast.LENGTH_SHORT).show();

                            }

                            scannerView.stopCamera();
                            scannerView.startCamera();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error

                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog.cancel();
                        }

                        Log.e(TAG, error.getErrorBody());

                        if (error.getErrorBody().contains("Invalid QR")){

                            AlertDialog.Builder builder= new AlertDialog.Builder(ScanQrActivity.this);
                            builder.setTitle("Error");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent mint = new Intent(ScanQrActivity.this, MainActivity.class);
                                    startActivity(mint);
                                }
                            });
                            builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    scannerView.resumeCameraPreview(ScanQrActivity.this);
                                    verifyQR(scanResult);

                                }
                            });
                            builder.setMessage("Invalid QR Code.");
                            AlertDialog alert = builder.create();
                            alert.show();

                        }
                        else if (error.getErrorBody().contains("Visit date is")){

                            AlertDialog.Builder builder= new AlertDialog.Builder(ScanQrActivity.this);
                            builder.setTitle("Error");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent mint = new Intent(ScanQrActivity.this, MainActivity.class);
                                    startActivity(mint);
                                }
                            });
                            builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    scannerView.resumeCameraPreview(ScanQrActivity.this);

                                }
                            });
                            builder.setMessage(error.getErrorBody());
                            AlertDialog alert = builder.create();
                            alert.show();

                        } else

                        Toast.makeText(ScanQrActivity.this, ""+error.getErrorBody(), Toast.LENGTH_SHORT).show();

                    }
                });


    }
}