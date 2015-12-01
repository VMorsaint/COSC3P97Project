package com.ben.cosc3p97project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.EGLConfig;

import javax.microedition.khronos.opengles.GL10;

import rajawali.lights.DirectionalLight;
import rajawali.materials.DiffuseMaterial;
import rajawali.materials.TextureInfo;
import rajawali.primitives.Sphere;
import rajawali.renderer.RajawaliRenderer;


/**
 * Created by Ben on 11/29/2015.
 */
public class BodyRenderer extends RajawaliRenderer {

    private DirectionalLight mLight;
    private Sphere mSphere;

    public BodyRenderer(Context context){
        super(context);
        setFrameRate(60);
    }


    public void initScene() {
		mLight = new DirectionalLight(1f, 0.2f, -1.0f); // set the direction
		mLight.setColor(1.0f, 1.0f, 1.0f);
		mLight.setPower(2);

		Bitmap bg = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.earthtruecolor_nasa_big);
		DiffuseMaterial material = new DiffuseMaterial();
		mSphere = new Sphere(1, 18, 18);
		mSphere.setMaterial(material);
		mSphere.addLight(mLight);
		mSphere.addTexture(mTextureManager.addTexture(bg));
		addChild(mSphere);

		mCamera.setZ(4.2f);
    }

    public void onDrawFrame(GL10 glUnused) {
        super.onDrawFrame(glUnused);
        mSphere.setRotY(mSphere.getRotY() + 1);
    }
}
