package com.massacre.codigotutoria.aysnctask;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.massacre.codigotutoria.utils.CodigoTutoriaConstant;
import com.massacre.codigotutoria.utils.ConfigUtils;
import com.massacre.codigotutoria.utils.LocalStorage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileStore;

/**
 * Created by saurabh on 9/9/17.
 */

public class LoadStaticFiles extends AsyncTask<Context,Void,Void> {

    @Override
    protected Void doInBackground(Context... contexts) {
        Context context=contexts[0];
        try {
            ConfigUtils configUtils=new ConfigUtils();
            LocalStorage localStorage=new LocalStorage();
            if(!LocalStorage.isFileAvailable(
                    CodigoTutoriaConstant.STATIC_FILES_ANGULAR_MATERIAL,
                    CodigoTutoriaConstant.LOCAL_STATIC,
                    context)) {
                String angular=configUtils.getContentFromAssets(CodigoTutoriaConstant.STATIC_FILES_ANGULAR_MATERIAL,CodigoTutoriaConstant.LOCAL_STATIC,context);
                localStorage.saveFile(angular,
                        CodigoTutoriaConstant.STATIC_FILES_ANGULAR_MATERIAL, CodigoTutoriaConstant.LOCAL_STATIC, context);
            }

            if(!LocalStorage.isFileAvailable(
                    CodigoTutoriaConstant.STATIC_FILES_ANIMATE,
                    CodigoTutoriaConstant.LOCAL_STATIC,
                    context)) {
                String animate=configUtils.getContentFromAssets(CodigoTutoriaConstant.STATIC_FILES_ANIMATE,CodigoTutoriaConstant.LOCAL_STATIC,context);
                localStorage.saveFile(animate,
                        CodigoTutoriaConstant.STATIC_FILES_ANIMATE,
                        CodigoTutoriaConstant.LOCAL_STATIC,
                        context);
            }
            if(!LocalStorage.isFileAvailable(
                    CodigoTutoriaConstant.STATIC_FILES_JQUERY,
                    CodigoTutoriaConstant.LOCAL_STATIC,
                    context)) {
                String jquery=configUtils.getContentFromAssets(CodigoTutoriaConstant.STATIC_FILES_JQUERY,CodigoTutoriaConstant.LOCAL_STATIC,context);
                localStorage.saveFile(jquery,
                        CodigoTutoriaConstant.STATIC_FILES_JQUERY,
                        CodigoTutoriaConstant.LOCAL_STATIC,
                        context);
            }
            if(!LocalStorage.isFileAvailable(
                    CodigoTutoriaConstant.STATIC_FILES_MYSTYLE,
                    CodigoTutoriaConstant.LOCAL_STATIC,
                    context)) {
                String myStyle=configUtils.getContentFromAssets(CodigoTutoriaConstant.STATIC_FILES_MYSTYLE,CodigoTutoriaConstant.LOCAL_STATIC,context);
                localStorage.saveFile(myStyle,
                        CodigoTutoriaConstant.STATIC_FILES_MYSTYLE,
                        CodigoTutoriaConstant.LOCAL_STATIC,
                        context);
            }
            if(!LocalStorage.isFileAvailable(
                    CodigoTutoriaConstant.STATIC_FILES_MYSCRIPT,
                    CodigoTutoriaConstant.LOCAL_STATIC,
                    context)
                    ) {
                String myScript=configUtils.getContentFromAssets(CodigoTutoriaConstant.STATIC_FILES_MYSCRIPT,CodigoTutoriaConstant.LOCAL_STATIC,context);
                localStorage.saveFile(myScript,

                        CodigoTutoriaConstant.STATIC_FILES_MYSCRIPT,
                        CodigoTutoriaConstant.LOCAL_STATIC,
                        context);
            }
            if(!LocalStorage.isFileAvailable(
                    CodigoTutoriaConstant.STATIC_FILES_PRETTYPRINT,
                    CodigoTutoriaConstant.LOCAL_STATIC,
                    context)
                    ) {
                String myScript=configUtils.getContentFromAssets(CodigoTutoriaConstant.STATIC_FILES_PRETTYPRINT,CodigoTutoriaConstant.LOCAL_STATIC,context);
                localStorage.saveFile(myScript,

                        CodigoTutoriaConstant.STATIC_FILES_PRETTYPRINT,
                        CodigoTutoriaConstant.LOCAL_STATIC,
                        context);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
