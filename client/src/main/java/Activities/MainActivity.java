package Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.familymapclient.R;

import Fragments.LoginFragment;
import Fragments.MapFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener {

    private FragmentManager fragMag = getSupportFragmentManager();

    // AppCompat onCreate override
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragment = fragMag.findFragmentById(R.id.fragment_container);

        if ((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey("Re-sync"))){
            Fragment mapFragment = new MapFragment();
            FragmentTransaction fragmentTransaction = fragMag.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, mapFragment).commit();
        }
        else if (fragment == null) {
            fragment = new LoginFragment();
            ((LoginFragment) fragment).setLoginListener(this);
            fragMag.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    // On Login success
    @Override
    public void loginComplete() {
        Fragment mapFragment = new MapFragment();
        FragmentTransaction fragmentTransaction = fragMag.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mapFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
