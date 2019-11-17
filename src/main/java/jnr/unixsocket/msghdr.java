/*
 * Copyright 2019 Robert Middleton.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jnr.unixsocket;

import jnr.ffi.Struct;

/**
 * Represents a `struct msghdr`.
 */
public class msghdr extends Struct {
    
    public Pointer msg_name = new Pointer();
    public Unsigned32 msg_namelen = new Unsigned32();
    public iovec[] msg_iov = null;
    public size_t msg_iovlen = new size_t();
    public Pointer msg_control = new Pointer();
    public Unsigned32 msg_controllen = new Unsigned32();
    public size_t msg_flags = new size_t();
    
    public msghdr(jnr.ffi.Runtime runtime){
        super(runtime);
    }
    
    public void addIoVec( iovec iovec ){
        if( msg_iov == null ){
            msg_iov = new iovec[10];
        }
        
        if( msg_iovlen.intValue() + 1 == msg_iov.length ){
            // Expand our array to put in this new entry
            iovec[] old = msg_iov;
            msg_iov = new iovec[msg_iov.length * 2];
            for( int x = 0; x < old.length; x++ ){
                msg_iov[x] = old[x];
            }
        }
        
        msg_iov[msg_iovlen.intValue()] = iovec;
        msg_iovlen.set(msg_iovlen.intValue() + 1);
    }
    
}
