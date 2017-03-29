package xii.protonox.hu.audioplayer.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import xii.protonox.hu.audioplayer.service.MusicService;
import xii.protonox.hu.audioplayer.data.Song;

import xii.protonox.hu.audioplayer.R;
import xii.protonox.hu.audioplayer.gateway.SongGateway;

/**
 * Created by poler on 2015. 10. 22.
 */
public class SongDetailFragment extends android.support.v4.app.Fragment
{
    private static final String ARG_INDEX = "ARG_INDEX";


    private static View parent;

    private Song song;

    private int mSongIndex;



    public static SongDetailFragment newInstance(int index) {
        SongDetailFragment fragment = new SongDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    public SongDetailFragment() {
        // Required empty public constructor
    }




    @Override
    public void onResume() {
        super.onResume();

        if(MusicService.instance != null) {
            if(MusicService.instance.getSONG_POS() != mSongIndex)
            {
                update(MusicService.instance.getSONG_POS());
            }
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_song_detail, container, false);

        parent = view;

        int index = 0;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_INDEX);
        }

        update(index);

        //Setup buttons
        view.findViewById(R.id.button_prev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButtonPrevious();
            }
        });

        view.findViewById(R.id.button_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButtonPause();
            }
        });

        view.findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButtonNext();
            }
        });

        return view;
    }




    public void onClickButtonPrevious() {
        //Toast.makeText(getActivity(), "Prev", Toast.LENGTH_SHORT).show();
        if(MusicService.instance != null) {
            MusicService.instance.previousSong();
            update(MusicService.instance.getSONG_POS());
        }
    }

    public void onClickButtonPause() {
        //Toast.makeText(getActivity(), "Pause", Toast.LENGTH_SHORT).show();
        if(MusicService.instance != null) {
            MusicService.instance.playPauseSong();

        }
    }

    public void onClickButtonNext() {
        //Toast.makeText(getActivity(), "Next", Toast.LENGTH_SHORT).show();
        if(MusicService.instance != null) {
            MusicService.instance.nextSong();
            update(MusicService.instance.getSONG_POS());

        }
    }




    //Called every time if  the currently playing song has changed
    private void update(int index) {
        TextView title = (TextView)parent.findViewById(R.id.title);
        TextView folder = (TextView)parent.findViewById(R.id.folder);
        SeekBar seekbar = (SeekBar)parent.findViewById(R.id.seekBar);

        //Get the Clicked item Data
        song = SongGateway.getSongs().get(index);

        mSongIndex = index;

        saveCurrentSongName(song.getSongName());

        //Reset the progress bar
        seekbar.setProgress(0);

        title.setText(song.getSongName());
        folder.setText(song.getSongAlbumName());
    }


    //Save the currently playing songName
    //Also increment the total played songs Count
    //to SharedPreferences
    private void saveCurrentSongName(String songName) {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_last_played_name), songName);
        editor.apply();



        int songsPlayedCount = sharedPref.getInt(getString(R.string.saved_played_music_count), 0);
        editor = sharedPref.edit();
        editor.putInt(getString(R.string.saved_played_music_count), (songsPlayedCount+1));
        editor.apply();

        //Toast.makeText(getActivity(), Integer.toString(songsPlayedCount), Toast.LENGTH_SHORT).show();
    }



}
