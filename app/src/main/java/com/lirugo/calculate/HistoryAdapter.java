package com.lirugo.calculate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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
        ViewHolder viewHolder;

        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        History history = histories.get(position);
        viewHolder.inputView.setText(history.getInput());
        viewHolder.outputView.setText(history.getOutput());

        return convertView;
    }

    private class ViewHolder {
        final EditText inputView;
        final TextView outputView;

        ViewHolder(View view){
            inputView = (EditText) view.findViewById(R.id.input);
            outputView = (TextView) view.findViewById(R.id.output);
        }

    }
}
