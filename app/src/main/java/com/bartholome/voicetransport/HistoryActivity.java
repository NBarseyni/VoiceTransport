package com.bartholome.voicetransport;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import org.w3c.dom.Comment;

import java.util.List;

public class HistoryActivity extends ListActivity {

    private SearchDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dataSource = new SearchDataSource(this);
        dataSource.open();

        List<Search> values = dataSource.getAllSearchs();

        ArrayAdapter<Search> adapter = new ArrayAdapter<Search>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    @Override
    protected void onResume() {
        dataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }
}
