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


 import com.ceph.rbd.jna.Rbd;
 import com.sun.jna.Native;
 import com.sun.jna.Pointer;
 
 public interface RbdExt extends Rbd {
 
 
    RbdExt INSTANCE = (RbdExt) Native.loadLibrary("rbd", RbdExt.class);
 
 
     int rbd_create4(Pointer io, String name, long size, Pointer opts);

     void rbd_image_options_create(Pointer opts);
     void rbd_image_options_destroy(Pointer opts);
     int rbd_image_options_set_string(Pointer image,int option_name,String dataPool);
     int rbd_image_options_get_string(Pointer pointer, int i, String string,int length);
     int rbd_get_data_pool_id(Pointer pointer);

 }
 