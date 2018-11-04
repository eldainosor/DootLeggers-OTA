package com.rage.bootleggersota.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rage.bootleggersota.Adapter.ChangelogAdapter;
import com.rage.bootleggersota.Modal.BuildModal;
import com.rage.bootleggersota.R;

import java.util.ArrayList;

public class UpdateFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<BuildModal> list = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    private ArrayList<String> data;
    private BuildModal buildModal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_update, container, false);
        defineItems(layout);
        createModal();
        addUpdateToList();
        setupRecycler();
        return layout;
    }

    private void defineItems (View layout) {
        recyclerView = layout.findViewById(R.id.recyclerViewUpdates);
    }

    private void setupRecycler () {
        adapter = new ChangelogAdapter(list, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void createModal () {
        if (getArguments() != null) {
            data = getArguments().getStringArrayList("data");
            String ver = data.get(0).substring(data.get(0).indexOf('=')+1);
            String date = data.get(1).substring(data.get(1).lastIndexOf('-')+1);
            String codename = data.get(2).substring(data.get(2).indexOf('=')+1);
            date = convertToString(date);
            String downloadLink = data.get(4).substring(data.get(4).indexOf('=')+1);
            String totatlSize = data.get(5).substring(data.get(5).indexOf('=')+1);
            String filename = data.get(1).substring(data.get(1).indexOf('=')+1);
            buildModal = new BuildModal(false, ver + " (" + codename + ") - " + date, makeChangelog(),totatlSize, downloadLink, filename);
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

    private String makeChangelog () {
        StringBuilder changelog = new StringBuilder();
        int pos = 7; // changelog starts at 6th pos
        for (int i = pos; i < data.size(); i++) {
            changelog.append("- ").append(data.get(i)).append("\n");
        }
        return changelog.toString().trim();
    }

    private void refreshRecycler () {
        recyclerView.notify();
    }

    private void addUpdateToList () {
        list.add(new BuildModal(true, getContext().getResources().getString(R.string.card_update_text_stylised), ContextCompat.getColor(getContext(), R.color.colorAccent)));
        list.add(buildModal);
        list.add(new BuildModal(true, getContext().getResources().getString(R.string.old_builds_negative), ContextCompat.getColor(getContext(), R.color.colorTextDark)));
    }

}
