<?php


namespace App\Models;


use Illuminate\Database\Eloquent\Model;

class Zona extends Model {
    protected $fillable = [
        'nombre'
    ];
    protected $table="zonas";

    public $timestamps = false;
}
