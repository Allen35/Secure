package com.example.secure.FileExplorer;

import java.util.LinkedList;

public class RecyclerListOrganize {

    public static void Organize(LinkedList<Model> data)
    {
        int dataSize = data.size(), numFile = getNumFile(data, dataSize);
        separateFileFromDir(data, numFile, dataSize);

        //SortArrayList SortArrayLis = new SortArrayList();

        //long start = System.nanoTime();
        SortArrayList.mergesort(data, 0, dataSize - numFile - 1);
        SortArrayList.mergesort(data, dataSize - numFile, dataSize - 1);
        //long end = System.nanoTime();
        //System.out.println("Mergesort statico: " + (end-start));
    }

    public static void separateFileFromDir(LinkedList<Model> data, int numFile, int dataSize)
    {
        if(numFile == dataSize)
            return;
        else
        {
            int check = 0, index = 0;
            while(check < numFile)
            {
                if(data.get(index).isDirectory()) {
                    index++;
                }

                if(!data.get(index).isDirectory)
                {
                    data.add(data.get(index));
                    data.remove(index);
                    check++;
                }
            }
        }
    }

    private static int getNumFile(LinkedList<Model> data, int dataSize)
    {
        int counter = 0;
        for(int i = 0; i < dataSize; i++)
        {
            if(!data.get(i).isDirectory())
                counter++;
        }
        return counter;
    }
}