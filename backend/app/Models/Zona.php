<?php


namespace App\Models;


use Illuminate\Database\Eloquent\Model;

class Zona extends Model {
    protected $fillable = [
        'nombre','localizacion'
    ];
    protected $table="zonas";

    public $timestamps = false;
}
