package modernexpensetrackerbyamitmaity.example.modernexpensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;


    Toolbar myToolbar;
    ViewPager viewPager;
    MyTripAdapter adapter;
    private ExtendedFloatingActionButton crreat_trip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tabLayout = findViewById(R.id.tabLayout);
        viewPager=(ViewPager)findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Current"));
        tabLayout.addTab(tabLayout.newTab().setText("Next"));
        tabLayout.addTab(tabLayout.newTab().setText("History"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("Expense Tracker");



        crreat_trip = findViewById(R.id.create_button);

        crreat_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreteATripFunction();
            }
        });

        adapter = new MyTripAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_new_content_facebook){
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.menu_new_content_twitter){
            Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {


        MaterialDialog mDialog = new MaterialDialog.Builder ( this )
                .setTitle ( "Are you sure you want to exit ?" )
                .setCancelable ( false )
                .setPositiveButton ( "Exit",R.drawable.ic_baseline_block_24, new MaterialDialog.OnClickListener () {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        MainActivity.super.onBackPressed ();
                    }


                } )
                .setNegativeButton ( "No", new MaterialDialog.OnClickListener () {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        dialogInterface.cancel ();
                    }

                } )
                .build ();

        // Show Dialog
        mDialog.show ();


    }

    private void CreteATripFunction() {
        CreateTripCustomDialogClass cdd=new CreateTripCustomDialogClass(MainActivity.this);
        cdd.show();

    }

}