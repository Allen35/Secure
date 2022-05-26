package com.example.secure.FileExplorer;

import java.io.File;

public class Model {

    private File itemPath;
    private int image;
    public final boolean isDirectory;


    public Model(File itemPath, int image, boolean isDirectory)
    {
        this.itemPath = itemPath;
        this.image = image;
        this.isDirectory = isDirectory;
    }

    public int getImage()
    {
        return image;
    }

    public String getItemName()
    {
        return itemPath.getName();
    }

    public String getItemPath()
    {
        return itemPath.toString();
    }

    public boolean isDirectory()
    {
        return isDirectory;
    }

    @Override
    public String toString()
    {
        return itemPath.getName();
    }
}
