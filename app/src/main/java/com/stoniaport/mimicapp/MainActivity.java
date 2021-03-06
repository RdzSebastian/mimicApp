package com.stoniaport.mimicapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;

public class MainActivity extends Activity {

    Equipo equipo1 = new Equipo("Equipo 1", 0, true);
    Equipo equipo2 = new Equipo("Equipo 2", 0, false);

    Pelicula pelicula = new Pelicula();

    private TextView countDownText;
    private long timeLeftInMilliseconds = 60000; // 1min
    CountDownTimer countDownTimer;

    Button customButton;
    boolean jugando;

    MediaPlayer mp;

    ImageView selectorDeEquipo1;
    ImageView selectorDeEquipo2;
    Toast backtoast;


    //------------------------- OnCreate --------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countDownText = findViewById(R.id.tiempo);

        pelicula.setPelicula("Presiona para obtener pelicula");

        mp = MediaPlayer.create(this,R.raw.silbato);

        customButton = findViewById(R.id.buttonEmpezar);
        customButton.setSelected(false);
        jugando=false;

        selectorDeEquipo1 = findViewById(R.id.selectorEquipo1);
        selectorDeEquipo1.setVisibility(View.VISIBLE);

        selectorDeEquipo2 = findViewById(R.id.selectorEquipo2);
        selectorDeEquipo2.setVisibility(View.INVISIBLE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mostrarResultado();
    }


    //-------------- OnResume (cuando vienen de otra activity) -----------------

    @Override
    public void onResume() {
        super.onResume();
        Bundle datos = getIntent().getExtras();

        if (datos != null) {

            setearDatos(datos);

            boolean acerto = datos.getBoolean("acerto");

            if (acerto) {
                pelicula.peliculaYaJugada();
            }

            cambioDeEquipo();
        }
    }


    private void setearDatos(Bundle datos) {

        equipo1.setNombre(datos.getString("equipo1"));
        equipo1.setPuntos(datos.getInt("puntos1"));
        equipo1.setTurno(datos.getBoolean("turno1"));

        equipo2.setNombre(datos.getString("equipo2"));
        equipo2.setPuntos(datos.getInt("puntos2"));
        equipo2.setTurno(datos.getBoolean("turno2"));

        pelicula.setPelicula(datos.getString("pelicula"));

        pelicula.setUltimas15(datos.getStringArrayList("ultimas15"));
        pelicula.setPeliculaYaJugada(datos.getStringArrayList("peliculaYaJugada"));
        pelicula.setCantidadDeVecesQuePediUnaPelicula(datos.getInt("cantidadDeVecesQuePediUnaPelicula"));
    }


    //-------------- OnDestroy (cuando cerras la app) -----------------


    protected void onDestroy(){

        super.onDestroy();
    }


    //-------------- OnStop(cuando minimizas la app) -----------------
    protected void onStop(){

        Intent datos = getIntent();

        datos.putExtra("equipo1",equipo1.getNombre());
        datos.putExtra("puntos1",equipo1.getPuntos());
        datos.putExtra("turno1",equipo1.isTurno());

        datos.putExtra("equipo2",equipo2.getNombre());
        datos.putExtra("puntos2",equipo2.getPuntos());
        datos.putExtra("turno2",equipo2.isTurno());

        datos.putExtra("pelicula", pelicula.getPeliculaString());

        datos.putExtra("ultimas15",pelicula.getUltimas15());
        datos.putExtra("peliculaYaJugada",pelicula.getPeliculaYaJugada());
        datos.putExtra("cantidadDeVecesQuePediUnaPelicula",pelicula.getCantidadDeVecesQuePediUnaPelicula());

        super.onStop();
    }

    
    //------------ Va a buscar la peli al metodo de la class ----------------

    public void buscarPelicula(View Vista){
        try {
            pelicula.getPelicula();
            }catch (Exception e){
                System.out.println("Error buscando pelicula");
        }

        while (pelicula.yaSalio()) {
            pelicula.getPelicula();
        }

        mostrarResultado();
    }


    //---- Dependiendo la cantidad de caracteres, cambia el marginTop en la parte visual ------

