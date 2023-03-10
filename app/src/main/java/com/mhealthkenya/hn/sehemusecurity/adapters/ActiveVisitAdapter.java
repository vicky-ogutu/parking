package com.mhealthkenya.hn.sehemusecurity.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.fxn.stash.Stash;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mhealthkenya.hn.sehemusecurity.R;
import com.mhealthkenya.hn.sehemusecurity.dependancies.Constants;
import com.mhealthkenya.hn.sehemusecurity.dependancies.Tools;
import com.mhealthkenya.hn.sehemusecurity.dependancies.ViewAnimation;
import com.mhealthkenya.hn.sehemusecurity.models.ActiveVisit;
import com.mhealthkenya.hn.sehemusecurity.models.auth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.mhealthkenya.hn.sehemusecurity.dependancies.AppController.TAG;

public class ActiveVisitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ActiveVisit> items = new ArrayList<>();

    private auth loggedInUser;
    private View root;
    private Context context;
    private ActiveVisitAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(ActiveVisitAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ActiveVisitAdapter(Context context, List<ActiveVisit> items) {
        this.items = items;
        this.context = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView visitorName;
        public TextView phoneNumber;
        public TextView carDetails;
        public TextView idNumber;
        public TextView visitDate;
        public TextView visitTime;
        public ImageButton bt_expand;
        public MaterialButton endVisit;
        public View lyt_expand;
        public View lyt_parent;


        public OriginalViewHolder(View v) {
            super(v);

            visitorName = (TextView) v.findViewById(R.id.txt_person);
            phoneNumber = (TextView) v.findViewById(R.id.txt_phone_number);
            carDetails = (TextView) v.findViewById(R.id.txt_vehicle_reg);
            idNumber = (TextView) v.findViewById(R.id.txt_id_no);
            visitDate = (TextView) v.findViewById(R.id.txt_date);
            visitTime = (TextView) v.findViewById(R.id.txt_time);
            bt_expand = (ImageButton) v.findViewById(R.id.bt_expand);
            endVisit = (MaterialButton) v.findViewById(R.id.btn_end_visit);
            lyt_expand = (View) v.findViewById(R.id.lyt_expand);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);


        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_active_visit, parent, false);
        vh = new ActiveVisitAdapter.OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ActiveVisit obj = items.get(position);
        if (holder instanceof ActiveVisitAdapter.OriginalViewHolder) {
            ActiveVisitAdapter.OriginalViewHolder view = (ActiveVisitAdapter.OriginalViewHolder) holder;

            view.visitorName.setText("Name: " + obj.getFirst_name() + " " + obj.getLast_name());
            view.phoneNumber.setText("Phone: " + obj.getMsisdn());
            view.visitDate.setText("Date: " + obj.getDate());
            view.visitTime.setText("Time: " + obj.getTime());
            //added


            if (obj.getVehicle_reg().isEmpty()){

                view.carDetails.setVisibility(View.GONE);
            }
            else {
                view.carDetails.setText("Car: " + obj.getVehicle_reg());
            }

            if (obj.getNational_id().isEmpty()){

                view.idNumber.setVisibility(View.GONE);
            }
            else {
                view.idNumber.setText("ID No: " + obj.getNational_id());
            }


            view.bt_expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean show = toggleLayoutExpand(!obj.expanded, v, view.lyt_expand);
                    items.get(position).expanded = show;
                }
            });

            view.endVisit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    JSONObject jsonObject = new JSONObject();
                    try {

                        jsonObject.put("uid", obj.getUuid());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    loggedInUser = (auth) Stash.getObject(Constants.AUTH_TOKEN, auth.class);

                    String auth_token = loggedInUser.getAuth_token();

                    AndroidNetworking.post(Constants.ENDPOINT+Constants.END_VISIT)
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
                                        String  message = response.has("messages") ? response.getString("messages") : "" ;
                                        String  errors = response.has("errors") ? response.getString("errors") : "" ;

                                        if (status){

                                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                            view.endVisit.setEnabled(false);

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

                                    Snackbar.make(root.findViewById(R.id.frag_active_visits), "" + error.getErrorBody(), Snackbar.LENGTH_LONG).show();
                                }
                            });

                }
            });


            // void recycling view
            if(obj.expanded){
                view.lyt_expand.setVisibility(View.VISIBLE);
            } else {
                view.lyt_expand.setVisibility(View.GONE);
            }
            Tools.toggleArrow(obj.expanded, view.bt_expand, false);

        }
    }

    private boolean toggleLayoutExpand(boolean show, View view, View lyt_expand) {
        Tools.toggleArrow(show, view);
        if (show) {
            ViewAnimation.expand(lyt_expand);
        } else {
            ViewAnimation.collapse(lyt_expand);
        }
        return show;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
