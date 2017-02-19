import com.jogamp.opengl.GL;
import org.joml.Vector4f;
import util.PolygonMesh;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by David Fasano on 2/15/2016.
 */
public class ObjectCreator {
    public static void main(String[] args) {
        eggMaker();
//        cupMaker();
    }

    public static void eggMaker(){
        util.PolygonMesh tmesh = new util.PolygonMesh();

        ArrayList<Vector4f> verts = new ArrayList<Vector4f>();
        ArrayList<Integer> triangles =  new ArrayList<Integer>();

        int STACKS = 10;
        int SLICES = 16;

        Vector4f v;
        for (int i=0;i<STACKS;i++)
        {
            for (int j=0;j<SLICES;j++)
            {
                float r = (float) Math.cos(Math.PI*i/(STACKS-1)-Math.PI/2.0f);
                float z = (float) Math.sin(Math.PI*i/(STACKS-1)-Math.PI/2.0f)/5;
                float x = (float) (Math.cos(Math.PI*j/ (SLICES-1))*r) /5;
                float y = (float) (Math.sin(Math.PI*j/ (SLICES-1))*r *2) /5;

                v = new Vector4f(x, y, z, 1.0f);

                verts.add(v);
            }
        }
        tmesh.setVertexPositions(verts);

        for (int i=0;i<STACKS-1;i++)
        {
            for (int j=0;j<SLICES;j++)
            {
                triangles.add(i*SLICES+j);
                triangles.add(i*SLICES+(j+1)%SLICES);
                triangles.add(((i+1)%STACKS)*SLICES+(j+1)%SLICES);

                triangles.add(i*SLICES+j);
                triangles.add(((i+1)%STACKS)*SLICES+(j+1)%SLICES);
                triangles.add(((i+1)%STACKS)*SLICES+j);
            }
        }

        tmesh.setPrimitiveType(GL.GL_TRIANGLES);
        tmesh.setPrimitiveSize(3);
        tmesh.setPrimitives(triangles);

        exportMesh(tmesh, "halfegg.obj");

    }

//    public static void cupMaker(){
//        util.PolygonMesh tmesh = new util.PolygonMesh();
//
//        ArrayList<Vector4f> verts = new ArrayList<Vector4f>();
//        ArrayList<Integer> triangles =  new ArrayList<Integer>();
//
//        int STACKS = 10;
//        int SLICES = 16;
//
//        Vector4f v;
//        for (int i=0;i<STACKS;i++)
//        {
//            double phi;
//
//            phi = -0.5*Math.PI + Math.PI*i/(STACKS-1);
//            for (int j=0;j<SLICES;j++)
//            {
//                float r = (float) Math.cos(Math.PI*i/((STACKS-1))-Math.PI/2.0f) - 50;
//                float z = (float) -Math.sin(Math.PI*i/((STACKS-1))-Math.PI/2.0f)/5;
//                float x = (float) -(Math.cos(Math.PI*j/ (SLICES-1))*r) /5;
//                float y = (float) -(Math.sin(Math.PI*j/ (SLICES-1))*r *2) /5;
//
//                v = new Vector4f(x, y, z, 1.0f);
//
//                verts.add(v);
//            }
//        }
//        for (int i=0;i<STACKS;i++)
//        {
//            double phi;
//
//            phi = -0.5*Math.PI + Math.PI*i/(STACKS-1);
//            for (int j=0;j<SLICES;j++)
//            {
//                float r = (float) Math.cos(Math.PI*i/((STACKS-1))-Math.PI/2.0f);
//                float z = (float) -Math.sin(Math.PI*i/((STACKS-1))-Math.PI/2.0f)/5 - 50;
//                float x = (float) -(Math.cos(Math.PI*j/ (SLICES-1))*r) /5 - 50;
//                float y = (float) -(Math.sin(Math.PI*j/ (SLICES-1))*r *2) /5 - 50;
//
//                v = new Vector4f(x, y, z, 1.0f);
//
//                verts.add(v);
//            }
//        }
//        tmesh.setVertexPositions(verts);
//
//        for (int i=0;i<STACKS-1;i++)
//        {
//            for (int j=0;j<SLICES;j++)
//            {
//                triangles.add(i*SLICES+j);
//                triangles.add(i*SLICES+(j+1)%SLICES);
//                triangles.add(((i+1)%STACKS)*SLICES+(j+1)%SLICES);
//
//                triangles.add(i*SLICES+j);
//                triangles.add(((i+1)%STACKS)*SLICES+(j+1)%SLICES);
//                triangles.add(((i+1)%STACKS)*SLICES+j);
//            }
//        }
//
//        tmesh.setPrimitiveType(GL.GL_TRIANGLES);
//        tmesh.setPrimitiveSize(3);
//        tmesh.setPrimitives(triangles);
//
//        exportMesh(tmesh, "cup.obj");
//
//    }
    public static void exportMesh(PolygonMesh mesh, String fileName) {
        util.ObjExporter exporter = new util.ObjExporter();

        File f = new File("models/" + fileName);
        try {
            OutputStream file = new FileOutputStream(f);
            exporter.exportFile(mesh, file);
        }catch (FileNotFoundException e) {
            System.err.println("Failed to create file");
        }
    }
}
