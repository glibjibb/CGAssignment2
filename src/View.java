import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.GLBuffers;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import util.Material;


import java.io.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


/**
 * Created by ashesh on 9/18/2015.
 *
 * The View class is the "controller" of all our OpenGL stuff. It cleanly encapsulates all our OpenGL functionality from the rest of Java GUI, managed
 * by the JOGLFrame class.
 */
public class View
{
    private int WINDOW_WIDTH,WINDOW_HEIGHT, CAMERA_DISTANCE;
    private Matrix4f proj,trackballTransform;
    private Stack<Matrix4f> modelView;
    private ArrayList<util.ObjectInstance> meshObjects;
    private ArrayList<util.Material> meshMaterials;
    private ArrayList<Matrix4f> meshTransforms;
    private float mouseStartX, mouseStartY;
    util.ShaderProgram program;
    private int modelviewLocation,projectionLocation,colorLocation;



    public View()
    {
        proj = new Matrix4f();
        proj.identity();

        modelView = new Stack<Matrix4f>();

        meshObjects = new ArrayList<util.ObjectInstance>();
        meshMaterials = new ArrayList<Material>();
        meshTransforms = new ArrayList<Matrix4f>();
        trackballTransform = new Matrix4f().identity();
        CAMERA_DISTANCE = 150;
        mouseStartX = 0;
        mouseStartY = 0;

    }

