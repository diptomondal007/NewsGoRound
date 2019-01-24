package com.example.dipto.newsgoround;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.example.dipto.newsgoround.FragmentsClasses.LocalNewsTab;
import com.example.dipto.newsgoround.FragmentsClasses.ScienceNewsTab;
import com.example.dipto.newsgoround.FragmentsClasses.SportsNewsTab;
import com.example.dipto.newsgoround.FragmentsClasses.TechNewsTab;

import com.example.dipto.newsgoround.FragmentsClasses.TopHeadLinesNewsTab;
import com.example.dipto.newsgoround.FragmentsClasses.ViewPageAdapterTabView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;

    private DrawerLayout drawerLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingSearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tabLayoutId);
        viewPager = findViewById(R.id.viewPagerId);
        ViewPageAdapterTabView adapter = new ViewPageAdapterTabView(getSupportFragmentManager());
        adapter.AddFragment(new TopHeadLinesNewsTab(),  "Top Headlines");
        adapter.AddFragment(new LocalNewsTab(),  "Local");
        adapter.AddFragment(new ScienceNewsTab(),  "Science");
        adapter.AddFragment(new TechNewsTab() , "Tech");
        adapter.AddFragment(new SportsNewsTab() , "Sports");



        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        mSearchView = findViewById(R.id.floating_search);


        //navigation_drawer
        drawerLayout = findViewById(R.id.drawerLayout);
        mSearchView.attachNavigationDrawerToMenuButton(drawerLayout);

        //navigation_drawer

        mAuth = FirebaseAuth.getInstance();



        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);





    }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            openActivityLogin();
        }
    }

    //____________if not logged in__________

    public void openActivityLogin(){
        Intent intent = new Intent(MainActivity.this,SignInActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
        finish();

    }


    //_________Navigation Drawer Click Listener________


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.nav_logOut:

                FirebaseAuth.getInstance().signOut();
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                startActivity(new Intent(this , SignInActivity.class));

                break;

            case R.id.nav_home:

        }



        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }





}
