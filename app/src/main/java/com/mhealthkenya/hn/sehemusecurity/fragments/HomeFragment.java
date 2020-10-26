package com.mhealthkenya.hn.sehemusecurity.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mhealthkenya.hn.sehemusecurity.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HomeFragment extends Fragment {

    private Unbinder unbinder;
    private View root;
    private Context context;

    @BindView(R.id.verifyVisitorLayout)
    CardView verifyVisitorLayout;

    @BindView(R.id.announceVisitorLayout)
    CardView announceVisitorLayout;

    @BindView(R.id.myProfileLayout)
    CardView myProfileLayout;

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
        root = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, root);

        verifyVisitorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.nav_verfy_visitor);
                Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show();

            }
        });

        announceVisitorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.nav_announce_visitor);

            }
        });

        myProfileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.nav_profile);

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}