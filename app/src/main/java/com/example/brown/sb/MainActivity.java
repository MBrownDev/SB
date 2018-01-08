package com.example.brown.sb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle mDrawerToggle;

    String count;
    String nm,em;
    courseDatabase cdHelper;
    SQLiteDatabase db;
    Cursor cursor;
    View nView;
    TextView name, email;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        openData();


        nm = getSharedPreferences("logInfo",MODE_PRIVATE).getString("name","");
        em = getSharedPreferences("logInfo",MODE_PRIVATE).getString("username","");

        navigationView = (NavigationView)findViewById(R.id.navigation_view);
//        nView = navigationView.getHeaderView(R.layout.header);
//        name = (TextView)nView.findViewById(R.id.username);
//        email = (TextView)nView.findViewById(R.id.email);
//        name.setText(nm);
//        email.setText(em);

        setupNavDrawer();
        addNavDrawerItems();

        nView = navigationView.getHeaderView(0);

        name = (TextView)nView.findViewById(R.id.username);
        email = (TextView)nView.findViewById(R.id.email);
        name.setText(nm);
        email.setText(em);

        Intent intent = new Intent(MainActivity.this,ReceiverService.class);
        Intent noteIntent = new Intent(MainActivity.this,NotificationService.class);
        startService(intent);
        startService(noteIntent);

        MessageToast.message(this,"hat");

        count = "SELECT count(*) FROM " + cdHelper.helper.TABLE_NAME;
        cursor = db.rawQuery(count,null);

        final FrameLayout content = (FrameLayout)findViewById(R.id.content);

        home_screen cl = new home_screen();
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        tran.replace(R.id.content,cl);
        tran.addToBackStack(null);
        tran.commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    public void openData(){
        cdHelper = new courseDatabase(this);
        db = cdHelper.helper.getWritableDatabase();
        cdHelper.open();
    }

    public void addClass(View view){
        add_screen as = new add_screen();
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        tran.replace(R.id.content,as);
        tran.addToBackStack(null);
        tran.commit();
    }


    private void setupNavDrawer(){


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open, R.string.drawer_close){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }
    private void addNavDrawerItems(){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                if(item.isChecked())item.setChecked(false);
                else item.setChecked(true);

                drawerLayout.closeDrawers();

                switch (item.getItemId()){
                    case R.id.home_:
                        home_screen cl = new home_screen();
                        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
                        tran.replace(R.id.content,cl);
                        tran.addToBackStack(null);
                        tran.commit();
                        Toast.makeText(getApplicationContext(),"Inbox",Toast.LENGTH_LONG).show();
                        return true;
                    case R.id.add_class:
                        if(findViewById(R.id.content) != null) {
                            add_screen add = new add_screen();
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content, add);
                            ft.addToBackStack(null);
                            ft.commit();
                        }
                        Toast.makeText(getApplicationContext(),"Inbox",Toast.LENGTH_LONG).show();
                        return true;
                    case R.id.add_assignment:
                        if(findViewById(R.id.content) != null){
                            add_assignment aa = new add_assignment();
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content, aa);
                            ft.addToBackStack(null);
                            ft.commit();
                        }
                        return true;
                    case R.id.location:
                        Intent intent = new Intent(MainActivity.this,LocationService.class);
                        startService(intent);

                        MessageToast.message(MainActivity.this,"called");
                        return true;
                    case R.id.log_out:
                        SharedPreferences settings = getSharedPreferences("logInfo", MODE_PRIVATE);
                        settings.edit().clear().commit();
                        Intent i = new Intent(MainActivity.this,login_screen.class);
                        startActivity(i);
                        finish();
                        return true;
                    default:
                        return true;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.location_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.location:
                Intent i = new Intent(MainActivity.this,add_friend_screen.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_up_info,R.anim.no_change);
        }
        return super.onOptionsItemSelected(item);
    }


}
