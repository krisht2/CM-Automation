package Devices;

import java.util.*;

public class Devices{
    public int width;
    public int height;
    public String agent;
    public int pixelRatio;

    public static Devices samsungS9=new Devices(360,740,"Mozilla/5.0 (Linux; Android 9; SM-G965F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.62 Mobile Safari/537.36",4);

    public static Devices samsungS9_Landscape=new Devices(740,360,"Mozilla/5.0 (Linux; Android 9; SM-G965F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.62 Mobile Safari/537.36",4);


    public static Devices iPhoneX=new Devices(375,812,"Mozilla/5.0 (iPhone; CPU iPhone OS 12_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.2 Mobile/15E148 Safari/604.1",3);
    public static Devices iPhone6S=new Devices(375,667,"Mozilla/5.0 (iPhone; CPU iPhone OS 12_4_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.2 Mobile/15E148 Safari/604.1",2);
    public static Devices iPhone5=new Devices(320,568,"Mozilla/5.0 (iPhone; CPU iPhone OS 12_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.1 Mobile/15E148 Safari/604.1\n",2);
    public static Devices iPad=new Devices(768,1024,"Mozilla/5.0 (iPad; CPU OS 11_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.0 Mobile/15E148 Safari/604.1",2);

    public static Devices iPhoneX_Landscape=new Devices(812,375,"Mozilla/5.0 (iPhone; CPU iPhone OS 12_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.2 Mobile/15E148 Safari/604.1",3);
    public static Devices iPhone6S_Landscape=new Devices(667,375,"Mozilla/5.0 (iPhone; CPU iPhone OS 12_4_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.2 Mobile/15E148 Safari/604.1",2);
    public static Devices iPhone5_Landscape=new Devices(568,320,"Mozilla/5.0 (iPhone; CPU iPhone OS 12_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.1 Mobile/15E148 Safari/604.1\n",2);
    public static Devices iPad_Landscape=new Devices(1024,768,"Mozilla/5.0 (iPad; CPU OS 11_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.0 Mobile/15E148 Safari/604.1",2);

    public static List <Devices> allDevices= new ArrayList<>(Arrays.asList(samsungS9,iPhoneX,iPhone6S,iPhone5,iPad,samsungS9_Landscape,iPhoneX_Landscape,iPhone6S_Landscape,iPhone5_Landscape,iPad_Landscape));
    public static List <Devices> landscapeDevices= new ArrayList<>(Arrays.asList(samsungS9_Landscape,iPhoneX_Landscape,iPhone6S_Landscape,iPhone5_Landscape,iPad_Landscape));
    public static List <Devices> potraitDevices= new ArrayList<>(Arrays.asList(samsungS9,iPhoneX,iPhone6S,iPhone5,iPad));


    public Devices(int width,int height,String agent, int pixelRatio){
        this.width=width;
        this.height=height;
        this.agent=agent;
        this.pixelRatio=pixelRatio;
    }

    public static Map<String,Object> sendEmulator(Devices device){
        Map<String, Object> emulator = new HashMap<>();

        emulator.put("width", device.width);
        emulator.put("height", device.height);
        emulator.put("pixelRatio", device.pixelRatio);

        Map<String, Object> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceMetrics", emulator);
        mobileEmulation.put("userAgent",device.agent);

        return  mobileEmulation;
    }
}