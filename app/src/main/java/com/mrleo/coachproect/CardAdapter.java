package com.mrleo.coachproect;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CardAdapter extends ArrayAdapter<Card>{
    public CardAdapter(@NonNull Context context, ArrayList<Card> cards) {
        super(context, R.layout.card_row, cards);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View cardView = inflater.inflate(R.layout.card_row, parent, false);
        String title = getItem(position).getTitle();
        String time = getItem(position).getHours() + "H : " + getItem(position).getMinutes() + "M";
        TextView titleView = cardView.findViewById(R.id.textViewTitle);
        TextView timeView = cardView.findViewById(R.id.textViewTime);
        titleView.setText(title);
        timeView.setText(time);
        return cardView;
    }
}
