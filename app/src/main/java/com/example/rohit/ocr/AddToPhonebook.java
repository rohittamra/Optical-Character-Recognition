package com.example.rohit.ocr;

import android.content.ContentProviderOperation;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddToPhonebook extends AppCompatActivity implements  View.OnClickListener{
    EditText e1,e2;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_phonebook);

        e1=(EditText)findViewById(R.id.name);
        e2=(EditText)findViewById(R.id.mobilenumber);

        values v=new values();
        String namegot=v.getName();
        String numbergot=v.getNumber();
        System.out.println("@@@@@@namegot:::::::"+namegot+"     number :::: "+numbergot);
        e1.setText(""+namegot);
        e2.setText(""+numbergot);
        // e8=(EditText)findViewById(R.id.address);
        b1=(Button)findViewById(R.id.button);
        b1.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if(view ==b1)
        {

            String DisplayName = e1.getText().toString();
            String MobileNumber = e2.getText().toString();
            //String address = e8.getText().toString();
            ArrayList<ContentProviderOperation> ops = new ArrayList < ContentProviderOperation > ();

            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .build());

            //------------------------------------------------------ Names
            if (DisplayName != null) {
                ops.add(ContentProviderOperation.newInsert(
                        ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(
                                ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                                DisplayName).build());
            }

            //------------------------------------------------------ Mobile Number
            if (MobileNumber != null) {
                ops.add(ContentProviderOperation.
                        newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        .build());
            }

            // Asking the Contact provider to create a new contact
            try {
                getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                Toast.makeText(getApplicationContext(), "Added to phonebook", Toast.LENGTH_LONG).show();


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
