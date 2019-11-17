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

import java.nio.charset.Charset;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test passing FileDescriptors across a Unix socket.
 */
public class FileDescriptorPassingTest {
    
    private UnixSocketPair socketPair;
   
    @Before
    public void setUp() throws Exception {
        socketPair = new UnixSocketPair();
    }

    @After
    public void tearDown() throws Exception {
        socketPair.close();
    }
    
    @Test
    public void testSendingFileDescriptor(){
        int[] fds = new int[2];
        
        Native.INSTANCE.pipe( fds );
        
        UnixSocketChannel server = (UnixSocketChannel)socketPair.server().getChannel();
        UnixSocketChannel client = (UnixSocketChannel)socketPair.client().getChannel();
        
        jnr.ffi.Runtime runtime = jnr.ffi.Runtime.getSystemRuntime();
        
        String teststring = "Test String";
        iovec first = new iovec(runtime);
        first.iov_base.get().putString(0, teststring, teststring.length() + 1, Charset.forName( "UTF-8" ) );
        first.iov_len.set( teststring.length() + 1 );
        
        
        
        msghdr tosend = new msghdr(runtime);
        //tosend.
        
        //tosend.msg_control = 
    }
}
