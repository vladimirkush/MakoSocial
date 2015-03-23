package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.vj.makosocial.R;
import java.util.ArrayList;
import java.util.HashMap;

public class EventCommentsListAdapter extends BaseAdapter {

    private ArrayList<HashMap<String,String>> eventComments = new ArrayList<HashMap<String,String>>();
    private Context context;
    private LayoutInflater inflater;

    public EventCommentsListAdapter(ArrayList<HashMap<String,String>> list, Context context) {
        this.context = context;
        this.eventComments = list;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return eventComments.size();
    }

    @Override
    public Object getItem(int position) {
        return eventComments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        public TextView whoCommented;
        public TextView theComment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        View row = convertView;

        if (row == null){
            row = inflater.inflate(R.layout.comment_item,parent,false);
            holder = new ViewHolder();

            //find elements
            holder.whoCommented = (TextView) row.findViewById(R.id.who_commented);
            holder.theComment = (TextView) row.findViewById(R.id.the_comment);
            row.setTag(holder);
        }else {
            holder = (ViewHolder) row.getTag();
        }

        //assign values
        String who = eventComments.get(position).get("whoCommented");
        String what = eventComments.get(position).get("theComment");
        holder.whoCommented.setText(who);
        holder.theComment.setText(what);

        return row;
    }
}
