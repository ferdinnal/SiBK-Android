package com.sibk.tasik.Utility;


import java.util.Random;

public class Website {
    private String domain;
    //domain di arahkan ke halaman Codeigniter
    private String new_domain;
    //domain di arahkan ke halaman Codeigniter HTTP
    private String main_domain;
    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    public Website()
    {

        this.domain="http://192.168.1.108/sibkapi/api";
        this.new_domain="http://192.168.1.108/sibkapi/api";
        this.main_domain="http://192.168.1.108/sibkapi";
    }

    public String getDomain()
    {
        return this.domain;
    }

    public String getMainDomain()
    {
        return this.main_domain;
    }

    public String getHash()
    {
        int sizeOfRandomString=20;
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    public String getNewDomain()
    {
        return this.new_domain;
    }
}
