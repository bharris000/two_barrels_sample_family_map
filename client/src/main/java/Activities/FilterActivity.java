package Activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familymapclient.R;

import java.util.ArrayList;
import java.util.List;

import Adapters.FilterAdapter;
import Utilities.DataModel;

public class FilterActivity extends AppCompatActivity {

    private RecyclerView mRecycler;
    private DataModel dataModel = DataModel.initialize();

    // AppCompat onCreate override
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecycler = findViewById(R.id.filter_recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        recyclerUpdate();
    }

    // update list of filters
    private void recyclerUpdate() {
        List<String> filterList = new ArrayList<>();
        filterList.add("Father's Side");
        filterList.add("Mother's Side");
        filterList.add("Male Events");
        filterList.add("Female Events");

        List<String> eventTypes = dataModel.getEventTypes();
        filterList.addAll(eventTypes);
        FilterAdapter mFilterAdapter = new FilterAdapter(filterList, this);
        mRecycler.setAdapter(mFilterAdapter);
    }

    // AppCompat button override
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
