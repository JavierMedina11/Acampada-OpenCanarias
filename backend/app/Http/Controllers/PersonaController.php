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
        $persona->apellidos = $request->apellidos;
        $persona->dni = $request->dni;
        $persona->url_img = $request->url_img;
        $persona->save();
        return "Post has been created!";
    }


    //PUT
    public function updatePost(Request $request){
        $persona = Persona::where('id', $request->id)->first();
        $persona->nombre = $request->nombre;
        $persona->apellidos = $request->apellidos;
        $persona->dni = $request->dni;
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
