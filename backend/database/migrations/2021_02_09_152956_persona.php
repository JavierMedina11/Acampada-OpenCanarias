<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class Persona extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('personas', function (Blueprint $table) {
            $table->id('id');
            $table->string('nombre');
            $table->string('apellido1');
            $table->string('apellido2');
            $table->string('tipo_documento');
            $table->string('dni')->unique()->notNullable();
            $table->string('fecha_nacimiento');
            $table->string('mail');
            $table->string('direccion');
            $table->string('telefono');
            $table->string('url_img');
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
