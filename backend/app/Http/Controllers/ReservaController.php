<?php


namespace App\Http\Controllers;

use App\Models\Reserva;
use Illuminate\Http\Request;

class ReservaController {

    //GETTER DE TODA LA VIDA
    public function index(Request $request){
        return Reserva::all();
    }

    //GETTER DE UN SOLO ELEMENTO
    public function show($id){
        return Reserva::findOrFail($id);
    }
/*
    public function showReservZone($id_zona){
        return Reserva::all()->where('id_zona', $id_zona);;
    }*/

    public function showReservZoneDate($id_zona, $date_picker){
        return Reserva::all()
            ->where('id_zona', $id_zona)
            ->where('fecha_entrada', $date_picker)->values();
    }

    public function showReservLocalizador($localizador){
        return Reserva::all()
            ->where('localizador_reserva', $localizador)->values();
    }

    //POST/*
    public function createPost(Request $request){
        $zona = new Reserva();
        $zona->id_persona = $request->id_persona;
        $zona->fecha_entrada = $request->fecha_entrada;
        $zona->fecha_salida = $request->fecha_salida;
        $zona->localizador_reserva = $request->localizador_reserva;
        $zona->num_personas = $request->num_personas;
        $zona->num_vehiculos = $request->num_vehiculos;
        $zona->checkin = $request->checkin;
        $zona->fecha_checkin = $request->fecha_checkin;
        $zona->id_zona = $request->id_zona;
        $zona->save();
        return "Post has been created!";
    }


    //PUT
    public function updatePost(Request $request){
        $zona = Reserva::where('id', $request->id)->first();
        $zona->id_persona = $request->id_persona;
        $zona->fecha_entrada = $request->fecha_entrada;
        $zona->fecha_salida = $request->fecha_salida;
        $zona->localizador_reserva = $request->localizador_reserva;
        $zona->num_personas = $request->num_personas;
        $zona->num_vehiculos = $request->num_vehiculos;
        $zona->checkin = $request->checkin;
        $zona->fecha_checkin = $request->fecha_checkin;
        $zona->id_zona = $request->id_zona;
        $zona->save();
        return "Post has been updated!";
    }

    //DELETE
    public function delete($id){
        Reserva::destroy($id);
        return response()->json([
            'res' => true,
            'message' => 'Registro ELIMINADO de la vida correctamente'
        ]);
    }

}
