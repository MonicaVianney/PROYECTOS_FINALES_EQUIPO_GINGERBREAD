package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TTT extends AppCompatActivity  implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];
    private boolean player1Turn = true;

    private int player1Points;
    private int player2Points;
    private TextView textViewPlayer1;
    String ganador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_t_t);
        textViewPlayer1 = findViewById(R.id.text_view_p1);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "btn" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }
        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reiniciar();
            }
        });
    }
    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("-")) {
            return;
        }
        if (player1Turn) {
            ((Button) v).setText("X");
        } else {
            ((Button) v).setText("O");
        }

        if (BuscarGanador()) {
            if (player1Turn) {
                textViewPlayer1.setText("GANA X");
                ganador = "Gana X";
                ganador(v);

            } else {
                textViewPlayer1.setText("GANA 0");
                ganador = "Gana O";
                ganador(v);
            }
        } else {
            player1Turn = !player1Turn;
        }
    }

    public void ganador(View v){
        new AlertDialog.Builder(this)
                .setTitle(ganador)
                .setMessage("")
                .setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                Toast.makeText(getApplicationContext(),ganador, Toast.LENGTH_SHORT).show();

                            }
                        }).setNeutralButton(":3",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"", Toast.LENGTH_SHORT).show();
                    }
                }).show();


    }

    private boolean BuscarGanador() {
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        //verificar si gana en horizontal
        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("-")) {
                return true;
            }

        }
        //verificar si gana en vertical
        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("-")) {
                return true;
            }
        }

        //verificar si gana en diagonal izquierda arriva a derecha abajo.
        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("-")) {
            return true;
        }

        //verificar si gana en diagnoal derecha arriva a izquierda abajo.
        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("-")) {
            return true;
        }

        //si no se cumple nada nadie ah ganado aun
        return false;
    }



    //reinicia el juego, vaciando todos los botones, pone el contador en 0 y inicia el jugador 1
    private void reiniciarTablero() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("-");
            }
        }

        player1Turn = true;
    }

    //puntuaciones a 0 y llama a a reiniciar el tablero
    private void reiniciar() {
        textViewPlayer1.setText("");
        reiniciarTablero();
    }

    //guardamos todo por si acaso cambiamos de orientacion

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putBoolean("player1Turn", player1Turn);
    }

    //restauramos todo.
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
    }

    public void menu (View v){
        TTT.this.finish();
    }
}
