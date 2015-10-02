package com.example.guelmis.ffap.signaling;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by mario on 09/25/15.
 */
public class JObjRequester {
    private class getJSONObject extends AsyncTask<String,JSONObject,JSONObject>
    {
        private JSONObject jsonobj;
        private List<NameValuePair> params;

        public getJSONObject(List<NameValuePair> paramlist){
            super();
            params = paramlist;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        public JSONObject getObj(String url) {
            return JSONParser.getJSONFromUrl(url, params);
        }

        @Override
        protected JSONObject doInBackground(String... args){
            jsonobj = getObj(args[0]);
            return jsonobj;
        }
    }

    private class postJSONObject extends AsyncTask<String,JSONObject,JSONObject>
    {
       // private JSONObject jsonobj;
        private List<NameValuePair> params;

        public postJSONObject(List<NameValuePair> paramlist){
            super();
            params = paramlist;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        public JSONObject postObj(String url) {
            return JSONParser.postJSONFromUrl(url, params);
        }

        @Override
        protected JSONObject doInBackground(String... args){
            //jsonobj =
            return postObj(args[0]);// jsonobj;
        }
    }

    public JSONObject get(String url, List<NameValuePair> paramlist) throws ExecutionException, InterruptedException {
        return new getJSONObject(paramlist).execute(url).get();
    }

    public JSONObject post(String url, List<NameValuePair> paramlist) throws ExecutionException, InterruptedException {
        return new postJSONObject(paramlist).execute(url).get();
    }
}
