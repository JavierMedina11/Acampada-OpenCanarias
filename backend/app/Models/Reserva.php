<?php


namespace App\Models;


use Illuminate\Database\Eloquent\Model;

class Reserva extends Model {
    public $timestamps = false;
    protected $fillable = [
        'id_persona','fecha_entrada','fecha_salida','localizador_reserva','num_personas','num_vehiculos','checkin','fecha_checkin', 'id_zona'
        ];
    protected $table="reservas";
}
