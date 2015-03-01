package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.vj.makosocial.R;

import java.util.ArrayList;

import dbObjects.MakoEvent;

public class ListViewMakoAdapter extends BaseAdapter {
    ArrayList<MakoEvent> makoEvents = new ArrayList<MakoEvent>();
    Context context;
    LayoutInflater inflater;

    public ListViewMakoAdapter( Context context) {
        this.context = context;

        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return makoEvents.size();
    }

    @Override
    public Object getItem(int position) {
        return makoEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    //view holder for performance increase
    static class ViewHolder {
        public TextView eventName;
        public TextView eventDescr;
        public TextView likes;
        public TextView comments;
        public ImageView pic;
        public RatingBar ratingBar;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        View row = convertView;

        if (row == null){
            row = inflater.inflate(R.layout.list_item,parent,false);
            holder = new ViewHolder();

            //find elements
            holder.eventName = (TextView) row.findViewById(R.id.etEventName);
            holder.eventDescr = (TextView) row.findViewById(R.id.etEventDescription);
            holder.likes = (TextView) row.findViewById(R.id.tvLikes);
            holder.comments = (TextView) row.findViewById(R.id.tvComments);
            holder.pic = (ImageView) row.findViewById(R.id.ivMainPic);
            holder.ratingBar = (RatingBar) row.findViewById(R.id.rbRating);
            row.setTag(holder);
        }else {
            holder = (ViewHolder) row.getTag();
        }

        //assign values
        holder.eventName.setText(makoEvents.get(position).getName());
        holder.eventDescr.setText(makoEvents.get(position).getDescription());
        holder.likes.setText(makoEvents.get(position).getLikes()+"");
        //holder.comments.setText(makoEvents.get(position).getComments());
        holder.pic.setImageBitmap(makoEvents.get(position).getPicture());
        holder.ratingBar.setRating(makoEvents.get(position).getRating());


        return row;
    }

    public void updateEntries(ArrayList<MakoEvent> entries){
        makoEvents = entries;
        notifyDataSetChanged();
    }
}
