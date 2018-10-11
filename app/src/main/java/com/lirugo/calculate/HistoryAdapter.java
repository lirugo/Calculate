package com.lirugo.calculate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;

public class HistoryAdapter extends ArrayAdapter<History> {

    private LayoutInflater inflater;
    private int layout;
    private List<History> histories;

    public HistoryAdapter(@NonNull Context context, int resource, List<History> histories) {
        super(context, resource, histories);
        this.histories = histories;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(this.layout, parent, false);

        EditText input = (EditText) view.findViewById(R.id.input);
        TextView output = (TextView) view.findViewById(R.id.output);

        History history = histories.get(position);

        input.setText(history.getInput());
        output.setText(history.getOutput());

        return view;
    }
}
