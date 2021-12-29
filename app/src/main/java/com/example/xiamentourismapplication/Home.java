package com.example.xiamentourismapplication;

import static com.gun0912.tedpermission.TedPermissionUtil.isGranted;

import android.Manifest;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;


public class Home extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    DestinationDatabaseHelper destinationDatabaseHelper;
    ArrayList<Destination> destinations;
    RecyclerView recyclerView;
    DestinationAdapter adapter;
    Random random;
    FloatingActionButton food, attraction, shopping, museum, beach;
    GlobalClass globalVariable;

//    Variable used in Currency Calculator
    Spinner currency;
    Button calculate;
    EditText amount;
    TextView result;
    String selectedCurrency = "";

    //    Variable used to retrieve user location
    private FusedLocationProviderClient fusedLocationProviderClient;
    private double userLatitude = 0, userLongitude = 0;


    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        globalVariable = (GlobalClass) getActivity().getApplicationContext();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if(isGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (userLatitude == 0 && userLongitude == 0) {
                getLastLocation();
            }
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.randomRecyclerView);
        food = view.findViewById(R.id.filter_food);
        attraction = view.findViewById(R.id.filter_attraction);
        shopping = view.findViewById(R.id.filter_shopping);
        beach = view.findViewById(R.id.filter_beach);
        museum = view.findViewById(R.id.filter_museum);
        currency = view.findViewById(R.id.currency_spinner);
        amount = view.findViewById(R.id.et_amount);
        calculate = view.findViewById(R.id.btn_calculate);
        result = view.findViewById(R.id.tv_result);

        destinationDatabaseHelper = new DestinationDatabaseHelper(getContext());
        destinations = new ArrayList<>();
        random = new Random();

        int random_num = 0;
        ArrayList<Integer> number_list = new ArrayList<>();

        int limit = destinationDatabaseHelper.getNumberOfRecords();


        if(globalVariable.getNumber_list().isEmpty()){
            for (int i = 0;  i < 5; i++){
                do{
                    random_num = random.nextInt(limit) + 1;
                }while(number_list.contains(random_num));
                number_list.add(random_num);
            }
        }
        if(!number_list.isEmpty()){
            globalVariable.setNumber_list(number_list);
        }

        getRecommendedDestination(globalVariable.getNumber_list());
        recyclerView.setHasFixedSize(true);
        adapter = new DestinationAdapter(getContext(), getActivity(), destinations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(), R.array.currency_array, R.layout.support_simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        currency.setAdapter(arrayAdapter);
        food.setOnClickListener(this);
        beach.setOnClickListener(this);
        attraction.setOnClickListener(this);
        museum.setOnClickListener(this);
        shopping.setOnClickListener(this);

        currency.setOnItemSelectedListener(this);

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_amount = amount.getText().toString();
                if(selectedCurrency.equals(""))
                {
                    Toast.makeText(getContext(), "Please select a currency", Toast.LENGTH_SHORT).show();
                }
                else if(txt_amount.equals(""))
                {
                    Toast.makeText(getContext(), "Please enter an amount", Toast.LENGTH_SHORT).show();
                }
                else if(Double.parseDouble(txt_amount) > 999999999)
                {
                    Toast.makeText(getContext(), "The amount entered is too large", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    CurrencyCalculator calculator = new CurrencyCalculator();
                    String txt_result = calculator.convertCurrency(Double.parseDouble(txt_amount), selectedCurrency);
                    Toast.makeText(getContext(), "Calculation Success", Toast.LENGTH_SHORT).show();
                    result.setText(txt_result);
                }
            }
        });
    }

    public void getRecommendedDestination(ArrayList<Integer> number_list){
        Cursor cursor = destinationDatabaseHelper.getRecommendDestination(number_list);
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String address = cursor.getString(2);
                String description = cursor.getString(3);
                String operation_hours = cursor.getString(4);
                String phone = cursor.getString(5);
                String website = cursor.getString(6);
                double latitude = cursor.getDouble(7);
                double longitude = cursor.getDouble(8);
                byte[] image = cursor.getBlob(9);
                int type_id = cursor.getInt(10);

                destinations.add(new Destination(id, name, address, description, operation_hours, phone, website, latitude, longitude, image, type_id));
            }
        }
    }

    @Override
    public void onClick(View view) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        String category = "";
        switch (view.getId())
        {
            case R.id.filter_food:
                category = "restaurant";
                break;
            case R.id.filter_attraction:
                category = "attraction";
                break;
            case R.id.filter_shopping:
                category = "shopping";
                break;
            case R.id.filter_beach:
                category = "beach";
                break;
            case R.id.filter_museum:
                category = "museum";
                break;
        }
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.content, FilteredDestination.newInstance(category)).addToBackStack(null).commit();
    }

    //    get user current location
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        fusedLocationProviderClient.getLocationAvailability().addOnSuccessListener(new OnSuccessListener<LocationAvailability>() {
            @Override
            public void onSuccess(@NonNull LocationAvailability locationAvailability) {
                if(locationAvailability.isLocationAvailable()){
                    Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
                    locationTask.addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            userLatitude = location.getLatitude();
                            userLongitude = location.getLongitude();
                            globalVariable.setLatitude(userLatitude);
                            globalVariable.setLongitude(userLongitude);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedCurrency = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}