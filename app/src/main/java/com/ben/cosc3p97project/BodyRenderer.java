package com.ben.cosc3p97project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.widget.Toast;

import javax.microedition.khronos.opengles.GL10;

import rajawali.BaseObject3D;
import rajawali.lights.DirectionalLight;
import rajawali.materials.AMaterial;
import rajawali.materials.GouraudMaterial;
import rajawali.materials.SimpleMaterial;
import rajawali.materials.TextureInfo;
import rajawali.parser.AParser;
import rajawali.parser.ObjParser;
import rajawali.primitives.Cube;
import rajawali.primitives.Sphere;
import rajawali.renderer.RajawaliRenderer;
import rajawali.util.ObjectColorPicker;
import rajawali.util.OnObjectPickedListener;


/**
 * Created by Ben on 11/29/2015.
 */
public class BodyRenderer extends RajawaliRenderer{

    private DirectionalLight gLight;
    private BaseObject3D bodyModel, bodyGroup;
    private ObjectColorPicker objectPicker;
    private TextureInfo transparentTexture;

    /* body part click detectors */
    private BaseObject3D leftHand, rightHand,
            leftUpperArm, rightUpperArm,
            leftForeArm, rightForeArm,
            chest, abdomin, head,
            leftLeg, rightLeg,
            leftFoot, rightFoot,
            lowerBack, upperBack;

    public BodyRenderer(Context context) {
        super(context);
        setFrameRate(60);
    }

