/*
 * Copyright (C) 2016 Marcus Linke
 *
 * This file is part of the JNR project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jnr.unixsocket.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;

import jnr.constants.platform.Shutdown;
import jnr.enxio.channels.Native;
import jnr.enxio.channels.NativeSelectableChannel;
import jnr.enxio.channels.NativeSelectorProvider;
import jnr.unixsocket.msghdr;

public abstract class AbstractNativeSocketChannel extends SocketChannel
    implements ByteChannel, NativeSelectableChannel {

    private final Common common;

    public AbstractNativeSocketChannel(int fd) {
        this(NativeSelectorProvider.getInstance(), fd);
    }

    AbstractNativeSocketChannel(SelectorProvider provider, int fd) {
        super(provider);
        common = new Common(fd);
    }

    public void setFD(int fd) {
        common.setFD(fd);
    }

    public final int getFD() {
        return common.getFD();
    }

    @Override
    protected void implCloseSelectableChannel() throws IOException {
        Native.close(common.getFD());
    }

    @Override
    protected void implConfigureBlocking(boolean block) throws IOException {
        Native.setBlocking(common.getFD(), block);
    }

    public int read(ByteBuffer dst) throws IOException {
        return common.read(dst);
    }

    @Override
    public long read(ByteBuffer[] dsts, int offset,
            int length) throws IOException {
        return common.read(dsts, offset, length);
    }

    public int write(ByteBuffer src) throws IOException {
        return common.write(src);
    }

    @Override
    public long write(ByteBuffer[] srcs, int offset,
            int length) throws IOException {
        return common.write(srcs, offset, length);
    }

    @Override
    public SocketChannel shutdownInput() throws IOException {
        int n = Native.shutdown(common.getFD(), SHUT_RD);
        if (n < 0) {
            throw new IOException(Native.getLastErrorString());
        }
        return this;
    }

    @Override
    public SocketChannel shutdownOutput() throws IOException {
        int n = Native.shutdown(common.getFD(), SHUT_WR);
        if (n < 0) {
            throw new IOException(Native.getLastErrorString());
        }
        return this;
    }

    private static final int SHUT_RD = Shutdown.SHUT_RD.intValue();
    private static final int SHUT_WR = Shutdown.SHUT_WR.intValue();
}
