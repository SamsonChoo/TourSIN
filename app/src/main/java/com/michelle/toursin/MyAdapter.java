package com.michelle.toursin;

/**
 * Created by acer on 2/12/2017.
 */
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Tab1history.JsonData[] imageJsonData;
    private static int viewHolderCount = 0;
    Context parentContext;


    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Context parentContext, Tab1history.JsonData[] imagedata) {
        this.parentContext = parentContext;
        this.imageJsonData = imagedata;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layoutIDForListItem = R.layout.card_item;
        LayoutInflater inflater = LayoutInflater.from(parentContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIDForListItem,parent,shouldAttachToParentImmediately);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        viewHolderCount++;
        Log.i("Norman","on CreateViewHolder called " + viewHolderCount + "times");

        return myViewHolder;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public CardView mCardView;
        public TextView mTextView;
        public ImageView mImageView;
        public Button mButton;
        public MyViewHolder(View v) {
            super(v);

            mCardView = (CardView) v.findViewById(R.id.card_view);
            mTextView = (TextView) v.findViewById(R.id.USS);
            mImageView = (ImageView) v.findViewById(R.id.imageView);
            mButton = (Button) v.findViewById(R.id.button_booking);
        }

        public void bind(int position){
            final String url = imageJsonData[position].url;
            String filename = imageJsonData[position].file;
            String packageName = parentContext.getPackageName();
            String typeOfResource = "drawable";
            int resID = parentContext.getResources().getIdentifier(filename, typeOfResource, packageName);
            mImageView.setImageResource(resID);
            mTextView.setText(imageJsonData[position].name);
            mButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    context.startActivity(intent);
                }
            });
        }
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return imageJsonData.length;
    }
}
