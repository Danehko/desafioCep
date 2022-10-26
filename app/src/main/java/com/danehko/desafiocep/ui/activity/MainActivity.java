package com.danehko.desafiocep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.danehko.desafiocep.R;
import com.danehko.desafiocep.dao.AddressDAO;
import com.danehko.desafiocep.model.Address;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AddressDAO dao = new AddressDAO();

        setTitle("Checagem de CEP");

        //Buscando Cep
        EditText campoCep = findViewById(R.id.activity_main_text_cep);

        Button botaoPesquisar = findViewById(R.id.activity_main_bt_search);
        botaoPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cep = campoCep.getText().toString();
                Address local = new Address(cep, "", "", "", "");
                dao.save(local);

                startActivity(new Intent(MainActivity.this, AddressActivity.class));

                Toast.makeText(MainActivity.this, "", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AddressDAO dao = new AddressDAO();

        ListView listAddress = findViewById(R.id.activity_main_list_address);
        listAddress.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dao.all()));
    }
}
