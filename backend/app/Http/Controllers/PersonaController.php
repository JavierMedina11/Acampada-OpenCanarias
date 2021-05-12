<?php


namespace App\Http\Controllers;

use App\Models\Persona;
use Illuminate\Http\Request;


class PersonaController extends Controller{
//GETTER DE TODA LA VIDA
    public function index(Request $request){
        return Persona::all();
    }

    //GETTER DE UN SOLO ELEMENTO
    public function show($id){
        return Persona::findOrFail($id);
    }

    //POST/*
    public function createPost(Request $request){
        $persona = new Persona();
        $persona->nombre = $request->nombre;
        $persona->apellido1 = $request->apellido1;
        $persona->apellido2 = $request->apellido2;
        $persona->tipo_documento = $request->tipo_documento;
        $persona->dni = $request->dni;
        $persona->fecha_nacimiento = $request->fecha_nacimiento;
        $persona->mail = $request->mail;
        $persona->direccion = $request->direccion;
        $persona->telefono = $request->telefono;
        $persona->url_img = $request->url_img;
        $persona->save();
        return "Post has been created!";
    }


    //PUT
    public function updatePost(Request $request){
        $persona = Persona::where('id', $request->id)->first();
        $persona->nombre = $request->nombre;
        $persona->apellido1 = $request->apellido1;
        $persona->apellido2 = $request->apellido2;
        $persona->tipo_documento = $request->tipo_documento;
        $persona->dni = $request->dni;
        $persona->fecha_nacimiento = $request->fecha_nacimiento;
        $persona->mail = $request->mail;
        $persona->direccion = $request->direccion;
        $persona->telefono = $request->telefono;
        $persona->url_img = $request->url_img;
        $persona->save();
        return "Post has been updated!";
    }

    //DELETE
    public function delete($id){
        Persona::destroy($id);
        return response()->json([
            'res' => true,
            'message' => 'Registro ELIMINADO de la vida correctamente'
        ]);
    }
}
