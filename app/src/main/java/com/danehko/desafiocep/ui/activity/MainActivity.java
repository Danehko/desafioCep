package com.danehko.desafiocep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.danehko.desafiocep.R;
import com.danehko.desafiocep.database.AddressDatabase;
import com.danehko.desafiocep.database.dao.RoomAddressDAO;
import com.danehko.desafiocep.model.Address;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<Address> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestQueue queue = Volley.newRequestQueue(this);

        AddressDatabase database = Room.databaseBuilder(this, AddressDatabase.class, "desafioCep.db").allowMainThreadQueries().build();
        RoomAddressDAO dao = database.getRoomAlunoDAO();
        setTitle("Checagem de CEP");
        //Buscando Cep
        EditText campoCep = findViewById(R.id.activity_main_text_cep);

        Button botaoPesquisar = findViewById(R.id.activity_main_bt_search);
        botaoPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cep = campoCep.getText().toString();
                if(cep.length()>7 && cep.length()<10){
                    if(cep.length()==9){
                        cep = cep.replace("-","");
                    }

                    List<Address> localDatabase = dao.getAddress(cep);

                    if (!localDatabase.isEmpty()) {

                        Address local = localDatabase.get(0);

                        Intent goToTheAddress = new Intent(MainActivity.this, AddressActivity.class);
                        goToTheAddress.putExtra("address", local);
                        startActivity(goToTheAddress);
                    } else {

                        String url = "https://viacep.com.br/ws/" + cep + "/json/";

                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        String cepReceived = "";
                                        String logradouroReceived = "";
                                        String bairroReceived = "";
                                        String localidadeReceived = "";
                                        String ufReceived = "";

                                        JSONObject obj = null;
                                        try {
                                            obj = new JSONObject(response);
                                            cepReceived = obj.getString("cep").replace("-","");
                                            logradouroReceived = obj.getString("logradouro");
                                            bairroReceived = obj.getString("bairro");
                                            localidadeReceived = obj.getString("localidade");
                                            ufReceived = obj.getString("uf");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Address local = new Address(cepReceived, logradouroReceived, bairroReceived, localidadeReceived, ufReceived);
                                        dao.save(local);

                                        Intent goToTheAddress = new Intent(MainActivity.this, AddressActivity.class);
                                        goToTheAddress.putExtra("address", local);
                                        startActivity(goToTheAddress);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(MainActivity.this, "CEP invalido", Toast.LENGTH_LONG).show();
                                    }
                                }
                        );
                        queue.add(stringRequest);
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "CEP invalido", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AddressDatabase database = Room.databaseBuilder(this, AddressDatabase.class, "desafioCep.db").allowMainThreadQueries().build();
        RoomAddressDAO dao = database.getRoomAlunoDAO();

        ListView listAddress = findViewById(R.id.activity_main_list_address);
        List<Address> adresses = dao.all();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, adresses);
        listAddress.setAdapter(adapter);
        listAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                               @Override
                                               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                   Address address = adresses.get(position);

                                                   Intent goToTheAddress = new Intent(MainActivity.this, AddressActivity.class);
                                                   goToTheAddress.putExtra("address", address);
                                                   startActivity(goToTheAddress);
                                               }
                                           }
        );
        registerForContextMenu(listAddress);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Remover");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AddressDatabase database = Room.databaseBuilder(this, AddressDatabase.class, "desafioCep.db").allowMainThreadQueries().build();
        RoomAddressDAO dao = database.getRoomAlunoDAO();

        AdapterView.AdapterContextMenuInfo menuinfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Address chosenAddress = adapter.getItem(menuinfo.position);
        dao.remove(chosenAddress);
        adapter.remove(chosenAddress);
        return super.onContextItemSelected(item);

    }
}
