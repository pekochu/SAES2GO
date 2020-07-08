package xyz.luisnglbrv.sigma;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Base64;

import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by LuisAngel on 12/12/16.
 */

public class Preferencias {

    private final String KEY_ESCUELA = "escuela";
    private final String KEY_URL = "url";
    private final String KEY_BOLETA = "boleta_num";
    private final String KEY_NOMBRE = "alumno";
    private final String KEY_PASS = "boleta_pass";
    private final String KEY_GUARDAR = "data_saved";
    private final String KEY_SSL_ERROR = "ssl_error";
    private final String KEY_IS_VALID = "is_valid";
    private final String KEY_NIVEL = "nivel";
    private final String KEY_ID_DRAW = "draw_id";
    private final String KEY_HORARIO = "horarioSaved";
    private final String KEY_FIRST_NOTICE = "firstNotice";
    private final String KEY_NOTIFICATIONS_ACTIVE = "receive_notifications";
    private final String KEY_RECEIVE_UPDATES = "receive_updates";
    private final String KEY_HORARIO_OFFLINE_TIP = "horario_offline_tip";
    private final String KEY_HORARIO_TIP = "horario_tip";
    private final String KEY_THEME = "int_estilo";
    private final String KEY_COLOR_SELECTED = "color_selected";
    private final String KEY_COLOR_FOOTER = "color_footer";
    private final String KEY_COLOR_TAB = "color_tab";
    private final String KEY_MOD_SAES = "mod_saes";
    public static final String KEY_SSL_ACCEPTED = "ssl_user_accepted";
    public static final String KEY_ITEM_SELECTED = "main_item_selected";

    private Context mContext;

    public Preferencias(Context context){
        mContext = context;
    }

    private SharedPreferences getSettings(){
        return mContext.getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
    }

    public void setString(String key, String value){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key){
        return getSettings().getString(key, null);
    }

    public void setInteger(String key, int value){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInteger(String key){
        return getSettings().getInt(key, -1);
    }

    public void setBoolean(String key, Boolean value){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getBoolean(String key){
        return getSettings().getBoolean(key, false);
    }

    public void setNombre(String nombre){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_NOMBRE, nombre);
        editor.commit();
    }

    public String getNombre(){
        return getSettings().getString(KEY_NOMBRE, null);
    }

    public void setBoleta(String boleta){
        String s1 = Encriptar(boleta);
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_BOLETA, s1);
        editor.commit();
    }

    public String getBoleta(){
        String s2 = null;
        try{ s2 = Desencriptar(getSettings().getString(KEY_BOLETA, null)); }
        catch(Exception e){ e.printStackTrace(); }
        return s2;
    }

