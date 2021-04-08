<?php


namespace App\Models;


use Illuminate\Database\Eloquent\Model;

class Persona extends Model {
    protected $fillable = [
        'nombre', 'apellidos', 'dni', 'url_img'
    ];
    protected $table="personas";

    public $timestamps = false;
}
