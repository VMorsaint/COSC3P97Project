package com.ben.cosc3p97project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private float yRotation, xRotation;

    public BodyRenderer(Context context){
        super(context);
        setFrameRate(60);
    }


    public void initScene() {
		mLight = new DirectionalLight(1f, 0.2f, -1.0f); // set the direction
		mLight.setColor(1.0f, 1.0f, 1.0f);
		mLight.setPower(2);

        ObjParser objParser = new ObjParser(mContext.getResources(), mTextureManager, R.raw.male_figure_obj);
        try{
            objParser.parse();
            mBody = objParser.getParsedObject();
            mBody.addLight(mLight);
            mBody.setY(-10);
            addChild(mBody);
        }catch(AParser.ParsingException e){
            Log.v("custom print", e.getMessage());
        }

        DiffuseMaterial material = new DiffuseMaterial();
        material.addTexture(new TextureInfo(R.drawable.earthtruecolor_nasa_big));
        Sphere sp = new Sphere(1, 24, 24);
        sp.setMaterial(material);
        sp.addLight(mLight);
        addChild(sp);

		mCamera.setZ(35f);
        mCamera.setY(6f);
    }

    public void onDrawFrame(GL10 glUnused) {
        super.onDrawFrame(glUnused);
    }

    public void setRotation(float x, float y){
        mBody.setRotX(mBody.getRotX() + x);
        mBody.setRotY(mBody.getRotY() + y);
    }
}
