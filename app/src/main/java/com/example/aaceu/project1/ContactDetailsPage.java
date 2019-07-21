package com.example.aaceu.project1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactDetailsPage extends AppCompatActivity {

    TextView fullName;
    ImageView contactImage;
    TextView callTextView;
    TextView emailTextView;
    TextView companyTextView;
    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details_page);

        fullName = (TextView) findViewById(R.id.detailsFullName);
        contactImage = (ImageView) findViewById(R.id.contactImage);
        callTextView = (TextView) findViewById(R.id.phoneNumber);
        emailTextView = (TextView) findViewById(R.id.emailAddress);
        companyTextView = (TextView) findViewById(R.id.companyName);


        // get the data passed from listView when it's pressed
        // the intent is temporarily stored in Bundle object and extract and set to related text View in the details page
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // userID will be needed for updating this contact details to database
            userID = bundle.getInt("userID");
            fullName.setText(bundle.getString("name"));
            // in this app there's two images for contact image stored in this project
            // use text to distinguish between them as storing text is easier than storing image in database
            if(bundle.getString("image").equalsIgnoreCase("person1")) {
                contactImage.setImageResource(R.drawable.person1);
            }
            else {
                contactImage.setImageResource(R.drawable.person2);
            }
            callTextView.setText(bundle.getString("phoneNumber"));
            emailTextView.setText(bundle.getString("email"));
            companyTextView.setText(bundle.getString("company"));

        }
    }

    public void GoToEditContact(View view) {
        //change from current activity to EditContactActivity
        // all users related information are placed in editText View so that user can edit it
        // use intent to pass all the information related data of this contact person
        Intent intent = new Intent(ContactDetailsPage.this, EditContactActivity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("name", fullName.getText().toString());
        intent.putExtra("phoneNumber", callTextView.getText().toString());
        intent.putExtra("email", emailTextView.getText().toString());
        intent.putExtra("company", companyTextView.getText().toString());
        startActivity(intent);
    }

    /*
        the following phone call from pressing a button is learned from
        https://developer.android.com/guide/topics/manifest/uses-permission-element
        https://developer.android.com/training/permissions/requesting#java
        https://developer.android.com
        Note: this is not reusing the code from there, but rather learn from these website to make fit with my method
     */
    public void CallContact(View view) {
        // checks whether the user allows permission for calling phone
        // if yes, then pass the contact phone number and call the phone
        if (ContextCompat.checkSelfPermission(ContactDetailsPage.this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(ContactDetailsPage.this, "Allow the app to call phone in your setting first!",Toast.LENGTH_LONG).show();
        }
        else
        {
            // pass the intent to action_call - which will open phone app and call the setData value in the phone
            Intent callPhoneIntent = new Intent(Intent.ACTION_CALL);
            callPhoneIntent.setData(Uri.parse("tel:" + callTextView.getText().toString()));
            startActivity(callPhoneIntent);
        }
    }
    public void MessageContact(View view)
    {
        // checks if permission for sending sms is allowed for this app
        // if yes pass the phone number using intent then it will open the phone dial with the phone number input
        // then you can choose to send sms
        if (ContextCompat.checkSelfPermission(ContactDetailsPage.this,
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            // permission is not allowed
            Toast.makeText(ContactDetailsPage.this, "Allow permission for sending sms in your setting!",Toast.LENGTH_LONG).show();
        } else {
            Intent messageIntent = new Intent(Intent.ACTION_VIEW);
            messageIntent.setData(Uri.parse("tel:" + callTextView.getText().toString()));
            startActivity(messageIntent);
        }
    }

    public void EmailContact(View view)
    {
        // call ACTION_VIEW from intent
        // add email address to the data - mailto will be recognise as email address and open email app in android
        // start the activity and pass the data, which is email address
        Intent emailIntent = new Intent(Intent.ACTION_VIEW);
        emailIntent.setData(Uri.parse("mailto:" + emailTextView.getText().toString()));
        startActivity(emailIntent);
    }
}
