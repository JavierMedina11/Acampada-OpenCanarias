<?php


namespace App\Http\Controllers;


use App\Models\Zona;
use Illuminate\Http\Request;


class ZonaController extends Controller{

    //GETTER DE TODA LA VIDA
    public function index(){
        return Zona::all();
    }

    //GETTER DE UN SOLO ELEMENTO
    public function show($id){
        return Zona::findOrFail($id);
    }

    //POST/*
    public function createPost(Request $request){
        $zona = new Zona();
        $zona->nombre = $request->nombre;
        $zona->localizacion = $request->localizacion;
        $zona->url_img = $request->url_img;
        $zona->save();
        return "Post has been created!";
    }


    //PUT
    public function updatePost(Request $request){
        $zona = Zona::where('id', $request->id)->first();
        $zona->nombre = $request->nombre;
        $zona->localizacion = $request->localizacion;
        $zona->url_img = $request->url_img;
        $zona->save();
        return "Post has been updated!";
    }

    //DELETE
    public function delete($id){
        Zona::destroy($id);
        return response()->json([
            'res' => true,
            'message' => 'Registro ELIMINADO de la vida correctamente'
        ]);
    }


}
