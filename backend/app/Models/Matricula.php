<?php


namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Matricula extends Model {
    protected $fillable = [
        'matricula', 'tipo'
    ];
    protected $table="matriculas";

    public $timestamps = false;
}