    public void setPass(String pass){
        String s1 = Encriptar(pass);
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_PASS, s1);
        editor.commit();
    }

    public String getPass(){
        String s2 = null;
        try{ s2 = Desencriptar(getSettings().getString(KEY_PASS, null)); }
        catch(Exception e){ e.printStackTrace(); }
        return s2;
    }

    public void setURL(String url){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_URL, url);
        editor.commit();
    }

    public String getURL(){
        return getSettings().getString(KEY_URL, null);
    }

    public void setEscuela(String escuela){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_ESCUELA, escuela);
        editor.commit();
    }

    public String getEscuela(){
        return getSettings().getString(KEY_ESCUELA, null);
    }

    public void setRecordar(Boolean recordar){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putBoolean(KEY_GUARDAR, recordar);
        editor.commit();
    }

    public boolean getRecordar(){
        return getSettings().getBoolean(KEY_GUARDAR, false);
    }

    public void setSsl(Boolean recordar){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putBoolean(KEY_SSL_ERROR, recordar);
        editor.commit();
    }

    public boolean getSsl(){
        return getSettings().getBoolean(KEY_SSL_ERROR, false);
    }

    public void setIsValid(Boolean valid){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putBoolean(KEY_IS_VALID, valid);
        editor.commit();
    }

    public boolean getIsValid(){
        return getSettings().getBoolean(KEY_IS_VALID, false);
    }

    public void setIdDraw(int nivel, int id){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putInt(KEY_NIVEL, nivel);
        editor.putInt(KEY_ID_DRAW, id);
        editor.commit();
    }

    public int getNivel(){
        return getSettings().getInt(KEY_NIVEL, 2);
    }

    public int getIdDraw(){
        return getSettings().getInt(KEY_ID_DRAW, 0);
    }

    public void setHorarioGuardado(boolean bool){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putBoolean(KEY_HORARIO, bool);
        editor.commit();
    }

    public boolean getHorarioGuardado(){
        return getSettings().getBoolean(KEY_HORARIO, false);
    }

    public void setHorOffTip(boolean bool){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putBoolean(KEY_HORARIO_OFFLINE_TIP, bool);
        editor.commit();
    }

    public boolean getHorOffTip(){
        return getSettings().getBoolean(KEY_HORARIO_OFFLINE_TIP, false);
    }

    public void setHorarioTip(boolean bool){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putBoolean(KEY_HORARIO_TIP, bool);
        editor.commit();
    }

    public boolean getHorarioTip(){
        return getSettings().getBoolean(KEY_HORARIO_TIP, false);
    }

    public void setFirstNotice(boolean bool){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putBoolean(KEY_FIRST_NOTICE, bool);
        editor.commit();
    }

    public boolean getFirstNotice(){
        return getSettings().getBoolean(KEY_FIRST_NOTICE, false);
    }

    public boolean getNotificationActivated(){
        return getSettings().getBoolean(KEY_NOTIFICATIONS_ACTIVE, false);
    }

    public void setNotificationsActivated(boolean bool){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putBoolean(KEY_NOTIFICATIONS_ACTIVE, bool);
        editor.commit();
    }

    public boolean getUpdates(){
        return getSettings().getBoolean(KEY_RECEIVE_UPDATES, true);
    }

    public void setUpdates(boolean bool){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putBoolean(KEY_RECEIVE_UPDATES, bool);
        editor.commit();
    }

    private String Encriptar(String texto) {
        String secretKey = mContext.getResources().getString(R.string.c1);
        String base64EncryptedString = "";
        if(texto != null){
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
                byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

                SecretKey key = new SecretKeySpec(keyBytes, mContext.getResources().getString(R.string.c2));
                Cipher cipher = Cipher.getInstance(mContext.getResources().getString(R.string.c2));
                cipher.init(Cipher.ENCRYPT_MODE, key);

                byte[] plainTextBytes = texto.getBytes("utf-8");
                byte[] buf = cipher.doFinal(plainTextBytes);
                byte[] base64Bytes = Base64.encode(buf, Base64.DEFAULT);
                base64EncryptedString = new String(base64Bytes);
            }catch (Exception e){ e.printStackTrace(); }
        }
        return base64EncryptedString;
    }
    private String Desencriptar(String textoEncriptado) throws Exception {
        String secretKey = mContext.getResources().getString(R.string.c1);
        String base64EncryptedString = "";
        if(textoEncriptado != null){
            try {
                byte[] message = Base64.decode(textoEncriptado.getBytes("utf-8"), Base64.DEFAULT);
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
                byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
                SecretKey key = new SecretKeySpec(keyBytes,
                        mContext.getResources().getString(R.string.c2));

                Cipher decipher = Cipher.getInstance( mContext.getResources().getString(R.string.c2));
                decipher.init(Cipher.DECRYPT_MODE, key);

                byte[] plainText = decipher.doFinal(message);

                base64EncryptedString = new String(plainText, "UTF-8");
            } catch (Exception ex){ ex.printStackTrace(); }
        }
        return base64EncryptedString;
    }

    public void setStyle(int id){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putInt(KEY_THEME, id);
        editor.commit();
    }

    public int getStyle(){
        return getSettings().getInt(KEY_THEME, R.style.AppTheme_NoActionBar);
    }

    public void setColorSelected(int color){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putInt(KEY_COLOR_SELECTED, color);
        editor.commit();
    }

    public int getColorSelected(){
        return getSettings().getInt(KEY_COLOR_SELECTED, Color.parseColor("#009688"));
    }

    public void setColorFooter(String color){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_COLOR_FOOTER, color);
        editor.commit();
    }

    public String getColorFooter(){
        return getSettings().getString(KEY_COLOR_FOOTER, "#90009688");
    }

    public void setColorTab(String color){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_COLOR_TAB, color);
        editor.commit();
    }

    public String getColorTab(){
        return getSettings().getString(KEY_COLOR_TAB, "#B2DFDB");
    }

    public void setModSaesMessage(boolean bool){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putBoolean(KEY_MOD_SAES, bool);
        editor.commit();
    }

    public boolean getModSaesMessage(){
        return getSettings().getBoolean(KEY_MOD_SAES, false);
    }
}
