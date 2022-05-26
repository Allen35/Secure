package com.example.secure.FileExplorer;

import java.io.File;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RecursiveTreeSearch extends Thread {

	private long folderSize;
	private File directory;
	
	public RecursiveTreeSearch(File directory)
	{
		this.directory = directory;

	}
	
	public void run()
	{
		folderSize = getDirectorySizeLegacyTreeMap(directory);
	}
	
    public long getDirectorySizeLegacyTreeMap(File dir)
    {
        long length = 0;
        long place;
        File[] files = dir.listFiles();
        if (files != null) 
        {
            for (File file : files) 
            {
                if(file.isFile())
                {
                	if(Cache.Instance.gettMap().containsKey(file.toString())) {
                		length += Cache.Instance.gettMap().get(file.toString());
                	} else {
                    	Cache.Instance.getLock().writeLock().lock();
						Cache.Instance.gettMap().put(file.toString(),  file.length());
						Cache.Instance.getLock().writeLock().unlock();
                        length += file.length();
                	}
                }
                else
                {
                	if(Cache.Instance.gettMap().containsKey(file.toString())) {
                		length += Cache.Instance.gettMap().get(file.toString());
                	} else {
                    	place = getDirectorySizeLegacyTreeMap(file);
						Cache.Instance.getLock().writeLock().lock();
						Cache.Instance.gettMap().put(file.toString(), place);
						Cache.Instance.getLock().writeLock().unlock();
                    	length += place;
                	}
                }
            }
        }
        return length;
    }
    
    public File getDirectory() {
    	return this.directory;
    }
    
    public long getFolderSize() {
    	return this.folderSize;
    }
}
