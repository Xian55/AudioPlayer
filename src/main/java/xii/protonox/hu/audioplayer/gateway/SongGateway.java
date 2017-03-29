package xii.protonox.hu.audioplayer.gateway;

import java.util.ArrayList;

import xii.protonox.hu.audioplayer.data.Song;

/**
 * Created by poler on 2015. 10. 22..
 */
public class SongGateway
{
    private static ArrayList<Song> mSongs;



    public static ArrayList<Song> getSongs()
    {
        return mSongs;
    }

    public static void setSongs(ArrayList<Song> songs)
    {
        mSongs = songs;
    }

}
