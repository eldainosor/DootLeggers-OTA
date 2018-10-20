package com.rage.dootleggersota.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.rage.dootleggersota.R;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class UpdateFragment extends Fragment {

    private TextView updateName, updateDate, updateCodenmae;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_update, container, false);
        defineObjects(layout);
        setValues();
        return  layout;
    }

    private void defineObjects (View layout) {
        updateName = layout.findViewById(R.id.textViewUpdateName);
        updateDate = layout.findViewById(R.id.textViewUpdateDate);
        updateCodenmae = layout.findViewById(R.id.textViewUpdateCodename);
    }

    private void setValues () {
        if (getArguments() != null) {
            ArrayList<String> data = getArguments().getStringArrayList("data");
            String versionnumber = "Bootleggers v" + data.get(0).substring(data.get(0).indexOf('=')+1);
            String versionDate = convertToString(data.get(1).substring(data.get(1).lastIndexOf("-")+1));
            String codeName = data.get(2).substring(data.get(2).lastIndexOf('=')+1);
            updateName.setText(versionnumber);
            updateDate.setText(versionDate);
            updateCodenmae.setText(codeName);
        }
    }

    private String convertToString (String code) {
        String year = code.substring(0, 4);
        int monthCode = Integer.parseInt(code.substring(4, 6));
        String day = code.substring(6);
        String month = "";
        switch (monthCode) {
            case 1 : month = "January";
                     break;
            case 2 : month = "February";
                     break;
            case 3 : month = "March";
                     break;
            case 4 : month = "April";
                     break;
            case 5 : month = "May";
                     break;
            case 6 : month = "June";
                     break;
            case 7 : month = "July";
                     break;
            case 8 : month = "August";
                     break;
            case 9 : month = "September";
                     break;
            case 10 : month = "October";
                     break;
            case 11 : month = "November";
                     break;
            case 12 : month = "December";
                     break;
        }
        return month + " " + day + ", " + year;
    }
}
