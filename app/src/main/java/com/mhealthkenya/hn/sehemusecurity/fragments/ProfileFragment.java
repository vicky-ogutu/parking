package com.mhealthkenya.hn.sehemusecurity.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.fxn.stash.Stash;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.mhealthkenya.hn.sehemusecurity.R;
import com.mhealthkenya.hn.sehemusecurity.dependancies.Constants;
import com.mhealthkenya.hn.sehemusecurity.models.User;
import com.mhealthkenya.hn.sehemusecurity.models.auth;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.mhealthkenya.hn.sehemusecurity.dependancies.AppController.TAG;


public class ProfileFragment extends Fragment {

    @BindView(R.id.card_name)
    MaterialTextView card_name;

    @BindView(R.id.card_phone)
    MaterialTextView card_phone;

    @BindView(R.id.etxt_first_name)
    TextInputEditText etxt_first_name;

    @BindView(R.id.etxt_lastname)
    TextInputEditText etxt_last_name;

    @BindView(R.id.etxt_email)
    TextInputEditText etxt_email;

    @BindView(R.id.btn_update_profile)
    MaterialButton btn_update_profile;


    private Unbinder unbinder;
    private View root;
    private Context context;

    private auth loggedInUser;


    public void onAttach(Context ctx) {
        super.onAttach(ctx);
        this.context = ctx;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, root);

        loggedInUser = (auth) Stash.getObject(Constants.AUTH_TOKEN, auth.class);

        loadCurrentUser();

        btn_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateUserDetails();

            }
        });

        return root;
    }

    private void loadCurrentUser(){

        String auth_token = loggedInUser.getAuth_token();


        AndroidNetworking.get(Constants.ENDPOINT+Constants.CURRENT_USER)
                .addHeaders("Authorization","Token "+ auth_token)
                .addHeaders("Content-Type", "application.json")
                .addHeaders("Accept", "*/*")
                .addHeaders("Accept", "gzip, deflate, br")
                .addHeaders("Connection","keep-alive")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
//                        Log.e(TAG, response.toString());

                        try {

                            int id = response.has("id") ? response.getInt("id") : 0;
                            String first_name = response.has("first_name") ? response.getString("first_name") : "";
                            String last_name = response.has("last_name") ? response.getString("last_name") : "";
                            String email = response.has("email") ? response.getString("email") : "";
                            String phone_no = response.has("msisdn") ? response.getString("msisdn") : "";
                            String gender = response.has("gender") ? response.getString("gender") : "";
                            int access_level = response.has("access_level") ? response.getInt("access_level") : 0;


                            User newUser = new User(id,first_name,last_name,email,phone_no,gender,access_level);

                            card_name.setText(first_name+" "+last_name);
                            card_phone.setText(phone_no);

                            etxt_first_name.setText(first_name);
                            etxt_last_name.setText(last_name);
                            etxt_email.setText(email);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
//                        Log.e(TAG, error.getErrorBody());

                        Snackbar.make(root.findViewById(R.id.frag_profile), "Error: " + error.getErrorBody(), Snackbar.LENGTH_LONG).show();

                    }
                });

    }

    private void updateUserDetails() {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("email", etxt_email.getText().toString());
            jsonObject.put("first_name", etxt_first_name.getText().toString());
            jsonObject.put("last_name", etxt_last_name.getText().toString());



        } catch (JSONException e) {
            e.printStackTrace();
        }

        String auth_token = loggedInUser.getAuth_token();

        AndroidNetworking.patch(Constants.ENDPOINT+Constants.UPDATE_USER)
                .addHeaders("Authorization","Token "+ auth_token)
                .addHeaders("Accept", "*/*")
                .addHeaders("Accept", "gzip, deflate, br")
                .addHeaders("Connection","keep-alive")
                .setContentType("application.json")
                .addJSONObjectBody(jsonObject) // posting json
                .build()
                .getAsJSONObject(new JSONObjectRequestListener(){
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, response.toString());

                        try {

                            String  errors = response.has("error") ? response.getString("error") : "" ;


                            if (response.has("id")){

                                NavHostFragment.findNavController(ProfileFragment.this).navigate(R.id.nav_home);
                                Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                            }
                            else{

                                Toast.makeText(context, errors, Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e(TAG, error.getErrorBody());

                        Snackbar.make(root.findViewById(R.id.frag_profile), "Error: "+error.getErrorBody(), Snackbar.LENGTH_LONG).show();
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}