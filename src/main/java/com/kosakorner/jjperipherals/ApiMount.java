package com.kosakorner.jjperipherals;

import dan200.computercraft.api.filesystem.IMount;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class ApiMount implements IMount {

    private File path;

    public ApiMount(File dir) {
        path = new File(dir, "lua");
        path.mkdirs();
        extractResource("groups");
    }

    public void extractResource(String name) {
        try {
            URL url = getClass().getClassLoader().getResource("assets/jjperipherals/lua/" + name);
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            InputStream inputStream =  connection.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(new File(path, "/" + name));
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean exists(String path) throws IOException {
        return new File(this.path, path).exists();
    }

    @Override
    public boolean isDirectory(String path) throws IOException {
        return new File(this.path, path).isDirectory();
    }

    @Override
    public void list(String path, List<String> contents) throws IOException {
        File file = this.path;

        for (File f : file.listFiles()) {
            contents.add(f.getName());
        }
    }

    @Override
    public long getSize(String path) throws IOException {
        return new File(this.path, path).getTotalSpace();
    }

    @Override
    public InputStream openForRead(String path) throws IOException {
        return new FileInputStream(new File(this.path, path));
    }

}
