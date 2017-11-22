package com.example.danielli.watsthat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class GetPhoto extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE=1;
    static final int REQUEST_IMAGE_GET = 1;
    private ImageView photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_photo);
        photo=(ImageView) findViewById(R.id.tv_photo);
    }

    public void onClickTakePhotoButton(View v){
        Intent takePictureIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
        }
    }

    public void onClickLoadPhotoButton(View v){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }


    public void onClickGetInfoButton(View v){
        Bitmap bitmap = ((BitmapDrawable)photo.getDrawable()).getBitmap();
        new getImageDescriptiors().execute(convertBitmap(bitmap));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_IMAGE_CAPTURE&&resultCode==RESULT_OK){
            Bundle extras=data.getExtras();
            Bitmap imageBitMap=(Bitmap)extras.get("data");
            photo.setImageBitmap(imageBitMap);
        }
        /*
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            Bitmap thumbnail = data.getParcelable("data");
        }*/
    }

    private byte[] convertBitmap(Bitmap map){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        map.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private class getImageDescriptiors extends AsyncTask<byte[],Void,String[]>{
        @Override
        protected String[] doInBackground(byte[]... bytes) {
            return ImageRecognizer.SINGLETON.predictionResult(bytes[0]);
        }

        @Override
        protected void onPostExecute(String[] strings) {
            for(String string: strings){
                System.out.println(string);
            }
            System.out.println(strings.length);
            Intent showInfo=new Intent(GetPhoto.this,ItemInfo.class);
            showInfo.putExtra("tags",strings);
            startActivity(showInfo);
        }
    }
}
