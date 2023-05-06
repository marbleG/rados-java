package marble;
import com.ceph.rbd.RbdImage;
import com.sun.jna.Pointer;

public class RbdImageExt extends RbdImage{

    public RbdImageExt(Pointer image, String name) {
        super(image, name);
    }
    

}