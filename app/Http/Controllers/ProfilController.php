<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Validation\Rule;
use Validator;
use App\Models\Profil;

class ProfilController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        $profil = Profil::all();

        if(count($profil) > 0){
        return response([
                'message' => 'Retrieve All Success',
                'data' => $profil
            ], 200);
        }

        return response([
            'message' => 'Empty',
            'data' => null
        ], 400);
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        
    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        $profil = Profil::find($id);
        if(!is_null($profil)){
            return response([
                'message' => 'Retrieve Profile Success',
                'data' => $profil
            ], 200);
        }

        return response([
            'message' => 'Profile Not Found',
            'data' => null
        ],404);
    
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function edit($id)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        $profil = Profil::find($id);

        if(is_null($profil)){
            return response([
                'message' => 'User Not Found',
                'data' => null
            ], 404);
        }

        $updateDataUser = $request->all();
        $validate = Validator::make($updateDataUser, [
            'username' => 'required',
            'email' => 'required',
            'password' => 'required',
            'tanggalLahir' => 'required',
            'notelp' => 'required',


        ]);

        if($validate->fails()) 
            return response(['message' => $validate->errors()], 400);

        $profil->username = $updateDataUser['username'];
        $profil->email = $updateDataUser['email'];
        $profil->password = $updateDataUser['password'];
        $profil->tanggalLahir = $updateDataUser['tanggalLahir'];
        $profil->notelp = $updateDataUser['notelp'];


        if($profil->save()){
             return response([
                'message'=> 'Update Profile Success',
                'data' => $profil
             ], 200);
        }

        return response([
            'message'=> 'Update Profile Failed',
            'data' => null
        ], 400);
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        $profil = Profil::find($id);

        if(is_null($profil)){
            return response([
                'message' => 'Profile Not Found',
                'data' => null
            ], 404);
        }

        if($profil->delete()){
            return response([
                'message' => 'Delete Profile Success',
                'data' => $profil
            ], 200);
        }

        return response([
            'message' => 'Delete Profile Failed',
            'data' => null
        ], 400);
    }
    
}
