package com.linkallcloud.core.resource.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.linkallcloud.core.lang.Streams;
import com.linkallcloud.core.lang.util.Disks;
import com.linkallcloud.core.resource.NutResource;

/**
 * 记录了一个磁盘文件资源
 * 
 */
public class FileResource extends NutResource {

    private File file;

    public FileResource(File f) {
        this.file = f;
        this.name = f.getName();
        setSource(f.getAbsolutePath());
        priority = 150;
    }

    public FileResource(String base, File file) {
        base = Disks.normalize(Disks.getCanonicalPath(base));
        if (base == null)
            base = "";
        else if (!base.endsWith("/"))
            base += "/";
        this.name = Disks.normalize(Disks.getCanonicalPath(file.getAbsolutePath()));
        this.name = this.name.substring(this.name.indexOf(base) + base.length()).replace('\\', '/');
        this.file = file.getAbsoluteFile();
        setSource(base + " " + this.file.getAbsolutePath());
    }

    public File getFile() {
        return file;
    }

    public FileResource setFile(File file) {
        this.file = file;
        return this;
    }

    public InputStream getInputStream() throws IOException {
        return Streams.fileIn(file);
    }
    
    public boolean equals(Object obj) {
    	if (obj == null)
    		return false;
    	if (obj == this)
    		return true;
    	if (! (obj instanceof FileResource))
    		return false;
    	return ((FileResource)obj).file.equals(file);
    }

    public int hashCode() {
    	return file.hashCode();
    }
    
    public String toString() {
        return "File["+file.getAbsolutePath()+"]";
    }
}
