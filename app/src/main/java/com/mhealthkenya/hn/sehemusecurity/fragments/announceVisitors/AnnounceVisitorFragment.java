package com.mhealthkenya.hn.sehemusecurity.fragments.announceVisitors;

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
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.fxn.stash.Stash;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.mhealthkenya.hn.sehemusecurity.R;
import com.mhealthkenya.hn.sehemusecurity.dependancies.Constants;
import com.mhealthkenya.hn.sehemusecurity.models.Organization;
import com.mhealthkenya.hn.sehemusecurity.models.PersonVisiting;
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


public class AnnounceVisitorFragment extends Fragment {

    @BindView(R.id.first_name)
    TextInputEditText etxt_first_name;

    @BindView(R.id.last_name)
    TextInputEditText etxt_last_name;

    @BindView(R.id.id_number)
    TextInputEditText etxt_id_number;

    @BindView(R.id.phone_number)
    TextInputEditText etxt_phone_number;

    @BindView(R.id.car_registraion)
    TextInputEditText etxt_car_registraion;

    @BindView(R.id.property_Spinner)
    SearchableSpinner property_Spinner;

    @BindView(R.id.personVisiting_Spinner)
    SearchableSpinner personVisiting_Spinner;

    @BindView(R.id.btn_submit)
    MaterialButton btn_submit;

    private Unbinder unbinder;
    private View root;
    private Context context;

    private auth loggedInUser;

    private int organizationID = 0;
    private int personID = 0;

    ArrayList<String> organizationList;
    ArrayList<Organization> organizations;

    ArrayList<String> personList;
    ArrayList<PersonVisiting> person;

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
        root = inflater.inflate(R.layout.fragment_announce_visitor, container, false);
        unbinder = ButterKnife.bind(this, root);

        loggedInUser = (auth) Stash.getObject(Constants.AUTH_TOKEN, auth.class);

        property_Spinner.setTitle("Select Organisation visiting ");
        property_Spinner.setPositiveButton("OK");

        personVisiting_Spinner.setTitle("Select the person visiting");
        personVisiting_Spinner.setPositiveButton("OK");

        getProperty();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                walkinVisit();

            }
        });

        return root;
    }

    private void walkinVisit(){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("first_name", etxt_first_name.getText().toString());
            jsonObject.put("last_name", etxt_last_name.getText().toString());
            jsonObject.put("person_visit", personID);
            jsonObject.put("national_id", etxt_id_number.getText().toString());
            jsonObject.put("msisdn", etxt_phone_number.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String auth_token = loggedInUser.getAuth_token();

        AndroidNetworking.post(Constants.ENDPOINT+Constants.WALKIN_VISIT)
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

                        try {

                            boolean  status = response.has("success") && response.getBoolean("success");
                            String  message = response.has("message") ? response.getString("message") : "" ;
                            String  errors = response.has("errors") ? response.getString("errors") : "" ;

                            if (status){

                                NavHostFragment.findNavController(AnnounceVisitorFragment.this).navigate(R.id.nav_home);
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                            }else{

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
                        Log.e(TAG, String.valueOf(error.getErrorCode()));

                        if (error.getErrorBody().contains("An error occurred")){

                            Snackbar.make(root.findViewById(R.id.frag_announce_visitor), "Ensure you fill the required details.", Snackbar.LENGTH_LONG).show();

                        }
                        else{

                            Snackbar.make(root.findViewById(R.id.frag_announce_visitor), "" + error.getErrorBody(), Snackbar.LENGTH_LONG).show();


                        }

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
                                    property_Spinner.setSelection(pos);

//                                    organizationID = organizations.get(aa.getCount()-1).getId();

                                    property_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                                            personVisiting_Spinner.setAdapter(null);

                                            organizationID = organizations.get(position).getId();

                                            if (organizationID !=0){

                                                getPersonVisiting(organizationID);

                                            }
                                            else {

                                                Toast.makeText(context, "Error Loading...", Toast.LENGTH_SHORT).show();

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

    private void getPersonVisiting(int orgId) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("organization_id", orgId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String auth_token = loggedInUser.getAuth_token();

        AndroidNetworking.post(Constants.ENDPOINT+Constants.PERSON_VISITING)
                .addHeaders("Authorization","Token "+ auth_token)
                .addHeaders("Content-Type", "application.json")
                .addHeaders("Accept", "*/*")
                .addHeaders("Accept", "gzip, deflate, br")
                .addHeaders("Connection","keep-alive")
                .addJSONObjectBody(jsonObject) // posting json
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.e(TAG, response.toString());

                        try {

                            person = new ArrayList<PersonVisiting>();
                            personList = new ArrayList<String>();

                            person.clear();
                            personList.clear();

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = (JSONObject) response.get(i);

                                int id = jsonObject.has("id") ? jsonObject.getInt("id") : 0;
                                String uuid = jsonObject.has("uuid") ? jsonObject.getString("uuid") : "";
                                String name = jsonObject.has("name") ? jsonObject.getString("name") : "";
                                String created_at = jsonObject.has("created_at") ? jsonObject.getString("created_at") : "";
                                String updated_at = jsonObject.has("updated_at") ? jsonObject.getString("updated_at") : "";
                                int organization = jsonObject.has("unit_id") ? jsonObject.getInt("unit_id") : 0;
                                int created_by = jsonObject.has("created_by") ? jsonObject.getInt("created_by") : 0;
                                String updated_by = jsonObject.has("updated_by") ? jsonObject.getString("updated_by") : "";

                                PersonVisiting newPersonVisiting = new PersonVisiting(id,name,uuid,created_at,updated_at,organization,created_by,updated_by);

                                person.add(newPersonVisiting);
                                personList.add(newPersonVisiting.getPersonName());



                            }

                            person.add(new PersonVisiting(0,"Select the person you are visiting.","--select--","--select--","--select--",0,0,"--select--"));
                            personList.add("Select the person you are visiting.");

                            int pos =person.indexOf(new PersonVisiting(0,"Select the person you are visiting.","--select--","--select--","--select--",0,0,"--select--"));
                            if (pos > personID)
                                pos=0;

                            ArrayAdapter<String> aa=new ArrayAdapter<String>(context,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    personList){
                                @Override
                                public int getCount() {
                                    return super.getCount(); // you don't display last item. It is used as hint.
                                }
                            };

                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            if (personVisiting_Spinner != null) {
                                personVisiting_Spinner.setAdapter(aa);
                                personVisiting_Spinner.setSelection(pos);

                                personVisiting_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                                        personID = person.get(position).getId();

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                            Snackbar.make(root.findViewById(R.id.frag_announce_visitor), e.getMessage(), Snackbar.LENGTH_LONG).show();


                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                        // handle error

                        Log.e(TAG, String.valueOf(anError.getErrorCode()));

                        Toast.makeText(context, anError.getErrorBody(), Toast.LENGTH_SHORT).show();

                    }
                });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}