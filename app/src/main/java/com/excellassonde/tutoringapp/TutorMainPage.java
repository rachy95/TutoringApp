package com.excellassonde.tutoringapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import android.app.Fragment;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class TutorMainPage extends AppCompatActivity {
    public static int numberOfSessions;
    private DrawerLayout drawerLayout;
    private ListView mDrawerList;

    TextView displayMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_main_page);
        displayMessage = (TextView) findViewById(R.id.display_text);
        addListenerOnMenuButton();

        String[] menuOptions = getResources().getStringArray(R.array.menu_options);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.drawer_list_item, menuOptions);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        TextView welcomeTextView = (TextView) toolbar.findViewById(R.id.welcome_message);
        String welcomeMessage = String.format(getString(R.string.toolbar_welcome_message), Utils.getTutorName());
        welcomeTextView.setText(welcomeMessage);

        Fragment fragment = new TutorMainPageFragment();
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();


    }

    public void addListenerOnMenuButton() {
        ImageButton menu_opener = (ImageButton) findViewById(R.id.menu_opener);

        menu_opener.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                drawerLayout.openDrawer(Gravity.START);
            }

        });
    }


    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        Intent intent = null;
        if(position == 1){
            intent = new Intent(this, AdjustAvailability.class);
        }
        if(position == 2){
            intent = new Intent(this, ConfirmSession.class);
        }
        if(position == 3){
            intent = new Intent(this, FirstActivity.class);
        }
        if(position == 4){
            startActivity(Utils.sendEmail("","",""));
        }
        if(position == 5){
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signOut();
            Toast.makeText(this, "Sign out successful", Toast.LENGTH_LONG).show();
            intent = new Intent(this, FirstActivity.class);
        }

        if(intent != null)
            startActivity(intent);

        drawerLayout.closeDrawer(mDrawerList);
    }

    public static int getNumberOfSessions() {
        return numberOfSessions;
    }





}
