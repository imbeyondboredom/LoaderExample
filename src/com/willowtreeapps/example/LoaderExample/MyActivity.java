package com.willowtreeapps.example.LoaderExample;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyActivity extends FragmentActivity implements LoaderCallbacks<ResultOrException<String,Exception>> {

    private static String PARAM_URL = "url";

    private int LOADER_ID = 3;
    private TextView textView;
    private ProgressBar progress;
    private Button button1;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        button1 = (Button)findViewById(R.id.button);
        progress = (ProgressBar)findViewById(R.id.progressBar);
        textView = (TextView)findViewById(R.id.textview);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyActivity.this.disableForLoading();
                Bundle bundle = new Bundle();
                bundle.putString(PARAM_URL,"http://www.google.com");
                MyActivity.this.getSupportLoaderManager().initLoader(LOADER_ID,bundle,MyActivity.this);
            }
        });

        Loader<Object> loader = getSupportLoaderManager().getLoader(LOADER_ID);
        if(loader != null && loader.isStarted())
        {
            disableForLoading();

            //Reconnect with the loader
            Bundle b = new Bundle();
            b.putString(PARAM_URL, savedInstanceState.getString(PARAM_URL));
            getSupportLoaderManager().initLoader(LOADER_ID, b, this);
        }
        else
        {
            progress.setVisibility(View.INVISIBLE);
        }
    }

    private void disableForLoading()
    {
        progress.setVisibility(View.VISIBLE);
        button1.setEnabled(false);
    }


    @Override
    public Loader<ResultOrException<String,Exception>> onCreateLoader(int i, Bundle bundle) {
        return new ExampleLoader(this,bundle.getString(PARAM_URL));
    }

    @Override
    public void onLoadFinished(Loader<ResultOrException<String, Exception>> resultOrExceptionLoader,
            ResultOrException<String, Exception> result)  {
        button1.setEnabled(true);
        progress.setVisibility(View.INVISIBLE);
        if(result.hasResult())
        {
            textView.setText((String)result.getResult());
        }
        else
        {
            textView.setText("Exception occured");
        }
    }

    @Override
    public void onLoaderReset(Loader<ResultOrException<String, Exception>> resultOrExceptionLoader) {
    }

    private static class ExampleLoader extends ExecutorServiceLoader<String>{

        String url;

        public ExampleLoader(Context context, String url) {
            super(context);
            this.url = url;
        }

        @Override
        protected ResultOrException<String, Exception> loadInBackground() {
            ResultOrException<String,Exception> result;
            try
            {
                for(int a=0; a<4; a++)
                {
                    Thread.sleep(1000,0);
                }
                result = new ResultOrException<String,Exception>("I am done processing the request");
            }
            catch(Exception e)
            {
                result= new ResultOrException<String,Exception>(e);
            }
            return result;
        }
    }
}
