package com.mhealthkenya.hn.sehemusecurity.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.mhealthkenya.hn.sehemusecurity.models.Organization;
import com.mhealthkenya.hn.sehemusecurity.models.User;
import com.mhealthkenya.hn.sehemusecurity.models.auth;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.mhealthkenya.hn.sehemusecurity.dependancies.AppController.TAG;


public class ProfileFragment extends Fragment {

    @BindView(R.id.card_name)
    MaterialTextView card_name;

    @BindView(R.id.card_property)
    MaterialTextView card_property;

    @BindView(R.id.card_phone)
    MaterialTextView card_phone;

    @BindView(R.id.etxt_first_name)
    TextInputEditText etxt_first_name;

    @BindView(R.id.etxt_lastname)
    TextInputEditText etxt_last_name;

    @BindView(R.id.etxt_email)
    TextInputEditText etxt_email;

    @BindView(R.id.property_Spinner)
    SearchableSpinner property_Spinner;

    @BindView(R.id.btn_update_profile)
    MaterialButton btn_update_profile;


    private Unbinder unbinder;
    private View root;
    private Context context;

    private auth loggedInUser;

    private int organizationID = 0;

    ArrayList<String> organizationList;
    ArrayList<Organization> organizations;


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

        property_Spinner.setTitle("Select your work place.");
        property_Spinner.setPositiveButton("OK");

        loadMyProfile();
        getProperty();

        btn_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateUserDetails();

            }
        });

        return root;
    }

    private void loadMyProfile(){

        String auth_token = loggedInUser.getAuth_token();


        AndroidNetworking.get(Constants.ENDPOINT+Constants.MY_PROFILE)
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

                            boolean  status = response.has("success") && response.getBoolean("success");
                            String  message = response.has("message") ? response.getString("message") : "" ;
                            String  errors = response.has("errors") ? response.getString("errors") : "" ;

                            if (status){

                                JSONObject myProfile = response.has("user") ? response.getJSONObject("user") : null ;

                                int id = myProfile.has("id") ? myProfile.getInt("id") : 0;
                                String first_name = myProfile.has("first_name") ? myProfile.getString("first_name") : "";
                                String last_name = myProfile.has("last_name") ? myProfile.getString("last_name") : "";
                                String email = myProfile.has("email") ? myProfile.getString("email") : "";
                                String phone_no = myProfile.has("msisdn") ? myProfile.getString("msisdn") : "";
                                String gender = myProfile.has("gender") ? myProfile.getString("gender") : "";


                                JSONObject myProperty = response.has("Property") ? response.getJSONObject("Property") : null ;
                                int idP = myProperty.has("id") ? myProperty.getInt("id") : 0;
                                String name = myProperty.has("name") ? myProperty.getString("name") : "";


                                card_name.setText(first_name+" "+last_name);
                                card_phone.setText(phone_no);
                                card_property.setText(name);

                                etxt_first_name.setText(first_name);
                                etxt_last_name.setText(last_name);
                                etxt_email.setText(email);

                                organizationID = idP;

                            }


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

    private void getProperty() {

        String auth_token = loggedInUser.getAuth_token();

        AndroidNetworking.get(Constants.ENDPOINT+Constants.ORGANIZATION)
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
                        Log.e(TAG, response.toString());

                        try {

                            boolean  status = response.has("success") && response.getBoolean("success");
                            String  message = response.has("message") ? response.getString("message") : "" ;
                            String  errors = response.has("errors") ? response.getString("errors") : "" ;


                            organizations = new ArrayList<Organization>();
                            organizationList = new ArrayList<String>();

                            organizations.clear();
                            organizationList.clear();

                            if (status){

                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject facility = (JSONObject) jsonArray.get(i);

                                    int id = facility.has("id") ? facility.getInt("id") : 0;
                                    String uuid = facility.has("uuid") ? facility.getString("uuid") : "";
                                    String name = facility.has("name") ? facility.getString("name") : "";
                                    String logo = facility.has("logo") ? facility.getString("logo") : "";
                                    String floor = facility.has("floor") ? facility.getString("floor") : "";
                                    String extra_info = facility.has("extra_info") ? facility.getString("extra_info") : "";
                                    String created_at = facility.has("created_at") ? facility.getString("created_at") : "";
                                    String updated_at = facility.has("updated_at") ? facility.getString("updated_at") : "";
                                    int unit_id = facility.has("unit_id") ? facility.getInt("unit_id") : 0;
                                    int created_by = facility.has("created_by") ? facility.getInt("created_by") : 0;
                                    String updated_by = facility.has("updated_by") ? facility.getString("updated_by") : "";


                                    Organization newOrganization = new Organization(id,uuid,name,logo,floor,extra_info,created_at,updated_at,unit_id,created_by,updated_by);

                                    organizations.add(newOrganization);
                                    organizationList.add(newOrganization.getName());
                                }


                                organizations.add(new Organization(0,"Select your Organization.","Select your Organization.","--select--","--select--","--select--","--select--","--select--",0,0,""));
                                organizationList.add("Select your Organization.");

                                int pos =organizations.indexOf(new Organization(0,"Select your Organization.","Select your Organization.","--select--","--select--","--select--","--select--","--select--",0,0,""));
                                if (pos >= organizationID)
                                    pos=0;

                                ArrayAdapter<String> aa=new ArrayAdapter<String>(context,
                                        android.R.layout.simple_spinner_dropdown_item,
                                        organizationList){
                                    @Override
                                    public int getCount() {
                                        return super.getCount(); // you don't display last item. It is used as hint.
                                    }
                                };

                                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                if (property_Spinner != null){
                                    property_Spinner.setAdapter(aa);
                                    property_Spinner.setSelection(organizationID);

//                                    organizationID = organizations.get(aa.getCount()-1).getId();

                                    property_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                                            organizationID = organizations.get(position).getId();

                                            if (organizationID !=0){



                                            }
                                            else {

                                                Toast.makeText(context, "Please select an organization...", Toast.LENGTH_SHORT).show();

                                            }



                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });

                                }

                            } else{

                                Snackbar.make(root.findViewById(R.id.frag_announce_visitor), errors, Snackbar.LENGTH_LONG).show();


                            }



                        } catch (JSONException e) {
                            e.printStackTrace();

                            Snackbar.make(root.findViewById(R.id.frag_announce_visitor), e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }


                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error

                        Log.e(TAG, String.valueOf(error.getErrorCode()));

                        Snackbar.make(root.findViewById(R.id.frag_announce_visitor), error.getErrorDetail(), Snackbar.LENGTH_LONG).show();

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