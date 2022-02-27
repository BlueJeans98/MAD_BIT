package com.example.tab_layout;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> implements Filterable {
    //Initialize variable

    Activity activity;
    ArrayList<ContactModel> arrayList;

    private final Context context2;
    private final ArrayList<ContactModel> unfilteredList;

    public MainAdapter(Activity activity, ArrayList<ContactModel> arrayList, Context context){
        this.activity = activity;
        this.arrayList = arrayList;
        this.context2 = context;
        this.unfilteredList = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context2.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate (R.layout.item_contact, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Initialize contact model
        ContactModel model = arrayList.get(position);

        //Set name
        holder.tvName.setText(model.getName());
        //Set number
        //holder.tvNumber.setText(model.getNumber());
        String temp = model.getNumber();
        String num = "";
        int j = 0;
        for(int i = 0; i < temp.length(); ++i) {
            char buf = temp.charAt(i);
            if(!Character.isDigit(buf)) continue;
            if(j == 3 || j == 7) num = num.concat(Character.toString('-'));
            num = num.concat(Character.toString(buf));
            j++;
        }
        holder.tvNumber.setText(num);
    }

    @Override
    public int getItemCount() {
        //Return array list size
        return arrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String str = constraint.toString();
                if(str.isEmpty()) {
                    arrayList = unfilteredList;
                } else {
                    ArrayList<ContactModel> filteringList = new ArrayList<>();

                    for(ContactModel item: unfilteredList) {
                        if(item.getName().contains(str))
                            filteringList.add(item);
                    }

                    arrayList = filteringList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = arrayList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                arrayList = (ArrayList<ContactModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    interface OnItemClickListener {
        void onItemClick(View v, int position); //뷰와 포지션값
    }

    interface OnItemLongClickListener {
        void onItemLongClick(View v, int position);
    }

    //리스너 객체 참조 변수
    private OnItemClickListener mListener = null;
    private OnItemLongClickListener mListenerL = null;
    //리스너 객체 참조를 어댑터에 전달 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mListenerL = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Initialize variable
        TextView tvName, tvNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Assign variable
            tvName = itemView.findViewById(R.id.tv_name);
            tvNumber = itemView.findViewById(R.id.tv_number);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition ();
                    if(position!=RecyclerView.NO_POSITION){
                        if (mListener!=null){
                            mListener.onItemClick(view,position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition ();
                    if(position!=RecyclerView.NO_POSITION){
                        if (mListenerL!=null){
                            mListenerL.onItemLongClick(view,position);
                        }
                    }
                    return true;
                }
            });

        }
    }
}