    //initialize the scene
    @Override
    public void initScene() {

        //contians all the shapes of the body
        bodyGroup = new BaseObject3D();

        //transparent texture for body part 
        transparentTexture = mTextureManager.addTexture(
                BitmapFactory.decodeResource(mContext.getResources(), R.drawable.transparent));

        //init the light
        gLight = new DirectionalLight(1f, 0.2f, -1.0f); // set the direction
        gLight.setColor(1.0f, 1.0f, 1.0f);
        gLight.setPower(2);

        //create object picker
        objectPicker = new ObjectColorPicker(this);

        //set activity as the listener to receive the body part clicked
        objectPicker.setOnObjectPickedListener((BodyActivity)getContext());

        //parse body from file
        ObjParser objParser = new ObjParser(mContext.getResources(), mTextureManager, R.raw.male_figure_obj);
        try {

            //parse the file
            objParser.parse();

            //get the new parsed object
            bodyModel = objParser.getParsedObject();

            //add some light to the object
            bodyModel.addLight(gLight);

        } catch (AParser.ParsingException e) {
            Log.e("Error Occured", e.getMessage());
            Toast.makeText(getContext(), "Error occrued loading the model", Toast.LENGTH_SHORT).show();
            ((BodyActivity) getContext()).finish();
        }
        /* touch detectors */

        //left hand
        leftHand = new Sphere(1, 24, 24);
        leftHand.setMaterial(new SimpleMaterial(AMaterial.ALPHA_MASKING));
        leftHand.addTexture(transparentTexture);
        leftHand.setName("LeftHand");
        leftHand.setPosition(7f, 1.3f, 0);

        //left foot
        leftFoot = new Sphere(1, 26, 24);
        leftFoot.setMaterial(new SimpleMaterial(AMaterial.ALPHA_MASKING));
        leftFoot.addTexture(transparentTexture);
        leftFoot.setName("LeftFoot");
        leftFoot.setPosition(1.25f, -10f, -0.5f);


        //right hand
        rightHand = new Sphere(1, 24, 24);
        rightHand.setMaterial(new SimpleMaterial(AMaterial.ALPHA_MASKING));
        rightHand.addTexture(transparentTexture);
        rightHand.setName("RightHand");
        rightHand.setPosition(-7f, 1f, 0);

        //right foot
        rightFoot = new Sphere(1, 24, 24);
        rightFoot.setMaterial(new SimpleMaterial(AMaterial.ALPHA_MASKING));
        rightFoot.addTexture(transparentTexture);
        rightFoot.setName("RightFoot");
        rightFoot.setPosition(-1.25f, -10f, -0.5f);

        //head 
        head = new Sphere(1, 35, 35);
        head.setMaterial(new SimpleMaterial(AMaterial.ALPHA_MASKING));
        head.addTexture(transparentTexture);
        head.setName("Head");
        head.setPosition(0f, 7f, -0.5f);

        chest = new Cube(1);
        chest.setMaterial(new SimpleMaterial(AMaterial.ALPHA_MASKING));
        chest.addTexture(transparentTexture);
        chest.setName("Chest");
        chest.setPosition(0, 4.15f, 0);
        chest.setScaleX(4.8f);
        chest.setScaleY(2.9f);

        abdomin = new Cube(1);
        abdomin.setMaterial(new SimpleMaterial(AMaterial.ALPHA_MASKING));
        abdomin.addTexture(transparentTexture);
        abdomin.setName("Abdomin");
        abdomin.setPosition(0, 1.5f, 0);
        abdomin.setScaleX(4.8f);
        abdomin.setScaleY(2.9f);

        leftUpperArm = new Sphere(1, 24, 24);
        leftUpperArm.setMaterial(new SimpleMaterial(AMaterial.ALPHA_MASKING));
        leftUpperArm.addTexture(transparentTexture);
        leftUpperArm.setName("LeftUpperArm");
        leftUpperArm.setPosition(3f, 3.4f, -1f);

        rightUpperArm = new Sphere(1, 24, 24);
        rightUpperArm.setMaterial(new SimpleMaterial(AMaterial.ALPHA_MASKING));
        rightUpperArm.addTexture(transparentTexture);
        rightUpperArm.setName("RightUpperArm");
        rightUpperArm.setPosition(-3f, 3.4f, -1f);
        
        leftForeArm = new Sphere(1, 24, 24);
        leftForeArm.setMaterial(new SimpleMaterial(AMaterial.ALPHA_MASKING));
        leftForeArm.addTexture(transparentTexture);
        leftForeArm.setName("LeftForeArm");
        leftForeArm.setPosition(5.3f, 2.5f, -1f);

        rightForeArm = new Sphere(1, 24, 24);
        rightForeArm.setMaterial(new SimpleMaterial(AMaterial.ALPHA_MASKING));
        rightForeArm.addTexture(transparentTexture);
        rightForeArm.setName("RightForeArm");
        rightForeArm.setPosition(-5.3f, 2.5f, -1f);

        lowerBack = new Cube(1);
        lowerBack.setMaterial(new SimpleMaterial(AMaterial.ALPHA_MASKING));
        lowerBack.addTexture(transparentTexture);
        lowerBack.setName("LowerBack");
        lowerBack.setPosition(0, 2f, -1.5f);
        lowerBack.setScaleX(4.8f);
        lowerBack.setScaleY(2.9f);

        upperBack = new Cube(1);
        upperBack.setMaterial(new SimpleMaterial(AMaterial.ALPHA_MASKING));
        upperBack.addTexture(transparentTexture);
        upperBack.setName("UpperBack");
        upperBack.setPosition(0, 4.25f, -1.5f);
        upperBack.setScaleX(4.8f);
        upperBack.setScaleY(2.9f);

        leftLeg = new Cube(1);
        leftLeg.setMaterial(new SimpleMaterial(AMaterial.ALPHA_MASKING));
        leftLeg.addTexture(transparentTexture);
        leftLeg.setName("LeftLeg");
        leftLeg.setPosition(1.35f, -5f, 0f);
        leftLeg.setScaleX(1.5f);
        leftLeg.setScaleY(6.5f);

        rightLeg = new Cube(1);
        rightLeg.setMaterial(new SimpleMaterial(AMaterial.ALPHA_MASKING));
        rightLeg.addTexture(transparentTexture);
        rightLeg.setName("RightLeg");
        rightLeg.setPosition(-1.35f, -5f, 0f);
        rightLeg.setScaleX(1.5f);
        rightLeg.setScaleY(6.5f);

        //add object to body group
        //this will ensure the rotation is applied to all objects
        bodyGroup.addChild(leftHand);
        bodyGroup.addChild(rightHand);
        bodyGroup.addChild(leftFoot);
        bodyGroup.addChild(rightFoot);
        bodyGroup.addChild(bodyModel);
        bodyGroup.addChild(head);
        bodyGroup.addChild(chest);
        bodyGroup.addChild(abdomin);
        bodyGroup.addChild(leftUpperArm);
        bodyGroup.addChild(leftForeArm);
        bodyGroup.addChild(rightUpperArm);
        bodyGroup.addChild(rightForeArm);
        bodyGroup.addChild(upperBack);
        bodyGroup.addChild(lowerBack);
        bodyGroup.addChild(leftLeg);
        bodyGroup.addChild(rightLeg);

        //register the object with the color picker
        objectPicker.registerObject(leftHand);
        objectPicker.registerObject(rightHand);
        objectPicker.registerObject(leftFoot);
        objectPicker.registerObject(rightFoot);
        objectPicker.registerObject(head);
        objectPicker.registerObject(chest);
        objectPicker.registerObject(abdomin);
        objectPicker.registerObject(leftUpperArm);
        objectPicker.registerObject(leftForeArm);
        objectPicker.registerObject(rightUpperArm);
        objectPicker.registerObject(rightForeArm);
        objectPicker.registerObject(upperBack);
        objectPicker.registerObject(lowerBack);
        objectPicker.registerObject(leftLeg);
        objectPicker.registerObject(rightLeg);

        //move camera back to see body
        mCamera.setZ(35f);

        //add objects to scene
        addChild(bodyGroup);
    }

    public void getObjectAt(float x, float y) {
        //call for object at click location
        //offset to get accurate click
        objectPicker.getObjectAt(x, (y - 120));
    }


    public void onDrawFrame(GL10 glUnused) {
        super.onDrawFrame(glUnused);
    }
    
    //used to roatet the body on the screen
    //gets called on touch events
    public void setRotation(float x, float y) {
        bodyGroup.setRotX(bodyGroup.getRotX() + x);
        bodyGroup.setRotY(bodyGroup.getRotY() + y);
    }

}
