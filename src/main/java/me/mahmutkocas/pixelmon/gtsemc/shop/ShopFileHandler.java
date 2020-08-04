package me.mahmutkocas.pixelmon.gtsemc.shop;

import me.mahmutkocas.pixelmon.gtsemc.References;

import java.io.*;
import java.util.ArrayList;

public class ShopFileHandler {
    File folder;
    File file;
    File toPay_file;
    protected ShopFileHandler() {
        folder = new File(References.SHOP_FOLDER_PATH);
        file = new File(References.SHOP_DATA_PATH);
        toPay_file = new File(References.CANT_PAY_DATA_PATH);
        if(!folder.exists())
            folder.mkdirs();
    }

    protected ArrayList<ShopEntry> readEntries() {
        ArrayList<ShopEntry> shopEntries;
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            else {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                shopEntries = (ArrayList<ShopEntry>) ois.readObject();
                ois.close();
                fis.close();
                return shopEntries;
            }
        } catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
        return new ArrayList<>();
    }

    protected boolean writeEntries(ArrayList<ShopEntry> entries) {
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(entries);
            oos.close();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }



    protected ArrayList<String> readToPay() {
        ArrayList<String> toPay;
        try {
            if(!toPay_file.exists()) {
                toPay_file.createNewFile();
            }
            else {
                FileInputStream fis = new FileInputStream(toPay_file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                toPay = (ArrayList<String>) ois.readObject();
                ois.close();
                fis.close();
                return toPay;
            }
        } catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
        return new ArrayList<>();
    }

    protected boolean writeToPay(ArrayList<String> toPay) {
        try {
            if(!toPay_file.exists()) {
                toPay_file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(toPay_file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(toPay);
            oos.close();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

}
