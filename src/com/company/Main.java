/*
Here's a fortune pig, you'll need this
                         _
 _._ _..._ .-',     _.._(`))
'-. `     '  /-._.-'    ',/
   )         \            '.
  / _    _    |             \
 |  a    a    /              |
 \   .-.                     ;
  '-('' ).-'       ,'       ;
     '-;           |      .'
        \           \    /
        | 7  .__  _.-\   \
        | |  |  ``/  /`  /
       /,_|  |   /,_/   /
          /,_/
 */



package com.company;

import com.company.Controller.MainFrame;
import com.company.Model.DUT;
import com.company.Model.Mobile;
import com.company.Model.eMTC;
import com.company.Model.NBIoT;
import com.company.Parser.NBIoTParser;
import com.company.Parser.ParserUtility;
import com.company.Parser.MobileParser;
import com.company.Parser.eMTCParser;

import java.awt.*;
import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {

    public static void main(String[] args) throws SQLException {
        // Initialize support variables
        ParserUtility.initializeSupportVariables();
        Frame mainFrame = new MainFrame();
        boolean flag = true;
        Mobile dut = null;
        eMTC catm = null;
        NBIoT nbiot = null;
        while (flag) {
            if (dut == null) {
                dut = new Mobile();
            }
            if (catm == null) {
                catm = new eMTC();
            }
            if (nbiot == null) {
                nbiot = new NBIoT();
            }
            System.out.println("<==============================================>");
            System.out.println("*        Welcome to CA and SIP Parser tool.    *");
            System.out.println("<                                              >");
            System.out.println("<                 Options:                     >");
            System.out.println("<                                              >");
            System.out.println("<               1: RRC Parser                  >");
            System.out.println("<                                              >");
            System.out.println("<               2: VoLTE Parser                >");
            System.out.println("<                                              >");
            System.out.println("<               3: CAT M Parser                >");
            System.out.println("<                                              >");
            System.out.println("<               4: NB-IoT Parser               >");
            System.out.println("<                                              >");
            System.out.println("<               5: Exit                        >");
            System.out.println("<                                              >");
            System.out.println("<==============================================>");
            Scanner inputChoice = new Scanner(System.in);
            String choice = inputChoice.nextLine();
            int chosenItem;
            if ( choice.matches(Utility.ISDIGIT_REGEX) ) {
                chosenItem = Integer.parseInt(choice);
            } else {
                chosenItem = Utility.ERROR_CODE;
            }
            switch (chosenItem) {
                case 1:
                    try {
                        dut = nasParser();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        dut =  volteParser(dut);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        catm = eMTCParser();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        nbiot = nbIoTParser();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    flag = false;
                    break;
                default: System.out.println("Insert a valid option please\n");
                    break;
            }
        }

    }

    /**
     * Private support method to manage parsing of NAS messages of smartphones
     * @return the updated instance of the dut
     */
    private static Mobile nasParser() throws UnsupportedEncodingException {
        File jsonTemp = null;
        boolean uncorrect = true;
        String inputFileName = "";
        while (jsonTemp == null) {
            while (uncorrect) {
                System.out.println("Please insert file name with extension (.pcap/.pcapng) " +
                        "in the same directory as the one in which the jar executable is");
                Scanner scan = new Scanner(System.in);
                inputFileName = scan.nextLine();
                Pattern p = Pattern.compile(Utility.FILE_NAME_REGEX);
                Matcher m = p.matcher(inputFileName);
                if (m.find()) {
                    String match = m.group(1);
                    // File loaded in memory
                    jsonTemp = FileManager.loadInputFile(inputFileName, Utility.S1AP_TSHARK_FILTER,Utility.TSHARK_OPTION_LEGACY_FILTER);
                    if (jsonTemp != null) {
                        uncorrect = false;
                    }
                } else {
                    System.out.println("You tried, you failed. Try again, with the right file format.");
                }
            }


        }



        // Create Json File Reader
        FileReader jsonFileReader = null;
        try {
            jsonFileReader = new FileReader(jsonTemp);
        } catch (FileNotFoundException e) {
            // Handle file not found exceptions
            e.printStackTrace();
        }
        Mobile dut = new Mobile();
        MobileParser parser = new MobileParser(jsonFileReader,dut);
        boolean packetValidity = parser.capabilitiesAttachValidator(dut);
        if (!packetValidity) {
            System.out.println(Utility.ERROR_CAUSE);
        } else {
            dut = (Mobile) parser.packetParser(dut);
        }

        // Parse sgsap for IMEI SVN
        jsonTemp = FileManager.loadInputFile(inputFileName, Utility.SGSAP_TSHARK_FILTER,Utility.TSHARK_OPTION_LEGACY_FILTER);
        try {
            jsonFileReader = new FileReader(jsonTemp);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        MobileParser sgsParser = new MobileParser(jsonFileReader, dut);
        dut = (Mobile) sgsParser.svnParser(dut);
        if (dut != null) {
            Mobile.printDUTCap(dut);
            DUT.printIMEISV(dut);

        }
        return dut;
    }
    /**
     * Private support method to parse pcap file containing SIP messages related to VoLTE sessions
     * @return the updated instance of the device
     */
    private static Mobile volteParser(Mobile dut) throws FileNotFoundException {
        String inputFileName = "";
        String imsi = "";
        File inputFile = null;
        FileReader volteFileReader = null;
        // FILE INSERTION
        boolean uncorrect = true;
        while (uncorrect) {
            System.out.println("Please insert SIP file name with extension (.pcap/.pcapng) in the same directory as the one in which the jar executable is");
            System.out.println();
            Scanner scan = new Scanner(System.in);
            inputFileName = scan.nextLine();
            Pattern p = Pattern.compile(Utility.FILE_NAME_REGEX);
            Matcher m = p.matcher(inputFileName);
            if (m.find()) {
                String match = m.group(1);
                uncorrect = false;
                try {
                    inputFile = FileManager.loadInputFile(inputFileName, Utility.SIP_TSHARK_FILTER,"");
                    volteFileReader = new FileReader(inputFile);
                } catch (Exception e) {
                    //e.printStackTrace();
                    uncorrect = true;
                }
            } else {
                System.out.println("You tried, you failed. Try again, with the right file format.");
            }
        }
        // IMSI INSERTION
        boolean uncorrect2 = true;
        boolean packetValidity = false;
        MobileParser volteParser = null;
        while (uncorrect2) {
            System.out.println("Please insert IMSI");
            System.out.println();
            Scanner scan = new Scanner(System.in);
            imsi = scan.nextLine();
            Pattern p = Pattern.compile(Utility.IMSI_REGEX);
            Matcher m = p.matcher(imsi);
            if (m.find() && imsi.length() == 15) {
                String match = m.group(1);
                dut.setImsi(imsi);
                volteParser = new MobileParser(volteFileReader, dut);
                packetValidity = volteParser.volteValidator(dut);
                if (packetValidity) {
                    uncorrect2 = false;
                    try {
                        volteFileReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if(Utility.ERROR_CAUSE.equals(Utility.WRONG_IMSI)) {
                    System.out.println(Utility.ERROR_CAUSE);
                    inputFile = FileManager.loadInputFile(inputFileName, Utility.SIP_TSHARK_FILTER,"");
                    volteFileReader = new FileReader(inputFile);
                    uncorrect2 = true;
                } else if (Utility.ERROR_CAUSE.equals(Utility.EMPTY_FILE)) {
                    System.out.println(Utility.ERROR_CAUSE);
                    try {
                        volteFileReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    uncorrect2 = false;
                } else {
                    System.out.println(Utility.ERROR_CAUSE);
                    uncorrect2 = false;
                }
            } else {
                System.out.println("Wrong IMSI Format");
            }
        }

        // Parse if packet is valid
        if (packetValidity) {
            dut = volteParser.volteRegisterParser(dut);
            // Parse invite
            inputFile = FileManager.loadInputFile(inputFileName,
                    Utility.FRAME_NUMBER_TSHARK_FILTER.concat(String.valueOf(Utility.INVITE_FRAME_NUMBER)),
                    Utility.TSHARK_OPTION_LEGACY_FILTER);
            volteFileReader = new FileReader(inputFile);
            MobileParser inviteParser = new MobileParser(volteFileReader, dut);
            dut = inviteParser.volteInviteParser(dut);
            dut.getLteCap().getVolteCap().setSupportedCodecs(ParserUtility.wiresharkWorkaroundCodecsParser(ParserUtility.duplicatedKeysWiresharkMediaAttr));
            dut.printVoLTEDUTCap(dut);
        }

        return dut;
    }

    /**
     * Private support method to parse eMTC features
     * @return void
     */
    private static eMTC eMTCParser(){
        File jsonTemp = null;
        boolean uncorrect = true;
        String inputFileName = "";
        while (jsonTemp == null) {
            while (uncorrect) {
                System.out.println("Please insert file name with extension (.pcap/.pcapng) " +
                        "in the same directory as the one in which the jar executable is");
                Scanner scan = new Scanner(System.in);
                inputFileName = scan.nextLine();
                Pattern p = Pattern.compile(Utility.FILE_NAME_REGEX);
                Matcher m = p.matcher(inputFileName);
                if (m.find()) {
                    String match = m.group(1);
                    // File loaded in memory
                    jsonTemp = FileManager.loadInputFile(inputFileName, Utility.S1AP_TSHARK_FILTER,Utility.TSHARK_OPTION_LEGACY_FILTER);
                    if (jsonTemp != null) {
                        uncorrect = false;
                    }
                } else {
                    System.out.println("You tried, you failed. Try again, with the right file format.");
                }
            }
        }
        // Create Json File Reader
        FileReader jsonFileReader = null;
        try {
            jsonFileReader = new FileReader(jsonTemp);
        } catch (FileNotFoundException e) {
            // Handle file not found exceptions
            e.printStackTrace();
        }
        eMTC dut = new eMTC();
        eMTCParser eMTCParser = new eMTCParser(jsonFileReader,dut);
        boolean packetValidity = eMTCParser.capabilitiesAttachValidator(dut);

        if (!packetValidity) {
            System.out.println(Utility.ERROR_CAUSE);
        } else {
            //dut = (Mobile) Parser.packetParser(dut);
            dut = (eMTC) eMTCParser.packetParser(dut);
            eMTC.printCatMDUTCap(dut);
        }
        jsonTemp.delete();

        return null;
    }

    /**
     * Private support method to parse NBIoT devices
     * @return void
     */
    private static NBIoT nbIoTParser(){
        File jsonTemp = null;
        boolean uncorrect = true;
        String inputFileName = "";
        while (jsonTemp == null) {
            while (uncorrect) {
                System.out.println("Please insert file name with extension (.pcap/.pcapng) " +
                        "in the same directory as the one in which the jar executable is");
                Scanner scan = new Scanner(System.in);
                inputFileName = scan.nextLine();
                Pattern p = Pattern.compile(Utility.FILE_NAME_REGEX);
                Matcher m = p.matcher(inputFileName);
                if (m.find()) {
                    String match = m.group(1);
                    // File loaded in memory
                    jsonTemp = FileManager.loadInputFile(inputFileName, Utility.S1AP_TSHARK_FILTER,Utility.TSHARK_OPTION_NBIOT_FILTER);
                    if (jsonTemp != null) {
                        uncorrect = false;
                    }
                } else {
                    System.out.println("You tried, you failed. Try again, with the right file format.");
                }
            }
        }
        // Create Json File Reader
        FileReader jsonFileReader = null;
        try {
            jsonFileReader = new FileReader(jsonTemp);
        } catch (FileNotFoundException e) {
            // Handle file not found exceptions
            e.printStackTrace();
        }
        NBIoT dut = new NBIoT();
        NBIoTParser parser = new NBIoTParser(jsonFileReader,dut);
        boolean packetValidity = parser.capabilitiesAttachValidator(dut);
        if (!packetValidity) {
            System.out.println(Utility.ERROR_CAUSE);
        } else {
            dut = (NBIoT) parser.packetParser(dut);
            NBIoT.printDUTNBIoTCap(dut);
        }
        return dut;
    }
}
