<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class Reserva extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('reservas', function (Blueprint $table) {
            $table->id('id');
            $table->unsignedBigInteger('id_persona')->nullable()->unsigned();
            $table->string('dni_persona');
            $table->string('fecha_entrada');
            $table->string('fecha_salida');
            $table->string('localizador_reserva');
            $table->integer('num_personas'); // deberia ser una lista de personas , probar con string largo
            $table->json('acompanantes');
            $table->integer('num_vehiculos');
            $table->integer('num_casetas');
            $table->integer('num_bus');
            $table->integer('num_caravanas');
            $table->string('checkin');
            $table->string('fecha_checkin');
            $table->string('incidencia');  //mirar bien este campo preguntar
            $table->json('incidencias'); // otra lista , probar con string largo, o con una tabla de incidencias aparte
            $table->string('estado');
            $table->unsignedBigInteger("id_zona");
            $table->foreign("id_zona")->references('id')->on('zonas');
            $table->foreign('id_persona')->references('id')->on('personas')->cascadeOnDelete();
            $table->foreign('dni_persona')->references('dni')->on('personas')->cascadeOnDelete();
        });
    }
    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        //
    }
}
