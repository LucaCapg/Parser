package com.company.Parser;

import com.company.Model.DUT;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;

/**
 * Interface implementing methods called by json parsers
 */
public interface ParserInterface {
    DUT attachParser(int objectIndex, DUT dut);

    DUT attachAcceptParser(DUT dut, int objectIndex);
    DUT capabilitiesSpecificParser(DUT dut,int objectIndex);
    Integer capabilityPacketParser(JsonObject cap, int i);
    DUT svnParser(DUT dut) throws UnsupportedEncodingException;
}
