package com.revoltcode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revoltcode.datacategory.*;
import com.revoltcode.model.Phone;
import com.revoltcode.model.calls.Call;
import com.revoltcode.model.calls.CallLog;
import com.revoltcode.model.GeoLocation;
import com.revoltcode.model.calls.Contact;
import com.revoltcode.model.device.Device;
import com.revoltcode.model.device.TrackingReport;
import com.revoltcode.model.media.DocumentFile;
import com.revoltcode.model.media.ImageFile;
import com.revoltcode.model.message.Sms;
import com.revoltcode.util.DataUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class DataGenerator {
    private static List<Phone> phones = new ArrayList<>();
    private static List<Device> devices = new ArrayList<>();
    private static List<Contact> contacts = new ArrayList<>();
    private static List<Call> calls = new ArrayList<>();
    private static List<Sms> messages = new ArrayList<>();
    private static List<CallLog> callLogs = new ArrayList<>();
    private static List<ImageFile> images = new ArrayList<>();
    private static List<DocumentFile> files = new ArrayList<>();

    private static List<String> sampleSms = new ArrayList<>();

    private static List<String> imageInternalStorage = new ArrayList<>();
    private static List<String> fileInternalStorage = new ArrayList<>();

    private static Map<Integer, Device> createdUserDevice = new HashMap<>();
    private static Map<String, String> IMEI = new HashMap<>();
    private static Map<String, String> ICCID = new HashMap<>();
    private static Map<String, String> IMSI = new HashMap<>();
    private static Map<String, String> deviceContacts = new HashMap<>();
    private static Map<String, String> deviceOwnerNumbers = new HashMap<>();

    private static Logger log = Logger.getLogger(DataGenerator.class.getName());

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static Random random = new Random();
    private static final int minLat = 5, minLong =5, maxLat = 10, maxLong = 10;

    public static void main(String[] args){

        try {
            initializePresetData();
            generateDeviceDataSet();

            log.info("*************"+DataUtil.getDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void configureLogger(){

        try {
            FileHandler fileHandler = new FileHandler("/home/revolt/Documents/CST sample data/generated data/device2.log");
            log.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            log.setUseParentHandlers(false);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static List<TrackingReport> generateTrackingData(int dataSize, String imsi, String imei) {

        List<TrackingReport> trackingReportData = new ArrayList<>();
        try {
            final var IMEI = DataUtil.generateIMEI();
            PhoneStatus phoneStatus = null;
            Integer haResponseCode = null;

            for(int i=0; i< random.nextInt(6)+1; i++) {
                int month = random.nextInt(12);
                int day = random.nextInt(29);
                int hour = random.nextInt(18);
                int minuites = random.nextInt(40);
                int seconds = random.nextInt(40);

                GeoLocation geoLoc = DataUtil.getRandomGeoLocation(minLong, maxLong, minLat, maxLat);

                switch (random.nextInt(2)) {
                    case 0:
                        phoneStatus = PhoneStatus.ONLINE;
                        haResponseCode = 200;
                        break;
                    case 1:
                        phoneStatus = PhoneStatus.OFFLINE;
                        haResponseCode = 404;
                        break;
                    default:
                }

                trackingReportData.add(TrackingReport.builder()
                        .trackingId(DataUtil.getSha54(UUID.randomUUID().toString()))
                        .alias("Test_sim")
                        .username("USER" + dataSize)
                        .msisdn(DataUtil.generateNumberRef(11))
                        .imsi(imsi)
                        .imei(imei)
                        .type("MSISDN")
                        .queryDate(((String.valueOf(day).toCharArray().length < 2) ? "0" + day : day) + "/"
                                + ((String.valueOf(month).toCharArray().length < 2) ? "0" + month : month) + "/2020" + " " + ((String.valueOf(hour).toCharArray().length < 2) ? "0" + hour : hour)
                                + ":" + ((String.valueOf(minuites).toCharArray().length < 2) ? "0" + minuites : minuites) + ":" + ((String.valueOf(seconds).toCharArray().length < 2) ? "0" + seconds : seconds))

                        .haLatitude(geoLoc.getLatitude())
                        .haLongitude(geoLoc.getLongitude())
                        .latitude(geoLoc.getLatitude())
                        .longitude(geoLoc.getLongitude())
                        .cellReference(DataUtil.generateNumberRef(9))
                        .phoneStatus(phoneStatus)
                        .locationQuality("UNKNOWN")
                        .locationAddress("NIGERIA")
                        .haResponseCode(haResponseCode)
                        .responseCode("MTN/ATI:27+0+0+0")
                        .build());
                }
            //saving generated data to CSV file
            //DataUtil.saveAsCSV("TrackingReportData", objectMapper.writeValueAsString(trackingReportData));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trackingReportData;
    }

    public static CallLog generateCallLogData(String phoneNumber, String imsi, String imei, Contact contact, String time1, String time2) {

        RecordType recordType = null;

        int month = random.nextInt(12);
        int day = random.nextInt(29);
        int hour = random.nextInt(18);
        int minuites = random.nextInt(40);
        int seconds = random.nextInt(40);

        var phoneBookIndex = random.nextInt(21);

        GeoLocation geoLoc1 = DataUtil.getRandomGeoLocation(minLong, maxLong, minLat, maxLat);
        GeoLocation geoLoc2 = DataUtil.getRandomGeoLocation(minLong, maxLong, minLat, maxLat);

        switch (random.nextInt(2)){
            case 0:
                recordType = RecordType.TERM_SMS;
                break;
            case 1:
                recordType = RecordType.GPRS;
                break;
            default:
        }

        Phone phone = phones.get(random.nextInt(21));

        CallLog callLog = CallLog.builder()
            .logId(DataUtil.getSha54(UUID.randomUUID().toString()))
            .callStart(time1)
            .callingNumber(phoneNumber)
            .calledNumber(contact.getPhoneNumber())//randomize
            .duration(random.nextInt(700))//randomize
            .recordType(recordType)//randomize
            .firstCell("30-20889-50321")
            .firstSite("Some Place")
            .lastCell("30-20889-50321")
            .lastSite("Some Place2")
            .mainNumber(phoneNumber)
            .imsi(imsi)
            .imei(imei)
            .make(phone.getBrand())
            .model(phone.getModel())
            .firstLatitude(geoLoc1.getLatitude())
            .firstLongitude(geoLoc1.getLongitude())
            .lastLatitude(geoLoc2.getLatitude())
            .lastLongitude(geoLoc2.getLongitude())
            .callDate("2020-" + ((String.valueOf(month).toCharArray().length < 2)? "0"+month : month) + "-"
                    + ((String.valueOf(day).toCharArray().length < 2)? "0"+day : day))
            .callTime(time2)
            .build();

        return callLog;
   }

   public static void initializePresetData(){
        phones.add(Phone.builder().brand("TECNO").model("TECNO TECNO CF7").build());
        phones.add(Phone.builder().brand("TECNO").model("TECNO PHANTOM A7").build());
        phones.add(Phone.builder().brand("TECNO").model("TECNO PHANTOM A5").build());
        phones.add(Phone.builder().brand("TECNO").model("TECNO LITE7").build());
        phones.add(Phone.builder().brand("TECNO").model("TECNO P3").build());
        phones.add(Phone.builder().brand("SAMSUNG").model("SAMSUNG GALAXY S11").build());
        phones.add(Phone.builder().brand("VIVO").model("VIVO X21").build());
        phones.add(Phone.builder().brand("SAMSUNG").model("SAMSUNG GALAXY S8").build());
        phones.add(Phone.builder().brand("SAMSUNG").model("SAMSUNG NOTE 10 PLUS").build());
        phones.add(Phone.builder().brand("IPHONE").model("IPHONE 8 PLUS").build());
        phones.add(Phone.builder().brand("IPHONE").model("IPHONE XS").build());
        phones.add(Phone.builder().brand("NOKIA").model("NOKIA LUMIA 255").build());
        phones.add(Phone.builder().brand("LG").model("LG G6").build());
        phones.add(Phone.builder().brand("GOOGLE PIXEL").model("GOOGLE PIXEL 4XL").build());
        phones.add(Phone.builder().brand("MOTOROLA").model("MOTOROLA G5S PLUS").build());
        phones.add(Phone.builder().brand("NOKIA").model("NOKIA 8").build());
        phones.add(Phone.builder().brand("BLACKBERRY").model("KEYONE").build());
        phones.add(Phone.builder().brand("LENOVO").model("LENOVO K8").build());
        phones.add(Phone.builder().brand("SONY").model("XPERIA XZS").build());
        phones.add(Phone.builder().brand("HTC").model("HTC ONE X10").build());
        phones.add(Phone.builder().brand("HUAWEI").model("HUAWEI P10").build());

        sampleSms.add("Hey, thanks for shopping with Jumia! We’ve got tons of exciting deals in our upcoming Collection. ");
        sampleSms.add("Stay tuned or visit www.cstore.com to learn more about commercial trading");
        sampleSms.add("Hello, thanks for subscribing to USWines.");
        sampleSms.add("We’re eager to serve you better, pls reply to this msg now with R for Red Wine, W for White, P for Rose Wine.");
        sampleSms.add("Hi, your fav messaging partner now lets you instantly engage with social media leads! Tap http://bit.ly/2nOFcRS to get started.");
        sampleSms.add("Hey Brandon, we’ve stocked our shelves with assorted mint cookie treats for you.");
        sampleSms.add("Hey bro have you acquired the arsenal?");
        sampleSms.add("Prepare for the operation, we bomb the capital tomorrow");
        sampleSms.add("I will kill you if you told anyone");
        sampleSms.add("Hi, just wanted to say hi");
        sampleSms.add("My brother and sister are coming home now");
        sampleSms.add("I'll be travelling to america tommoow");
        sampleSms.add("when will you come to america");
        sampleSms.add("what time is it now?");
        sampleSms.add("its about 2:am");
        sampleSms.add("make sure the nuclear ewapon is in place");
        sampleSms.add("All units were killed");
        sampleSms.add("Elope with the kids before he finds out");
        sampleSms.add("i'll be sending money tomorrow");
        sampleSms.add("Hi, i've missed you, where are you?");

        imageInternalStorage.add("Internal shared storage/Pictures/Screenshot/screenshot");
        imageInternalStorage.add("Internal shared storage/DCIM/Camera/");
        imageInternalStorage.add("Internal shared storage/Xender/Camera/");
        fileInternalStorage.add("Internal shared storage/mtklog/mobilelog/");
        fileInternalStorage.add("Internal shared storage/");
        fileInternalStorage.add("Internal shared storage/mtklog/gpsdbglog/");
       fileInternalStorage.add("Internal shared storage/Android/data/");
   }

   public static void generateDeviceDataSet() throws IOException, InterruptedException {

        FileWriter fw = new FileWriter("/home/revolt/Documents/NifiData/device.log", true);
        BufferedWriter bw = new BufferedWriter(fw);

        Device device = null;
        int count = 0, repeat = 0;
       while (devices.size() < 50) {

           Thread.sleep(5000);

           Phone phone = phones.get(random.nextInt(21));
           String imei = DataUtil.generateIMEI();

           while (IMEI.get(imei) != null) {
               imei = DataUtil.generateIMEI();
           }
           IMEI.put(imei, imei);

           String iccid = DataUtil.generateNumberRef(19);
           while (ICCID.get(iccid) != null) {
               iccid = DataUtil.generateNumberRef(19);
           }
           ICCID.put(iccid, iccid);

           String imsi = DataUtil.generateNumberRef(19);
           while (IMSI.get(imsi) != null) {
               imsi = DataUtil.generateNumberRef(19);
           }
           IMSI.put(imsi, imsi);

           int month = random.nextInt(12)+1;
           int month1 = random.nextInt(5)+1;
           int day = random.nextInt(29);
           int hour = random.nextInt(18);
           int minuites = random.nextInt(40);
           int seconds = random.nextInt(40);

           String nc = null;
           switch (random.nextInt(2)){
               case 0:
                   nc = "070";
                   break;

               case 1:
                   nc = "080";
                   break;

               case 2:
                   nc = "090";
                   break;

               case 3:
                   nc = "081";
                   break;

               default:
           }
           String phoneNumber = "";
           while(deviceOwnerNumbers.get(phoneNumber = nc+DataUtil.generateNumberRef(8)) != null){

           }
           deviceOwnerNumbers.put(phoneNumber, phoneNumber);

           device = createdUserDevice.get(random.nextInt(50));
           if(device != null){
               device.setContacts(generateDeviceContact());
               //device.setTrackingReports(generateTrackingData(count, imsi, imei));
               device.setDocumentFiles(generateDeviceFiles());
               device.setCallLogs(callLogs);
               device.setCalls(generateDeviceCalls(phoneNumber, imsi, imei, device.getContacts()));
               device.setSms(generateDeviceMessages(phoneNumber, device.getContacts()));
               device.setImageFiles(generateDeviceImages(device.getSelectedManufacture(), device.getSelectedModel()));

               repeat++;
               log.info("repeat "+repeat);
           }else {
               device = Device.builder()
                       .reportType("cell")
                       .selectedManufacture(phone.getBrand())
                       .selectedModel(phone.getModel())
                       .detectedManufacture(phone.getBrand())
                       .detectedModel(phone.getBrand())
                       .revision("8." + random.nextInt() + "." + random.nextInt() + " " + DataUtil.generateNumberRef(6) + " BCDEFH-" + DataUtil.generateNumberRef(6) + "V237")
                       .imei(imei)
                       .iccid(iccid)
                       .imsi(imsi)
                       .advertisingId(DataUtil.generateNumberRef(6) + "ef-e" + DataUtil.generateNumberRef(3) + "-" + DataUtil.generateNumberRef(4) + "-" + DataUtil.generateNumberRef(3) + "c-" + DataUtil.generateNumberRef(4) + "cd9e2acf")
                       .startDateTime(((String.valueOf(day).toCharArray().length < 2) ? "0" + day : day) + "/"
                               + ((String.valueOf(month).toCharArray().length < 2) ? "0" + month : month) + "/2020" + " " + ((String.valueOf(hour).toCharArray().length < 2) ? "0" + hour : hour)
                               + ":" + ((String.valueOf(minuites).toCharArray().length < 2) ? "0" + minuites : minuites) + ":" + ((String.valueOf(seconds).toCharArray().length < 2) ? "0" + seconds : seconds))

                       .endDateTime(((String.valueOf(day).toCharArray().length < 2) ? "0" + day : day) + "/"
                               + ((String.valueOf(month).toCharArray().length < 2) ? "0" + month : month) + "/2020" + " " + ((String.valueOf(hour).toCharArray().length < 2) ? "0" + hour : hour)
                               + ":" + ((String.valueOf(minuites).toCharArray().length < 2) ? "0" + minuites : minuites) + ":" + ((String.valueOf(seconds).toCharArray().length < 2) ? "0" + seconds : seconds))

                       .phoneDateTime((((String.valueOf(day).toCharArray().length < 2) ? "0" + day : day) + "/"
                               + ((String.valueOf(month).toCharArray().length < 2) ? "0" + month1 : month1) + "/2021" + " " + ((String.valueOf(hour).toCharArray().length < 2) ? "0" + hour : hour)
                               + ":" + ((String.valueOf(minuites).toCharArray().length < 2) ? "0" + minuites : minuites) + ":" + ((String.valueOf(seconds).toCharArray().length < 2) ? "0" + seconds : seconds)))
                       .connectionType("USB Cable")
                       .xmlFormat("1." + random.nextInt(9) + "." + random.nextInt(9) + "." + random.nextInt(9))
                       .software("7." + random.nextInt(45) + "." + random.nextInt(9) + "." + random.nextInt(90) + " UFED")
                       .serial(DataUtil.generateNumberRef(7))
                       .usingclient(random.nextInt(4) + 1)
                       .contacts(generateDeviceContact())
                       .trackingReports(generateTrackingData(count, imsi, imei))
                       .documentFiles(generateDeviceFiles())
                       .callLogs(callLogs)
                       .build();

               device.setCalls(generateDeviceCalls(phoneNumber, imsi, imei, device.getContacts()));
               device.setSms(generateDeviceMessages(phoneNumber, device.getContacts()));
               device.setImageFiles(generateDeviceImages(device.getSelectedManufacture(), device.getSelectedModel()));
               devices.add(device);
               count += 1;

               log.info(" data "+count);
           }
           createdUserDevice.put(count, device);
           String data = objectMapper.writeValueAsString(device);

           bw.write(data);
           bw.newLine();

       }
       bw.close();
   }

    public static List<Contact> generateDeviceContact() throws JsonProcessingException {
       contacts.clear();
        int x = 1;

        int bound = random.nextInt(6);

        while(contacts.size() < bound+1){
            log.info("generating "+bound);
            String nc = null;

            switch (random.nextInt(4)){
                case 0:
                    nc = "070";
                    break;

                case 1:
                    nc = "080";
                    break;

                case 2:
                    nc = "090";
                    break;

                case 3:
                    nc = "081";
                    break;

                default:
            }
            String phoneNumber = nc+DataUtil.generateNumberRef(8);

            while (deviceContacts.get(phoneNumber) != null) {
                phoneNumber = nc+DataUtil.generateNumberRef(8);
            }
            deviceContacts.put(phoneNumber, phoneNumber);

            String designation = null;
            switch (random.nextInt(3)){
                case 0:
                    designation = "General";
                    break;

                case 1:
                    designation = "Home";
                    break;

                case 2:
                    designation = "Mobile";
                    break;

                case 3:
                    designation = "Office";
                    break;

                default:
            }

            contacts.add(Contact.builder()
                    .contactId(DataUtil.getSha54(UUID.randomUUID().toString()))
                    .name("firstname"+x+" surname"+x)
                    .memory("Phone")
                    .designation(designation)
                    .phoneNumber(phoneNumber)
                    .email("")
                    .build());

            x++;
       }
       return contacts;
   }

    public static List<Call> generateDeviceCalls(String phoneNumber, String imsi, String imei, List<Contact> contacts) throws JsonProcessingException {
       callLogs.clear();
       calls.clear();

       int x = 1;
       int bound = random.nextInt(6);
       CallType callType = null;

       while(calls.size() < bound+1) {
           int month = random.nextInt(12)+1;
           int day = random.nextInt(29);
           int hour = random.nextInt(18);
           int minuites = random.nextInt(40);
           int seconds = random.nextInt(40);


           switch (random.nextInt(3)) {
               case 0:
                   callType = CallType.INCOMING;
                   break;

               case 1:
                   callType = CallType.MISSED;
                   break;

               case 2:
                   callType = CallType.OUTGOING;
                   break;
               default:
           }

           Contact contact = contacts.get(random.nextInt(contacts.size()));

           int day1 = ((String.valueOf(day).toCharArray().length < 2)? Integer.valueOf("0"+day) : day);
           int month1 = ((String.valueOf(month).toCharArray().length < 2)? Integer.valueOf("0"+month) : month);
           int hours1 = ((String.valueOf(hour).toCharArray().length < 2)? Integer.valueOf("0"+hour) : hour);
           int minuites1 = ((String.valueOf(minuites).toCharArray().length < 2)? Integer.valueOf("0"+minuites) : minuites);
           int seconds1 = ((String.valueOf(seconds).toCharArray().length < 2)? Integer.valueOf("0"+seconds) : seconds);

           int minuites2 = ((String.valueOf(minuites).toCharArray().length < 2)? Integer.valueOf("0"+minuites) : minuites);
           int seconds2 = ((String.valueOf(seconds).toCharArray().length < 2)? Integer.valueOf("0"+seconds) : seconds);

           Call call = Call.builder()
                   .callId(DataUtil.getSha54(UUID.randomUUID().toString()))
                   .type(callType)
                   .number(contact.getPhoneNumber())
                   .name(contact.getName())
                   .timestamp( day1+ "/" + month1 +"/2020"  + " " + hours1 + ":" + minuites1 + ":" + seconds1)
                   .duration("00:"+minuites2+":"+seconds2)
                   .build();

           calls.add(call);

           callLogs.add(generateCallLogData(phoneNumber, imsi, imei, contact, call.getTimestamp(), call.getDuration()));
           x++;
       }

       return calls;
   }

    public static List<Sms> generateDeviceMessages(String phoneNumber, List<Contact> contacts) throws JsonProcessingException {
        messages.clear();
        int x = 1;
       int bound = random.nextInt(6);

        while(messages.size() < bound+1){
            int month = random.nextInt(12)+1;
            int day = random.nextInt(29);
            int hour = random.nextInt(18);
            int minuites = random.nextInt(40);
            int seconds = random.nextInt(40);

            MsgStatus msgStatus = null;
            MsgType msgType = null;

            switch (random.nextInt(2)){
                case 0:
                    msgStatus = MsgStatus.READ;
                    break;

                case 1:
                    msgStatus = MsgStatus.UNREAD;
                    break;
                default:
            }
            switch (random.nextInt(2)){
                case 0:
                    msgType = MsgType.OUTGOING;
                    break;

                case 1:
                    msgType = MsgType.INCOMING;
                    break;
                default:
            }

            Contact contact = contacts.get(random.nextInt(contacts.size()));

            messages.add(Sms.builder()
                    .smsId(DataUtil.getSha54(UUID.randomUUID().toString()))
                    .number(contact.getPhoneNumber())
                    .name(contact.getName())
                    .timestamp(((String.valueOf(day).toCharArray().length < 2)? "0"+day : day) + "/"
                            + ((String.valueOf(month).toCharArray().length < 2)? "0"+month : month)+"/2020"  + " " + ((String.valueOf(hour).toCharArray().length < 2)? "0"+hour : hour)
                            + ":" + ((String.valueOf(minuites).toCharArray().length < 2)? "0"+minuites : minuites)  + ":" + ((String.valueOf(seconds).toCharArray().length < 2)? "0"+seconds : seconds))

                    .status(msgStatus)
                    .folder("Inbox")
                    .storage("Phone")
                    .type(msgType)
                    .text(sampleSms.get(random.nextInt(20)))
                    .smsc(phoneNumber)
                    .build());

            x++;
        }
        return messages;
   }

    public static List<ImageFile> generateDeviceImages(String manufacturer, String model) throws JsonProcessingException {
    images.clear();
        int x =1;
        int bound = random.nextInt(6+1);

        while(images.size() < bound){
            int month = random.nextInt(12)+1;
            int day = random.nextInt(29);
            int hour = random.nextInt(18);
            int minuites = random.nextInt(40);
            int seconds = random.nextInt(40);

            int height = random.nextInt(100);
            images.add(ImageFile.builder()
                .imageId(DataUtil.getSha54(UUID.randomUUID().toString()))
                .name("screenshot_2021"+month+""+day+"_"+x+"png")
                .storedName("screenshot_2021"+month+""+day+"_"+x+"png")
                .thumbLocation("Images/screenshot_2021"+month+""+day+"_"+x+"png")
                .path(imageInternalStorage.get(random.nextInt(3)))
                .memory("Phone")
                .size(Integer.valueOf(DataUtil.generateNumberRef(4)))
                .dateTime(((String.valueOf(day).toCharArray().length < 2)? "0"+day : day) + "/"
                        + ((String.valueOf(month).toCharArray().length < 2)? "0"+month : month)+"/2020"  + " " + ((String.valueOf(hour).toCharArray().length < 2)? "0"+hour : hour)
                        + ":" + ((String.valueOf(minuites).toCharArray().length < 2)? "0"+minuites : minuites)  + ":" + ((String.valueOf(seconds).toCharArray().length < 2)? "0"+seconds : seconds))

                .dateTimeModified(((String.valueOf(day).toCharArray().length < 2)? "0"+day : day) + "/"
                        + ((String.valueOf(month).toCharArray().length < 2)? "0"+month : month)+"/2020"  + " " + ((String.valueOf(hour).toCharArray().length < 2)? "0"+hour : hour)
                        + ":" + ((String.valueOf(minuites).toCharArray().length < 2)? "0"+minuites : minuites)  + ":" + ((String.valueOf(seconds).toCharArray().length < 2)? "0"+seconds : seconds))

                .resolution(String.valueOf(height+10+" X "+height))
                .exifDateTime(((String.valueOf(day).toCharArray().length < 2)? "0"+day : day) + "/"
                        + ((String.valueOf(month).toCharArray().length < 2)? "0"+month : month)+"/2020"  + " " + ((String.valueOf(hour).toCharArray().length < 2)? "0"+hour : hour)
                        + ":" + ((String.valueOf(minuites).toCharArray().length < 2)? "0"+minuites : minuites)  + ":" + ((String.valueOf(seconds).toCharArray().length < 2)? "0"+seconds : seconds))
                .resolution(String.valueOf(height+10+" X "+height))
                .pixelResolution(String.valueOf(height+10+" X "+height))
                .cameraMake(manufacturer)
                .cameraModel(model)
                .build());

            x++;
        }

       return  images;
   }

    public static List<DocumentFile> generateDeviceFiles() throws JsonProcessingException {
        files.clear();
        int x = 1;
        int bound = random.nextInt(6+1);

        while (files.size() < bound) {
            int month = random.nextInt(12)+1;
            int day = random.nextInt(29);
            int hour = random.nextInt(18);
            int minuites = random.nextInt(40);
            int seconds = random.nextInt(40);

            files.add(DocumentFile.builder()
                    .docId(DataUtil.getSha54(UUID.randomUUID().toString()))
                    .name("document"+1)
                    .storedName("000~file"+x+".txt")
                    .path(fileInternalStorage.get(random.nextInt(4)))
                    .memory(((random.nextInt(2) == 0)? "Phone" : "SD card"))
                    .size(Integer.valueOf(DataUtil.generateNumberRef(4)))
                    .dateTime(((String.valueOf(day).toCharArray().length < 2)? "0"+day : day) + "/"
                            + ((String.valueOf(month).toCharArray().length < 2)? "0"+month : month)+"/2020"  + " " + ((String.valueOf(hour).toCharArray().length < 2)? "0"+hour : hour)
                            + ":" + ((String.valueOf(minuites).toCharArray().length < 2)? "0"+minuites : minuites)  + ":" + ((String.valueOf(seconds).toCharArray().length < 2)? "0"+seconds : seconds))
                    .dateTimeModified(((String.valueOf(day).toCharArray().length < 2)? "0"+day : day) + "/"
                            + ((String.valueOf(month).toCharArray().length < 2)? "0"+month : month)+"/2020"  + " " + ((String.valueOf(hour).toCharArray().length < 2)? "0"+hour : hour)
                            + ":" + ((String.valueOf(minuites).toCharArray().length < 2)? "0"+minuites : minuites)  + ":" + ((String.valueOf(seconds).toCharArray().length < 2)? "0"+seconds : seconds))
                    .build());

            x++;
        }
        return files;
    }

    /*
       convert => {
          "duration" => "integer"
          "firstLongitude" => "float"
          "firstLatitude" => "float"
          "lastLongitude" => "float"
          "lastLatitude" => "float"
	      "imsi" => "string"
	      "lastSite" => "string"
     }
     add_field => ["source", "%{firstLatitude},%{firstLongitude}"]
     add_field => ["destination", "%{lastLatitude},%{lastLongitude}"]
    */

   /* TASK TABLE
    *
    * RESEARCH ON KIBANA VISUALIZATION //
    * RESOLVE AND RESEARCH DATA RELATIONSHIP ON ELASTIC SEARCH //
    * GENERATE TEST DATA ACROSS DATASETS AND EFFECT RELATIONSHIP
    * DEFINE DATA MAPPINGS AND INJECT DATA INTO ELASTICSEARCH
    *
    * CREATE AGGREGATIONS AND VISUALIZE DATA IN ELASTICSEARCH.
    */
}

























