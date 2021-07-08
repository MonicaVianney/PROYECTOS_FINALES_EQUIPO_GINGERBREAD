package com.example.tictactoe;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    public void tic(View v){
        Intent intent = new Intent(this, TTT.class);
        startActivity(intent);

    }

    public void ahorcado(View v){
        Intent intent = new Intent(this, Ahorcado.class);
        startActivity(intent);

    }

    public void calculador(View v){
        Intent intent = new Intent(this, Calculador.class);
        startActivity(intent);

    }

    public void salida(View v){
        finish();
    }
}