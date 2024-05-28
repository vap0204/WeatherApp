package com.example.weaterapp_coursework_2101681070;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weaterapp_coursework_2101681070.R;
import com.example.weaterapp_coursework_2101681070.WeatherDbHelper;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {

    private Cursor cursor;
    private OnItemClickListener listener;

    public SearchHistoryAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    public interface OnItemClickListener {
        void onItemClick(long id);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cityTextView;
        public TextView temperatureTextView;
        public Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            cityTextView = itemView.findViewById(R.id.textViewCityHistory);
            temperatureTextView = itemView.findViewById(R.id.textViewTemperatureHistory);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            String city = cursor.getString(cursor.getColumnIndexOrThrow(WeatherDbHelper.COLUMN_CITY));
            double temperature = cursor.getDouble(cursor.getColumnIndexOrThrow(WeatherDbHelper.COLUMN_TEMPERATURE));

            holder.cityTextView.setText(city);
            holder.temperatureTextView.setText(String.valueOf(temperature));

            // Set click listener for the delete button
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(WeatherDbHelper.COLUMN_ID));
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(id); // Pass the item ID to the listener
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        notifyDataSetChanged();
    }
}
