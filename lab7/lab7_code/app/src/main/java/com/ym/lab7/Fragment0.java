package com.ym.lab7;

import android.content.Context;
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

public class Fragment0 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_0, container, false);

        Button ok = (Button)rootView.findViewById(R.id.ok);
        Button clear = (Button)rootView.findViewById(R.id.clear);
        final TextView newPassword = (TextView)rootView.findViewById(R.id.newPassword);
        final TextView confirmPassword = (TextView)rootView.findViewById(R.id.confirmPassword);
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("lab7pre", Context.MODE_PRIVATE);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strNewPassword = newPassword.getText().toString();
                String strConfirmPassword = confirmPassword.getText().toString();

                if (strNewPassword.equals("") || strConfirmPassword.equals("")) {
                    Toast.makeText(getActivity(), "Password cannot be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!strNewPassword.equals(strConfirmPassword)) {
                    Toast.makeText(getActivity(), "Password mismatch.", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("password", newPassword.getText().toString());
                editor.commit();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPassword.setText("");
                confirmPassword.setText("");
            }
        });

        return rootView;
    }
}