    private void initObjects(GL3 gl) throws FileNotFoundException
    {
        util.PolygonMesh mesh;
        util.ObjectInstance o;
        Matrix4f transform;
        util.Material mat;

        InputStream in;

        mat =  new util.Material();

        //my object

        in = new FileInputStream("models/halfegg.obj");
        mesh = util.ObjImporter.importFile(in,true);
        o = new util.ObjectInstance(gl,program,mesh,new String("halfegg"));
        mat.setAmbient(0.4f,0.1f,0); //only this one is used currently to determine color
        mat.setDiffuse(1,0,0);
        mat.setSpecular(1,0,0);
        meshObjects.add(o);
        meshMaterials.add(new Material(mat));
        transform = new Matrix4f().mul(new Matrix4f().translate(0,18,0))
                .mul(new Matrix4f().scale(25,25,25));
        meshTransforms.add(transform);

        //object 2
        in = new FileInputStream("models/cone.obj");
        mesh = util.ObjImporter.importFile(in,true);
        o = new util.ObjectInstance(gl,program,mesh,new String("cone"));
        mat.setAmbient(0,1,0); //only this one is used currently to determine color
        mat.setDiffuse(0,1,0);
        mat.setSpecular(0,1,0);
        meshObjects.add(o);
        meshMaterials.add(new Material(mat));
        //first scale, and then translate. Read down to up for order in which transforms are applied
        transform = new Matrix4f().mul(new Matrix4f().translate(-80,30.5f,0))
                                  .mul(new Matrix4f().scale(20,100,20));
        meshTransforms.add(transform);

        //object 3
        in = new FileInputStream("models/neptune.obj");
        mesh = util.ObjImporter.importFile(in,true);
        o = new util.ObjectInstance(gl,program,mesh,new String("neptune"));
        mat.setAmbient(1,0,1); //only this one is used currently to determine color
        mat.setDiffuse(0,1,0);
        mat.setSpecular(0,1,0);
        meshObjects.add(o);
        meshMaterials.add(new Material(mat));
        //first scale, and then translate. Read down to up for order in which transforms are applied
        transform = new Matrix4f().mul(new Matrix4f().translate(90,20,35))
                .mul(new Matrix4f().scale(30,30,30));
        meshTransforms.add(transform);

        //object 4
        in = new FileInputStream("models/sphere.obj");
        mesh = util.ObjImporter.importFile(in,true);
        o = new util.ObjectInstance(gl,program,mesh,new String("sphere"));
        mat.setAmbient(1,0,0); //only this one is used currently to determine color
        mat.setDiffuse(0,1,0);
        mat.setSpecular(0,1,0);
        meshObjects.add(o);
        meshMaterials.add(new Material(mat));
        //first scale, and then translate. Read down to up for order in which transforms are applied
        transform = new Matrix4f().mul(new Matrix4f().translate(-50,12.55f,-55))
                .mul(new Matrix4f().scale(30,15,30));
        meshTransforms.add(transform);

        //object 5
        in = new FileInputStream("models/cylinder.obj");
        mesh = util.ObjImporter.importFile(in,true);
        o = new util.ObjectInstance(gl,program,mesh,new String("cylinder"));
        mat.setAmbient(0,1,1); //only this one is used currently to determine color
        mat.setDiffuse(0,1,0);
        mat.setSpecular(0,1,0);
        meshObjects.add(o);
        meshMaterials.add(new Material(mat));
        //first scale, and then translate. Read down to up for order in which transforms are applied
        transform = new Matrix4f().mul(new Matrix4f().translate(90,30.5f,-60))
                .mul(new Matrix4f().scale(20,100,20));
        meshTransforms.add(transform);

        //object 6
        in = new FileInputStream("models/cone.obj");
        mesh = util.ObjImporter.importFile(in,true);
        o = new util.ObjectInstance(gl,program,mesh,new String("cone2"));
        mat.setAmbient(1,1,1); //only this one is used currently to determine color
        mat.setDiffuse(0,1,0);
        mat.setSpecular(0,1,0);
        meshObjects.add(o);
        meshMaterials.add(new Material(mat));
        //first scale, and then translate. Read down to up for order in which transforms are applied
        transform = new Matrix4f().mul(new Matrix4f().translate(90,80,-60))
                .mul(new Matrix4f().scale(100,100,100));
        meshTransforms.add(transform);

        //object 7
        in = new FileInputStream("models/cone.obj");
        mesh = util.ObjImporter.importFile(in,true);
        o = new util.ObjectInstance(gl,program,mesh,new String("cone3"));
        mat.setAmbient(0.5f,0.7f,0); //only this one is used currently to determine color
        mat.setDiffuse(0,1,0);
        mat.setSpecular(0,1,0);
        meshObjects.add(o);
        meshMaterials.add(new Material(mat));
        //first scale, and then translate. Read down to up for order in which transforms are applied
        transform = new Matrix4f().mul(new Matrix4f().translate(50,24,-10))
                .mul(new Matrix4f().scale(50,75,100));
        meshTransforms.add(transform);

//        //my hollow object
//        in = new FileInputStream("models/cup.obj");
//        mesh = util.ObjImporter.importFile(in,true);
//        o = new util.ObjectInstance(gl,program,mesh,new String("cup"));
//        mat.setAmbient(1,1,5); //only this one is used currently to determine color
//        mat.setDiffuse(0,1,0);
//        mat.setSpecular(0,1,0);
//        meshObjects.add(o);
//        meshMaterials.add(new Material(mat));
//        //first scale, and then translate. Read down to up for order in which transforms are applied
//        transform = new Matrix4f().mul(new Matrix4f().translate(-30,30,70))
//                .mul(new Matrix4f().scale(100,100,100));
//        meshTransforms.add(transform);

        //object 8
        in = new FileInputStream("models/box.obj");
        mesh = util.ObjImporter.importFile(in,true);
        o = new util.ObjectInstance(gl,program,mesh,new String("box2"));
        mat.setAmbient(0.45f,0.1f,0.1f); //only this one is used currently to determine color
        mat.setDiffuse(0,1,0);
        mat.setSpecular(0,1,0);
        meshObjects.add(o);
        meshMaterials.add(new Material(mat));
        //first scale, and then translate. Read down to up for order in which transforms are applied
        transform = new Matrix4f().mul(new Matrix4f().translate(-100,30.5f,-45))
                .mul(new Matrix4f().scale(30,50,20));
        meshTransforms.add(transform);

        //object 9
        in = new FileInputStream("models/sphere.obj");
        mesh = util.ObjImporter.importFile(in,true);
        o = new util.ObjectInstance(gl,program,mesh,new String("sphere2"));
        mat.setAmbient(0.8f,0.4f,0.8f); //only this one is used currently to determine color
        mat.setDiffuse(0,1,0);
        mat.setSpecular(0,1,0);
        meshObjects.add(o);
        meshMaterials.add(new Material(mat));
        //first scale, and then translate. Read down to up for order in which transforms are applied
        transform = new Matrix4f().mul(new Matrix4f().translate(-10,10,50))
                .mul(new Matrix4f().scale(10,10,10));
        meshTransforms.add(transform);

        //object 10
        in = new FileInputStream("models/cone.obj");
        mesh = util.ObjImporter.importFile(in,true);
        o = new util.ObjectInstance(gl,program,mesh,new String("cone4"));
        mat.setAmbient(0.5f,0.5f,0.5f); //only this one is used currently to determine color
        mat.setDiffuse(0,1,0);
        mat.setSpecular(0,1,0);
        meshObjects.add(o);
        meshMaterials.add(new Material(mat));
        //first scale, and then translate. Read down to up for order in which transforms are applied
        transform = new Matrix4f().mul(new Matrix4f().translate(-100,10.5f,40))
                .mul(new Matrix4f().scale(45,20,45));
        meshTransforms.add(transform);

        //tabletop
        in = new FileInputStream("models/box.obj");
        mesh = util.ObjImporter.importFile(in,true);
        o = new util.ObjectInstance(gl,program,mesh,new String("tabletop"));
        mat.setAmbient(0,0,1); //only this one is used currently to determine color
        mat.setDiffuse(0,0,1);
        mat.setSpecular(0,0,1);
        meshObjects.add(o);
        meshMaterials.add(new Material(mat));
        transform = new Matrix4f().mul(new Matrix4f().scale(300,10,150));
        meshTransforms.add(transform);

        //leg 1
        in = new FileInputStream("models/box.obj");
        mesh = util.ObjImporter.importFile(in,true);
        o = new util.ObjectInstance(gl,program,mesh,new String("leg1"));
        mat.setAmbient(0,0,1); //only this one is used currently to determine color
        mat.setDiffuse(0,0,1);
        mat.setSpecular(0,0,1);
        meshObjects.add(o);
        meshMaterials.add(new Material(mat));
        transform = new Matrix4f().mul(new Matrix4f().translate(-142,-155,-67))
                .mul(new Matrix4f().scale(15,300,15));
        meshTransforms.add(transform);
        //leg 2
        in = new FileInputStream("models/box.obj");
        mesh = util.ObjImporter.importFile(in,true);
        o = new util.ObjectInstance(gl,program,mesh,new String("leg2"));
        mat.setAmbient(0,0,1); //only this one is used currently to determine color
        mat.setDiffuse(0,0,1);
        mat.setSpecular(0,0,1);
        meshObjects.add(o);
        meshMaterials.add(new Material(mat));
        transform = new Matrix4f().mul(new Matrix4f().translate(-142,-155,67))
                .mul(new Matrix4f().scale(15,300,15));
        meshTransforms.add(transform);
        //leg 3
        in = new FileInputStream("models/box.obj");
        mesh = util.ObjImporter.importFile(in,true);
        o = new util.ObjectInstance(gl,program,mesh,new String("leg3"));
        mat.setAmbient(0,0,1); //only this one is used currently to determine color
        mat.setDiffuse(0,0,1);
        mat.setSpecular(0,0,1);
        meshObjects.add(o);
        meshMaterials.add(new Material(mat));
        transform = new Matrix4f().mul(new Matrix4f().translate(142,-155,-67))
                .mul(new Matrix4f().scale(15,300,15));
        meshTransforms.add(transform);
        //leg 4
        in = new FileInputStream("models/box.obj");
        mesh = util.ObjImporter.importFile(in,true);
        o = new util.ObjectInstance(gl,program,mesh,new String("leg4"));
        mat.setAmbient(0,0,1); //only this one is used currently to determine color
        mat.setDiffuse(0,0,1);
        mat.setSpecular(0,0,1);
        meshObjects.add(o);
        meshMaterials.add(new Material(mat));
        transform = new Matrix4f().mul(new Matrix4f().translate(142,-155,67))
                .mul(new Matrix4f().scale(15,300,15));
        meshTransforms.add(transform);
    }

