/*
 *    Copyright 2022 The DSMS Authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package marble;

import java.util.Objects;

import com.ceph.rados.IoCTX;
import com.ceph.rbd.Rbd;
import com.ceph.rbd.RbdException;
import com.ceph.rbd.RbdImage;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

public class RbdWrapper extends Rbd {
    final static RbdExt rbdExt;

    Pointer io;

    static {
        rbdExt = RbdExt.INSTANCE;
    }

    public RbdWrapper(IoCTX io) {
        super(io);
        this.io = io.getPointer();
    }

    public void create(String name, long size, String dataPool) throws RbdException {
        Pointer opts = setOptions(createOptions(), dataPool);
        int r = rbdExt.rbd_create4(this.io, name, size, opts.getPointer(0));
        if (r < 0) {
            throw new RbdException("Failed to create image " + name, r);
        }
    }

    public static Pointer createOptions() {
        final Pointer p = new Memory(Native.POINTER_SIZE);
        rbdExt.rbd_image_options_create(p);
        return p;
    }

    public static Pointer setOptions(Pointer pointer, String dataPool) throws RbdException {
        int r = 0;
        if (dataPool != null) {
            r = rbdExt.rbd_image_options_set_string(pointer.getPointer(0), 10, dataPool);
        }
        if (r < 0) {
            throw new RbdException("Failed to set option " + dataPool, r);
        }
        return pointer;
    }

    public int getDataPoolId(String rbdName) throws RbdException {
        int dataPoolId = 0;
        try (RbdImage rbdImage = open(rbdName)) {
            dataPoolId = rbdExt.rbd_get_data_pool_id(rbdImage.getPointer());
            if (dataPoolId <= 0) {
                throw new RbdException("Failed to get data pool id " + rbdName, dataPoolId);
            }
        } catch (Exception e) {
            throw new RbdException(e.getMessage());
        }
        return dataPoolId;
    }
}
