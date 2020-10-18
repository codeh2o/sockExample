package FileChannelDemo;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @program: socket
 * @description:
 * @author: h2o
 * @create: 2020-10-19 00:33
 **/

interface FileCopyRunner {
    void copyFile(File source, File target);
}

public class FileCopyDemo {

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        FileCopyRunner noBufferStreamCopy = new FileCopyRunner() {
            public void copyFile(File source, File target) {
                InputStream fin = null;
                OutputStream fout = null;
                try {
                    fin = new FileInputStream(source);
                    fout = new FileOutputStream(target);

                    int result;
                    while ((result = fin.read()) != -1) {
                        fout.write(result);
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    close(fin);
                    close(fout);
                }


            }
        };

        FileCopyRunner BufferStreamCopy = new FileCopyRunner() {
            public void copyFile(File source, File target) {
                BufferedInputStream fin = null;
                BufferedOutputStream fout = null;
                try {
                    fin = new BufferedInputStream(new FileInputStream(source));
                    fout = new BufferedOutputStream(new FileOutputStream(target));

                    byte[] buffer = new byte[1024];
                    int result;

                    while ((result = fin.read(buffer)) != -1) {
                        fout.write(buffer, 0, result);
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    close(fin);
                    close(fout);
                }
            }
        };

        FileCopyRunner nioBufferCopy = new FileCopyRunner() {
            public void copyFile(File source, File target) {
                FileChannel fin = null;
                FileChannel fout = null;

                try {
                    fin = new FileInputStream(source).getChannel();
                    fout = new FileOutputStream(target).getChannel();

                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    while (fin.read(buffer) != -1) {
                        //buffer写模式转化为读模式
                        buffer.flip();


                        //循环直到所有数据都写入
                        while (buffer.hasRemaining()) {
                            fout.write(buffer);
                        }

                        //读模式转化成写模式
                        buffer.clear();

                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    close(fin);
                    close(fout);
                }

            }
        };

        FileCopyRunner nioTransferCopy = new FileCopyRunner() {
            public void copyFile(File source, File target) {
                FileChannel fin = null;
                FileChannel fout = null;

                try {
                    fin = new FileInputStream(source).getChannel();
                    fout = new FileOutputStream(target).getChannel();
                    long transferedBytesCount = 0l;
                    long size = fin.size();
                    while (transferedBytesCount < size) {
                        //传输了的字节数
                        transferedBytesCount += fin.transferTo(0, fout.size(), fout);
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    close(fin);
                    close(fout);
                }
            }
        };

    }
}
