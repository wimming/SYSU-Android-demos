package com.ym.lab7;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ym on 16-11-15.
 */

public class Fragment1 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_1, container, false);

        Button ok = (Button)rootView.findViewById(R.id.ok);
        Button clear = (Button)rootView.findViewById(R.id.clear);
        final TextView password = (TextView)rootView.findViewById(R.id.password);
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("lab7pre", Context.MODE_PRIVATE);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strPassword = password.getText().toString();

                if (strPassword.equals("")) {
                    Toast.makeText(getActivity(), "Password cannot be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!strPassword.equals(sharedPreferences.getString("password", ""))) {
                    Toast.makeText(getActivity(), "Invalid password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                startActivity(new Intent(getActivity(), EditActivity.class));
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.setText("");
            }
        });

        return rootView;
    }
}