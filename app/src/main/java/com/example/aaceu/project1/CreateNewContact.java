package com.example.aaceu.project1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class CreateNewContact extends AppCompatActivity {
    Button saveButton;
    Button cancelButton;
    EditText contactNameInput;
    EditText phoneNumberInput;
    EditText emailInput;
    EditText companyInput;

    // radio button variable for checking whether which option is checked for contact image
    RadioButton person1;
    RadioButton person2;

    // for error printing message
    // every time the user doesn't put the appropriate data and try to save it, the message updated from
    // fieldsValidation method
    private static String errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_contact);

        // assign each variables defined in above to the related id in XML file
        saveButton = (Button) findViewById(R.id.saveButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        contactNameInput = (EditText) findViewById(R.id.contactNameInput);
        phoneNumberInput = (EditText) findViewById(R.id.phoneNumberInput);
        emailInput = (EditText) findViewById(R.id.emailInput);
        companyInput = (EditText) findViewById(R.id.urCompanyInput);
        person1 = (RadioButton) findViewById(R.id.contactIcon1);
        person2 = (RadioButton) findViewById(R.id.contactIcon2);
    }

    public void saveNewContact(View view) {
        // if the validation of the fields return true then save the data to database then deserialize the data to Contact object and populate listView
        if (fieldsValidation())
        {
            // this intent will be passed to MainActivity, then will be save to database and extract the data from
            // database again and store each record to Contact object list then populate the listView from this list of Contact instance
            Intent intent = new Intent(CreateNewContact.this, MainActivity.class);
            intent.putExtra("contactName", contactNameInput.getText().toString());
            intent.putExtra("phoneNumber", phoneNumberInput.getText().toString());
            intent.putExtra("email", emailInput.getText().toString());
            intent.putExtra("company", companyInput.getText().toString());

            // if person1 radio button is checked then assign person1 text to the image key
            // if person2 is checked do assign person2 string value to image key
            if(person1.isChecked()) {
                intent.putExtra(("image"), "person1");
            }
            else if (person2.isChecked()) {
                intent.putExtra(("image"), "person2");
            }
            startActivity(intent);

        } else {
            // show errorMessage set from fieldsValidation method as a alert dialog to user
            // so that user know what is going on
            final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CreateNewContact.this);
            alertBuilder.setMessage(errorMessage);
            alertBuilder.setCancelable(true);
            //clicking OK will cancel the alert dialog
            alertBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog errorAlertDialog = alertBuilder.create();
            errorAlertDialog.show();
        }

    }

    // pressing cancel button will bring the user to MainActivity page
    public void cancelNewContact(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    // using patterns are learned from Android Studio official Developer website
    // https://developer.android.com/reference/android/util/Patterns
    public boolean fieldsValidation()
    {
        boolean result = true;
        // assign false to result if the input name is less than 2 characters or is empty
        if (contactNameInput.getText().toString().isEmpty() || contactNameInput.getText().toString().length() < 2) {
            result = false;
            errorMessage = "Name cannot be empty or must be greater than two characters!";
            return result;
        }

        // assign result to false if phone number is empty or matches the builtin patterns of phone in android studio
        if (phoneNumberInput.getText().toString().isEmpty() || !Patterns.PHONE.matcher(phoneNumberInput.getText().toString()).matches()) {
            result = false;
            errorMessage = "Phone cannot be empty or must be a phone number format!";
            return result;
        }

        // checks with email address patterns and assign false to result if not match
        // this field can be empty
        if (emailInput.getText().toString().isEmpty())
        {
            // we do not have to do anything as result is set to true
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput.getText().toString()).matches()) {
            result = false;
            errorMessage = "Email address is not email format!";
            return result;
        }

        //if company is less than two characters assign result as false
        // this fields can be empty
        if (companyInput.getText().toString().isEmpty())
        {

        } else if (companyInput.getText().toString().length() < 2) {
            result = false;
            errorMessage = "If company is insert, then must be longer than two characters length!";
            return result;
        }

        // if none is pressed in two radio buttons, assign result to false
        if (person1.isChecked() || person2.isChecked())
        {

        } else {
            result = false;
            errorMessage = "You have to select one of the image!";
            return result;
        }
        return result;
    }
}