    public void init(GLAutoDrawable gla) throws Exception
    {
        GL3 gl = gla.getGL().getGL3();




        //compile and make our shader program. Look at the ShaderProgram class for details on how this is done
        program = new util.ShaderProgram();
        program.createProgram(gl,"shaders/default.vert","shaders/default.frag");

        //get input variables that need to be given to the shader program
        projectionLocation = program.getUniformLocation(gl,"projection");
        modelviewLocation = program.getUniformLocation(gl,"modelview");
        colorLocation = program.getUniformLocation(gl,"vColor");

        initObjects(gl);



    }



    public void draw(GLAutoDrawable gla)
    {
        GL3 gl = gla.getGL().getGL3();

        gl.glClearColor(0,0,0, 1);
        gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);
        gl.glEnable(GL.GL_DEPTH_TEST);

        program.enable(gl);

        /*
         *In order to change the shape of this triangle, we can either move the vertex positions above, or "transform" them
         * We use a modelview matrix to store the transformations to be applied to our triangle.
         * Right now this matrix is identity, which means "no transformations"
         */
        modelView.push(new Matrix4f());

        //set the camera at (0,0,50), looking at (0,0,0) w  ith its 'up' direction as (0,1,0)
        modelView.peek().mul(
                            new Matrix4f().lookAt(new Vector3f(0,0,CAMERA_DISTANCE),new Vector3f(0,0,0),new Vector3f(0,1,0)));



