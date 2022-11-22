<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Validator;
use App\Models\Profil;
use Illuminate\Support\Facades\Session;
use Illuminate\Support\Facades\Hash;

class AuthController extends Controller
{
    
    public function register(Request $request)
    {
        $dataRegist = $request->all();
        $validate = Validator::make($dataRegist, [
            'username' => 'required',
            'email' => 'required',
            'password' => 'required',
            'tanggalLahir' => 'required',
            'notelp' => 'required',
        ]);

        if($validate->fails()) 
            return response(['message' => $validate->errors()], 400);

    
        
        $profil = Profil::create($dataRegist);
        return response([
            'message' => 'Create Profile Success',
            'data' => $profil
        ],200);
    }

    public function loginCheck(Request $request)
    {
        $loginData = $request->all();

        $validate = Validator::make($loginData,[
            'username' => 'required',
            'password' => 'required'
        ]);

        if($validate->fails())
            return response(['message' => $validate->errors()],400);

        $checkUser = Profil::where('username',$loginData["username"])->where('password',$loginData["password"])->exists();

        if($checkUser) {
            return response([
                'message' => 'User Found',
                'data' => Profil::where('username',$loginData["username"])->where('password',$loginData["password"])->first()
            ],200);
        }

        return response([
            'message' => 'User not found',
            'data' => null
        ],400);
    }

    public function logout(Request $request)
    {
        $user = Auth::user()->token();
        $user->revoke();

        return response()->json([
            'message' => 'Logout Success',
        ], 200);
    }
   
}
