package cs155.jray;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Created by edenzik on 10/20/15.
 * An animated schene containing an Animator interface. Initially null, must be added for animations.
 */
public class AnimatedScene3D extends Scene3D {
    public Animator3D anim = null;
    public AnimatedScene3D(String name){
        super(name);
    }


}
