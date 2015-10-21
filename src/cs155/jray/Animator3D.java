package cs155.jray;

import java.util.Iterator;

/**
 * Created by edenzik on 10/20/15.
 * An interface whose only job is to be an iterator, with each iteration producing a new (transformed) scene.
 * This is really only an iterator, but we might want to add some fields in here someday.
 */
public interface Animator3D extends Iterator<Scene3D>{}
