package xii.protonox.hu.audioplayer.data;

import android.net.Uri;

/**
 * Created by poler on 2015. 10. 19..
 */
public class Song {

    private String mName, mAlbum_name, mFullPath, mDuration;
    private Uri mUri;
    private int mId;



    public Song(){ }
    public Song(String name , int id ,  String album_name , String full_path , String duration , Uri songuri ){
        this.mName = name;
        this.mId = id;
        this.mAlbum_name = album_name;
        this.mFullPath = full_path;
        this.mDuration = duration;
        this.mUri = songuri;
    }


    public String getSongName() {
        return mName;
    }

    public void setSongName(String mSongName) {
        this.mName = mSongName;
    }



    public String getSongFullPath() {
        return mFullPath;
    }

    public void setSongFullPath(String mSongFullPath) {
        this.mFullPath = mSongFullPath;
    }



    public String getSongAlbumName() {
        return mAlbum_name;
    }

    public void setSongAlbumName(String mSongAlbumName) {
        this.mAlbum_name = mSongAlbumName;
    }



    public String getSongDuration() {
        return mDuration;
    }

    public void setSongDuration(String mSongDuration) {
        this.mDuration = mSongDuration;
    }



    public int getSongId() {
        return mId;
    }

    public void setSongId(int mSongId) {
        this.mId = mSongId;
    }



    public void setSongUri(Uri uri){ this.mUri = uri; }

    public Uri getSongUri(){
        return this.mUri;
    }

}
