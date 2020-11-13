package com.mhealthkenya.hn.sehemusecurity.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mhealthkenya.hn.sehemusecurity.R;
import com.mhealthkenya.hn.sehemusecurity.models.VerifiedVisitor;

public class VerifiedVisitorDialog extends BottomSheetDialogFragment {

    private Context context;
    private VerifiedVisitor visitor = null;


    private TextView name;
    private TextView first_name;
    private TextView id_no;
    private TextView vehicle_reg;
    private TextView date_of_visit;
    private TextView phone_number;
    private TextView time;
    private TextView person_visiting;


    private View view;

    public VerifiedVisitorDialog() {
        // Required empty public constructor
    }

    public static VerifiedVisitorDialog newInstance(VerifiedVisitor visitor, Context context) {
        VerifiedVisitorDialog fragment = new VerifiedVisitorDialog();
        fragment.visitor = visitor;
        fragment.context = context;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.info_verified_visitor, container, false);


        name = view.findViewById(R.id.name);
        phone_number = view.findViewById(R.id.phone_number);
        first_name = view.findViewById(R.id.first_name);
        vehicle_reg = view.findViewById(R.id.vehicle_reg);
        phone_number = view.findViewById(R.id.phone_number);
        id_no = view.findViewById(R.id.id_no);
        date_of_visit = view.findViewById(R.id.date_of_visit);
        time = view.findViewById(R.id.time);
        person_visiting = view.findViewById(R.id.person_visiting);


        name.setText(visitor.getFirst_name());
        phone_number.setText(visitor.getMsisdn());
        first_name.setText(visitor.getFirst_name());
        vehicle_reg.setText(visitor.getVehicle_reg());
        phone_number.setText(visitor.getMsisdn());
        id_no.setText(visitor.getNational_id());
        date_of_visit.setText(visitor.getDate());
        time.setText(visitor.getTime());
        person_visiting.setText(visitor.getPerson_visit());


        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }



}
