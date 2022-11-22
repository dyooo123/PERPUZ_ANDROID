<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Validation\Rule;
use Validator;
use App\Models\Peminjam;

class PeminjamController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        $dataPeminjam = Peminjam::all();

        if(count($dataPeminjam) > 0){
        return response([
                'message' => 'Retrieve All Success',
                'data' => $dataPeminjam
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
        $storeData = $request->all();
        $validate = Validator::make($storeData, [
            'nama' => 'required',
            'alamat' => 'required',
            'judulBukuPinjaman' => 'required',
            'tanggalPinjam' => 'required',
            'tanggalKembali' => 'required',
        ]);

        if($validate->fails()) 
            return response(['message' => $validate->errors()], 400);
        
        $peminjam = Peminjam::create($storeData);
        
        return response([
            'message' => 'Add Peminjam Success',
            'data' => $peminjam
        ],200);
    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        $dataPeminjam = Peminjam::find($id);
        if(!is_null($dataPeminjam)){
            return response([
                'message' => 'Retrieve Peminjam Success',
                'data' => $dataPeminjam
            ], 200);
        }

        return response([
            'message' => 'Peminjam Not Found',
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
        $dataPeminjam = Peminjam::find($id);

        if(is_null($dataPeminjam)){
            return response([
                'message' => 'Peminjam Not Found',
                'data' => null
            ], 404);
        }

        $updateDataUser = $request->all();
        $validate = Validator::make($updateDataUser, [
            'nama' => 'required',
            'alamat' => 'required',
            'judulBukuPinjaman' => 'required',
            'tanggalPinjam' => 'required',
            'tanggalKembali' => 'required',

        ]);

        if($validate->fails()) 
            return response(['message' => $validate->errors()], 400);

        $dataPeminjam->nama = $updateDataUser['nama'];
        $dataPeminjam->alamat = $updateDataUser['alamat'];
        $dataPeminjam->judulBukuPinjaman = $updateDataUser['judulBukuPinjaman'];
        $dataPeminjam->tanggalPinjam = $updateDataUser['tanggalPinjam'];
        $dataPeminjam->tanggalKembali = $updateDataUser['tanggalKembali'];


        if($dataPeminjam->save()){
             return response([
                'message'=> 'Update Peminjam Success',
                'data' => $dataPeminjam
             ], 200);
        }

        return response([
            'message'=> 'Update Peminjam Failed',
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
        $dataPeminjam = Peminjam::find($id);

        if(is_null($dataPeminjam)){
            return response([
                'message' => 'Peminjam Not Found',
                'data' => null
            ], 404);
        }

        if($dataPeminjam->delete()){
            return response([
                'message' => 'Delete Peminjam Success',
                'data' => $dataPeminjam
            ], 200);
        }

        return response([
            'message' => 'Delete Peminjam Failed',
            'data' => null
        ], 400);
    }
}
