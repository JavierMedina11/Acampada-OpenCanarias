<?php


namespace App\Http\Controllers;


use App\Models\Matricula;
use Illuminate\Http\Request;

class MatriculaController extends Controller{

    //GETTER DE TODA LA VIDA
    public function index(){
        return Matricula::all();
    }

    //GETTER DE UN SOLO ELEMENTO
    public function show($id){
        return Matricula::findOrFail($id);
    }

    //POST/*
    public function createPost(Request $request){
        $matricula = new Matricula();
        $matricula->matricula = $request->matricula;
        $matricula->tipo = $request->tipo;
        $matricula->save();
        return "Post has been created!";
    }


    //PUT
    public function updatePost(Request $request){
        $matricula = Matricula::where('id', $request->id)->first();
        $matricula->matricula = $request->matricula;
        $matricula->tipo = $request->tipo;
        $matricula->save();
        return "Post has been updated!";
    }

    //DELETE
    public function delete($id){
        Matricula::destroy($id);
        return response()->json([
            'res' => true,
            'message' => 'Registro ELIMINADO de la vida correctamente'
        ]);
    }


}
