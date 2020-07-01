package org.jpos.rest;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

import java.io.InputStream;

public class UnpackISOMessage {
    public static void main(String[] args) {
        UnpackISOMessage iso = new UnpackISOMessage();
        try {
            ISOMsg isoMsg = iso.parseISOMessage();
            iso.printISOMessage(isoMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ISOMsg parseISOMessage() throws Exception {
        String message = "02003220000000808000000010000000001500120604120000000112340001840";
        System.out.printf("Message = %s%n", message);
        try {
            // Load package from resources directory.
            InputStream is = getClass().getResourceAsStream("/fields.xml");
            GenericPackager packager = new GenericPackager(is);
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.unpack(message.getBytes());
            return isoMsg;
        } catch (ISOException e) {
            throw new Exception(e);
        }
    }

    private void printISOMessage(ISOMsg isoMsg) {
        try {
            System.out.printf("MTI = %s%n", isoMsg.getMTI());
            for (int i = 1; i <= isoMsg.getMaxField(); i++) {
                if (isoMsg.hasField(i)) {
                    System.out.printf("Field (%s) = %s%n", i, isoMsg.getString(i));
                }
            }
        } catch (ISOException e) {
            e.printStackTrace();
        }
    }
}