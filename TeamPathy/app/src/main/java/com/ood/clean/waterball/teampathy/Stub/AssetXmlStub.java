package com.ood.clean.waterball.teampathy.Stub;

import android.content.Context;
import android.content.res.AssetManager;

import com.ood.clean.waterball.teampathy.MyUtils.FileParser;

import java.io.IOException;
import java.io.InputStream;

public class AssetXmlStub {
    private static String xml;

    public static String getXml(Context context) throws IOException {
        if (xml == null)
        {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("testwbs.xml");
            xml = FileParser.readInputStream(inputStream);
        }
        return xml;
    }


}
