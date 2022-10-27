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

import com.danehko.desafiocep.R;
import com.danehko.desafiocep.database.AddressDatabase;
import com.danehko.desafiocep.database.dao.RoomAddressDAO;
import com.danehko.desafiocep.model.Address;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<Address> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                Address local = new Address(cep, "teste", "bola", "cachorro", "taco");

                if(!dao.containsPrimaryKey(cep))
                {
                    dao.save(local);

                    Intent goToTheAddress = new Intent(MainActivity.this, AddressActivity.class);
                    goToTheAddress.putExtra("address", local);
                    startActivity(goToTheAddress);
                }
                else{
                    Toast.makeText(MainActivity.this, "CEP já utilizado", Toast.LENGTH_LONG).show();
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
        menu.add("Remoover");
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
