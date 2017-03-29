/**
 * Created by poler on 2015. 10. 19..
 */

package xii.protonox.hu.audioplayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import xii.protonox.hu.audioplayer.fragment.SongDetailFragment;
import xii.protonox.hu.audioplayer.fragment.SongListFragment;



public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Inflate layout
        setContentView(R.layout.activity_main);

        moveToListSong();


        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.main_activity_title, R.string.main_activity_title) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }




    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item))
        {
            setSongListCount();
            setLastPlayedSongName();
            setPlayedMusicCount();

            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
            if (getSupportActionBar() != null)
            {
                getSupportActionBar().setTitle(getString(R.string.app_name));
            }
        }
        super.onBackPressed();
    }





    //By Default Show the SongList Fragment
    public void moveToListSong() {
        SongListFragment fragment = SongListFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }




    //OnClick at one element of the SongList element will direct to SongDetailFragment
    public void moveToDetailFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.container, SongDetailFragment.newInstance(position));
        transaction.addToBackStack(null);
        transaction.commit();
    }





    //On the DrawerLayout, show the total number of songs
    //Loaded from SharedPreferences
    private void setSongListCount() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        int songsCount = sharedPref.getInt(getString(R.string.saved_list_count), 0);

        TextView textView = (TextView)findViewById(R.id.total_songs_text);

        String text = getResources().getString(R.string.ic_songs_count);

        text = text.concat(" " + Integer.toString(songsCount));

        textView.setText(text);
    }

    //On the DrawerLayout, show the the last played song name
    //Loaded from SharedPreferences
    private void setLastPlayedSongName() {

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String lastPlayedSong = sharedPref.getString(getString(R.string.saved_last_played_name), "NONE");

        TextView textView = (TextView)findViewById(R.id.last_played_song);

        String text = getResources().getString(R.string.ic_last_played_song);

        text = text.concat(" " + lastPlayedSong);

        textView.setText(text);
    }

    //On the DrawerLayout, show the total number of played music
    //Loaded from SharedPreferences
    private void setPlayedMusicCount() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        int songsPlayedCount = sharedPref.getInt(getString(R.string.saved_played_music_count), 0);

        TextView textView = (TextView)findViewById(R.id.ic_played_music_count);

        String text = getResources().getString(R.string.ic_played_music_count);

        text = text.concat(" " + Integer.toString(songsPlayedCount) + " times");

        textView.setText(text);
    }


}

