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


@Path("/packIsoEle")
public class PackISOMessage {

        @GET
        @Produces({MediaType.APPLICATION_JSON})
        @Consumes(MediaType.TEXT_PLAIN)
        @Path("/{IsoMsgFromGet}")
        //@Path("/{name}/{family}")


        public Response echoGet(
                //@PathParam("name") String name,
                @PathParam("IsoMsgFromGet") String tMsgFromGet,
                String tMsgFromBody
        ) {

            PackISOMessage iso = new PackISOMessage();
            String message = null;
            try {
                message = iso.buildISOMessage();
                System.out.printf("\"Message\": \"%s\",%n", message);
            } catch (Exception e) {
                e.printStackTrace();
            }


            Map<String, Object> resp = new HashMap<>();
            resp.put("success", "true");
            //resp.put("Name", name);
            resp.put("IsoMsgFromGet", message);
            resp.put("IsoMsgFromBody", tMsgFromBody);
            Response.ResponseBuilder rb = Response.ok(resp,
                    MediaType.APPLICATION_JSON).status(Response.Status.OK);
            return rb.build();
        }


        private String buildISOMessage() throws Exception {
            try {
                // Load package from resources directory.
                InputStream is = getClass().getResourceAsStream("/fields.xml");
                GenericPackager packager = new GenericPackager(is);

                ISOMsg isoMsg = new ISOMsg();
                isoMsg.setPackager(packager);
                isoMsg.setMTI("0800");

                isoMsg.set(3, "000010");
                isoMsg.set(4, "1500");
                isoMsg.set(7, "1206041200");
                isoMsg.set(11, "000001");
                isoMsg.set(41, "12340001");
                isoMsg.set(49, "840");
                printISOMessage(isoMsg);

                byte[] result = isoMsg.pack();
                return new String(result);
            } catch (ISOException e) {
                throw new Exception(e);
            }
        }



        private void printISOMessage(ISOMsg isoMsg) {
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