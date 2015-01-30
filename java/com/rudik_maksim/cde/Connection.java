package com.rudik_maksim.cde;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Максим on 19.03.14.
 */
public class Connection{
    protected String host = "https://de.ifmo.ru/";
    protected String login = "";
    protected String password = "";
    protected CookieStore cStore;
    protected CookieManager cManager;

    Connection(){}
    Connection(String login, String password){
        this.login = login;
        this.password = password;
    }

    public boolean Connect() throws IOException {
        URL de = new URL(host + "servlet/?Rule=LOGON&LOGIN=" + login + "&PASSWD=" + password);
        HttpURLConnection conn;
        InputStream stream;

        cManager = new CookieManager();
        cManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cManager);

        conn  = (HttpURLConnection)de.openConnection();

        long len = (long)conn.getContentLength();
        if (len != 0){
            stream = new BufferedInputStream(conn.getInputStream());
            int c;
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            while (((c = stream.read())!=-1)){
                b.write(c);
            }
            String code = b.toString("Cp1251");
            if (code.contains("Invalid"))
                return false;

            cStore =  cManager.getCookieStore();
            List<HttpCookie> cookies = cStore.getCookies();

            stream.close();
        }

        Global.Application.SHARED_PREFS_NAME = login + "_cde_prefs";
        Global.Application.FILE_PROTOCOL = login + "_file_protocol.txt";

        return true;
    }

    public String getLogin(){
        return login;
    }

    public String getPassword(){
        return password;
    }
}