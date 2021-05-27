<?php


namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Matricula extends Model {
    protected $fillable = [
        'matricula', 'tipo', 'localizador', 'estado'
    ];
    protected $table="matriculas";

    public $timestamps = false;
}
