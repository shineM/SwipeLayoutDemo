# SwipeLayout
A simple swipe menu layout, RecyclerView support✔️.
No more library,just copy the SwipeLayout.java to your widget, use it and improve it.
## How to use(RecyclerView Demo)
in your item_xxx.xml
```
...
<xyz.zhong.swipelayoutdemo.SwipeableLayout
    android:layout_width="match_parent"
    android:layout_height="50dp"
    android:id="@+id/swipe_layout"
    android:orientation="vertical">
        
    <TextView
       android:layout_width="80dp"
        android:layout_height="match_parent"
        android:background="#e66a6a"
        android:gravity="center"
        android:text="Delete"
        android:layout_gravity="right"
        android:textColor="#ffffff"
        android:textSize="18sp" />

    <TextView
        android:id="@+id/text_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#ffffff"
        android:text="swipe me!"
        android:gravity="center"
        android:layout_gravity="center" />
</xyz.zhong.swipelayoutdemo.SwipeableLayout>
...
```

in your adapter code

wrap your data source
```
 private class ItemData {
            public String text;
            public boolean isPinned;

            public ItemData(String text, boolean isPinned) {
                this.text = text;
                this.isPinned = isPinned;
            }
        }
```
don't forget to bind state for preventing RecyclerView recycle item pinned state
```
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
```
# Screenshot
<img height="600" src="https://cloud.githubusercontent.com/assets/5700392/18496660/39361d8e-7a5a-11e6-8d94-e08125bd5d2c.png"/>
