<?php

use App\Http\Controllers\PersonaController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\ZonaController;
use App\Http\Controllers\MatriculaController;
use App\Http\Controllers\ReservaController;
use App\Http\Controllers\OperarioController;
use PHPJasper\PHPJasper;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/
//php -S 192.168.1.129:8000 -t ./public
Route::middleware('auth:api')->get('/user', function (Request $request) {
    return $request->user();
});

//  ----- LOG IN -----
Route::post('/signin', [OperarioController::class, 'signIn']);
Route::post('/login', [OperarioController::class, 'logIn']);
Route::post('/logout', [OperarioController::class, 'logOut']);

//  ----- OPERARIOS -----
Route::get('/operarios', [OperarioController::class, 'index']);
Route::get('/operario-id/{id}', [OperarioController::class, 'getById']);
Route::delete('/delete-operario/{id}', [OperarioController::class, 'delete']);
Route::put('/update-operario/{id}', [OperarioController::class, 'update']);

//Route::group(['middleware' => 'auth:api'], function(){

    //  ----- ZONAS -----
    Route::get('/zona', [ZonaController::class, 'index']);
    Route::get('/zona/{id}', [ZonaController::class, 'show']);
    Route::delete('/delete-zona/{id}', [ZonaController::class, 'delete']);
    Route::put('/update-zona/{id}', [ZonaController::class, 'updatePost']);
    Route::post('/add-zona', [ZonaController::class, 'createPost']);

    //  ----- MATRICULAS -----
    Route::get('/matriculas', [MatriculaController::class, 'index']);
    Route::get('/matriculas/{id}', [MatriculaController::class, 'show']);
    Route::delete('/delete-matricula/{id}', [MatriculaController::class, 'delete']);
    Route::put('/update-matricula/{id}', [MatriculaController::class, 'updatePost']);
    Route::post('/add-matricula', [MatriculaController::class, 'createPost']);


//  ----- PERSONAS -----
    Route::get('/persona', [PersonaController::class, 'index']);
    Route::get('/persona/{id}', [PersonaController::class, 'show']);
    Route::delete('/persona/{id}', [PersonaController::class, 'delete']);
    Route::put('/persona/{id}', [PersonaController::class, 'updatePost']);
    Route::post('/persona', [PersonaController::class, 'createPost']);

//  ----- RESERVAS -----
    Route::get('/reserva', [ReservaController::class, 'index']);
    Route::get('/reserva-no-check', [ReservaController::class, 'showReserNotChecked']);
    Route::get('/reserva-check', [ReservaController::class, 'showReserChecked']);
    Route::get('/reserva/{id}', [ReservaController::class, 'show']);
    Route::get('/reserva_local/{localizador}', [ReservaController::class, 'showReservLocalizador']);
    Route::get('/reserva_zone/{id_zona}/{date_picker}', [ReservaController::class, 'showReservZoneDate']);
    Route::get('/reserva_zone_date/{date_picker}', [ReservaController::class, 'showReservDate']);
    Route::delete('/reserva/{id}', [ReservaController::class, 'delete']);
    Route::put('/reservaUpdate/{id}', [ReservaController::class, 'updatePost']);
    Route::put('/reservaUpdate-checkin/{id}', [ReservaController::class, 'updateCheck']);
    Route::post('/reservaCreate', [ReservaController::class, 'createPost']);

//});

Route::get('/compilarReporteParametros', function () {
    $input = base_path() .
        '/public/reports/zonasAca.jrxml';

    $jasper = new PHPJasper;
    $jasper->compile($input)->execute();

    return response()->json([
        'status' => 'ok',
        'msj' => '??Reporte compilado!'
    ]);
});

Route::get('/reporteParametros', function () {
    $input = base_path() .
        '/public/reports/zonasAca.jasper';
    $output = base_path() .
        '/public/reports';
    $options = [
        'format' => ['pdf'],
        'params' => [],
        'db_connection' => [
            'driver' => 'mysql', //mysql, ....
            'username' => 'root',
            'password' => 'root',
            'host' => '127.0.0.1',
            'database' => 'acampadasOC2',
            'port' => '3306'
        ]
    ];

    $jasper = new PHPJasper;

    $jasper->process(
        $input,
        $output,
        $options
    )->execute();

    $pathToFile = base_path() .
        '/public/reports/zonasAca.pdf';
    return response()->file($pathToFile);
});
