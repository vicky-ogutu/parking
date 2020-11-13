package com.mhealthkenya.hn.sehemusecurity.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    private MaterialButton btn_sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stash.init(this);
        setContentView(R.layout.activity_login);


        til_phone = findViewById(R.id.til_phone);
        edtxt_phone = findViewById(R.id.edtxt_phone);
        til_password = findViewById(R.id.til_password);
        edtxt_password = findViewById(R.id.edtxt_password);
        btn_sign_in = findViewById(R.id.btn_sign_in);

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginRequest();

            }
        });

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



                        if (error.getErrorBody().contains("Unable to log in with provided credentials.")){

                            Snackbar.make(findViewById(R.id.login_lyt), "Invalid phone number or password." , Snackbar.LENGTH_LONG).show();

                            til_phone.setError("Please confirm the phone number is correct!");
                            til_password.setError("Please confirm the password is correct!");

                        }
                        else if(error.getErrorBody().contains("Clock-out time passed,") ){

                            Snackbar.make(findViewById(R.id.login_lyt), "Clock-out time passed, cannot login to app " , Snackbar.LENGTH_LONG).show();


                        }

                        else {

                            Snackbar.make(findViewById(R.id.login_lyt), "" + error.getErrorBody(), Snackbar.LENGTH_LONG).show();


                        }


                    }
                });



    }


}