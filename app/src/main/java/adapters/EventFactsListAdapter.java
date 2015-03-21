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

import dbObjects.MakoEventFact;

public class EventFactsListAdapter extends BaseAdapter {

    private ArrayList<MakoEventFact> eventFacts = new ArrayList<MakoEventFact>();
    private Context context;
    private LayoutInflater inflater;

    public EventFactsListAdapter(ArrayList<MakoEventFact> list, Context context) {
        this.context = context;
        this.eventFacts = list;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return eventFacts.size();
    }

    @Override
    public Object getItem(int position) {
        return eventFacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        public String factID;
        public String makoEventID;
        public String content;
        public String contentType;
        public String URL;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        View row = convertView;

        if (row == null){
            row = inflater.inflate(R.layout.fact_item,parent,false);
            holder = new ViewHolder();

            //find elements in layout
            holder.whoCommented = (TextView) row.findViewById(R.id.who_commented);
            holder.theComment = (TextView) row.findViewById(R.id.the_comment);
            row.setTag(holder);
        }else {
            holder = (ViewHolder) row.getTag();
        }

        //assign values to elements in layout
        String who = eventComments.get(position).get("whoCommented");
        String what = eventComments.get(position).get("theComment");
        holder.whoCommented.setText(who);
        holder.theComment.setText(what);

        return row;
    }
}
