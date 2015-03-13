package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vj.makosocial.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import dbObjects.MakoEvent;


public class WifgetConfigListAdapter extends BaseAdapter{
    final String LOG_TAG = "widget";
    final String FORMAT = "EEE MMM dd kk:mm";

    ArrayList<MakoEvent> makoEvents = new ArrayList<MakoEvent>();
    Context             context;
    LayoutInflater      inflater;
    SimpleDateFormat    sdf;

    //view holder for performance increase
    static class WidgetConfViewHolder {
        public TextView eventName;
        public TextView date;
        public ImageView pic;

    }

    public WifgetConfigListAdapter(Context context) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WidgetConfViewHolder holder;
        View row = convertView;
        Date makoEventdate;
        Date currentDate;

        if (row == null){
            row = inflater.inflate(R.layout.wgconf_list_item_layout,parent,false);
            holder = new WidgetConfViewHolder();

            //find elements
            holder.eventName = (TextView) row.findViewById(R.id.wg_conf_etEventName);
            holder.pic = (ImageView) row.findViewById(R.id.wgconf_ivMainPic);
            holder.date = (TextView) row.findViewById(R.id.wgconf_tv_date);
            row.setTag(holder);
        }else {
            holder = (WidgetConfViewHolder) row.getTag();
        }

        //assign values
        makoEventdate = makoEvents.get(position).getStartDate();
        currentDate = new Date();

        if(currentDate.after(makoEventdate)){   // if not in the future - no need to display
            //row =(RelativeLayout) row.findViewById(R.id.rl_empty);
            row=new RelativeLayout(context);
        }else{

            sdf = new SimpleDateFormat(FORMAT);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC+2"));
            holder.eventName.setText(makoEvents.get(position).getName());
            holder.date.setText(sdf.format(makoEventdate));
            holder.pic.setImageBitmap(makoEvents.get(position).getPicture());
        }




        return row;
    }

    public void updateEntries(ArrayList<MakoEvent> entries){
        makoEvents = entries;
        notifyDataSetChanged();
    }
}
