package com.example.danielli.watsthat;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;

/**
 * Created by danielli on 11/5/17.
 */

public enum ImageRecognizer {
    SINGLETON;



    ClarifaiClient client = new ClarifaiBuilder("b134c18adbcf44ecb2bec2fa1d0abe3a")
            .buildSync();


    public String[] predictionResult(byte[] imageFile){
        List<ClarifaiOutput<Concept>> concepts= client.getDefaultModels().generalModel() // You can also do client.getModelByID("id") to get your custom models
                .predict()
                .withInputs(
                        ClarifaiInput.forImage(imageFile))
                .executeSync()
                .get();

        List<String> descriptions=new ArrayList<>();
        for (ClarifaiOutput<Concept> output: concepts){
            for(Concept concept:output.data()){
                    if(concept.value()>0.7){
                        descriptions.add(concept.name());
                    }
                }
        }

        String[] array=new String[descriptions.size()];


        return descriptions.toArray(array);
    }



}