        //the trackball is essentially another transformation to the whole scene
        modelView.peek().mul(trackballTransform);

    /*
     *Supply the shader with all the matrices it expects.
    */
        FloatBuffer fb = Buffers.newDirectFloatBuffer(16);
        FloatBuffer mfb = Buffers.newDirectFloatBuffer(4);

        gl.glUniformMatrix4fv(projectionLocation,1,false,proj.get(fb));
        //return;


        gl.glPolygonMode(GL.GL_FRONT_AND_BACK,GL3.GL_FILL); //OUTLINES

        for (int i=0;i<meshObjects.size();i++)
        {
            modelView.push(new Matrix4f(modelView.peek())); //save the current modelview
            Matrix4f transform = meshTransforms.get(i);
            modelView.peek().mul(transform);

            //The total transformation is whatever was passed to it, with its own transformation
            gl.glUniformMatrix4fv(modelviewLocation,1,false,modelView.peek().get(fb));
            //set the color for all vertices to be drawn for this object
            gl.glUniform4fv(colorLocation,1,meshMaterials.get(i).getAmbient().get(fb));
            meshObjects.get(i).draw(gla);
            modelView.pop();
        }
        modelView.pop();

    /*
     *OpenGL batch-processes all its OpenGL commands.
          *  *The next command asks OpenGL to "empty" its batch of issued commands, i.e. draw
     *
     *This a non-blocking function. That is, it will signal OpenGL to draw, but won't wait for it to
     *finish drawing.
     *
     *If you would like OpenGL to start drawing and wait until it is done, call glFinish() instead.
     */
        gl.glFlush();

        program.disable(gl);



    }

    public void mousePressed(int x,int y)
    {
        mouseStartX = x;
        mouseStartY = y;
        System.out.println("Pressed");
    }

    public void mouseReleased(int x,int y)
    {
        System.out.println("Released");
    }

    public void mouseDragged(int x,int y)
    {
        System.out.println("Dragged!");

        float xAngle = (float) Math.toRadians(x - mouseStartX)/10;
        float yAngle = (float) Math.toRadians(y - mouseStartY)/10;

        mouseStartX = x;
        mouseStartY = y;

        trackballTransform.rotate(xAngle,0,1,0);
        trackballTransform.rotate(yAngle,1,0,0);
    }

    public void reshape(GLAutoDrawable gla,int x,int y,int width,int height)
    {
        GL gl = gla.getGL();
        WINDOW_WIDTH = width;
        WINDOW_HEIGHT = height;
        gl.glViewport(0, 0, width, height);

        proj = new Matrix4f().perspective((float)Math.toRadians(120.0f),(float)width/height,0.1f,10000.0f);
       // proj = new Matrix4f().ortho(-400,400,-400,400,0.1f,10000.0f);

    }

    public void dispose(GLAutoDrawable gla)
    {
        GL3 gl = gla.getGL().getGL3();

    }



}
