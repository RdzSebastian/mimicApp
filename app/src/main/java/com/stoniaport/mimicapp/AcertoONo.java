package com.stoniaport.mimicapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AcertoONo extends AppCompatActivity {


    Equipo equipo1 = new Equipo();
    Equipo equipo2 = new Equipo();

    Pelicula pelicula = new Pelicula();

    boolean cambioDeEquipo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerto_ono);

        Bundle estado = getIntent().getExtras();
        if (estado != null) {
            equipo1.setNombre(estado.getString("equipo1"));
            equipo1.setPuntos(estado.getInt("puntos1"));
            equipo1.setTurno(estado.getBoolean("turno1"));

            equipo2.setNombre(estado.getString("equipo2"));
            equipo2.setPuntos(estado.getInt("puntos2"));
            equipo2.setTurno(estado.getBoolean("turno2"));


            pelicula.setPelicula(estado.getString("pelicula"));
            pelicula.setUltimas15(estado.getStringArrayList("ultimas15"));
            pelicula.setPeliculaYaJugada(estado.getStringArrayList("peliculaYaJugada"));
            pelicula.setCantidadDeVecesQuePediUnaPelicula(estado.getInt("cantidadDeVecesQuePediUnaPelicula"));

        }
        cambioDeEquipo = true;

    }

    public void Si(View v) {

        if(equipo1.isTurno()) {
            equipo1.acerto();
        }
        else{
            equipo2.acerto();
        }

        Intent acerto = new Intent(AcertoONo.this, MainActivity.class);
        intent(acerto);

        acerto.putExtra("acerto",true); //solo se utiliza para que no se vuelva a repetir la pelicula

        startActivity(acerto);
    }

    public void No(View v) {
        Intent noAcerto = new Intent(AcertoONo.this, MainActivity.class);
        intent(noAcerto);

        noAcerto.putExtra("acerto",true); //solo se utiliza para que no se vuelva a repetir la pelicula

        startActivity(noAcerto);
    }

    public void intent(Intent intent){
        intent.putExtra("equipo1",equipo1.getNombre());
        intent.putExtra("puntos1",equipo1.getPuntos());
        intent.putExtra("turno1",equipo1.isTurno());

        intent.putExtra("equipo2",equipo2.getNombre());
        intent.putExtra("puntos2",equipo2.getPuntos());
        intent.putExtra("turno2",equipo2.isTurno());

        intent.putExtra("pelicula",pelicula.getPeliculaString());

        intent.putExtra("ultimas15",pelicula.getUltimas15());
        intent.putExtra("peliculaYaJugada",pelicula.getPeliculaYaJugada());
        intent.putExtra("cantidadDeVecesQuePediUnaPelicula",pelicula.getCantidadDeVecesQuePediUnaPelicula());

    }


    //----------------------- No se puede usar boton atras ----------------------

    public void onBackPressed() {
    }

}
