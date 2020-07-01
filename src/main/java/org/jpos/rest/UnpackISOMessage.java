package org.jpos.rest;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import java.io.InputStream;


@Path("/getIsoEle")
public class UnpackISOMessage {
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/{IsoMsgFromGet}")

    //new main
    public Response echoGet(
            //@PathParam("name") String name,
            @PathParam("IsoMsgFromGet") String tMsgFromGet,
            String tMsgFromBody
    ) {
        //removed the
        UnpackISOMessage iso = new UnpackISOMessage();
        ISOMsg isoMsg = null;
        try {
             isoMsg = iso.parseISOMessage(tMsgFromBody);
            iso.printISOMessage(isoMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Map<String, Object> resp = new HashMap<>();
        resp.put("success", "true");
        //resp.put("Name", name);
        resp.put("IsoMsgFromBody", isoMsg);
        resp.put("IsoMsgFromGet", tMsgFromGet);

        Response.ResponseBuilder rb = Response.ok(resp,
                MediaType.APPLICATION_JSON).status(Response.Status.OK);
        return rb.build();
    }




    public ISOMsg parseISOMessage(String message) throws Exception {
//        String message= "0200FABF469F29E0C12000000000040000221646603898000201580120000000000040000000000005770411101202714425000136931012000411220404110411605190200100C00000000C00000000C00000000C0000000006476117374660389800020158D22042011384200106210910110013693201TERMID01HCB_CODE       HEYZIEL                MAURITIUS      MU480936004151001000000012591313000000516310152001012000001040002682008040000000000VB12ISS     CALT24R15Snk013693013693CALVisaGrp                48000182218Postilion:MetaData270218Postilion:OBS:dCvv111214TRANSACTION_ID111220OriginalPosEntryMode111218Postilion:OBS:dCvv110214TRANSACTION_ID215309101367222006220OriginalPosEntryMode149020";

        //\"{string}" , {newline}
        System.out.printf("\"Message\": \"%s\",%n", message);
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

    //add "" double quote to jsonify the data elements
    public void printISOMessage(ISOMsg isoMsg) {
        try {
            System.out.printf("\"MTI\":\"%s\",%n", isoMsg.getMTI());
            for (int i = 1; i <= isoMsg.getMaxField(); i++) {
                if (isoMsg.hasField(i)) {
                    System.out.printf("\"%s\": \"%s\",%n", i, isoMsg.getString(i));
                }
            }
        } catch (ISOException e) {
            e.printStackTrace();
        }
    }
}