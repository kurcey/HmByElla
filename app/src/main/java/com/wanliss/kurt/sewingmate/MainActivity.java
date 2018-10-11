package com.wanliss.kurt.sewingmate;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wanliss.kurt.sewingmate.DTO.StoreDisplayDTO;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements PosterAdapter.ListItemClickListener {
    private Menu mSettingMenu;
    private Menu mDrawerMenu;

    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    ;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mStoreDisplayInfo = mDatabase.getReference("images/" + mUser.getUid());
    private List<StoreDisplayDTO> mAllStore;

    private RecyclerView mStoreRecyclerView;
    private PosterAdapter mStoreRecyclerViewAdapter;

    private TextView mErrorMessageDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavDrawer(this));
        this.mDrawerMenu = navigationView.getMenu();


        //GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, 1);
        mStoreRecyclerView = findViewById(R.id.rv_posters);
        mStoreRecyclerView.setLayoutManager(layoutManager);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if (mUser != null) {
            MenuItem signIn = mDrawerMenu.findItem(R.id.nav_sign_in);
            signIn.setTitle(" sign out " + mUser.getDisplayName());
        }
        mAllStore = new ArrayList<StoreDisplayDTO>();
        mStoreDisplayInfo.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                getAllClients(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                getAllClients(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                //  clientDeletion(dataSnapshot);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        this.mSettingMenu = menu;
/*
        if(mUser != null){
            MenuItem signIn = mSettingMenu.findItem(R.id.nav_sign_in);
            signIn.setTitle(" sign out "+ mUser.getDisplayName());
        }
*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onListItemClick(StoreDisplayDTO clickedMovie) {
        Context context = MainActivity.this;
      /*  Class destinationActivity = DetailsView.class;
        Intent startChildActivityIntent = new Intent(context, destinationActivity);
        startChildActivityIntent.putExtra("clickedMovie", clickedMovie);
        startActivity(startChildActivityIntent);
        */
    }

    private void showErrorMessage() {
        mStoreRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void setNewAdapter(List<StoreDisplayDTO> additionalMovieList) {
        mStoreRecyclerViewAdapter = new PosterAdapter(additionalMovieList, MainActivity.this);
        mStoreRecyclerView.setAdapter(mStoreRecyclerViewAdapter);
    }

    private void getAllClients(DataSnapshot dataSnapshot) {
        // for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
        //StoreDisplayDTO singleClient =  singleSnapshot.getValue(StoreDisplayDTO.class);
        StoreDisplayDTO singleClient = dataSnapshot.getValue(StoreDisplayDTO.class);
        mAllStore.add(singleClient);

        //if (mAllStore == null) {
        //mNumberListItems = movieSearchResults.size();
        setNewAdapter(mAllStore);
        //} else {
        //    showErrorMessage();
        //}
        //  }
    }
}
