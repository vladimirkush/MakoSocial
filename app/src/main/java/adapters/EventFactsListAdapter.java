package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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

        int count = getCount();
        count = 0;
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
        public TextView content;
        public String contentType;
        public TextView URL;
        public Button gotoUrl;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        View row = convertView;

        if (row == null){
            row = inflater.inflate(R.layout.fact_item,parent,false);
            holder = new ViewHolder();

            //find elements in layout
            holder.content = (TextView) row.findViewById(R.id.fact_content);
            holder.URL = (TextView) row.findViewById(R.id.fact_url);
            holder.gotoUrl = (Button) row.findViewById(R.id.goto_url);
            row.setTag(holder);
        }else {
            holder = (ViewHolder) row.getTag();
        }

        //assign values to elements in layout
        String content_str = eventFacts.get(position).getContent();
        String url_str = eventFacts.get(position).getURL();
        holder.content.setText(content_str);
        holder.URL.setText("More info at: "+url_str);

        if (!url_str.equals("")) {
            holder.URL.setVisibility(View.VISIBLE);
            holder.gotoUrl.setVisibility(View.VISIBLE);
        }

        return row;
    }
}
