<?php


namespace App\Models;


use Illuminate\Database\Eloquent\Model;

class Persona extends Model {
    protected $fillable = [
        'nombre', 'apellido1','apellido2','tipo_documento','dni','fecha_nacimiento','mail','direccion','telefono', 'url_img'
    ];
    protected $table="personas";

    public $timestamps = false;
}
