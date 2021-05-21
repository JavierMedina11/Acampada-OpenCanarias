<?php


namespace App\Models;


use Illuminate\Database\Eloquent\Model;

class Reserva extends Model {
    public $timestamps = false;
    protected $casts = [
        'acompanantes' => 'array',
        'matriculas' => 'array',
        'incidencias' => 'array'
    ];
    protected $fillable = [
        'id_persona','dni_persona','fecha_entrada','fecha_salida',
        'localizador_reserva','num_personas','acompanantes','num_vehiculos',
        'num_casetas','num_bus','num_caravanas', 'matriculas','checkin','fecha_checkin',
        'incidencia','incidencias','estado','id_zona'
        ];
    protected $table="reservas";
}
