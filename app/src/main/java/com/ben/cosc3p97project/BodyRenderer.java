package com.ben.cosc3p97project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.EGLConfig;
import android.util.Log;

import javax.microedition.khronos.opengles.GL10;

import rajawali.BaseObject3D;
import rajawali.lights.DirectionalLight;
import rajawali.materials.DiffuseMaterial;
import rajawali.materials.TextureInfo;
import rajawali.parser.AParser;
import rajawali.parser.ObjParser;
import rajawali.primitives.Sphere;
import rajawali.renderer.RajawaliRenderer;


/**
 * Created by Ben on 11/29/2015.
 */
public class BodyRenderer extends RajawaliRenderer {

    private DirectionalLight mLight;
    private BaseObject3D mBody;

    public BodyRenderer(Context context){
        super(context);
        setFrameRate(60);
    }


    public void initScene() {
		mLight = new DirectionalLight(1f, 0.2f, -1.0f); // set the direction
		mLight.setColor(1.0f, 1.0f, 1.0f);
		mLight.setPower(2);

        /*
		Bitmap bg = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.earthtruecolor_nasa_big);
		DiffuseMaterial material = new DiffuseMaterial();
		mSphere = new Sphere(1, 18, 18);
		mSphere.setMaterial(material);
		mSphere.addLight(mLight);
		mSphere.addTexture(mTextureManager.addTexture(bg));
		addChild(mSphere);
		*/

        ObjParser objParser = new ObjParser(mContext.getResources(), mTextureManager, R.raw.male_figure_obj);
        try{
            objParser.parse();
            mBody = objParser.getParsedObject();
            mBody.setLight(mLight);
            addChild(mBody);
        }catch(AParser.ParsingException e){
            Log.v("custom print", e.getMessage());
        }


		mCamera.setZ(4.2f);
    }

    public void onDrawFrame(GL10 glUnused) {
        super.onDrawFrame(glUnused);
        mBody.setRotY(mBody.getRotY() + 1);
    }
}
