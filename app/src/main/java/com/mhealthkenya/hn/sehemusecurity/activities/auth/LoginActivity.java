package com.mhealthkenya.hn.sehemusecurity.activities.auth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.fxn.stash.Stash;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mhealthkenya.hn.sehemusecurity.R;
import com.mhealthkenya.hn.sehemusecurity.activities.MainActivity;
import com.mhealthkenya.hn.sehemusecurity.activities.ScanQrActivity;
import com.mhealthkenya.hn.sehemusecurity.dependancies.Constants;
import com.mhealthkenya.hn.sehemusecurity.models.auth;

import org.json.JSONException;
import org.json.JSONObject;

import static com.mhealthkenya.hn.sehemusecurity.dependancies.AppController.TAG;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout til_phone;
    private TextInputEditText edtxt_phone;
    private TextInputLayout til_password;
    private TextInputEditText edtxt_password;
    private TextView forgotPassword;
    private MaterialButton btn_sign_in;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stash.init(this);
        setContentView(R.layout.activity_login);

        initialise();

        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setTitle("Signing In...");
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);



        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mint = new Intent(LoginActivity.this,ResetPasswordActivity.class);
                mint.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mint);

            }
        });

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* pDialog.show();
                loginRequest();*/

                Intent intent =new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

    }

    public void initialise(){

        til_phone = findViewById(R.id.til_phone);
        edtxt_phone = findViewById(R.id.edtxt_phone);
        til_password = findViewById(R.id.til_password);
        edtxt_password = findViewById(R.id.edtxt_password);
        forgotPassword = findViewById(R.id.tv_forgot_password);
        btn_sign_in = findViewById(R.id.btn_sign_in);


    }

    private void loginRequest() {


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("password", edtxt_password.getText().toString());
            jsonObject.put("msisdn", edtxt_phone.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(Constants.ENDPOINT+Constants.LOGIN)
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

//                        Log.e(TAG, response.toString());

                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog.cancel();
                        }

                        try {
                            boolean  status = response.has("success") && response.getBoolean("success");
                            String error = response.has("error") ? response.getString("error") : "";
                            String auth_token = response.has("auth_token") ? response.getString("auth_token") : "";

                            if (response.has("auth_token")){

                                Bundle bundle = new Bundle();
                                bundle.putString("auth", auth_token);

                                auth newUser = new auth(auth_token);

                                Stash.put(Constants.AUTH_TOKEN, newUser);

                                Intent mint = new Intent(LoginActivity.this, MainActivity.class);
                                mint.putExtras(bundle);
                                mint.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(mint);

                                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();


                            }else if (!status){

                                if (pDialog != null && pDialog.isShowing()) {
                                    pDialog.hide();
                                    pDialog.cancel();
                                }

                                Snackbar.make(findViewById(R.id.login_lyt), error, Snackbar.LENGTH_LONG).show();

                            }
                            else {

                                Snackbar.make(findViewById(R.id.login_lyt), error, Snackbar.LENGTH_LONG).show();


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e(TAG, error.getErrorBody());

                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog.cancel();
                        }



                        if (error.getErrorBody().contains("Unable to log in with provided credentials.")){

                            Snackbar.make(findViewById(R.id.login_lyt), "Invalid phone number or password." , Snackbar.LENGTH_LONG).show();

                            til_phone.setError("Please confirm the phone number is correct!");
                            til_password.setError("Please confirm the password is correct!");

                        }
                        else if(error.getErrorBody().contains("Clock-out time passed,") ){


                            AlertDialog.Builder builder= new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("Alert.");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                    edtxt_phone.getText().clear();
                                    edtxt_password.getText().clear();

                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.cancel();

                                }
                            });
                            builder.setMessage("Clock-out time passed, cannot login to app. Please try again at your check in time.");
                            AlertDialog alert = builder.create();
                            alert.show();


                        }else if (error.getErrorBody().contains("invalid credentials")){

                            Snackbar.make(findViewById(R.id.login_lyt), "Please provide your phone number and password." , Snackbar.LENGTH_LONG).show();

                            til_phone.setError("Please enter a phone number!");
                            til_password.setError("Please enter a password!");

                        }
                        else if(error.getErrorCode() == 0 ){

                            Snackbar.make(findViewById(R.id.login_lyt), "Please try again later!" , Snackbar.LENGTH_LONG).show();

                        }
                        else if(error.getErrorCode() == 500 ){

                            Snackbar.make(findViewById(R.id.login_lyt), "Internal Server Error. Please try again later!" , Snackbar.LENGTH_LONG).show();

                        }
                        else {

                            Snackbar.make(findViewById(R.id.login_lyt), "" + error.getErrorBody(), Snackbar.LENGTH_LONG).show();

                        }

                    }
                });

    }

}