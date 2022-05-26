package com.company;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.company.Thread.FileStreamExternalThread;

public class FileManager {
    private static String apex = "\"";
    private static File tempFIle;
    private static FileOutputStream fos;

    /**
     * Private method to load input file (Json)
     */
    public static File loadInputFile(String inputFileName, String filter, String option){
        if (filter.equals(Utility.FRAME_NUMBER_TSHARK_FILTER.concat(String.valueOf(Utility.INVITE_FRAME_NUMBER))))
            filter = filter.concat("\" -r ");
        Utility.PCAP_RRC_FILE_NAME = inputFileName;
        inputFileName = apex.concat(inputFileName).concat(apex);
        String execute = "tshark -2 " + filter;
        String execute3 = " -l -n -T json";
        execute = execute.concat(inputFileName).concat(option).concat(execute3);
        try {
            tempFIle = new File(Utility.PROGRAM_PATH.concat("\\temp.json"));
            fos = new FileOutputStream(tempFIle);
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(execute);
            FileStreamExternalThread outFile = new FileStreamExternalThread(proc.getInputStream(), fos);
            outFile.start();
            int exitVal = proc.waitFor();
            if (exitVal != 0) {
                System.out.println("The inserted file cannot be found. Are you sure you have inserted the right name? " + inputFileName);
                fos.flush();
                fos.close();
                tempFIle.delete();
                tempFIle = null;
            } else {
                String json = Utility.PROGRAM_PATH.concat("\\temp.json");
                tempFIle = Paths.get(json).toFile();
            }
            fos.flush();
            fos.close();
        } catch (Exception e) {
            System.out.println("Error during packet conversion");
            e.printStackTrace();
        }
        return tempFIle;
    }

    /**
     * Support method to delete temp files
     * @param fr file reader of the json file
     */
    public static void deleteTempFile(FileReader fr) {
        try {
            fr.close();
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tempFIle.delete();
    }

    /**
     * Method managing print of capabilities(Retrieved as a looong string) to file
     * @param outFilePath the path of the output file
     * @param stringToWrite the string containing the DUT capabilities
     */
    public static void writeOutputToFile(String outFilePath, String stringToWrite) {
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(outFilePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OutputStreamWriter outputWriter = new OutputStreamWriter(fileOut);
        BufferedWriter writer = new BufferedWriter(outputWriter);

        String[] words = stringToWrite.split("\n");
        for (String word : words) {
            try {
                writer.write(word);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Method managing print of capabilities(Retrieved as an excel workbook) to file
     * @param outFilePath the path of the output file
     * @param workbook the XSSFWorkbook containing the DUT capabilities
     */
    public static void writeOutputToExcel(String outFilePath, XSSFWorkbook workbook) {
        try {
			FileOutputStream outputStream = new FileOutputStream(outFilePath);
			workbook.write(outputStream);
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
