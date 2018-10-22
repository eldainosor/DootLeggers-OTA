package com.rage.dootleggersota.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rage.dootleggersota.R;

public class BaseFragment extends Fragment {

    private TextView middle, bottom;
    private CardView card;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_main, container, false);
        defineItems(layout);
        setData();
        return  layout;
    }

    private void defineItems (View layout) {
        middle = layout.findViewById(R.id.textViewUpdateStatusMain);
        bottom = layout.findViewById(R.id.textViewCardBottom);
        card = layout.findViewById(R.id.cardViewBottom);
    }

    private void setData () {
        if (getArguments() != null) {
            String mid = getArguments().getString("middle");
            String bot = getArguments().getString("bottom");
            boolean showCard = getArguments().getBoolean("showCard");
            middle.setText(mid);
            if (showCard)
                bottom.setText(bot);
            else
                card.setVisibility(View.GONE);
        }
    }

}
