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

import java.util.*;
import java.util.logging.Logger;

public class DataGenerator {
    private static List<Phone> phones = new ArrayList<>();
    private static List<Device> devices = new ArrayList<>();
    private static List<Contact> contacts = new ArrayList<>();
    private static List<Call> calls = new ArrayList<>();
    private static List<Sms> messages = new ArrayList<>();
    private static List<ImageFile> images = new ArrayList<>();
    private static List<DocumentFile> files = new ArrayList<>();

    private static List<String> sampleSms = new ArrayList<>();

    private static List<String> imageInternalStorage = new ArrayList<>();
    private static List<String> fileInternalStorage = new ArrayList<>();

    private static Map<String, String> IMEI = new HashMap<>();
    private static Map<String, String> ICCID = new HashMap<>();
    private static Map<String, String> IMSI = new HashMap<>();
    private static Map<String, String> ADVERTISINGID = new HashMap<>();
    private static Map<String, String> deviceContacts = new HashMap<>();

    private static Map<String, List<Contact>> userContacts = new HashMap<>();

    private static Logger log = Logger.getLogger(DataGenerator.class.getName());
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static Random random = new Random();
    private static final int minLat = 5, minLong =5, maxLat = 10, maxLong = 10;

    public static void main(String[] args){

        log.info("generating device data....");

        try {
            initializePresetData();

            //generateTrackingData(1000);
            //generateCallLogData(1000);
            generateDeviceDataSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TrackingReport generateTrackingData(int dataSize, String imsi, String imei) {
        try {
            final var IMEI = DataUtil.generateIMEI();
            PhoneStatus phoneStatus = null;
            Integer haResponseCode = null;
            List<TrackingReport> trackingReportData = new ArrayList<>();

                int month = random.nextInt(12);
                int day = random.nextInt(29);
                int hour = random.nextInt(18);
                int minuites = random.nextInt(40);
                int seconds = random.nextInt(40);

                GeoLocation geoLoc = DataUtil.getRandomGeoLocation(minLong, maxLong, minLat, maxLat);

                switch (random.nextInt(2)){
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

                return TrackingReport.builder()
                        .Alias("Test_sim")
                        .Username("USER"+dataSize)
                        .MSISDN(DataUtil.generateNumberRef(11))
                        .IMSI(imsi)
                        .IMEI(imei)
                        .Type("MSISDN")
                        .QueryDate(((String.valueOf(day).toCharArray().length < 2)? "0"+day : day) + "/"
                                + ((String.valueOf(month).toCharArray().length < 2)? "0"+month : month)+"/2020"  + " " + ((String.valueOf(hour).toCharArray().length < 2)? "0"+hour : hour)
                                + ":" + ((String.valueOf(minuites).toCharArray().length < 2)? "0"+minuites : minuites)  + ":" + ((String.valueOf(seconds).toCharArray().length < 2)? "0"+seconds : seconds))

                        .HALatitude(geoLoc.getLatitude())
                        .HALongitude(geoLoc.getLongitude())
                        .Latitude(geoLoc.getLatitude())
                        .Longitude(geoLoc.getLongitude())
                        .CellReference(DataUtil.generateNumberRef(9))
                        .PhoneStatus(phoneStatus)
                        .LocationQuality("UNKNOWN")
                        .LocationAddress("NIGERIA")
                        .HAResponseCode(haResponseCode)
                        .ResponseCode("MTN/ATI:27+0+0+0")
                        .build();

            //saving generated data to CSV file
            //DataUtil.saveAsCSV("TrackingReportData", objectMapper.writeValueAsString(trackingReportData));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void generateCallLogData(int dataSize) {

        try{

            List<CallLog> callLogData = new ArrayList<>();
            RecordType recordType = null;
            List<String> phoneBook = DataUtil.getPhoneBook();

            for(var i=1; i<=dataSize; i++){

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

                callLogData.add(CallLog.builder()
                        .CallStart("2020-" + ((String.valueOf(month).toCharArray().length < 2)? "0"+month : month) + "-"
                                + ((String.valueOf(day).toCharArray().length < 2)? "0"+day : day) + "T" + ((String.valueOf(hour).toCharArray().length < 2)? "0"+hour : hour)
                                + ":" + ((String.valueOf(minuites).toCharArray().length < 2)? "0"+minuites : minuites)  + ":" + ((String.valueOf(seconds).toCharArray().length < 2)? "0"+seconds : seconds))

                        .CallingNumber(phoneNumber)
                        .CalledNumber(phoneBook.get((phoneBookIndex == 0)? 20 : phoneBookIndex))//randomize
                        .Duration(random.nextInt(700))//randomize
                        .RecordType(recordType)//randomize
                        .FirstCell("30-20889-50321")
                        .FirstSite("Some Place1")
                        .LastCell("30-20889-50321")
                        .LastSite("Some Place2")
                        .MainNumber(phoneNumber)
                        .Imsi(DataUtil.generateNumberRef(12))
                        .Imei(DataUtil.generateIMEI())
                        .Make(phone.getBrand())
                        .Model(phone.getModel())
                        .FirstLatitude(geoLoc1.getLatitude())
                        .FirstLongitude(geoLoc1.getLongitude())
                        .LastLatitude(geoLoc2.getLatitude())
                        .LastLongitude(geoLoc2.getLongitude())
                        .CallDate("2020-" + ((String.valueOf(month).toCharArray().length < 2)? "0"+month : month) + "-"
                                + ((String.valueOf(day).toCharArray().length < 2)? "0"+day : day))
                        .CallTime("00:"+((String.valueOf(minuites).toCharArray().length < 2)? "0"+minuites : minuites)+":"+((String.valueOf(seconds).toCharArray().length < 2)? "0"+seconds : seconds))
                        .build());
            }
            //saving generated data to CSV file
            DataUtil.saveAsCSV("CallData", objectMapper.writeValueAsString(callLogData));
        }catch (Exception e){
            e.printStackTrace();
        }
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

   public static void generateDeviceDataSet() throws JsonProcessingException {
        Device device = null;
        int count = 1;
       while (devices.size() < 200) {
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
                   .startDateTime(((String.valueOf(day).toCharArray().length < 2)? "0"+day : day) + "/"
                           + ((String.valueOf(month).toCharArray().length < 2)? "0"+month : month)+"/2020"  + " " + ((String.valueOf(hour).toCharArray().length < 2)? "0"+hour : hour)
                           + ":" + ((String.valueOf(minuites).toCharArray().length < 2)? "0"+minuites : minuites)  + ":" + ((String.valueOf(seconds).toCharArray().length < 2)? "0"+seconds : seconds))

                   .endDateTime(((String.valueOf(day).toCharArray().length < 2)? "0"+day : day) + "/"
                           + ((String.valueOf(month).toCharArray().length < 2)? "0"+month : month)+"/2020"  + " " + ((String.valueOf(hour).toCharArray().length < 2)? "0"+hour : hour)
                           + ":" + ((String.valueOf(minuites).toCharArray().length < 2)? "0"+minuites : minuites)  + ":" + ((String.valueOf(seconds).toCharArray().length < 2)? "0"+seconds : seconds))

                   .phoneDateTime((((String.valueOf(day).toCharArray().length < 2)? "0"+day : day) + "/"
                           + ((String.valueOf(month).toCharArray().length < 2)? "0"+month1 : month1)+"/2021"  + " " + ((String.valueOf(hour).toCharArray().length < 2)? "0"+hour : hour)
                           + ":" + ((String.valueOf(minuites).toCharArray().length < 2)? "0"+minuites : minuites)  + ":" + ((String.valueOf(seconds).toCharArray().length < 2)? "0"+seconds : seconds)))
                   .connectionType("USB Cable")
                   .xmlFormat("1." + random.nextInt(9) + "." + random.nextInt(9) + "." + random.nextInt(9))
                   .software("7." + random.nextInt(45) + "." + random.nextInt(9) + "." + random.nextInt(90)+" UFED")
                   .serial(DataUtil.generateNumberRef(7))
                   .usingclient(random.nextInt(4)+1)
                   .trackingReports(generateTrackingData(count, imsi, imei))
                   .build();
           devices.add(device);

           count += 1;
       }
       DataUtil.saveAsCSV("device", objectMapper.writeValueAsString(devices));

       generateDeviceContact(device);
       generateDeviceCalls(device);
       generateDeviceMessages(device);
       generateDeviceImages(device);
       generateDeviceFiles(device);
   }

   public static void generateDeviceContact(Device device) throws JsonProcessingException {
       contacts.clear();
        int x = 1;
        int bound = random.nextInt(100);

        while(contacts.size() < bound){
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
                    .name("firstname"+x+" surname"+x)
                    .memory("Phone")
                    .imei(device.getImei())
                    .designation(designation)
                    .phoneNumber(phoneNumber)
                    .email("")
                    .build());

            x++;
       }
        userContacts.put(device.getImei(), contacts);
       DataUtil.saveAsCSV("device-contact", objectMapper.writeValueAsString(contacts));
   }

   public static void generateDeviceCalls(Device device) throws JsonProcessingException {
       int x = 1;
       int bound = random.nextInt(1000);
       CallType callType = null;

       while(calls.size() < bound) {
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

           Contact contact = userContacts.get(device.getImei()).get(random.nextInt(userContacts.get(device.getImei()).size()));
           calls.add(Call.builder()
                   .imei(devices.get(random.nextInt(200)).getImei())
                   .type(callType)
                   .number(contact.getPhoneNumber())
                   .name(contact.getName())
                   .timestamp(((String.valueOf(day).toCharArray().length < 2)? "0"+day : day) + "/"
                           + ((String.valueOf(month).toCharArray().length < 2)? "0"+month : month)+"/2020"  + " " + ((String.valueOf(hour).toCharArray().length < 2)? "0"+hour : hour)
                           + ":" + ((String.valueOf(minuites).toCharArray().length < 2)? "0"+minuites : minuites)  + ":" + ((String.valueOf(seconds).toCharArray().length < 2)? "0"+seconds : seconds))

                   .duration("00:"+((String.valueOf(minuites).toCharArray().length < 2)? "0"+minuites : minuites)+":"+((String.valueOf(seconds).toCharArray().length < 2)? "0"+seconds : seconds))
                   .build());

           x++;
           }
       DataUtil.saveAsCSV("device-call", objectMapper.writeValueAsString(calls));
       }

       public static void generateDeviceMessages(Device device) throws JsonProcessingException {
            messages.clear();
            int x = 1;
           int bound = random.nextInt(1000);

            while(messages.size() < bound){
                int month = random.nextInt(12)+1;
                int day = random.nextInt(29);
                int hour = random.nextInt(18);
                int minuites = random.nextInt(40);
                int seconds = random.nextInt(40);

                Contact contact = userContacts.get(device.getImei()).get(random.nextInt(userContacts.get(device.getImei()).size()));
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

                String nc = null, name = null;
                switch (random.nextInt(4)){
                    case 0:
                        nc = "+23470";
                        name = "Airtel";
                        break;

                    case 1:
                        nc = "+23480";
                        name = "GLO";
                        break;

                    case 2:
                        nc = "+23490";
                        name = "Etisalat";
                        break;

                    case 3:
                        nc = "+23481";
                        name = "MTN";
                        break;

                    default:
                }
                String phoneNumber = nc+DataUtil.generateNumberRef(8);

                messages.add(Sms.builder()
                        .imei(devices.get(random.nextInt(200)).getImei())
                        .number(contact.getPhoneNumber())
                        .name(name)
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
           DataUtil.saveAsCSV("device-sms", objectMapper.writeValueAsString(messages));
       }

       public static void generateDeviceImages(Device device) throws JsonProcessingException {
        images.clear();
            int x =1;
           int bound = random.nextInt(1000);

            while(images.size() < bound){
                int month = random.nextInt(12)+1;
                int day = random.nextInt(29);
                int hour = random.nextInt(18);
                int minuites = random.nextInt(40);
                int seconds = random.nextInt(40);

                int height = random.nextInt(100);
                images.add(ImageFile.builder()

                        .imei(device.getImei())
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
                        .cameraMake(device.getDetectedManufacture())
                        .cameraModel(device.getDetectedModel())
                        .build());

                x++;
            }

           DataUtil.saveAsCSV("device-image", objectMapper.writeValueAsString(images));
       }

    public static void generateDeviceFiles(Device device) throws JsonProcessingException {
        files.clear();
        int x = 1;
        int bound = random.nextInt(1000);

        while (files.size() < bound) {
            int month = random.nextInt(12)+1;
            int day = random.nextInt(29);
            int hour = random.nextInt(18);
            int minuites = random.nextInt(40);
            int seconds = random.nextInt(40);

            files.add(DocumentFile.builder()
                    .imei(device.getImei())
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
        DataUtil.saveAsCSV("device-file", objectMapper.writeValueAsString(files));
    }
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

























