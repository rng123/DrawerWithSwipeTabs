package com.androidbelieve.drawerwithswipetabs;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Manohar on 09-11-2016.
 */
public class MyAds
{
        public String subcat;
        private String status;
        private String specs;
        private String price;
        private String date;
        private String rentw;
        private String rentm;
        private String per;
        private String aid;
        private String link;
        private int image_ads;
        private String slink;
        public MyAds() {
        }

    static String Month(Date d)
    {
        int i=d.getMonth();
        switch(i)
        {
            case 0: return "Jan";
            case 1: return "Feb";
            case 2: return "Mar";
            case 3: return "Apr";
            case 4: return "May";
            case 5: return "Jun";
            case 6: return "July";
            case 7: return "Aug";
            case 8: return "Sept";
            case 9: return "Oct";
            case 10: return "Nov";
            case 11: return "Dec";
            default:return "Dec";
        }

    }

        public MyAds(String status, String specs, String price, String date, int image_ads) {
            this.status = status;
            this.specs = specs;
            this.price = price;
            this.date = date;
            this.image_ads=image_ads;
        }


    public MyAds(String status, String specs, String price, String date, int image_ads,String subcat) {
        this.status = status;
        this.specs = specs;
        this.price = price;
        this.date = date;
        this.image_ads=image_ads;
        this.subcat=subcat;
    }
        public MyAds(String status, String specs, String price, String timestamp, String aid,String subcat)throws ParseException {
            this.status = status;
            this.specs = specs;
            this.price = price;
            this.subcat=subcat;
            this.aid=aid;
            link=Config.link+"viewthumb.php?aid="+aid;
            Date today=new Date();
            Date date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timestamp);
            date.setTime(date.getTime()+19800000);
            Log.v("timeStamp",timestamp);
            Log.v("date",date.toString());
            if(today.getDay()==date.getDay())
                this.date="Today ";
            else if(today.getDay()==date.getDay()+1)
                this.date="Yesterday ";
            else
            {

                //this.date=date.getDay()+" "+Month(date)+" ";
                this.date=new SimpleDateFormat("d MMMM").format(date);
                if(!(today.getYear()==date.getYear()))
                    this.date+=" "+date.getYear()+" ";
            }
            Log.v("Date written",this.date);

        }
    public MyAds(String status, String specs, String price, String timestamp, String aid,String subcat, String rentw, String rentm, String per)throws ParseException {
        this.status = status;
        this.specs = specs;
        this.price = price;
        this.subcat=subcat;
        this.aid=aid;
        this.rentw = rentw;
        this.rentm = rentm;
        this.per = per;
        slink=Config.link+"imgthumbnail.php?id="+aid;

        Date today=new Date();
        Date yesterday=new Date();
        yesterday.setTime(today.getTime()-((long)864E5));
        Date date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timestamp);
        date.setTime(date.getTime()+19800000);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        Log.v("timeStamp",timestamp);
        Log.v("date",date.toString());
        if(sdf.format(today).equals(sdf.format(date)))
            this.date="Today ";
        else if(sdf.format(yesterday).equals(sdf.format(date)))
            this.date="Yesterday ";
        else
        {

            //this.date=date.getDay()+" "+Month(date)+" ";
            this.date=new SimpleDateFormat("d MMMM").format(date);
            if(!(today.getYear()==date.getYear()))
                this.date+=" "+date.getYear()+" ";
        }
        Log.v("Date written",this.date);

    }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSpecs() {
            return specs;
        }

        public void setSpecs(String specs) {
            this.specs = specs;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getImage_ads() {
            return image_ads;
        }

        public void setImage_ads(int image_ads) {
            this.image_ads = image_ads;
        }

        public String getAid(){return this.aid;}

        public String getsLink(){
            return this.slink;
        }

    public String getLink(){
        return this.link;
    }
    public String getSubcat(){
        return this.subcat;
    }
    public void setSubcat(String subcat) {
        this.subcat = subcat;
    }

    public String getrentweek() {
        return rentw;
    }

    public void setrentweek(String rentw) {
        this.rentw = rentw;
    }

    public String getrentmonth() {
        return rentm;
    }

    public void setrentmonth(String rentm) {
        this.rentm = rentm;
    }

    public String getper() {
        return per;
    }

    public void setper(String per) {
        this.per = per;
    }

}