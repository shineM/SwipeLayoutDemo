package xyz.zhong.swipelayoutdemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        initRV();
    }

    private void initRV() {
        adapter = new MyAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
        });
    }

    private class MyAdapter extends RecyclerView.Adapter {
        private List<ItemData> data = new ArrayList<>();
        private Context context;

        public MyAdapter(Context context) {
            this.context = context;
            for (int i = 0; i < 30; i++) {
                data.add(new ItemData(i + "", false));
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            MyViewHolder h = (MyViewHolder) holder;
            h.textView.setText(data.get(position).text);
            h.swipeableLayout.setOnPinnedListener(new SwipeableLayout.OnPinnedListener() {
                @Override
                public void onPinned() {
                    //do something you like,such as control single pinned item
                    data.get(position).isPinned = true;
                }

                @Override
                public void onDisPinned() {
                    data.get(position).isPinned = false;
                }
            });
            if (data.get(position).isPinned) {
                h.swipeableLayout.setPinned(true, false);
            } else {
                h.swipeableLayout.setPinned(false, false);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        private class MyViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            SwipeableLayout swipeableLayout;

            public MyViewHolder(final View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.text_view);
                swipeableLayout = (SwipeableLayout) itemView.findViewById(R.id.swipe_layout);

            }
        }

        private class ItemData {
            public String text;
            public boolean isPinned;

            public ItemData(String text, boolean isPinned) {
                this.text = text;
                this.isPinned = isPinned;
            }
        }

    }

}
