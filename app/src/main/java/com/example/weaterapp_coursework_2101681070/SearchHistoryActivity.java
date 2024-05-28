package com.example.weaterapp_coursework_2101681070;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weaterapp_coursework_2101681070.R;
import com.example.weaterapp_coursework_2101681070.SearchHistoryAdapter;
import com.example.weaterapp_coursework_2101681070.WeatherDataSource;

public class SearchHistoryActivity extends AppCompatActivity {

    private WeatherDataSource dataSource;
    private RecyclerView recyclerView;
    private SearchHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);

        recyclerView = findViewById(R.id.recyclerViewSearchHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dataSource = new WeatherDataSource(this);
        dataSource.open();

        // Retrieve data from the database
        Cursor cursor = dataSource.getAllSearchHistory();

        adapter = new SearchHistoryAdapter(cursor);
        recyclerView.setAdapter(adapter);

        // Handle delete button click in adapter
        adapter.setOnItemClickListener(new SearchHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(long id) {
                boolean isDeleted = dataSource.deleteSearchHistory(id);
                if (isDeleted) {
                    // Refresh RecyclerView after deletion
                    Cursor updatedCursor = dataSource.getAllSearchHistory();
                    adapter.swapCursor(updatedCursor);
                    Toast.makeText(SearchHistoryActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SearchHistoryActivity.this, "Failed to delete item", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }
}
