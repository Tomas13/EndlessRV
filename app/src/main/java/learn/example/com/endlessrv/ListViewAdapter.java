package learn.example.com.endlessrv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jean on 8/13/2016.
 */
public class ListViewAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<News> news;

    ListViewAdapter(Context context, ArrayList<News> news){
        ctx = context;
        this.news = news;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public Object getItem(int i) {
        return news.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View childView = view;
        if (childView == null) childView = lInflater.inflate(R.layout.lv_item, viewGroup, false);

        ((TextView) childView.findViewById(R.id.tv_date)).setText(news.get(i).getDate());
        ((TextView) childView.findViewById(R.id.tv_news)).setText(news.get(i).getMessage());

        return childView;
    }
}
