<?php


namespace App\Http\Controllers;


use App\Models\Operario;
use Illuminate\Support\Facades\Hash;
use Illuminate\Http\Request;
use Illuminate\Support\Str;
use Illuminate\Validation\ValidationException;

class OperarioController extends Controller
{
    public function signIn(Request $request){
        try {
            $this->validate($request, [
                'email' => "required|Regex:'^[^@]+@[^@]+\.[a-zA-Z]{2,}$'",
                'dni' => 'required|Regex:/^\d{8}[A-Z]{1}/'
            ]);
        } catch (ValidationException $e) {
            return response()->json([
                'res'=>false,
                'message'=>'Check email or DNI'
            ]);
        }
        $input = $request ->all();
        $input['password'] = Hash::make($request->password);
        if ($request->has("url_img"))
            $input["url_img"] = $this->loadImage($request->url_img);
        Operario::create($input);

        return response()->json([
            'res'=>true,
            'message'=>'Registro creado correctamente'
        ]);
    }

    public function logIn(Request $request){
        $user = Operario::whereEmail($request->email)->first();

        if(!is_null($user) && Hash::check($request->password, $user->password)){
            $user->api_token = Str::random(150);
            $user->save();

            return response()->json([
                'res'=>true,
                'id_operario'=>$user->id,
                'api_token'=>$user->api_token,
                'message'=>'Ha accedido correctamente'
            ]);
        }else{
            return response()->json([
                'res'=>false,
                'message'=>'Email o contraseña incorrecta'
            ]);
        }
    }

    public function logOut(){
        $op = auth()->user();
        $op->api_token = null;
        $op->save();

        return response()->json([
            'res'=>true,
            'message'=>'Ha salido correctamente'
        ]);
    }


    public function getById($id){
        return Operario::where('id',$id)->get();
    }

    public function update($id, Request $request){
        try {
            $this->validate($request, [
                'email' => "required|Regex:'^[^@]+@[^@]+\.[a-zA-Z]{2,}$'",
                'dni' => 'required|Regex:/^\d{8}[A-Z]{1}/'
            ]);
        } catch (ValidationException $e) {
            return response()->json([
                'res'=>false,
                'message'=>'Check email or DNI'
            ]);
        }
        $input = $request ->all();
        if ($request->has("url_img"))
            $input["url_img"] = $this->loadImage($request->url_image);

        $user = Operario::where('id',$id);
        $user->update($input);
        return response()->json([
            'res'=>true,
            'message'=>'Ususario modificado correctamente'
        ]);
    }


    //DELETE
    public function delete($id){
        $user = Operario::where('id',$id);
        $user->delete();
        return response()->json([
            'res'=>true,
            'message'=>'Usuario eliminado correctamente'
        ]);
    }

    public function loadImage($file){
        $photoName = time().".". $file->getClientOriginalExtension();
        $file->move(base_path("/public/operario_image"),$photoName);
        return $photoName;
    }

}
