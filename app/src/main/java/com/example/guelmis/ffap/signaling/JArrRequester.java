package com.example.guelmis.ffap.signaling;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class JArrRequester {

    private class getJSONArray extends AsyncTask<String,JSONArray,JSONArray>
    {
        private JSONArray jsonArr1;
        private List<NameValuePair> params;

        public getJSONArray(List<NameValuePair> paramlist){
            super();
            params = paramlist;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        public JSONArray getArray(String url) {
            return JSONParser.getJSONArrFromUrl(url, params);
        }

        @Override
        protected JSONArray doInBackground(String... args){
            jsonArr1 = getArray(args[0]);
            return jsonArr1;
        }
    }

    private class postJSONArray extends AsyncTask<String,JSONArray,JSONArray>
    {
        private JSONArray jsonArr1;
        private List<NameValuePair> params;

        public postJSONArray(List<NameValuePair> paramlist){
            super();
            params = paramlist;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        public JSONArray getArray(String url) {
            return JSONParser.postJSONArrFromUrl(url, params);
        }

        @Override
        protected JSONArray doInBackground(String... args){
            jsonArr1 = getArray(args[0]);
            return jsonArr1;
        }
    }

    public JSONArray get(String url, List<NameValuePair> paramlist) throws ExecutionException, InterruptedException {
        return new getJSONArray(paramlist).execute(url).get();
    }

    public JSONArray post(String url, List<NameValuePair> paramlist) throws ExecutionException, InterruptedException {
        return new postJSONArray(paramlist).execute(url).get();
    }
}
