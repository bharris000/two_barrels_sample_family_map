package Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.familymapclient.R;
import com.google.android.gms.maps.GoogleMap;

import LoginTasks.DataTask;
import Utilities.DataModel;
import Utilities.Settings;

public class SettingActivity extends AppCompatActivity implements DataTask.DataContext {

    private Switch mLifeStory;
    private Switch mFamilyTree;
    private Switch mSpouseLines;

    private Spinner mLifeSpinner;
    private Spinner mFamilySpinner;
    private Spinner mSpouseSpinner;
    private Spinner mMapSpinner;

    private TextView mResync;
    private TextView mLogout;

    private Settings currSettings;
    private DataModel dataModel = DataModel.initialize();

    // AppCompat onCreate override
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLifeStory = findViewById(R.id.life_switch);
        mLifeStory.setChecked(dataModel.getSettings().isShowingStoryLines());
        mFamilyTree = findViewById(R.id.tree_switch);
        mFamilyTree.setChecked(dataModel.getSettings().isShowingFamilyLines());
        mSpouseLines = findViewById(R.id.spouse_switch);
        mSpouseLines.setChecked(dataModel.getSettings().isShowingSpouseLines());
        mLifeSpinner = findViewById(R.id.life_spinner);
        mFamilySpinner = findViewById(R.id.tree_spinner);
        mSpouseSpinner = findViewById(R.id.spouse_spinner);
        mMapSpinner = findViewById(R.id.map_spinner);
        mResync = findViewById(R.id.resync_text);
        mLogout = findViewById(R.id.logout_text);
        mResync.setLinksClickable(true);
        mLogout.setLinksClickable(true);

        ArrayAdapter<CharSequence> storyColors = ArrayAdapter.createFromResource(this,
                R.array.life_story_colors, R.layout.support_simple_spinner_dropdown_item);
        mLifeSpinner.setAdapter(storyColors);
        ArrayAdapter<CharSequence> spouseColors = ArrayAdapter.createFromResource(this,
                R.array.spouse_line_color, R.layout.support_simple_spinner_dropdown_item);
        mSpouseSpinner.setAdapter(spouseColors);
        ArrayAdapter<CharSequence> familyTreeColors = ArrayAdapter.createFromResource(this,
                R.array.family_tree_colors, R.layout.support_simple_spinner_dropdown_item);
        mFamilySpinner.setAdapter(familyTreeColors);
        ArrayAdapter<CharSequence> mapTypes = ArrayAdapter.createFromResource(this,
                R.array.map_types, R.layout.support_simple_spinner_dropdown_item);
        mMapSpinner.setAdapter(mapTypes);

        mLifeSpinner.setSelection(dataModel.getSettings().getSettingsSpinnerSelections(0));
        mFamilySpinner.setSelection(dataModel.getSettings().getSettingsSpinnerSelections(1));
        mSpouseSpinner.setSelection(dataModel.getSettings().getSettingsSpinnerSelections(2));
        mMapSpinner.setSelection(dataModel.getSettings().getSettingsSpinnerSelections(3));

        // listeners
        mLifeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position) {
                    case 0:
                        dataModel.getSettings().setStoryLineColor(Color.BLUE);
                        dataModel.getSettings().setSettingsSpinnerSelections(0, 0);
                        break;
                    case 1:
                        dataModel.getSettings().setStoryLineColor(Color.CYAN);
                        dataModel.getSettings().setSettingsSpinnerSelections(1, 0);
                        break;
                    case 2:
                        dataModel.getSettings().setStoryLineColor(Color.BLACK);
                        dataModel.getSettings().setSettingsSpinnerSelections(2, 0);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mFamilySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        dataModel.getSettings().setFamilyLineColor(Color.GREEN);
                        dataModel.getSettings().setSettingsSpinnerSelections(0, 1);
                        break;
                    case 1:
                        dataModel.getSettings().setFamilyLineColor(Color.YELLOW);
                        dataModel.getSettings().setSettingsSpinnerSelections(1, 1);
                        break;
                    case 2:
                        dataModel.getSettings().setFamilyLineColor(Color.WHITE);
                        dataModel.getSettings().setSettingsSpinnerSelections(2, 1);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mSpouseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        dataModel.getSettings().setSpouseLineColor(Color.MAGENTA);
                        dataModel.getSettings().setSettingsSpinnerSelections(0, 2);
                        break;
                    case 1:
                        dataModel.getSettings().setSpouseLineColor(Color.RED);
                        dataModel.getSettings().setSettingsSpinnerSelections(1, 2);
                        break;
                    case 2:
                        dataModel.getSettings().setSpouseLineColor(Color.GRAY);
                        dataModel.getSettings().setSettingsSpinnerSelections(2, 2);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mMapSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        dataModel.getSettings().setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        dataModel.getSettings().setSettingsSpinnerSelections(0, 3);
                        break;
                    case 1:
                        dataModel.getSettings().setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        dataModel.getSettings().setSettingsSpinnerSelections(1, 3);
                        break;
                    case 2:
                        dataModel.getSettings().setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        dataModel.getSettings().setSettingsSpinnerSelections(2, 3);
                        break;
                    case 3:
                        dataModel.getSettings().setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        dataModel.getSettings().setSettingsSpinnerSelections(2, 3);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mLifeStory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataModel.getSettings().setShowingStoryLines(isChecked);
            }
        });

        mFamilyTree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataModel.getSettings().setShowingFamilyLines(isChecked);
            }
        });

        mSpouseLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataModel.getSettings().setShowingSpouseLines(isChecked);
            }
        });

        mResync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currSettings = dataModel.getSettings();
                resyncApp();
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 0);
            }
        });

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

    //--****************-- Re-Sync Function --***************-- fixme
    private void resyncApp() {
        DataTask dataTask = new DataTask(dataModel.getServerHost(), dataModel.getIpAddress(), this);
        dataTask.execute(dataModel.getAuthToken());
    }

    //--****************-- Re-sync Communication --***************--
    @Override
    public void onExecuteCompleteData(String message) {
        if (message.equals("Welcome, " + dataModel.getUser().getFirstName() + " " + dataModel.getUser().getLastName())){
            Toast.makeText(this,"Success in Re-sync", Toast.LENGTH_SHORT).show();
            dataModel.setSettings(currSettings);

            Intent intent = new Intent(this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("Re-sync", 1);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(intent, 0);
        }
        else {
            Toast.makeText(this, "Re-sync Failed",Toast.LENGTH_SHORT).show();
        }
    }
}