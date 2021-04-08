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
            $table->string('fecha_entrada');
            $table->string('fecha_salida');
            $table->string('localizador_reserva');
            $table->integer('num_personas');
            $table->integer('num_vehiculos');
            $table->string('checkin');
            $table->string('fecha_checkin');
            $table->unsignedBigInteger("id_zona");
            $table->foreign("id_zona")->references('id')->on('zonas');
            $table->foreign('id_persona')->references('id')->on('personas')->cascadeOnDelete();
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
