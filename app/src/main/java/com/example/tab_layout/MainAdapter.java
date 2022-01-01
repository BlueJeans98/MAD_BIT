package com.example.tab_layout;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tab_layout.ContactModel;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{
    //Initialize variable

    Activity activity;
    ArrayList<ContactModel> arrayList;

    private String TAG = "Adapter";
    private Context context2;

    public MainAdapter(Activity activity, ArrayList<ContactModel> arrayList, Context context){
        this.activity = activity;
        this.arrayList = arrayList;
        this.context2 = context;
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
            num = num.concat(Character.toString(buf));
            j++;
            if(j == 3 || j == 7) num = num.concat(Character.toString('-'));
        }
        holder.tvNumber.setText(num);
    }

    @Override
    public int getItemCount() {
        //Return array list size
        return arrayList.size();
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
            final Button btn_edit = itemView.findViewById(R.id.btn_edit_init);
            final Button btn_delete = itemView.findViewById(R.id.btn_delete);

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
