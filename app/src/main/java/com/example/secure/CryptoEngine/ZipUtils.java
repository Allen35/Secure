package com.example.secure.CryptoEngine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    private List <String> fileList;
    private final String OUTPUT_ZIP_FILE;
    private final String SOURCE_FOLDER; // SourceFolder path

    public ZipUtils(String outputfile, String source_folder) {
        this.fileList = new ArrayList<>();
        this.OUTPUT_ZIP_FILE = outputfile;
        this.SOURCE_FOLDER = source_folder;
    }

    public void zipIt() {
        generateFileList(new File(SOURCE_FOLDER));
        byte[] buffer = new byte[1024];
        String source = new File(SOURCE_FOLDER).getName();
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(OUTPUT_ZIP_FILE);
            zos = new ZipOutputStream(fos);

            System.out.println("Output to Zip : " + OUTPUT_ZIP_FILE);
            FileInputStream in = null;

            for (String file: this.fileList)
            {
                System.out.println("File Added : " + file);
                ZipEntry ze = new ZipEntry(source + File.separator + file);
                zos.putNextEntry(ze);
                try {
                    in = new FileInputStream(SOURCE_FOLDER + File.separator + file);
                    int len;
                    while ((len = in .read(buffer)) > 0)
                    {
                        zos.write(buffer, 0, len);
                    }
                } finally {
                    in.close();
                }
            }
            zos.closeEntry();
            System.out.println("Folder successfully compressed");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateFileList(File node) {
        // add file only
        if (node.isFile()) {
            //System.out.println("Name: "+node.toString());
            fileList.add(generateZipEntry(node.toString()));
        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename: subNote)
            {
                generateFileList(new File(node, filename));
            }
        }
    }

    private String generateZipEntry(String file)
    {
        return file.substring(file.lastIndexOf("/")+1);
        //return file.substring(SOURCE_FOLDER.length() + 1);
    }
    
    public void zipMultipleFile(/*List<String> files*/) throws IOException {
        //List<String> srcFiles = files/*Arrays.asList(files)*/;
        generateFileList(new File(SOURCE_FOLDER));
        //System.out.println("FileList: "+fileList);
        new File(OUTPUT_ZIP_FILE);
        FileOutputStream fos = new FileOutputStream(OUTPUT_ZIP_FILE);
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        for (String srcFile : fileList)
        {
            File fileToZip = new File(SOURCE_FOLDER+"/"+srcFile);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);
 
            byte[] bytes = new byte[512];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }
        zipOut.close();
        fos.close();
    }
    
    public void unzip() {
        String OUTPUT_FILE = OUTPUT_ZIP_FILE;
        File dir = new File(OUTPUT_FILE);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[512];
        try {
            fis = new FileInputStream(SOURCE_FOLDER);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null)
            {
                String fileName = ze.getName();
                File newFile = new File(OUTPUT_FILE + File.separator + fileName);
                System.out.println("Unzipping to "+newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0)
                {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}