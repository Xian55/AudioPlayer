package xii.protonox.hu.audioplayer.fragment;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import xii.protonox.hu.audioplayer.MainActivity;
import xii.protonox.hu.audioplayer.service.MusicService;
import xii.protonox.hu.audioplayer.R;
import xii.protonox.hu.audioplayer.adapter.SongListAdapter;
import xii.protonox.hu.audioplayer.data.Song;
import xii.protonox.hu.audioplayer.gateway.SongGateway;


/**
 * Created by poler on 2015. 10. 19..
 */
public class SongListFragment extends Fragment {

    private static final String ARG_INDEX = "ARG_INDEX";

    //private CoordinatorLayout coordinatorLayout;



    private String[] STAR = {"*"};

    private ListView mListSongs;


    private MusicService serviceMusic;
    private Intent playIntent;

    //private ArrayList<Song> mSongList;
    private SongListAdapter mAdapterListFile;


    public static SongListFragment newInstance() {
        return new SongListFragment();
    }

    public SongListFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<Song> songList = new ArrayList<Song>();

        songList = listAllSongs();

        SongGateway.setSongs(songList);

        int songListCount = 0;

        //Save List Count
        if (songList != null) {
            songListCount = songList.size();

            //Toast.makeText(getActivity(), Integer.toString(songListCount), Toast.LENGTH_SHORT).show();

        }

        //Save the count of the list
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.saved_list_count), songListCount);
        editor.apply();



        mAdapterListFile = new SongListAdapter(getActivity().getApplicationContext(), SongGateway.getSongs());
        mAdapterListFile.setSongsList(SongGateway.getSongs());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.act_list_songs, container, false);

        mListSongs = (ListView) view.findViewById(R.id.list_songs_actimport);

        mListSongs.setAdapter(mAdapterListFile);


        //Handle on item click
        mListSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).moveToDetailFragment(position);
                    serviceMusic.setSelectedSong(position, MusicService.NOTIFICATION_ID);
                }
            }
        });


        return view;
    }




    //Fetch path to all the files from internal storage
    private ArrayList<Song> listAllSongs() {
        Cursor cursor;
        ArrayList<Song> songList = new ArrayList<Song>();
        Uri allSongsUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        //if (isSdPresent())
        //{
            cursor = getActivity().getApplicationContext().getContentResolver().query(allSongsUri, STAR, selection, null, null);
            if (cursor != null)
            {
                if (cursor.moveToFirst())
                {
                    do
                    {
                        //Find out the duration
                        //Ignore the < 0:01 duration songs


                        String duration = getDuration(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))));

                        Song song = new Song();

                        String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                        String[] res = data.split("\\.");

                        song.setSongName(res[0]);

                        //Log.d("test",res[0] );
                        song.setSongFullPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                        song.setSongId(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                        song.setSongFullPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                        song.setSongAlbumName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));

                        song.setSongUri(ContentUris.withAppendedId(
                                allSongsUri,
                                cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID))));


                        song.setSongDuration(duration);

                        songList.add(song);

                    } while (cursor.moveToNext());

                    cursor.close();
                    return songList;
                }
                cursor.close();
            }
        //}

        return null;
    }




    //Check whether sdcard is present or not
    //Only Test purpose
    private static boolean isSdPresent() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }





    //Convert the millisecs to min & sec
    private static String getDuration(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(6);
        sb.append(minutes < 10 ? "0" + minutes : minutes);
        sb.append(":");
        sb.append(seconds < 10 ? "0" + seconds : seconds);
        //sb.append(" Secs");
        return sb.toString();
    }




    //Create a connection between the Application and the Service
    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.PlayerBinder binder = (MusicService.PlayerBinder) service;

            //get service
            serviceMusic = binder.getService();
            serviceMusic.setSongList(SongGateway.getSongs());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };



    //public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    //    serviceMusic.setSelectedSong(position, MusicService.NOTIFICATION_ID);
    //}



    @Override
    public void onStart() {
        super.onStart();
        //Start service
        if (playIntent == null)
        {
            playIntent = new Intent(getActivity(), MusicService.class);
            getActivity().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            getActivity().startService(playIntent);
        }
    }

    @Override
    public void onDestroy() {
        //Stop service
        if (playIntent != null) {
            getActivity().stopService(playIntent);
        }
        serviceMusic = null;
        super.onDestroy();
    }



}
