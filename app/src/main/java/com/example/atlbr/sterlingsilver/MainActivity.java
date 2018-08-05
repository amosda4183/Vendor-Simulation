package com.example.atlbr.sterlingsilver;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {


    //Create variables for gathering user input
    String dateInput, nameInput, cardTypeInput,cardNumberInput,receiptString, runningTotalString, displayString = "";
    int numItemsIndex, grouponIndex = 0;

    //Variables to be used for calculations
    final double SALES_TAX = 1.06;
    final double itemCost = 45.0;

    double customerTotal = 0.0d;
    double runningTotal = 0.0d;

    int itemsPurchased = 0;
    int totalCustomers = 0;

    DecimalFormat currency = new DecimalFormat("$###,##0.00");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //setup action bar
       Toolbar toolbar = findViewById(R.id.app_bar);
       setSupportActionBar(toolbar);

        //Create objects for user input
        final EditText date = findViewById(R.id.date);
        final EditText name = findViewById(R.id.name);

        //Setup spinner objects and load with string array values, set to first index by default to avoid errors
        final Spinner numItems = findViewById(R.id.numItems);
        ArrayAdapter numAdapter = ArrayAdapter.createFromResource(this, R.array.numberOfItems, android.R.layout.simple_spinner_item);
        numAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numItems.setAdapter(numAdapter);

        final Spinner grouponCode = findViewById(R.id.grouponCode);
        final ArrayAdapter grouponAdapter = ArrayAdapter.createFromResource(this, R.array.grouponCode, android.R.layout.simple_spinner_item);
        grouponCode.setAdapter(grouponAdapter);

        //Setup radio button group for card type, visa selected by default to avoid errors
        final RadioButton visaRad = findViewById(R.id.visa);
        visaRad.setChecked(true);
        final RadioButton mastercardRad = findViewById(R.id.mastercard);
        final RadioButton amexRad = findViewById(R.id.amex);

        final EditText cardNumber = findViewById(R.id.cardNumber);

        //Create buttons and listeners for user control
        final Button calculate = findViewById(R.id.calculate);
        final Button clear = findViewById(R.id.clear);



        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Take edittext input and assign to string variables for output later

                    dateInput = date.getText().toString();

                    nameInput = name.getText().toString();

                    cardNumberInput = cardNumber.getText().toString();

                    //Check cardtype radio buttons
                    if(visaRad.isChecked())
                        cardTypeInput = "Visa";
                    else if(mastercardRad.isChecked())
                        cardTypeInput = "Mastercard";
                    else if(amexRad.isChecked())
                        cardTypeInput = "American Express";

                    //Check spinner selections
                    numItemsIndex = numItems.getSelectedItemPosition();
                    grouponIndex = grouponCode.getSelectedItemPosition();


                    //Assign variables based on spinner item selected
                    switch (numItemsIndex) {
                        case 0:
                            itemsPurchased = 1;
                            break;
                        case 1:
                            itemsPurchased = 2;
                            break;
                        case 2:
                            itemsPurchased = 3;
                            break;
                        case 3:
                            itemsPurchased = 4;
                            break;
                        case 4:
                            itemsPurchased = 5;
                            break;
                        case 5:
                            itemsPurchased = 6;
                            break;
                        default:
                            itemsPurchased = 0;
                    }

                    //Check for groupon code, changes number of items purchased by removing them from the calculation entirely, based on which groupon is used.
                    switch (grouponIndex) {
                        case 0:
                            break;
                        case 1:
                            itemsPurchased -= 1;
                            break;
                        case 2:
                            itemsPurchased -= 2;
                            break;
                        default:
                    }

                    //Check for blank fields, show toast message as appropriate, so user knows they forgot to enter data into a field
                if (date.getText().toString().length() == 0)
                    Toast.makeText(MainActivity.this, "Date cannot be blank", Toast.LENGTH_LONG).show();

                else if(name.getText().toString().length() == 0)
                    Toast.makeText(MainActivity.this,"Name cannot be blank", Toast.LENGTH_LONG).show();

                else if(cardNumber.getText().toString().length() == 0)
                    Toast.makeText(MainActivity.this,"Card number cannot be blank",Toast.LENGTH_LONG).show();
                else if(customerTotal < 0)
                    Toast.makeText(MainActivity.this, "MUST SELECT AT LEAST 2 ITEMS! SILVER2018 GROUPON WAS PURCHASED IN ADVANCE",Toast.LENGTH_LONG).show();

                else{
                    //Begin calculations based on selected options from user input
                    totalCustomers++;
                    customerTotal = (itemsPurchased * itemCost) * (SALES_TAX);
                    runningTotal += customerTotal;

                    //Generate output strings to be passed to second activity when menu item is selected
                    receiptString = "******Customer Receipt*****\n"+
                            "\nToday's Date: " + dateInput +
                            "\nReceipt for: " + nameInput +
                            "\nCustomer using card type: " + cardTypeInput +
                            "\nCard Number: " + cardNumberInput +
                            "\nCustomer Total: " + currency.format(customerTotal);

                    runningTotalString = "*****Totals for the Day*****\n"+
                            "\nCustomers Processed: " + totalCustomers +
                            "\nTotal Money Received: " + currency.format(runningTotal);

                    Toast.makeText(MainActivity.this, "Input Successfully Received", Toast.LENGTH_LONG).show();

                }
            }
        });

        //Clear fields and customerTotal to prepare for new customer, set default credit card type to visa
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Confirm input fields have been cleared
                Toast.makeText(MainActivity.this, "Input fields have been cleared, ready for new customer.", Toast.LENGTH_LONG).show();

                date.setText("");
                name.setText("");

                numItems.setSelection(0,true);
                grouponCode.setSelection(0,true);

                cardNumber.setText("");

                if(mastercardRad.isChecked())
                    mastercardRad.setChecked(false);
                else if(amexRad.isChecked())
                    amexRad.setChecked(false);

                visaRad.setChecked(true);

                customerTotal = 0;

                runningTotalString = "";

                receiptString = "";
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Determine which menu item was selected, and pass the appropriate string to the display activity, which was generated onClick
        switch (item.getItemId()) {

            case R.id.displayReceipt:
                Intent displayReceipt = new Intent(this, Display.class);
                displayReceipt.putExtra("receipt",receiptString);
                startActivity(displayReceipt);
                break;

            case R.id.displayTotals:
                Intent totals = new Intent(this, Totals.class);
                totals.putExtra("total", runningTotalString );
                startActivity(totals);
                break;

            default:
                //unkown error
        }
        return super.onOptionsItemSelected(item);
    }
}
