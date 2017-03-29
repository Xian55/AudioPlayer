package xii.protonox.hu.audioplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import xii.protonox.hu.audioplayer.data.Song;
import xii.protonox.hu.audioplayer.R;

/**
 * Created by poler on 2015. 10. 20..
 * This adapter helps to display the list of the songs
 */
public class SongListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Song> songList;

    public SongListAdapter(Context context, ArrayList<Song> list) {
        mContext = context;
        this.songList = list;
    }


    @Override
    public int getCount() {
        return songList.size();
    }


    @Override
    public Song getItem(int position) {
        return songList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            //Layout inflate for list item
            convertView = LayoutInflater.from(mContext).inflate(R.layout.song_list_item_no_album, null);
        }

        //ImageView mImgSong = (ImageView) convertView.findViewById(R.id.img_listitem_file);
        TextView mtxtSongName = (TextView) convertView.findViewById(R.id.txt_listitem_filename);
        TextView mTxtSongAlbumName = (TextView) convertView.findViewById(R.id.txt_listitem_albumname);
        TextView mTxtSongDuration = (TextView) convertView.findViewById(R.id.txt_listitem_duration);


        //mImgSong.setImageResource(R.drawable.no_clipart);
        mtxtSongName.setText(songList.get(position).getSongName());
        mTxtSongAlbumName.setText(songList.get(position).getSongAlbumName());
        mTxtSongDuration.setText(songList.get(position).getSongDuration());

        return convertView;
    }



    public void setSongsList(ArrayList<Song> list) {
        songList = list;
        this.notifyDataSetChanged();
    }


}
