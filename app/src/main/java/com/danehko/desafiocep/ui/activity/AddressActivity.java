package com.danehko.desafiocep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.danehko.desafiocep.R;
import com.danehko.desafiocep.model.Address;

public class AddressActivity extends AppCompatActivity {

    private TextView campoCep;
    private TextView campoLogradouro;
    private TextView campoBairro;
    private TextView campoCidade;
    private TextView campoEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        setTitle("Endereço");
        startupTextViews();
        Intent data = getIntent();
        Address enderecoCarregado = (Address) data.getSerializableExtra("address");
        setViewsAddress(enderecoCarregado, campoCep, campoLogradouro, campoBairro, campoCidade, campoEstado);

        Button botaoVoltar = findViewById(R.id.activity_address_bt_voltar);
        botaoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void startupTextViews() {
        campoCep = findViewById(R.id.activity_address_text_cep);
        campoLogradouro = findViewById(R.id.activity_address_text_logradouro);
        campoBairro = findViewById(R.id.activity_address_text_bairro);
        campoCidade = findViewById(R.id.activity_address_text_localidade);
        campoEstado = findViewById(R.id.activity_address_text_estado);
    }

    private void setViewsAddress(Address addres, TextView campoCep, TextView campoLogradouro, TextView campoBairro, TextView campoCidade, TextView campoEstado) {
        campoCep.setText(String.format("CEP: %s", addres.getCep().substring(0,5) + "-" + addres.getCep().substring(5)));
        campoLogradouro.setText(String.format("Logradouro: %s", addres.getLogradouro()));
        campoBairro.setText(String.format("Bairro: %s", addres.getBairro()));
        campoCidade.setText(String.format("Cidade: %s", addres.getCidade()));
        campoEstado.setText(String.format("UF: %s", addres.getEstado()));
    }
}