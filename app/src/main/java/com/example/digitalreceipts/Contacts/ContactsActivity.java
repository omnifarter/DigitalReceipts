package com.example.digitalreceipts.Contacts;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.loader.content.CursorLoader;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import android.widget.Toast;

import com.example.digitalreceipts.Billsplit.BIllSplitActivity;
import com.example.digitalreceipts.R;
import com.example.digitalreceipts.ReceiptItem;
import com.example.digitalreceipts.ReceiptsRoom;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {
    final String[] from = {ContactsContract.Contacts.DISPLAY_NAME};
    SimpleCursorAdapter simpleCursorAdapter;
    TextView textView;
    ListView l1;
    Button next;
    SearchView search_name;
    ArrayList<String> names;
    @RequiresApi(api = Build.VERSION_CODES.O)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        names = new ArrayList<String>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        l1 = findViewById(R.id.list_of_contacts);
        textView = findViewById(R.id.freq_contacted);
        final int height = textView.getHeight();
        //TODO fix thise
        Log.i("look",Integer.toString(height));
        l1.setTextFilterEnabled(true);
        search_name = findViewById(R.id.searchView);
        final Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        final int[] to = {R.id.contact_name};
        l1.setDivider(null);
        next = findViewById(R.id.next);
        final Cursor cursor_freq = getContentResolver().query(ContactsContract.Contacts.CONTENT_STREQUENT_URI,null,null,null);
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.contact_item_list, cursor_freq, from, to);
        l1.setAdapter(simpleCursorAdapter);
        l1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BIllSplitActivity.class);
                Intent current = getIntent();
                intent.putParcelableArrayListExtra("BILL_SPLIT",current.getParcelableArrayListExtra("BILL_SPLIT"));
                intent.putExtra("RECEIPT_NUMBER",current.getStringExtra("RECEIPT_NUMBER"));
                intent.putStringArrayListExtra("NAMES",names);
                startActivity(intent);
            }
        });
        simpleCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                String searchQuery = ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?";
                String[] args = {"%"+charSequence.toString()+"%"};
                Cursor cur = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, searchQuery, args, null);

                return cur;
            }
        });
        search_name.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(search_name.getQuery().length()==0){
                    textView.setText(R.string.freq_contacted);
                    textView.setHeight(height);
                    simpleCursorAdapter.swapCursor(cursor_freq);
                }
                else{
                    if(simpleCursorAdapter.getCursor() == cursor_freq){
                        textView.setText("");
                        textView.setHeight(0);
                        final Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                        simpleCursorAdapter.swapCursor(cursor);}
                    simpleCursorAdapter.getFilter().filter(s);

                    simpleCursorAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });
        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.contact_name);
                String text = textView.getText().toString();
                if(names.contains(text)){
                    names.remove(text);
                    Log.i("look","text removed");
                    Toast.makeText(getApplicationContext(), new String(text + " has been removed"), Toast.LENGTH_LONG).show();

                }
                else {
                    names.add(text);
                    textView.setPressed(true);
                    Log.i("look","text added");
                    Toast.makeText(getApplicationContext(), new String(text + " has been added"), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}