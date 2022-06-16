package com.mhealthkenya.hn.sehemusecurity.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.fxn.stash.Stash;
import com.google.android.material.snackbar.Snackbar;
import com.mhealthkenya.hn.sehemusecurity.R;
import com.mhealthkenya.hn.sehemusecurity.adapters.ActiveVisitAdapter;
import com.mhealthkenya.hn.sehemusecurity.dependancies.Constants;
import com.mhealthkenya.hn.sehemusecurity.models.ActiveVisit;
import com.mhealthkenya.hn.sehemusecurity.models.auth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.mhealthkenya.hn.sehemusecurity.dependancies.AppController.TAG;


public class ActiveVisitsFragment extends Fragment {

    private Unbinder unbinder;
    private View root;
    private Context context;

    private auth loggedInUser;

    private ActiveVisitAdapter mAdapter;
    private ArrayList<ActiveVisit> activeVisitArrayList;

    @BindView(R.id.shimmer_my_container)
    ShimmerFrameLayout shimmer_my_container;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.no_ative_visits_lyt)
    LinearLayout no_ative_visits_lyt;

    @BindView(R.id.error_lyt)
    LinearLayout error_lyt;

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
        root = inflater.inflate(R.layout.fragment_active_visits, container, false);
        unbinder = ButterKnife.bind(this, root);

        loggedInUser = (auth) Stash.getObject(Constants.AUTH_TOKEN, auth.class);

        activeVisitArrayList = new ArrayList<>();
        mAdapter = new ActiveVisitAdapter(context, activeVisitArrayList);


        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        recyclerView.setAdapter(mAdapter);



        loadActiveVisits();

        return root;
    }

    private void loadActiveVisits() {

        String auth_token = loggedInUser.getAuth_token();


        AndroidNetworking.get(Constants.ENDPOINT+Constants.ACTIVE_VISIT)
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

                        activeVisitArrayList.clear();

                        if (recyclerView!=null)
                            recyclerView.setVisibility(View.VISIBLE);

                        if (shimmer_my_container!=null){
                            shimmer_my_container.stopShimmerAnimation();
                            shimmer_my_container.setVisibility(View.GONE);
                        }

                        try {

                            boolean  status = response.has("success") && response.getBoolean("success");
                            String  message = response.has("message") ? response.getString("message") : "" ;
                            String  errors = response.has("errors") ? response.getString("errors") : "" ;

                            if (status){

                                JSONArray myArray = response.getJSONArray("visits");

                                if (myArray.length() > 0){


                                    for (int i = 0; i < myArray.length(); i++) {

                                        JSONObject item = (JSONObject) myArray.get(i);

                                        int id = item.has("id") ? item.getInt("id") : 0;
                                        String uuid = item.has("uuid") ? item.getString("uuid") : "";
                                        String temp = item.has("temp") ? item.getString("temp") : "";
                                        String first_name = item.has("first_name") ? item.getString("first_name") : "";
                                        String last_name = item.has("uuid") ? item.getString("last_name") : "";
                                        String vehicle_reg = item.has("vehicle_reg") ? item.getString("vehicle_reg") : "";
                                        String msisdn = item.has("msisdn") ? item.getString("msisdn") : "";
                                        String is_approved = item.has("is_approved") ? item.getString("is_approved") : "";
                                        String is_active = item.has("is_active") ? item.getString("is_active") : "";
                                        String is_started = item.has("is_started") ? item.getString("is_started") : "";
                                        String is_ended = item.has("is_ended") ? item.getString("is_ended") : "";
                                        String national_id = item.has("national_id") ? item.getString("national_id") : "";
                                        String created_at = item.has("created_at") ? item.getString("created_at") : "";
                                        String date = item.has("date") ? item.getString("date") : "";
                                        String time = item.has("time") ? item.getString("time") : "";
                                        String updated_at = item.has("updated_at") ? item.getString("updated_at") : "";
                                        String person_visit = item.has("person_visit") ? item.getString("person_visit") : "";
                                        String house_visit = item.has("house_visit") ? item.getString("house_visit") : "";
                                        String residential = item.has("residential") ? item.getString("residential") : "";
                                        int created_by = item.has("created_by") ? item.getInt("created_by") : 0;

                                        String house = item.has("house") ? item.getString("house") : "";


                                        ActiveVisit newActiveVisit = new ActiveVisit(id,uuid, temp, first_name, last_name,vehicle_reg,msisdn,is_approved,is_active,is_started,is_ended,national_id,created_at,date,time,updated_at,person_visit,house_visit,residential,created_by,house);

                                        activeVisitArrayList.add(newActiveVisit);
                                        mAdapter.notifyDataSetChanged();

                                    }
                                }else {
                                    //not data found
                                    no_ative_visits_lyt.setVisibility(View.VISIBLE);


                                }

                            }
                            else {

                                Snackbar.make(root.findViewById(R.id.frag_active_visits),message, Snackbar.LENGTH_LONG).show();

                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error

                        if (recyclerView!=null)
                            recyclerView.setVisibility(View.VISIBLE);

                        if (shimmer_my_container!=null){
                            shimmer_my_container.stopShimmerAnimation();
                            shimmer_my_container.setVisibility(View.GONE);
                        }

                        Log.e(TAG, String.valueOf(error.getErrorCode()));
                        Log.e(TAG, error.getErrorBody());

                        if (error.getErrorCode() == 0){

                            no_ative_visits_lyt.setVisibility(View.VISIBLE);
                        }
                        else {

                            error_lyt.setVisibility(View.VISIBLE);
                            Snackbar.make(root.findViewById(R.id.frag_active_visits), "Error: " + error.getErrorBody(), Snackbar.LENGTH_LONG).show();

                        }


                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmer_my_container.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        shimmer_my_container.stopShimmerAnimation();
        super.onPause();
    }

}