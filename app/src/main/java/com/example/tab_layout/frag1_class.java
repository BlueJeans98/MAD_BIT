package com.example.tab_layout;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class frag1_class extends Fragment implements TextWatcher {
    //Initialize variable
    RecyclerView recyclerView;
    ArrayList<ContactModel> arrayList;
    MainAdapter adapter;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment1, null);
        context = getContext();
        recyclerView = root.findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();
        adapter = new MainAdapter(getActivity(), arrayList, context);
        recyclerView.setAdapter(adapter);

        View view = inflater.inflate(R.layout.short_click, null, false);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(view);

        final ImageButton btn_call = view.findViewById(R.id.btn_call);
        final ImageButton btn_message = view.findViewById(R.id.btn_message);
        final ImageButton btn_video_call = view.findViewById(R.id.btn_video_call);
        final ImageButton btn_save_init = root.findViewById(R.id.btn_save_init);

        EditText editText = root.findViewById(R.id.search_bar);
        editText.addTextChangedListener(this);

        //Check permission
        checkPermission();

        btn_save_init.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick(View view) {
                saveInit();
            }

        });

        //리사이클러뷰 클릭 이벤트
        adapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {

            //아이템 클릭
            @Override
            public void onItemClick(View v, int position) {
                bottomSheetDialog.show();
            }

        });

        adapter.setOnItemLongClickListener(new MainAdapter.OnItemLongClickListener() {

            //아이템 클릭
            @Override
            public void onItemLongClick(View v, int position) {
                String name = arrayList.get(position).getName();
                String number = arrayList.get(position).getNumber();

                itemLongClick(name, number, position);
            }

        });

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        btn_message.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        btn_video_call.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        return root;
    }

    //AlertDialog 를 사용해서 데이터를 수정한다.
    private void editItem(String name, String number, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog, null, false);
        builder.setView(view);

        final AlertDialog dialog = builder.create();

        final Button btn_edit = view.findViewById(R.id.btn_edit);
        final Button btn_cancel_edit = view.findViewById(R.id.btn_cancel_edit);
        final EditText edit_name = view.findViewById(R.id.edit_editName);
        final EditText edit_number = view.findViewById(R.id.edit_editNumber);

        edit_name.setText(name);
        edit_number.setText(number);

        // 수정 버튼 클릭
        //어레이리스트 값을 변경한다.
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String editName = edit_name.getText().toString();
                String editNumber = edit_number.getText().toString();
                arrayList.get(position).setName(editName);
                arrayList.get(position).setNumber(editNumber);
                adapter.notifyItemChanged(position);
                dialog.dismiss();
                Toast toast = Toast.makeText(context, "수정되었습니다", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 1700);
                toast.show();
            }
        });

        // 취소 버튼 클릭
        btn_cancel_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void itemLongClick(String name, String number, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.long_click, null, false);
        builder.setView(view);

        final AlertDialog dialog = builder.create();

        final ImageButton btn_edit_init = view.findViewById(R.id.btn_edit_init);
        final ImageButton btn_delete = view.findViewById(R.id.btn_delete);

        btn_edit_init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                editItem(name, number, position);
                dialog.dismiss();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                arrayList.remove(position);
                adapter.notifyItemRemoved(position);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void saveInit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.save_click, null, false);
        builder.setView(view);

        final AlertDialog dialog = builder.create();

        final Button btn_save = view.findViewById(R.id.btn_save);
        final Button btn_cancel_save = view.findViewById(R.id.btn_cancel_save);

        final EditText edit_name = view.findViewById(R.id.edit_name);
        final EditText edit_number = view.findViewById(R.id.edit_number);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(edit_name.getText().length()==0&&edit_number.getText().length()==0){
                    Toast.makeText (context,"이름과 전화번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                }else{
                    String name = edit_name.getText().toString();
                    String number = edit_number.getText().toString();
                    edit_name.setText("");
                    edit_number.setText("");
                    ContactModel data = new ContactModel(name, number);

                    int lower_bound = arrayList.size();
                    for(int i = 0; i < arrayList.size(); ++i)
                        if(arrayList.get(i).getName().compareTo(name) >= 0) {
                            lower_bound = i;
                            break;
                        }
                    arrayList.add(lower_bound, data);
                    adapter.notifyItemInserted(lower_bound);
                }
                dialog.dismiss();
            }
        });

        btn_cancel_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void checkPermission() {
        //Check condition
        if(ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){
            //when permission is not granted
            //Request Permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS},100);
        }else{
            //When permission is granted
            //Create method
            getContactList();
        }
    }

    private void getContactList() {
        //initialize uri
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        //Sort by ascending
        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC";
        //Initialize cursor
        Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(
                uri,null,null,null,sort
        );
        //Check condition
        if(cursor.getCount() > 0){
            //When count is greater than 0
            //Use while loop
            while(cursor.moveToNext()){
                //Cursor move to next
                //Get Contact ID
                String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                //Get contact name
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                //initialize phone uri
                Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                //initialize selection
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?";
                //Initialize phone cursor
                Cursor phoneCursor =getActivity().getApplicationContext().getContentResolver().query(
                        uriPhone,null,selection,new String[]{id},null
                );
                //Check condition
                if(phoneCursor.moveToNext()){
                    //when phone cursor move to next
                    String number = phoneCursor.getString(phoneCursor.getColumnIndexOrThrow(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                    ));
                    //Initialize contact model
                    ContactModel model = new ContactModel();
                    //Set Name
                    model.setName(name);
                    //Set number
                    model.setNumber(number);
                    //Add model in array list
                    arrayList.add(model);
                    //Close phone cursor
                    phoneCursor.close();
                }
            }
            //Close cursor
            cursor.close();
        }
        //Set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        //Initialize adapter
        adapter = new MainAdapter(getActivity(),arrayList, context);
        //Set adapter
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Check condition
        if(requestCode == 100 && grantResults.length > 0 && grantResults[0]
                == PackageManager.PERMISSION_GRANTED){
            //When permission is granted
            //Call Method
            getContactList();
        }else{
            //When permission is denied
            //Display toast
            Toast.makeText(context,"Permission Denied.", Toast.LENGTH_SHORT).show();
            //Call check permission method
            checkPermission();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        adapter.getFilter().filter(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}


