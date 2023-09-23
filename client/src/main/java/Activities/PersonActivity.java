package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.familymapclient.R;

import java.util.ArrayList;
import java.util.List;

import Model.Person;
import Model.Event;
import Utilities.DataModel;
import Adapters.PersonAdapter;

public class PersonActivity extends AppCompatActivity {

    private Person selected;
    private TextView mFirstName;
    private TextView mLastName;
    private TextView mGender;
    private ExpandableListView mListView;
    private ExpandableListAdapter mListAdapter;
    private DataModel dataModel = DataModel.initialize();

    // AppCompat onCreate override
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("FamilyMap: Person Details");
        selected = dataModel.getSelectedPerson();

        mFirstName = findViewById(R.id.person_first_name);
        mLastName = findViewById(R.id.person_last_name);
        mGender = findViewById(R.id.person_gender);

        mFirstName.setText(selected.getFirstName());
        mLastName.setText(selected.getLastName());
        mGender.setText(selected.getGender().toUpperCase());

        mListView = findViewById(R.id.expandable_list_person_activity);
        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (groupPosition == 0){
                    Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                    intent.putExtra("Event", "Event");
                    dataModel.setSelectedEvent((Event) mListAdapter.getChild(groupPosition, childPosition));
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                    dataModel.setSelectedPerson((Person) mListAdapter.getChild(groupPosition, childPosition));
                    startActivity(intent);
                }
                return false;
            }
        });
        listUpdate();
    }

    // update list of people
    private void listUpdate() {
        List<Person> relatives = new ArrayList<>(dataModel.returnRelatives(selected.getPersonID()));

        List<Event> eventsList = new ArrayList<>(dataModel.getEventsOfPerson().get(selected.getPersonID()));
        eventsList = dataModel.timeFilter(eventsList);

        List<String> headers = new ArrayList<>();
        headers.add("Events");
        headers.add("Relatives");

        eventsList = filterEvents(eventsList);
        relatives = filterPersons(relatives);

        mListAdapter = new PersonAdapter(this, headers, eventsList, relatives, selected);
        mListView.setAdapter(mListAdapter);
    }

    // AppCompat button override
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

    // edit list of events based on selected filters
    private List<Event> filterEvents(List<Event> eventListInput) {
        List<Event> eventList = new ArrayList<>();
        for (Event current: eventListInput) {
            if (dataModel.displayableEvents().containsValue(current)){
                eventList.add(current);
            }
        }
        return eventList;
    }

    // edit list of people based on selected filters
    private List<Person> filterPersons(List<Person> personListInput) {
        List<Person> personList = new ArrayList<>();
        for (Person current: personListInput) {
            if (dataModel.isPersonVisible(current)){
                personList.add(current);
            }
        }
        return personList;
    }
}
