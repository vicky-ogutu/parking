package com.mhealthkenya.hn.sehemusecurity.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mhealthkenya.hn.sehemusecurity.R;
import com.mhealthkenya.hn.sehemusecurity.dependancies.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import static com.mhealthkenya.hn.sehemusecurity.dependancies.AppController.TAG;


public class ResetPasswordActivity extends AppCompatActivity {

    private TextInputLayout tilEmail;
    private TextInputEditText etxtEmail;
    private MaterialButton resetPassword;
    private TextView signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        initialize();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mint = new Intent(ResetPasswordActivity.this,LoginActivity.class);
                mint.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mint);

            }
        });


    }


    public void initialize(){

        tilEmail = (TextInputLayout) findViewById(R.id.til_email);
        etxtEmail = (TextInputEditText) findViewById(R.id.etxt_email);
        resetPassword= (MaterialButton) findViewById(R.id.btn_reset_password);
        signIn = (TextView) findViewById(R.id.tv_signin);

    }

    private void resetPassword(){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", etxtEmail.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(Constants.ENDPOINT+Constants.RESET_PASSWORD)
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



                        try {
                            boolean  status = response.has("success") && response.getBoolean("success");
                            String error = response.has("error") ? response.getString("error") : "";
                            String message = response.has("message") ? response.getString("message") : "";

                            if (status){


                                Intent mint = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                mint.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(mint);

                                Toast.makeText(ResetPasswordActivity.this, "Successful. Check your email!", Toast.LENGTH_SHORT).show();


                            }else if (!status){

                                Snackbar.make(findViewById(R.id.activity_reset_password), message, Snackbar.LENGTH_LONG).show();

                            }
                            else{



                                Snackbar.make(findViewById(R.id.activity_reset_password), error, Snackbar.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e(TAG, String.valueOf(error.getErrorCode()));


                        if (error.getErrorCode() == 0){

                            Intent mint = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                            mint.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mint);

                            Toast.makeText(ResetPasswordActivity.this, "Email sent. Check your email!", Toast.LENGTH_LONG).show();

                        }
                        else{

                            Snackbar.make(findViewById(R.id.activity_reset_password), "" + error.getErrorBody(), Snackbar.LENGTH_LONG).show();


                        }

                    }
                });

    }

}