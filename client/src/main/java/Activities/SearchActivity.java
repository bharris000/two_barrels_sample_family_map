package Activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familymapclient.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Model.*;
import Utilities.DataModel;
import Adapters.SearchAdapter;

public class SearchActivity extends AppCompatActivity {

    private EditText mSearchBar;
    private Button mSearchButton;
    private String searchRequest;
    private RecyclerView mRecycler;
    private RecyclerView.Adapter mAdapter;

    private DataModel dataModel = DataModel.initialize();

    // AppCompat onCreate override
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSearchBar = findViewById(R.id.search_text);
        mSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchRequest = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mSearchButton = findViewById(R.id.search_button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchRequest != null){
                    listUpdate();
                }
            }
        });

        mRecycler = findViewById(R.id.list_search_recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
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

    // update list based on search request
    private void listUpdate() {
        List<Object> resultList = new ArrayList<>();

        Map<String, Person> personList = dataModel.getPeople();
        returnPeople(personList, resultList);

        Map<String, Event> eventList = dataModel.displayableEvents();
        returnEvents(eventList, resultList);

        if (resultList.size() != 0) {
            mAdapter = new SearchAdapter(resultList, this);
            mRecycler.setAdapter(mAdapter);
        }
    }

    // get list of people
    private void returnPeople(Map<String, Person> people, List<Object> resultList) {
        for (Person current: people.values()) {
            if (current.getFirstName().toLowerCase().contains(searchRequest.toLowerCase())){
                resultList.add(current);
            }
            else if (current.getLastName().toLowerCase().contains(searchRequest.toLowerCase())){
                resultList.add(current);
            }
        }
    }

    // get list of events
    private void returnEvents(Map<String, Event> events, List<Object> resultList) {
        for (Event current: events.values()) {
            if (current.getEventType().toLowerCase().contains(searchRequest.toLowerCase())){
                resultList.add(current);
            }
            else if (current.getCountry().toLowerCase().contains(searchRequest.toLowerCase())){
                resultList.add(current);
            }
            else if (current.getCity().toLowerCase().contains(searchRequest.toLowerCase())){
                resultList.add(current);
            }
        }
    }
}