    void marginPeliculaSelect(String pelicula){
        if(pelicula.length()>18){
            TextView peliculaSelect = findViewById(R.id.PeliculaSelect);
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) peliculaSelect.getLayoutParams();
            mlp.setMargins(0, 0, 0, 0);
        }else{
            TextView peliculaSelect = findViewById(R.id.PeliculaSelect);
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) peliculaSelect.getLayoutParams();
            mlp.setMargins(0, 35, 0, 0);
        }
    }



    //---------------- Mostrar puntaje y equipo en la pantalla --------------------


    public void mostrarResultado() {
        marginPeliculaSelect(pelicula.getPeliculaString());

        TextView textEquipo1 = findViewById(R.id.Equipo1);
        textEquipo1.setText(equipo1.getNombre());

        TextView textPuntos1 = findViewById(R.id.puntos1);
        String nombreEquipo1String = Integer.toString(equipo1.getPuntos());
        textPuntos1.setText(nombreEquipo1String);


        TextView textEquipo2 = findViewById(R.id.Equipo2);
        textEquipo2.setText(equipo2.getNombre());

        TextView textPuntos2 = findViewById(R.id.puntos2);
        String nombreEquipo2String = Integer.toString(equipo2.getPuntos());
        textPuntos2.setText(nombreEquipo2String);


        TextView PeliculaV = findViewById(R.id.PeliculaSelect);
        PeliculaV.setText(pelicula.getPeliculaString());

        if(equipo1.isTurno()){
            textEquipo1.setTypeface(null,Typeface.BOLD);
            textEquipo2.setTypeface(Typeface.SANS_SERIF);

            selectorDeEquipo1 = findViewById(R.id.selectorEquipo1);
            selectorDeEquipo1.setVisibility(View.VISIBLE);

            selectorDeEquipo2 = findViewById(R.id.selectorEquipo2);
            selectorDeEquipo2.setVisibility(View.INVISIBLE);


        }else{
            textEquipo1.setTypeface(Typeface.SANS_SERIF);
            textEquipo2.setTypeface(null,Typeface.BOLD);

            selectorDeEquipo1 = findViewById(R.id.selectorEquipo1);
            selectorDeEquipo1.setVisibility(View.INVISIBLE);

            selectorDeEquipo2 = findViewById(R.id.selectorEquipo2);
            selectorDeEquipo2.setVisibility(View.VISIBLE);


            }

    }


    //--------------------Cambio de equipo------------------------------------


    public void cambioDeEquipo() {
        if (equipo1.isTurno()) {
            equipo1.setTurno(false);
            equipo2.setTurno(true);
        } else {
            equipo1.setTurno(true);
            equipo2.setTurno(false);
        }
        mostrarResultado();
    }



    //--------------------------Apreto Empezar sin pelicula----------------------------------

    public void noElegioPelicula() {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("No elegiste una pelicula");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
            }
        });

        dialogo1.show();

    }



    //--------------------------------Timmer----------------------------------------------


    public void startTimer(View view) {
        if(pelicula.getPeliculaString().equals("Presiona para obtener pelicula")){
            noElegioPelicula();
        }else {
            if(jugando) {
                Acierto();
                jugando=false;
            }
            else {
                customButton.setSelected(true);
                jugando=true;

                countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        timeLeftInMilliseconds = millisUntilFinished;
                        updateTimer();
                    }

                    @Override
                    public void onFinish() {
                        timeLeftInMilliseconds = 60000;
                        mp.start();
                        Acierto();
                    }


                }.start();
            }
        }
    }



    public void updateTimer(){
        int minutes = (int) timeLeftInMilliseconds / 60000;
        int seconds = (int) timeLeftInMilliseconds % 60000/1000;

        String timeLeftText;

        timeLeftText = "" + minutes;
        timeLeftText += ":";
        if(seconds<10) timeLeftText +="0";
        timeLeftText += seconds;

        countDownText.setText(timeLeftText);
    }


    public void restartTimer(){
        countDownTimer.cancel();
        countDownTimer = null;
        timeLeftInMilliseconds = 60000;
        updateTimer();

    }


    //-------Almacena datos si doy vuelta la pantalla o si la pongo en segundo plano---------------

    public void onSaveInstanceState(Bundle datos){

        datos.putString("equipo1",equipo1.getNombre());
        datos.putInt("equipo1",equipo1.getPuntos());

        datos.putString("equipo2",equipo2.getNombre());
        datos.putInt("equipo2",equipo2.getPuntos());

        datos.putString("pelicula", pelicula.getPeliculaString());

        datos.putStringArrayList("ultimas15",pelicula.getUltimas15());
        datos.putStringArrayList("peliculaYaJugada",pelicula.getPeliculaYaJugada());
        datos.putInt("cantidadDeVecesQuePediUnaPelicula",pelicula.getCantidadDeVecesQuePediUnaPelicula());

        super.onSaveInstanceState(datos);
    }


    public void onRestoreInstanceState(Bundle datos){

        super.onRestoreInstanceState(datos);

        setearDatos(datos);

        mostrarResultado();

    }


    //----------------------- Boton Stop o Termino el tiempo --------------------------------------

    public void Acierto() {

        if(countDownTimer != null) {
            restartTimer();
        }

        Intent AcertoONo = new Intent(MainActivity.this, AcertoONo.class);

        AcertoONo.putExtra("equipo1",equipo1.getNombre());
        AcertoONo.putExtra("puntos1",equipo1.getPuntos());
        AcertoONo.putExtra("turno1",equipo1.isTurno());

        AcertoONo.putExtra("equipo2",equipo2.getNombre());
        AcertoONo.putExtra("puntos2",equipo2.getPuntos());
        AcertoONo.putExtra("turno2",equipo2.isTurno());

        AcertoONo.putExtra("pelicula", pelicula.getPeliculaString());

        AcertoONo.putExtra("ultimas15",pelicula.getUltimas15());
        AcertoONo.putExtra("peliculaYaJugada",pelicula.getPeliculaYaJugada());
        AcertoONo.putExtra("cantidadDeVecesQuePediUnaPelicula",pelicula.getCantidadDeVecesQuePediUnaPelicula());

        startActivity(AcertoONo);

    }


    //----------------------- No se puede usar boton atras ----------------------

    public void onBackPressed() {
            if(backtoast!=null && backtoast.getView().getWindowToken()!=null) {
                moveTaskToBack(true);
            } else {
                backtoast = Toast.makeText(this, "Presiona devuelta para minimizar la app", Toast.LENGTH_SHORT);
                backtoast.show();
            }

    }


}

